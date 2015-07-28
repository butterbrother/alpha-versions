package org.butterbrother.odbased;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * Процесс сервиса и основной управляющий поток
 */
public class serviceProcess implements staticValues {
    // Статусы работы
    private boolean activity = false;
    private boolean needRestart = false;

    /**
     * Рабочий режим сервиса
     *
     * @param settings Настройки из файла настроек
     */
    public void execute(settingsStorage settings) {
        do {
            // Сбрасываем флаги активности при старте и рестарте
            needRestart = false;
            activity = true;

            // Читаем и проверяем настройки из файла настроек
            settingsStorage.ServiceSettings sConfig = settings.checkServiceSettings();

            // Генерируем секретную фразу для обмена сообщениями
            String secretPhrase = secretPhraseGenerator.generate(10);
            // Записываем в файл. Удалим по завершению работы
            try (PrintWriter pidRecord = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(sConfig.pidFile, StandardOpenOption.DELETE_ON_CLOSE)), true)) {
                pidRecord.println(secretPhrase);

                // Запускаем обработчик БД
                DBExecutor dbexec = new DBExecutor(sConfig.URL, sConfig.login, sConfig.password, sConfig.driver);
                // Запускаем сервер
                serviceSocket server = new serviceSocket(dbexec, sConfig.port, secretPhrase, this);

                // Ждём команды на завершение работы
                while (activity) {
                    Thread.sleep(100);
                }

                // Останавливаем обработчик
                dbexec.switchOff();
                // Останавливаем сервер
                server.switchOff();
            } catch (IOException e) {
                System.err.println("Unable write to pid file: " + e);
                System.exit(EXIT_ERR_STARTUP);
            } catch (DBExecutorInitException e) {
                System.exit(EXIT_ERR_STARTUP);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
                System.exit(EXIT_INTERRUPTED);
            }
        } while (needRestart);
    }

    /**
     * Сигнал остановки, от клиента
     */
    synchronized public void stopSignal() {
        activity = false;
    }

    /**
     * Сигнал перезапуска, от клиента
     */
    synchronized public void restartSignal() {
        activity = false;
        needRestart = true;
    }
}
