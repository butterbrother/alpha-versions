package org.butterbrother.simplejmx;

import javax.management.*;

/**
 * ���������� ��������� ���������� � heap
 */
public class MemoryInfo implements MemInfoMBean, DynamicMBean {

    /**
     * �������� heap
     * @return  ������ � bytes
     */
    @Override
    public long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * ����� heap
     * @return  ������ � bytes
     */
    @Override
    public long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * ������������� heap
     * @return  ������ � bytes
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
