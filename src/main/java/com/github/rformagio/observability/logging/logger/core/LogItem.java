package com.github.rformagio.observability.logging.logger.core;

import lombok.Getter;

@Getter
public class LogItem {

    private String key;
    private Object value;

    private LogItem(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static LogItem li(String key, Object value) {
        return new LogItem(key, value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
