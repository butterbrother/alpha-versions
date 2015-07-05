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

import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Для упрощённого использования Ini (ini4j).
 * <p/>
 * Данные хранятся в виде вложенного LinkedHashMap
 * Позволяет производить поиск параметров, игнорируя регистр
 * наименований параметров
 * <p/>
 * Версия - 0.2
 */
public class iniProp
        extends LinkedHashMap<String, LinkedHashMap<String, String>> {

    /**
     * Простая инициализация
     */
    public iniProp() {
    }

    /**
     * Инициализация ini-файлом
     *
     * @param iniFile Ini-файл
     */
    public iniProp(Ini iniFile) {
        importFromIni(iniFile);
    }

    /**
     * Инициализация указанием файла
     *
     * @param iniFileObject     ini-файл
     * @throws IOException      Файл не найден, ошибка ввода-вывода
     */
    public iniProp(File iniFileObject) throws IOException {
        if (iniFileObject.exists() && iniFileObject.canRead()) {
            Ini ini = new Ini(iniFileObject);
            importFromIni(ini);
        } else
            throw new FileNotFoundException("Ini file not found");
    }

    /**
     * Инициализация из ini-файла с указанием пути
     * и кодировки
     *
     * @param iniFileName Имя ini-файла
     * @param codePage    Кодировка файла
     * @throws IOException Файл не найден, либо ошибка В\В
     */
    public iniProp(String iniFileName, String codePage) throws IOException {
        File iniFile = new File(iniFileName);
        if (iniFile.exists() && iniFile.canRead()) {
            Ini iniFileObject = new Ini(new InputStreamReader(
                    new FileInputStream(iniFile),
                    codePage
            ));
            importFromIni(iniFileObject);
        } else
            throw new FileNotFoundException("Ini file not found");
    }

    /**
     * Выполняет импорт из Ini-файла с расчётом на одинарную вложенность
     * Ini ([Секция]->Параметр) и строковое хранение данных.
     * Пропускаются пустые секции и параметры.
     *
     * @param iniObject Ini-файл
     */
    public void importFromIni(Ini iniObject) {
        // Вначале выполняем импорт
        // Игнорируем все исключения (ini-файл может быть некорректным)
        if (iniObject == null)
            return;
        try {
            for (Map.Entry<String, Profile.Section> item : iniObject.entrySet()) {
                try {
                    this.put(item.getKey(), new LinkedHashMap<String, String>(item.getValue()));
                } catch (Exception Ignore) {
                }
            }
        } catch (Exception ignore) {
        }

        // Затем проходимся по значением, отсекая
        // сначала пустые параметры, а затем - секции
        Iterator<Map.Entry<String, LinkedHashMap<String, String>>> item = this.entrySet().iterator();
        while (item.hasNext()) {
            // Уровень секций
            Map.Entry<String, LinkedHashMap<String, String>> entry = item.next();
            Iterator<Map.Entry<String, String>> subItem = entry.getValue().entrySet().iterator();

            while (subItem.hasNext()) {
                // Уровень параметров
                Map.Entry<String, String> subEntry = subItem.next();
                // Стираем парные кавычки подзначений категорий
                // Ini4J при чтении производит trim
                if ((subEntry.getValue().startsWith("\"")) && (subEntry.getValue().endsWith("\""))) {
                    String buffer = subEntry.getValue();
                    subEntry.setValue(buffer.substring(1, buffer.length() - 1));
                }
                // Удаляем пустые подзначения
                if (subEntry.getValue().isEmpty())
                    subItem.remove();
            }

            // Удаляем пустые секции
            if (entry.getValue().size() == 0)
                item.remove();
        }
    }

    /**
     * Выполняет обратное преобразование в ini-файл
     *
     * @return Ini-файл
     */
    public Ini toIni() {
        // Создание ini-файла и перенесение данных
        Ini iniData = new Ini();
        for (String item : this.keySet()) {
            iniData.add(item).putAll(this.get(item));
        }

        // Обрамление в кавычки все элементы с пробелами в начале и в конце
        // Т.к. ini4j при чтении делает trim
        for (Map.Entry<String, Profile.Section> item : iniData.entrySet()) {
            for (Map.Entry<String, String> subItem : item.getValue().entrySet()) {
                String buffer = subItem.getValue();
                if (!buffer.equals(buffer.trim())) {
                    subItem.setValue('"' + buffer + '"');
                }
            }
        }

        return iniData;
    }

    /**
     * Выполняет поиск среди секций, игнорируя регистр
     *
     * @param searchCase Искомая секция
     * @return Имя категории либо null
     */
    
    public String searchSection(String searchCase) {
        for (Map.Entry<String, LinkedHashMap<String, String>> category : this.entrySet()) {
            if (category.getKey().equalsIgnoreCase(searchCase))
                return category.getKey();
        }
        return null;
    }

    /**
     * Выполняет поиск среди названий параметров, игнорируя регистр
     *
     * @param sectionName      Имя категории
     * @param searchCase        Искомый параметр
     * @return                  Имя параметра либо null
     */
    
    public String searchParameter(String sectionName, String searchCase) {
        for (Map.Entry<String, LinkedHashMap<String, String>> category : this.entrySet())
            if (category.getKey().equalsIgnoreCase(sectionName))
                for (Map.Entry<String, String> parameter: category.getValue().entrySet())
                    if (parameter.getKey().equalsIgnoreCase(searchCase))
                        return parameter.getKey();
        return null;
    }

    /**
     * Выполняет поиск значений параметров игнорируя регистр
     * названий секций и параметров
     *
     * @param sectionName  Имя категории
     * @param parameterName Имя параметра
     * @return Значение параметра либо null
     */
    
    public String getParameter(String sectionName, String parameterName) {
        for (Map.Entry<String, LinkedHashMap<String, String>> category : this.entrySet())
            if (category.getKey().equalsIgnoreCase(sectionName))
                for (Map.Entry<String, String> parameter : category.getValue().entrySet())
                    if (parameter.getKey().equalsIgnoreCase(parameterName))
                        return parameter.getValue();

        return null;
    }

    /**
     * Выполняет поиск значений параметров игнорируя регистр
     * названий секций и параметров
     *
     * @param sectionName   Имя секции
     * @param parameterName Имя параметра
     * @param defaultValue  Значение по-умолчанию (при отрицательном
     *                      результате поиска
     * @return Значение параметра, либо параметр по-умолчанию
     */
    public String getParameter(String sectionName, String parameterName, String defaultValue) {
        for (Map.Entry<String, LinkedHashMap<String, String>> category : this.entrySet())
            if (category.getKey().equalsIgnoreCase(sectionName))
                for (Map.Entry<String, String> parameter : category.getValue().entrySet())
                    if (parameter.getKey().equalsIgnoreCase(parameterName))
                        return parameter.getValue();

        return defaultValue;
    }

    /**
     * Выполняет поиск значений параметров long игнорируя регистр
     *
     * @param sectionName       Имя секции
     * @param parameterName     Имя параметра
     * @return                  Числовое значение
     * @throws NumberFormatException    Вернётся, если значение отсутствует
     */
    public long getLongParameter(String sectionName, String parameterName)
            throws NumberFormatException {
        return Long.parseLong(getParameter(sectionName, parameterName));
    }

    /**
     * Выполняет поиск значений параметров int игнорируя регистр
     *
     * @param sectionName       Имя секции
     * @param parameterName     Имя параметра
     * @return                  Числовое значение
     * @throws NumberFormatException    Вернётся, если значение отсутствует
     */
    public int getIntParameter(String sectionName, String parameterName)
        throws NumberFormatException {
        return Integer.parseInt(getParameter(sectionName, parameterName));
    }

    /**
     * Выполняет поиск значение параметров int игнорируя регистр
     *
     * @param sectionName       Имя секции
     * @param parameterName     Имя параметра
     * @param defaultValue      Значение по-умолчанию
     * @return                  Значение параметра либо умолчание в случае ошибки
     *                          поиска/парсинга
     */
    public int getIntParameter(String sectionName, String parameterName, int defaultValue) {
        try {
            return Integer.parseInt(getParameter(sectionName, parameterName));
        } catch (NumberFormatException exc) {
            return defaultValue;
        }
    }

    /**
     * Выполняет поиск значения параметров boolean игнорируя регистр
     * Возвращает true не только когда "true", но и при "yes" и "enable"
     *
     * @param sectionName       Имя секции
     * @param parameterName     Имя параметра
     * @return                  Значение параметра
     */
    public boolean getBooleanParameter(String sectionName, String parameterName) {
        String buffered = getParameter(sectionName, parameterName, "");
        return Boolean.parseBoolean(buffered) || buffered.equalsIgnoreCase("yes") || buffered.equalsIgnoreCase("enable");
    }
}
