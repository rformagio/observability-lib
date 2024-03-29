package com.github.rformagio.observability.logging;

import com.github.rformagio.observability.logging.logger.LoggerCumulative;
import com.github.rformagio.observability.logging.logger.LoggerCustom;
import com.github.rformagio.observability.logging.logger.LoggerHttp;
import com.github.rformagio.observability.logging.logger.core.LogItem;
import com.github.rformagio.observability.logging.model.Cumulative;
import com.github.rformagio.observability.logging.model.RequestContent;
import com.github.rformagio.observability.logging.model.ResponseContent;
import com.github.rformagio.observability.utils.LogType;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class FlexContentComponent {

    private static final String LOGGER_CUSTOM       = LoggerCustom.class.getTypeName();
    private static final String LOGGER_HTTP         = LoggerHttp.class.getTypeName();
    private static final String LOGGER_CUMULATIVE   = LoggerCumulative.class.getTypeName();

    private static final String KEY_TYPE                = "logType";
    private static final String KEY_LOGGER_NAME         = "loggerName";
    private static final String KEY_MESSAGE             = "message";
    private static final String KEY_REQUEST_CONTENT     = "request";
    private static final String KEY_RESPONSE_CONTENT    = "response";

    private final String loggerName;

    private FlexContentComponent(String loggerName) {
        this.loggerName = loggerName;
    }

    public static FlexContentComponent forLogger(String loggerName) {
        return new FlexContentComponent(loggerName);
    }

    public void fillContent(Map<String, Object> map, ILoggingEvent event) {
        if (LOGGER_CUSTOM.equals(loggerName)) {
            fillCustom(map, event);
        } else if (LOGGER_HTTP.equals(loggerName)) {
            fillRequestResponse(map, event);
        } else if (LOGGER_CUMULATIVE.equals(loggerName)) {
            fillCumulative(map, event);
        } else {
            fillDefault(map, event);
        }
    }

    private void fillCustom(Map<String, Object> map, ILoggingEvent event) {
        map.put(KEY_LOGGER_NAME, event.getCallerData()[2].getClassName());
        map.put(KEY_TYPE, LogType.CUSTOM.name());
        map.put(KEY_MESSAGE, getMessage(event));
        Map<String, Object> subMap = (Map<String, Object>) event.getArgumentArray()[0];
        map.putAll(subMap);
    }

    private void fillRequestResponse(Map<String, Object> map, ILoggingEvent event) {

        map.put(KEY_LOGGER_NAME, event.getLoggerName());

        if(LogType.REQUEST.name().equals(event.getMessage())) {
            RequestContent requestContent = (RequestContent) event.getArgumentArray()[0];
            map.put(KEY_TYPE, LogType.REQUEST.name());
            map.put(KEY_REQUEST_CONTENT, requestContent);
        } else {
            ResponseContent responseContent = (ResponseContent)event.getArgumentArray()[0];
            map.put(KEY_TYPE, LogType.RESPONSE.name());
            map.put(KEY_RESPONSE_CONTENT, responseContent);
        }

    }

    private void fillCumulative(Map<String, Object> map, ILoggingEvent event) {
        map.put(KEY_LOGGER_NAME, event.getLoggerName());
        map.put(KEY_TYPE, LogType.CUMULATIVE);
        Cumulative cumulative = (Cumulative)event.getArgumentArray()[0];
        map.put("cumulative", cumulative);

    }

    private void fillDefault(Map<String, Object> map, ILoggingEvent event) {
        map.put(KEY_LOGGER_NAME, event.getLoggerName());
        map.put(KEY_TYPE, LogType.DEFAULT);
        map.put(KEY_MESSAGE, event.getFormattedMessage());
        map.putAll(getAllLogItemsAsMap(event));

    }

    private String getMessage(ILoggingEvent event) {
        Object[] arguments = event.getArgumentArray();
        if(arguments.length > 1) {
            return arguments[1].toString();
        }

        return null;
    }

    private Map<String, Object> getAllLogItemsAsMap(ILoggingEvent event) {
        Map<String, Object> subMap;
        try {
            if (event.getArgumentArray() != null) {
                subMap = Arrays.stream(event.getArgumentArray())
                        .filter(a -> a instanceof LogItem)
                        .collect(
                                Collectors.toMap(e -> ((LogItem) e).getKey(), e -> ((LogItem) e).getValue())
                        );
            } else {
                subMap = Collections.EMPTY_MAP;
            }
        } catch (Exception e) {
            subMap = Collections.singletonMap("LOG_ERROR", "Duplicated keys are not allowed in log!! (Exception: " + e.getMessage() + ")");
        }

        return subMap;
    }

}
