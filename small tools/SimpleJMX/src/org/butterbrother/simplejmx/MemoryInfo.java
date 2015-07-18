package org.butterbrother.simplejmx;

import javax.management.*;

/**
 * Реализация получения информации о heap
 */
public class MemoryInfo implements MemInfoMBean, DynamicMBean {

    /**
     * Свободно heap
     * @return  данные в bytes
     */
    @Override
    public long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * Всего heap
     * @return  данные в bytes
     */
    @Override
    public long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * Задействовано heap
     * @return  данные в bytes
     */
    @Override
    public long getUsedMemory() {
        return getTotalMemory() - getFreeMemory();
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        return null;
    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        return null;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return null;
    }

    public Object getAttribute(String attribute)
            throws AttributeNotFoundException,
            MBeanException,
            ReflectionException {
        return null;
    }
}
