import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Подобие DirectoryScanner из Apache Ant.
 * Теперь на технологиях NIO
 */
public class NIODirectoryScanner {

    // Список найденных файлов
    LinkedList<Path> filesList = new LinkedList<>();
    // Список найденных каталогов
    LinkedList<Path> dirList = new LinkedList<>();
    // Список масок файлов и каталогов
    String[] includeList = new String[0];
    // Список исключаемых файлов и каталогов
    String[] excludeList = new String[0];
    // Стартовый каталог. По-умолчанию - текущий
    Path baseDir = Paths.get("./");
    boolean ignoreCase = false;

    /**
     * Установка начального каталога поиска
     *
     * @param baseDir Начальный каталог поиска
     */
    public void setBaseDir(Path baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * Переключение игнорирования регистра.
     * По-умолчанию не игнорируется
     *
     * @param ignoreCase игнорирование ригистра
     */
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    /**
     * Сканирование
     *
     * @throws IOException
     */
    public void scan() throws IOException {
        Files.walkFileTree(baseDir, new NIOFilter<>());
    }

    /**
     * Добавление включений
     *
     * @param items Маски файлов и каталогов
     */
    public void addIncludedList(String[] items) {
        includeList = buildPatterns(items, includeList);
    }

    /**
     * Добавление исключений
     *
     * @param items Маски файлов и каталогов
     */
    public void addExcludedList(String[] items) {
        excludeList = buildPatterns(items, excludeList);
    }

    /**
     * Формирование фильтрующих паттернов.
     * Добавляет так же предыдущий список
     *
     * @param items        Список масок
     * @param previousList Предыдущий список масок
     * @return Список паттернов
     */
    private String[] buildPatterns(String[] items, String[] previousList) {
        // Формируемый список
        LinkedList<String> buildIncludeList = new LinkedList<>();
        // Включаем прежний список
        buildIncludeList.addAll(Arrays.asList(previousList));
        // Формируем список включений
        for (String item : items) {
            try {
                // Обрабатываем каждую строку, замещаем ? и * на \? и \*
                item = item.replace(".", "\\.").replace("*", ".*");
                // Пробуем скомпилировать паттерн
                Pattern.compile(item);
                // Добавляем
                buildIncludeList.add(item);
                System.out.println(item);
            } catch (PatternSyntaxException ignore) {
                System.out.println("Warn, " + ignore);
            }
        }

        return buildIncludeList.toArray(new String[buildIncludeList.size()]);
    }

    /**
     * Добавление включений
     *
     * @param items Маски файлов и каталогов
     */
    public void addIncludedList(List<String> items) {
        addIncludedList(items.toArray(new String[items.size()]));
    }

    /**
     * Добавление исключений
     *
     * @param items Маски файлов и каталогов
     */
    public void addExcludedList(List<String> items) {
        addExcludedList(items.toArray(new String[items.size()]));
    }

    /**
     * Отдача списка найденных файлов
     *
     * @return Список найденных файлов
     */
    public Path[] getFoundedFiles() {
        return filesList.toArray(new Path[filesList.size()]);
    }

    /**
     * Отдача списка найденных каталогов
     *
     * @return Список найденных каталогов
     */
    public Path[] getFoundedDirs() {
        return dirList.toArray(new Path[dirList.size()]);
    }

    /**
     * Фильтрация каталогов
     *
     * @param <T>
     */
    private class NIOFilter<T extends Path> implements FileVisitor<T> {

        /**
         * Фильтрация перед перечислением каталога
         * Добавление каталога в список каталогов
         *
         * @param dir   Текущий каталог
         * @param attrs И его атрибуты
         * @return Возможность перечислить, из FileVisitResult
         * @throws IOException что-то пошло не так
         */
        @Override
        public FileVisitResult preVisitDirectory(T dir, BasicFileAttributes attrs) throws IOException {
            if (matchedMasks(dir.toString())) {
                dirList.add(dir);
                return FileVisitResult.CONTINUE;
            } else {
                return FileVisitResult.SKIP_SIBLINGS;
            }
        }

        /**
         * Добавление файла в список файлов
         *
         * @param file  Файл
         * @param attrs и его атрибуты
         * @return Продолжаем, всегда возвращается FilesVisitResult.CONTINUE
         * @throws IOException что-то пошло не так
         */
        @Override
        public FileVisitResult visitFile(T file, BasicFileAttributes attrs) throws IOException {
            if (matchedMasks(file.toString())) {
                filesList.add(file);
            }

            return FileVisitResult.CONTINUE;
        }

        /**
         * Проверка, что имя файла-каталога отвечает указанным включениям
         * и исключениям
         *
         * @param filename Имя файла
         * @return соответствие
         */
        private boolean matchedMasks(String filename) {
            if (ignoreCase) filename = filename.toLowerCase();  // Здесь и в дальнейшем срезаем регистр,
            // если есть соответствующий флаг

            // Вначале проверяем, входит ли имя в список исключений
            for (String ignore : excludeList) {
                if (ignoreCase) ignore = ignore.toLowerCase();
                if (Pattern.matches(ignore, filename)) return false;
            }

            // Далее, если есть список включений, проверяем, что в него входит данное имя файла
            if (includeList.length > 0) {
                for (String match : includeList) {
                    if (ignoreCase) match = match.toLowerCase();
                    // Если входит - отвечает маскам
                    if (filename.matches(match)) return true;
                }
                // Иначе не отвечает
                return false;
            }
            // Если нет списока включений и не в списке исключений - имя прошло фильтрацию
            return true;
        }

        @Override
        public FileVisitResult visitFileFailed(T file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(T dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }
}
