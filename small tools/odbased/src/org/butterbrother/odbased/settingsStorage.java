package org.butterbrother.odbased;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * Отвечает за чтение настроек из файла настроек,
 * хранения и отдачу параметров.
 */
public class settingsStorage implements staticValues {
    // Параметры
    private Properties settings;

    /**
     * Инициализация с чтением файла настроек.
     * Неудачное чтение настроек аварийно завершит работу приложения
     *
     * @param configFileName    Файл конфигурации
     */
    public settingsStorage(String configFileName) {
        try (BufferedReader settingsReader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(configFileName))),4096)) {
            settings = new Properties();
            settings.load(settingsReader);
        } catch (IOException e) {
            System.err.println("Unable to open configuration file " + configFileName + ": " + e);
            System.exit(EXIT_ERR_READ_CONFIG);
        }
    }

    /**
     * Получение параметра по его имени.
     * Регистр имени параметра при этом игнорируется.
     *
     * @param parameterName     Имя параметра, ключ
     * @return                  Значение параметра
     * @throws ParameterNotFoundException   Параметр не найден
     */
    public String getParameter(String parameterName) throws ParameterNotFoundException {
        for (Map.Entry<Object, Object> item : settings.entrySet()) {
            if (item.getKey().toString().equalsIgnoreCase(parameterName))
                return item.getValue().toString();
        }

        throw new ParameterNotFoundException();
    }
}
