package org.bicycles.yadbcmon.core;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Реализация возможности запуска самого себя в фоне под различными ОС.
 * В нашем случае необходимо для активации сбора данных, например от zabbix-агента
 * т.к. агент ждёт ответа от пользовательского скрипта не более чем 2-3 секунды,
 * а сам сбор данных может занимать длительное время
 */
public final class itselfBackground {
    // Билдер со всеми параметрами запуска в фоне
    ProcessBuilder executor;

    /**
     * Обычная инициализация
     *
     * @param cmdParams ключи запуска приложения в фоне
     *                  detach приписывается в конце в любом случае
     * @throws Exception невозможно запустить себя в фоне
     */
    public itselfBackground(String ... cmdParams) throws Exception {
        // Параметры запуска, специфичные для ОС
        String execAppend;  // путь к java
        String execPrecmd[];    // команды, необходимые для запуска в фоне
        String execDetachCmd = "&";   // суффикс, необходимый для отделения команды от терминала

        // Определяем ОС, из под которой запускаемся
        // Задаём специфичные параметры запуска
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("win")) {
            execAppend = "\\bin\\javaw.exe";
            execPrecmd = new String[0];
        } else if ((osName.startsWith("linux")) || (osName.startsWith("sunos"))) {
            execAppend = "/bin/java";
            execPrecmd = new String[1];
            execPrecmd[0] = "nohup";
        } else {
            // Если ОС нераспознана - способ запуска в фоне неизвестен
            throw new Exception("Unsupported OS - " + System.getProperty("os.name"));
        }

        // Определяем путь к исполнимому файлу java
        Path javaExec = Paths.get(System.getProperty("java.home"), execAppend);

        // Определяем расположение собственного jar-файла
        Path jarPath = Paths.get(
            itselfBackground.class.getProtectionDomain().getCodeSource().getLocation().toURI()
        );

        // Подготавливаем к запуску
        LinkedList<String> execBuild = new LinkedList<>();
        // Добавляем команды, необходимые для запуска в фоне, специфичные для ОС
        execBuild.addAll(Arrays.asList(execPrecmd));
        // Затем исполнимый путь к java и jar
        execBuild.add(javaExec.toString());
        execBuild.add("-jar");
        execBuild.add(jarPath.toString());
        // Далее ключи запуска в фоне
        execBuild.addAll(Arrays.asList(cmdParams));
        // Завершаем на ключе отделения от терминала
        execBuild.add(execDetachCmd);

        // Формируем исполнителя
        executor = new ProcessBuilder(execBuild);
    }

    /**
     * Запуск самого себя в фоне
     *
     * @throws IOException
     */
    public void exec() throws IOException {
        executor.start();
    }

    /**
     * Получение аргументов запуска
     *
     * @return  аргументы запуска
     */
    public List<String> getCmdLine() {
        return executor.command();
    }
}
