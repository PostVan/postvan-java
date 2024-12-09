package com.postvan.defaults.support;

import com.caoccao.javet.interfaces.IJavetLogger;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MyJavetLogger implements IJavetLogger {

    @Override
    public void debug(String message) {
        if (log.isDebugEnabled()) {
            log.debug(message);
        }
    }

    @Override
    public void error(String message) {
        if (log.isDebugEnabled()) {
            log.error(message);
        }
    }

    @Override
    public void error(String message, Throwable throwable) {
        if (log.isDebugEnabled()) {
            log.error(message, throwable);
        }
    }

    @Override
    public void info(String message) {
        if (log.isInfoEnabled()) {
            log.info(message);
        }
    }

    @Override
    public void warn(String message) {
        if (log.isWarnEnabled()) {
            log.warn(message);
        }
    }
}