package org.butterbrother.simplejmx;

/**
 * ��������� �������� ��������� ������
 */
public interface MemInfoMBean {
    /**
     * ��������� ������ ������� heap
     * @return  ����� ������ heap � B.
     */
    public long getTotalMemory();

    /**
     * ��������� ������� ���������������� heap
     * @return  ��������������� heap � B.
     */
    public long getUsedMemory();

    /**
     * ��������� ������� ���������� heap
     * @return  �������� � heap � B.
     */
    public long getFreeMemory();
}
