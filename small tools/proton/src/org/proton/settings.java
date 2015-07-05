package org.proton;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Инициализация и хранение настроек
 */
public class settings {
    private String source;  // Каталог - источник
    private String target;  // Каталог - приёмник
    // Каталог с индексом
    private String index;   // Путь из конфига
    private Path indexPath; // В виде Path

    private int minCount;   // Минимальное число файлов в каталоге-приёмнике
    private int uploadCount;// Число перемещающися файлов
    private int pause;      // Пауза между итерациями

    /**
     * Закрытая инициализация.
     * Для получения настроек необходима их загрузка и проверка через специальный метод
     *
     * @param source      источник
     * @param target      приёмник
     * @param index       индексы
     * @param minCount    минимальное число файлов
     * @param uploadCount перемещаемое число файлов
     */
    private settings(String source, String target, String index, int minCount, int uploadCount, int pause) {
        this.source = source;
        this.target = target;

        this.index = index;
        this.indexPath = Paths.get(index);

        this.minCount = minCount;
        this.uploadCount = uploadCount;
        this.pause = pause;
    }

    /**
     * Чтение и проверка настроек
     *
     * @return обработанные настройки
     */
    public static settings readSettings() {
        // Загружаем настройки
        Properties set = new Properties();
        try (InputStream fileProp = Files.newInputStream(Paths.get("proton.properties"))) {
            set.load(fileProp);
        } catch (IOException err) {
            System.err.println("Unable to load config file: " + err);
            System.exit(1);
        }

        // Получаем и проверяем настройки
        String source = set.getProperty("source");
        String target = set.getProperty("target");
        String index = set.getProperty("index");
        if (source == null || source.isEmpty()) {
            System.err.println("source not set");
            System.exit(1);
        }
        if (target == null || target.isEmpty()) {
            System.err.println("target not set");
            System.exit(1);
        }
        if (index == null || index.isEmpty()) {
            System.err.println("index not set");
            System.exit(1);
        }
        int minCount;
        int uploadCount;
        int pause;
        try {
            minCount = Integer.parseInt(set.getProperty("min", "10000"));
        } catch (NumberFormatException err) {
            System.err.println("Unable to parse min count, set default, 10000");
            minCount = 10000;
        }
        try {
            uploadCount = Integer.parseInt(set.getProperty("upload", "10000"));
        } catch (NumberFormatException err) {
            System.err.println("Unable to parse upload count, set default, 10000");
            uploadCount = 10000;
        }
        try {
            pause = Integer.parseInt(set.getProperty("pause", "30")) * 1000;
        } catch (NumberFormatException err) {
            System.err.println("Unable to parse pause, set default, 30 sec");
            pause = 30000;
        }

        return new settings(source, target, index, minCount, uploadCount, pause);
    }

    // Получение настроек
    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getIndex() {
        return index;
    }

    public Path getIndexPath() {
        return indexPath;
    }

    public int getMinCount() {
        return minCount;
    }

    public int getUploadCount() {
        return uploadCount;
    }

    public int getPause() {
        return pause;
    }
}
