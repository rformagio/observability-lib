package com.github.rformagio.observability.logging.logger;

import com.github.rformagio.observability.utils.LogLevel;
import com.github.rformagio.observability.utils.LogType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoggerCustom {

    public static void logInfoObject(String objectName, Object object) {
        setLog(toMapObject(objectName, object), LogLevel.INFO);
    }

    public static void logErrorObject(String objectName, Object object) {
        setLog(toMapObject(objectName, object), LogLevel.ERROR);
    }

    public static void logInfoObject(String objectName, Object object, String message) {
        setLog(message, toMapObject(objectName, object), LogLevel.INFO);
    }

    public static void logErrorObject(String objectName, Object object, String message) {
        setLog(message, toMapObject(objectName, object), LogLevel.ERROR);
    }

    public static void logInfoProperty(String key, String value) {
        setLog(toMapObject(key, value), LogLevel.INFO);
    }

    public static void logErrorProperty(String key, String value) {
        setLog(toMapObject(key, value), LogLevel.ERROR);
    }

    public static void logInfoProperty(String key, String value, String message) {
        setLog(message, toMapObject(key, value), LogLevel.INFO);
    }

    public static void logErrorProperty(String key, String value, String message) {
        setLog(message, toMapObject(key, value), LogLevel.ERROR);
    }

    private static Map<String, Object> toMapObject(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private static Map<String, String> toMapString(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private static void setLog(String message, Object o, LogLevel level) {
        String type = LogType.DEFAULT.name();
        switch (level) {
            case INFO:
                log.info(type, o, message); break;
            case ERROR:
                log.error(type, o, message); break;
            case DEBUG:
                log.debug(type, o, message); break;
            case WARN:
                log.warn(type, o, message); break;
            case TRACE:
                log.trace(type, o, message); break;

        }
    }

    private static void setLog(Object o, LogLevel level) {
        String type = LogType.DEFAULT.name();
        switch (level) {
            case INFO:
                log.info(type, o); break;
            case ERROR:
                log.error(type, o); break;
            case DEBUG:
                log.debug(type, o); break;
            case WARN:
                log.warn(type, o); break;
            case TRACE:
                log.trace(type, o); break;

        }
    }

}
