package com.github.butterbrother.dupfind;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.TreeMap;

/**
 * Поиск дубликатов в указанном каталоге
 */
public class StartHere {

    // Генератор контрольных сумм для MD5
    private static MessageDigest md5 = null;
    // Здесь уникальные значения хеш - имя файла
    private static TreeMap<String, String> unique = new TreeMap<>();
    // Буфер чтения
    private static byte[] buffer = new byte[4096];
    // Энкодер в base64, что бы хранить строку, а не массив байт в дереве
    private static Base64.Encoder encoder = Base64.getEncoder();
    private static boolean deleteFiles = false;

    /**
     * Точка входа в приложение.
     * @param args --
     */
    public static void main(String args[]) {
        Path source;

        String doDelete = System.getProperty("delete.files");
        deleteFiles = doDelete != null && Integer.parseInt(doDelete) > 0;

        try {
            md5 = MessageDigest.getInstance("MD5");
            if (args.length > 0) {
                for (String path : args) {
                    source = Paths.get(path);
                    recursiveFind(source);
                }
            }
        } catch (NoSuchAlgorithmException err) {
            System.err.println("JVM not supported. " + err.getMessage());
        }
    }

    /**
     * Рекурсивный поиск о обсчёт хешей для найденных файлов
     * @param directory каталог, где производим обсчёт
     */
    private static void recursiveFind(Path directory) {
        if (md5 == null) return;

        try (DirectoryStream<Path> paths = Files.newDirectoryStream(directory)) {

            for (Path item : paths) {
                if (Files.isDirectory(item)) {
                    // Если каталог - уходим в рекурсию с ним
                    recursiveFind(item);
                } else if (Files.isRegularFile(item)) {
                    // Если файл - открываем и считаем хеш-сумму
                    try (BufferedInputStream buffInput = new BufferedInputStream(Files.newInputStream(item))) {
                        md5.reset();
                        while (buffInput.read(buffer) > 0) {
                            md5.update(buffer);
                        }
                        // Конвертируем в строковое представление
                        String stringDigest = encoder.encodeToString(md5.digest());

                        // put выбрасывает предыдущее значение под ключом-хешем. Если что-то есть,
                        // значит хеш не уникальный
                        String previousValue = unique.put(stringDigest, item.toString());
                        if (previousValue != null) {
                            System.out.println(item.toString() + "\tduplicate of\t" + previousValue);
                            if (deleteFiles) {
                                try {
                                    Files.delete(item);
                                    System.out.println(item.toString() + " deleted.");
                                } catch (IOException dErr) {
                                    System.err.println("Unable to delete file " + item.toString() + ": " +
                                        dErr.getLocalizedMessage());
                                }
                            }
                            // вертаем взад оригинальное имя файла
                            unique.put(stringDigest, previousValue);
                        }
                    } catch (IOException rErr) {
                        System.err.println("Unable to read " + item.toString() + ": " +
                                (rErr.getLocalizedMessage() == null ? rErr.getMessage() : rErr.getLocalizedMessage()));
                    }
                }
            }
        } catch (IOException err) {
            System.err.println("Cannot enumerate " + directory.toString() + ": " +
                    (err.getLocalizedMessage() == null ? err.getMessage() : err.getLocalizedMessage()));
        }
    }
}
