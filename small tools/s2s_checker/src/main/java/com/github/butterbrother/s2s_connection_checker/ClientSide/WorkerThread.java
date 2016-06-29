package com.github.butterbrother.s2s_connection_checker.ClientSide;

/**
 * Рабочий процесс, выполняющий подключение к целевым серверам
 *
 * @see com.github.butterbrother.s2s_connection_checker.ClientSide.DefaultClient
 */

public abstract class WorkerThread {

    /**
     * Остановка рабочего процесса.
     */
    abstract void inactivate();
}
