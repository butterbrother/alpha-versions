package com.github.butterbrother.csvfinder.Core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;

/**
 * Путь с аттрибутами.
 * <p>
 * Хранит в себе кроме непосредственно пути
 * последний известный размер файла и дату
 * модификации файла.
 * А так же позволяет сверить их с текущими данными.
 * При получении пути через {@link #getPath()} сверяет текущие атрибуты с сохранёнными
 * ранее. Если они отличаются, {@link #isSame()} вернёт false.
 */
public class PathWithAttr {
    private Path path;
    private FileTime lastMod;
    private long fileSize;
    private long totalRowsNumber = -1;
    private boolean firstUse;

    /**
     * Инициализация
     *
     * @param pathToFile путь к файлу
     */
    public PathWithAttr(Path pathToFile) {
        this.path = pathToFile;

        recalculate();
    }

    /**
     * Получение атрибута - число переносов в строке.
     * Атрибут расчитывается извне и устанавливается в
     * {@link #setTotalRowsNumber(long)}.
     *
     * @return число переносов в файле.
     */
    public long getTotalRowsNumber() {
        return totalRowsNumber;
    }

    /**
     * Установка атрибута - число переносов в файле.
     * Расчитывается извне
     *
     * @param rowsNumber число переносов
     */
    public void setTotalRowsNumber(long rowsNumber) {
        totalRowsNumber = rowsNumber;
    }

    /**
     * Считывание-пересчитывание аттрибутов файла.
     */
    private void recalculate() {
        try {
            lastMod = Files.getLastModifiedTime(path);
            fileSize = Files.size(path);
        } catch (IOException err) {
            err.printStackTrace();
            fileSize = 0;
            lastMod = FileTime.fromMillis(Calendar.getInstance().getTimeInMillis());
        }

        firstUse = true;
    }

    /**
     * Получение пути к файлу.
     *
     * @return путь к файлу
     */
    public Path getPath() {
        return path;
    }

    /**
     * Признак того, что считываем один и тот же файл.
     * Годится при повторном считывании файла.
     *
     * @return при первом обращении вернёт false.
     * При повторном вернёт true, если файл не был
     * изменён реально (тот же размер и дата изменения).
     */
    public boolean isSame() {
        try {
            FileTime currentLastMod = Files.getLastModifiedTime(path);
            long currentSize = Files.size(path);

            if (firstUse) {
                if (currentLastMod.equals(lastMod) && currentSize == fileSize) {
                    firstUse = false;
                    return false;
                }
            } else if (currentLastMod.equals(lastMod) && currentSize == fileSize)
                return true;
            else {
                recalculate();
                return false;
            }

        } catch (IOException err) {
            err.printStackTrace();
            return false;
        }

        return false;
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
