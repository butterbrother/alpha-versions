package com.github.butterbrother.csvfinder;

import com.github.butterbrother.csvfinder.Core.PathWithAttr;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Обеспечивает работу непосредственно с CSV-файлом.
 * т.е. открывает поток для чтения, считывает число строк
 * и т.д.
 */
class CSVProvider
        implements Closeable, AutoCloseable {
    private Path pathToCSV;
    private PathWithAttr pathWithAttr;
    private Charset encoding;
    private FileSystem zipFS = null;
    private CSVParser csvParser = null;
    private BufferedReader stringReader = null;
    private boolean active = true;
    private boolean closed = false;

    /**
     * Инициализация и базовые проверки
     *
     * @param pathWithAttr Путь к csv-файлу. Либо объекту, содержащему этот csv-файл
     * @param encoding     Кодировка
     * @throws IOException Ошибка при проведении проверки
     */
    CSVProvider(PathWithAttr pathWithAttr, Charset encoding) throws IOException {
        // Базовые проверки на путь
        this.pathWithAttr = pathWithAttr;

        if (pathWithAttr == null) {
            throw new IOException("Не выбран CSV-файл");
        }

        this.pathToCSV = pathWithAttr.getPath();

        if (Files.notExists(pathToCSV)) {
            throw new IOException("Этот файл больше не существует");
        } else if (!Files.isRegularFile(pathToCSV)) {
            throw new IOException("Был выбран не файл");
        }

        // Проверка расширения
        String fileName = pathToCSV.getFileName().toString().toLowerCase();
        if (!(fileName.endsWith(".csv") || fileName.endsWith(".zip")))
            throw new IOException("Можно выбрать либо csv-файл, либо zip-архив");

        this.encoding = encoding;

        // Если это zip-архив, то будет работать с первым файлом внутри него
        if (fileName.endsWith(".zip")) {
            Map<String, String> zipEnv = new HashMap<>();
            zipEnv.put("create", "false");
            zipEnv.put("encoding", encoding.toString());

            URI zipUri = URI.create("jar:file:/" + pathToCSV.toString().replace('\\', '/'));
            this.pathToCSV = null;   // Путь к csv-файлу переопределится в дальнейшем.
            // То, что он может быть null - флаг неудачного считывания архива

            try {
                // Открываем файловую систему, берём первый файл
                zipFS = FileSystems.newFileSystem(zipUri, zipEnv);
                Path zipRoot = zipFS.getPath("/");

                // Переопределяем путь к csv-файлу на первый попавшийся csv-файл в архиве
                try (DirectoryStream<Path> zipListening = Files.newDirectoryStream(zipRoot)) {
                    for (Path item : zipListening) {
                        if (Files.isRegularFile(item)) {

                            String inZipName = item.getFileName().toString().toLowerCase();
                            if (inZipName.endsWith(".csv")) {
                                this.pathToCSV = item;
                                System.out.println("DEBUG: selected " + item + " into zip.");
                                break;
                            }
                        }
                    }
                }
            } catch (IOException zipIoErr) {
                try {
                    zipFS.close();
                } catch (IOException ignore) {
                }

                throw new IOException("Невозможно открыть ZIP-файл: " + zipIoErr.getMessage());
            }

            if (this.pathToCSV == null)
                throw new IOException("Отсутствует csv-файл в выбранном архиве");
        }
    }


    /**
     * Подсчёт числа переносов строк в файле.
     * Необходим для отображения прогресса.
     *
     * @return число строк в файле. Если вернулся -1 - произошла ошибка при чтении
     */
    long getFileLinesCount() throws IOException {
        if (closed)
            throw new IOException("Ошибка ПО - Файл уже закрыт. Обратитесь к разработчику.");

        if (pathWithAttr.isSame() && pathWithAttr.getTotalRowsNumber() >= 0)
            return pathWithAttr.getTotalRowsNumber();

        long linesCount = 0;
        long retCount = 0;

        try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(pathToCSV))) {
            byte buffer[] = new byte[1024];
            int readied, i;
            while (active && (readied = inputStream.read(buffer)) != -1) {
                for (i = 0; i < readied && active; ++i)
                    if (buffer[i] == '\n')
                        ++linesCount;
                    else if (buffer[i] == '\r')
                        ++retCount;
            }
        } catch (IOException ioErr) {
            return -1;
        }

        if (!active)
            return -1;

        pathWithAttr.setTotalRowsNumber(retCount > linesCount ? retCount : linesCount);
        return pathWithAttr.getTotalRowsNumber();
    }

    /**
     * Возвращает размер файла в байтах.
     *
     * @return размер файла
     * @throws IOException
     */
    long getFileSize() throws IOException {
        return Files.size(pathToCSV);
    }

    /**
     * Инициализирует считыватель csv-файла
     *
     * @param delimiter разделитель столбцов
     * @return считыватель
     * @throws IOException ошибка чтения
     */
    Iterable<CSVRecord> getCsvParser(String delimiter) throws IOException {
        stringReader = Files.newBufferedReader(pathToCSV, encoding);

        csvParser = new CSVParser(stringReader, CSVFormat
                .EXCEL
                .withDelimiter(delimiter.charAt(0))
                .withAllowMissingColumnNames()
        );

        return csvParser;
    }

    /**
     * Отдаёт буферизированный поток из файла
     *
     * @param size размер буфера. Если 0 либо меньше - используется размер
     *             по-умолчанию.
     * @return буферизированный поток
     * @throws IOException
     */
    BufferedInputStream getRawBufferedInput(int size) throws IOException {
        return size <= 0 ?
                new BufferedInputStream(Files.newInputStream(pathToCSV))
                : new BufferedInputStream(Files.newInputStream(pathToCSV), size);
    }

    /**
     * Закрытие
     */
    public void close() {
        active = false;

        closeParser();

        try {
            if (stringReader != null)
                stringReader.close();
        } catch (IOException ignore) {
        }

        try {
            if (zipFS != null)
                zipFS.close();
        } catch (IOException ignore) {
        }

        closed = true;
    }

    /**
     * Закрывает только парсер.
     */
    public void closeParser() {
        try {
            if (csvParser != null)
                csvParser.close();
        } catch (IOException ignore) {
        }
    }

    /**
     * Остановка всех текущих процессов чтения и анализа.
     * Меняет флаг активности, с которым свеняются анализаторы.
     */
    private void disable() {
        active = false;
    }

    /**
     * Получение выключателя
     *
     * @return выключатель
     */
    ProviderDisabler getDisabler() {
        return new ProviderDisabler(this);
    }

    /**
     * Выключитель провайдера.
     * Служит для обхода ограничения try-with-resource
     */
    class ProviderDisabler {
        private CSVProvider owner;

        ProviderDisabler(CSVProvider owner) {
            this.owner = owner;
        }

        void disable() {
            owner.disable();
        }
    }
}
