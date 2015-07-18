package org.butterbrother.simplejmx;

/**
 * Интерфейс проверки свободной памяти
 */
public interface MemInfoMBean {
    /**
     * Получение общего размера heap
     * @return  Общий размер heap в B.
     */
    public long getTotalMemory();

    /**
     * Получение размера задействованного heap
     * @return  Задействованный heap в B.
     */
    public long getUsedMemory();

    /**
     * Получение размера свободного heap
     * @return  Свободно в heap в B.
     */
    public long getFreeMemory();
}
