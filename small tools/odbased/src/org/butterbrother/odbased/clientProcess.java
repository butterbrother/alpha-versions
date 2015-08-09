package org.butterbrother.odbased;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Режим клиента. Отправляем запрос сервису, ждём
 * результата, отображаем результат
 */
public class clientProcess implements staticValues {

    /**
     * Работа в режиме клиента
     *
     * @param settings  Настройки из settingsStorage, используйте
     *                  checkClientSettings()
     * @param workMode  режим работы, определён в staticValues
     */
    public void execute(settingsStorage.ClientSettings settings, int workMode) {
        // Открываем сокет, считываем секретную фразу
        try (ClientSocket client = new ClientSocket(settings.port); BufferedReader pidReader = new BufferedReader(Files.newBufferedReader(settings.pidFile, StandardCharsets.UTF_8))) {
            String secretPhrase = pidReader.readLine();
            if (secretPhrase != null) {
                // Вначале авторизируемся
                client.sendRequest(secretPhrase);

                // Далее смотрим на режим работы
                switch (workMode) {
                    case WORK_MODE_STOP:
                        // Останов, отсылаем команду
                        client.sendRequest(STOP_STRING);
                        break;
                    case WORK_MODE_RESTART:
                        // Перезапуск, отсылаем команду
                        client.sendRequest(RESTART_STRING);
                        break;
                    case WORK_MODE_CLIENT:
                        // Режим работы клиента
                        // Смотрим, есть ли считанный запрос
                        String sqlCommand = settings.sqlRequest;
                        // Если нет - считываем из stdin
                        if (sqlCommand.isEmpty()) {
                            // Считываем из stdin, если нет
                            sqlCommand = getStdinSQLCommand();
                        }

                        // Отсылаем запрос, получаем и отображаем результат
                        client.sendRequest(sqlCommand);
                        while (! client.haveResponse()) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException eI) {
                                System.err.println("Interrupted");
                                System.exit(EXIT_INTERRUPTED);
                            }
                        }
                        System.out.println(client.getResponse());
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e);
        }
    }

    /**
     * Получение SQL-команды из стандартного потока либо pipe
     *
     * @return  SQL-команда
     */
    private String getStdinSQLCommand() {
        try (BufferedReader sqlReader = new BufferedReader(new InputStreamReader(System.in))) {
            if (System.in.available() == 0) {
                System.out.println("Enter SQL request (Ctrl+D to done):");
            }
            String buffer;
            StringBuilder sqlCmdBuilder = new StringBuilder();
            while ((buffer = sqlReader.readLine()) != null)
                sqlCmdBuilder.append(buffer).append('\n');

            return sqlCmdBuilder.toString();
        } catch (IOException e) {
            System.err.println("ERROR: unable read input: " + e);
            System.exit(EXIT_RUNTIME_ERROR);
        }

        return "";
    }
}
