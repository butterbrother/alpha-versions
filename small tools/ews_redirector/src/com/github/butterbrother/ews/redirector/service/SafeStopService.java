package com.github.butterbrother.ews.redirector.service;

/**
 * Сервис с плавной остановкой
 */
public abstract class SafeStopService
        implements Runnable{
    private boolean active = true;
    private boolean done = false;

    /**
     * Прекращает активность сервиса
     */
    public void safeStop() {
        active = false;
    }

    /**
     * Проверка активности сервиса
     * @return  текущий статус
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Проверка завершения работы сервиса
     * @return  текущий статус
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Установка флага завершения работы сервиса
     */
    protected void wellDone() {
        done = true;
        active = false;
    }
}
