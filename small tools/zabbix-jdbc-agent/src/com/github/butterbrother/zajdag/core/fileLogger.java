package com.github.butterbrother.zajdag.core;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Calendar;

/**
 * ����������� ������������ � ����
 */
public class fileLogger
    implements logger {

    // ���� � ����� ����
    private Path logPath;
    // ���� �������� ����� ����
    private Calendar logCreateDate;

    // ��� - �������� ������
    private boolean isCoreLogger;
    // ������ �� �������� ������, ���� ��� ������ ������
    private fileLogger coreLogger;

    // ���� ����
    private PrintWriter log;

    public fileLogger(String logPath) {

    }
}
