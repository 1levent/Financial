package com.financial.common.core.exception.cache;

/**
 * 未命中缓存异常
 * @author xinyi
 */
public class NoVlaInGuavaException extends RuntimeException {
    public NoVlaInGuavaException(String msg) {
        super(msg);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}