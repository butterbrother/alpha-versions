package com.github.butterbrother.zajdag.core;

/**
 * Логгер, который ничего не делает и ничего не пишет
 */
public class dummyLogger
    implements logger{

    @Override
    public logger getModuleLogger(String moduleName) {
        return new dummyLogger();
    }

    @Override
    public void debug(byte[] byteArray, Object... message) {
    }

    @Override
    public void debug(Object... message) {
    }

    @Override
    public void error(Exception error, Object... additionalInfo) {
    }

    @Override
    public void error(Object... message) {
    }

    @Override
    public void info(Object... message) {
    }

    @Override
    public void warning(Exception error, Object... additionalInfo) {
    }

    @Override
    public void warning(Object... message) {
    }
}
