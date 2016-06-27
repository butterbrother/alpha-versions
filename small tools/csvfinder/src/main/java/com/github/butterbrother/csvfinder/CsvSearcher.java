package com.github.butterbrother.csvfinder;

import com.github.butterbrother.csvfinder.Core.PathWithAttr;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Выполняет поиск в csv-таблице.
 * Заполняет таблицу результатов.
 * Выполняется в отдельном потоке.
 */
class CsvSearcher
        implements Runnable {
    private boolean active = true;
    private boolean done = false;
    private CSVProvider.ProviderDisabler disabler = null;
    private TableForm owner;

    CsvSearcher(TableForm owner) {
        this.owner = owner;

        new Thread(this).start();
    }

    public void run() {
        // Получаем данные для поиска
        PathWithAttr pathToCSV = owner.getSelectedCsvPath();
        Charset csvCharset = owner.getSelectedCharset();
        boolean csvHaveHead = owner.csvHaveHeaders();
        String delimiter = owner.getDelimiter();
        String search = owner.getSearchQuery();
        JTable ResultTable = owner.getResultPanel();
        JProgressBar progressBar = owner.getProgressBar();
        JLabel statusBar = owner.getStatusBar();

        boolean fastSearch = owner.isFastSearch();

        try (CSVProvider provider = new CSVProvider(pathToCSV, csvCharset)) {
            disabler = provider.getDisabler();

            if (!fastSearch)
                defaultSearch(provider, progressBar, statusBar, delimiter, search, csvHaveHead, ResultTable);
            else
                fastSearch(provider, progressBar, statusBar, delimiter, search, csvHaveHead, ResultTable, csvCharset);

        } catch (IOException ioErr) {
            JOptionPane.showMessageDialog(null, "Ошибка при чтении из файла: " + ioErr.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            active = false;
        }
        active = false;
        owner.unlockControls();
        done = true;
    }

    /**
     * Обычный поиск.
     *
     * @param provider
     * @param progressBar
     * @param statusBar
     * @param delimiter
     * @param search
     * @param csvHaveHead
     * @param ResultTable
     * @throws IOException
     */
    private void defaultSearch(CSVProvider provider,
                               JProgressBar progressBar,
                               JLabel statusBar,
                               String delimiter,
                               String search,
                               boolean csvHaveHead,
                               JTable ResultTable) throws IOException {
        // Подсчитываем число строк
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(false);

        statusBar.setText("Анализ файла...");
        final long totalRecords = provider.getFileLinesCount();
        System.out.println("DEBUG: total records: " + totalRecords);

        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(true);

        if (totalRecords >= 0) {
            // Обнуляем показатели прогрессбара
            progressBar.setValue(0);
            long currentProgress = 0;
            final long onePercentProgress = (totalRecords / 100) == 0 ? 1 : totalRecords / 100;
            System.out.println("DEBUG: one percent is " + onePercentProgress + " records.");
            int percentValue = 0;

            statusBar.setText("Поиск...");

            DefaultTableModel tableModel = null;

            boolean contain;
            String rowValue[] = null;   // Здесь раньше был Vector. Но TableModel с ним работает неадекватно
            int arrayIterator;

            for (CSVRecord record : provider.getCsvParser(delimiter)) {
                if (!active) break;

                contain = false;
                ++currentProgress;

                // Создаём массив для сбора данных
                if (rowValue == null)
                    rowValue = new String[record.size()];

                // Собираем значение 1й строки
                arrayIterator = 0;
                for (String cell : record) {
                    rowValue[arrayIterator] = cell;

                    if (cell.toLowerCase().contains(search))
                        contain = true;

                    // Игнорируем превышение длины стролбцов в строке
                    if (++arrayIterator == rowValue.length)
                        break;
                }

                // Создаём-пересоздаём таблицу, если была считана первая строка.
                if (tableModel == null) {
                    if (csvHaveHead) {
                        // Считаем первую строку заголовком
                        tableModel = new DefaultTableModel(rowValue, 0);
                        ResultTable.setModel(tableModel);

                        continue;   // идём к следующей строке
                    } else {
                        // Иначе создаём заголовки с номерами столбцов
                        String headers[] = new String[rowValue.length];
                        for (int i = 0; i < headers.length; ++i)
                            headers[i] = Integer.toString(i + 1);

                        tableModel = new DefaultTableModel(headers, 0);
                        ResultTable.setModel(tableModel);
                    }
                }

                // Добавляем значение, если оно содержится в файле
                if (contain) {
                    tableModel.addRow(rowValue);
                }

                if (currentProgress >= onePercentProgress) {
                    progressBar.setValue(++percentValue);
                    currentProgress = 0;
                }
            }

            System.out.println("DEBUG: records in current = " + currentProgress);
        }
    }

    /**
     * Экспериментальный быстрый поиск.
     *
     * Хотя я не уверен, что он будет реально быстрым.
     *
     * @param provider
     * @param progressBar
     * @param statusBar
     * @param delimiter
     * @param search
     * @param csvHaveHead
     * @param ResultTable
     * @param csvCharset
     * @throws IOException
     */
    private void fastSearch(CSVProvider provider,
                    JProgressBar progressBar,
                    JLabel statusBar,
                    String delimiter,
                    String search,
                    boolean csvHaveHead,
                    JTable ResultTable,
                    Charset csvCharset)
            throws IOException {
        DefaultTableModel tableModel = null;

        String rowValue[] = null; // Если добавлять в таблицу строки из Vector, то JTable иногда заполняет все
        // строки этим вектором. Хз, почему так. С массивом всё ок.
        int arrayIterator;

        final long fileSize = provider.getFileSize();

        if (fileSize > 0 && active) {

            // Вначале создаём заголовок. Здесь примерно всё так же, как и у обычного поиска
            // Для этого понадобится только первая строка
            Iterator<CSVRecord> csvRecords = provider.getCsvParser(delimiter).iterator();
            if (csvRecords.hasNext()) {
                CSVRecord csvRecord = csvRecords.next();

                if (csvHaveHead) {
                    // Создаём массив для сбора данных
                    rowValue = new String[csvRecord.size()];

                    // Собираем значение 1й строки
                    arrayIterator = 0;
                    for (String cell : csvRecord) {
                        rowValue[arrayIterator] = cell;

                        // Игнорируем превышение длины стролбцов в строке
                        if (++arrayIterator == rowValue.length)
                            break;
                    }

                    // Считаем первую строку заголовком
                    tableModel = new DefaultTableModel(rowValue, 0);
                    ResultTable.setModel(tableModel);
                } else {
                    // Иначе создаём заголовки с номерами столбцов
                    String headers[] = new String[csvRecord.size()];
                    for (int i = 0; i < headers.length; ++i)
                        headers[i] = Integer.toString(i + 1);

                    tableModel = new DefaultTableModel(headers, 0);
                    ResultTable.setModel(tableModel);
                }

            } else {
                // Нет данных в файле, завершаем работу
                active = false;
                tableModel = new DefaultTableModel(0, 0);
            }

            provider.closeParser();
        }

        // Получаем байты в кодировке файла. Этот массив будем искать.
        byte byteString[] = search.getBytes(csvCharset);

        byte buffer[] = new byte[16384];    // поисковый буфер

        long readied, totalReadied = 0;
        final long onePercentReadied = fileSize / 100;
        int percentValue = 0, i, index = 0, nextIndex;
        int found;  // сколько искомых байтов найдено в буфере
        int bufferToIndex = buffer.length - 1;  // граница буфера, индекс последнего элемента

        int readiedChar;
        int buf_pos = 0;

        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        try {
            try (BufferedInputStream rawInput = provider.getRawBufferedInput(buffer.length * 2)) {
                while ((readied = lineIntoBuffer(rawInput, buffer)) > 0 && active) {

                    totalReadied += readied;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG: index=" + index);
        }
    }

    private boolean searchBAintoBA(byte search[], byte buffer[]) {
        int found = 0;
        int index = 0;
        int lastBufferIndex = buffer.length - 1;
        int nextIndex = lastBufferIndex;

                    for (int i = 0; i < search.length; ++i) {
                        if (index > lastBufferIndex)
                            break;

                        index = Arrays.binarySearch(buffer, index, nextIndex, search[i]);

                        if (index < 0)
                            break;

                        ++found;
                        nextIndex = index + 1;

                        if ((buffer.length - index + 1) < search.length)
                            break;
                    }

                    return found >= search.length;
    }

    /**
     * Заполнение буфера из потока ввода.
     * Заполняет до тех пор, пока не будет встречен конец потока, либо
     * пока не будет встречен конец строки.
     *
     * @param stream    поток
     * @param buffer    буфер
     * @return          считанное число байт
     * @throws IOException  ошибка ввода-вывода
     */
    private int lineIntoBuffer(InputStream stream, byte buffer[]) throws IOException {
        int readied = 0;
        int readiedChar;

        load_buffer:
        {
            while (true) {
                readiedChar = stream.read();
                switch (readiedChar) {
                    case -1:
                        break load_buffer;
                    case '\n':
                        break load_buffer;
                    case '\r':
                        break load_buffer;
                    default:
                        buffer[readied++] = (byte) readiedChar;
                }

                if (readied >= buffer.length) break;
            }
        }

        return readied;
    }

    /**
     * Остановка процесса поиска
     */
    void stop() {
        active = false;
        if (disabler != null)
            disabler.disable();
    }

    /**
     * Возвращает признак завершения процесса поиска
     *
     * @return статус завершения
     */
    boolean isDone() {
        return done;
    }
}
