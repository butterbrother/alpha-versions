/*
* Copyright (c) 2014, Oleg Bobukh
* MIT License, http://opensource.org/licenses/mit-license.php
* with classpath restrictions.
*
* Данная лицензия разрешает лицам, получившим копию данного
* программного обеспечения и сопутствующей документации
* (в дальнейшем именуемыми «Программное Обеспечение»),
* безвозмездно использовать Программное Обеспечение
* без ограничений, включая неограниченное право на
* использование, копирование, изменение, добавление,
* публикацию, распространение, сублицензирование и/или
* продажу копий Программного Обеспечения, а также лицам,
* которым предоставляется данное Программное Обеспечение,
* при соблюдении следующих условий:
*
* Указанное выше уведомление об авторском праве и данные
* условия должны быть включены во все копии или значимые
* части данного Программного Обеспечения.
*
* ДАННОЕ ПРОГРАММНОЕ ОБЕСПЕЧЕНИЕ ПРЕДОСТАВЛЯЕТСЯ «КАК ЕСТЬ»,
* БЕЗ КАКИХ-ЛИБО ГАРАНТИЙ, ЯВНО ВЫРАЖЕННЫХ ИЛИ ПОДРАЗУМЕВАЕМЫХ,
* ВКЛЮЧАЯ ГАРАНТИИ ТОВАРНОЙ ПРИГОДНОСТИ, СООТВЕТСТВИЯ ПО ЕГО
* КОНКРЕТНОМУ НАЗНАЧЕНИЮ И ОТСУТСТВИЯ НАРУШЕНИЙ, НО НЕ
* ОГРАНИЧИВАЯСЬ ИМИ. НИ В КАКОМ СЛУЧАЕ АВТОРЫ ИЛИ
* ПРАВООБЛАДАТЕЛИ НЕ НЕСУТ ОТВЕТСТВЕННОСТИ ПО КАКИМ-ЛИБО
* ИСКАМ, ЗА УЩЕРБ ИЛИ ПО ИНЫМ ТРЕБОВАНИЯМ, В ТОМ ЧИСЛЕ, ПРИ
* ДЕЙСТВИИ КОНТРАКТА, ДЕЛИКТЕ ИЛИ ИНОЙ СИТУАЦИИ, ВОЗНИКШИМ ИЗ-ЗА
* ИСПОЛЬЗОВАНИЯ ПРОГРАММНОГО ОБЕСПЕЧЕНИЯ ИЛИ ИНЫХ ДЕЙСТВИЙ
* С ПРОГРАММНЫМ ОБЕСПЕЧЕНИЕМ.
*/

package org.butterbrother;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Системные утилиты - подгрузка библиотек, определение
 * местоположение и т.п.
 */
public class coreTools {

    /**
     * Выполняет поиск Jar-файла.
     * При включении в Jar файл отдаёт Jar.
     * Иначе отдаёт местоположение class-файла coreTools
     *
     * @return Файл
     */
    private static File getJARFile() {
        return new File(coreTools.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    }

    /**
     * Возвращает родительскую директорию исполнимого Jar-файла
     *
     * @return Исполнимая директория
     */
    public static File getJarFileLocation() {
        return getJARFile().getParentFile();
    }

    /**
     * Выполняет добавление в classpath всех jar-файлов
     * из директории lib, расположенной в той же родительской директории,
     * что и исполнимый Jar-файл
     */
    public static void libPreloader() {
        // Вначале получаем родительский каталог Jar-файла
        File gtf = getJarFileLocation();
        // Выполняем поиск поддиректории lib
        File[] sub = gtf.listFiles();
        // При отсутствии доступа sub может быть null
        if (sub == null) return;
        gtf = null;
        for (File item : sub)
            if (item.getName().toLowerCase().equals("lib"))
                gtf = item;
        if (gtf == null || (!gtf.isDirectory())) return;
        // Перечисляем lib и подгружаем jar-файлы
        sub = gtf.listFiles();
        if (sub == null) return;
        // Подгружаем
        for (File item : sub) {
            if (item.isFile() && item.canRead() && item.getName().toLowerCase().endsWith(".jar")) {
                try {
                    // http://stackoverflow.com/questions/1010919/adding-files-to-java-classpath-at-runtime
                    Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
                    method.setAccessible(true);
                    method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{item.toURI().toURL()});
                } catch (Throwable ignore) {
                }
            }
        }
    }

    /**
     * Проверяет наличие class
     * Отображает ошибку в случае, если не может получить имя класса
     * Завершает работу приложения
     *
     * @param className         Полный путь класса, включая все пакеты
     * @param errorMessage      Сообщение об ошибке
     * @param exitCode          Код ошибки
     */
    public static void checkClassAvailability(String className, String[] errorMessage, int exitCode) {
        try {
            Class.forName(className); //, false, className.getClass().getClassLoader());
        } catch (Exception exc) {
            for (String msg : errorMessage)
                System.err.println(msg);
            System.exit(exitCode);
        }
    }
}
