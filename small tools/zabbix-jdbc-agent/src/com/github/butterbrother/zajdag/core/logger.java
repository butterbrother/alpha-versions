package com.github.butterbrother.zajdag.core;

/**
 * ��������� ��� �������
 */
public interface logger {
    // ������ ���������� ������ �����
    void debug(Object ... message);
    void info(Object ... message);
    void warning(Object ... message);
    void warning(Exception error, Object ... additionalInfo);
    void error(Exception error);
    void error(Exception error, Object ... additionalInfo);
}
