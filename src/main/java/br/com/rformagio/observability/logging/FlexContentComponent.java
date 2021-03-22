package br.com.rformagio.observability.logging;

import br.com.rformagio.observability.logging.logger.LoggerCumulative;
import br.com.rformagio.observability.logging.logger.LoggerCustom;
import br.com.rformagio.observability.logging.logger.LoggerHttp;
import br.com.rformagio.observability.logging.model.Cumulative;
import br.com.rformagio.observability.logging.model.RequestContent;
import br.com.rformagio.observability.logging.model.ResponseContent;
import br.com.rformagio.utils.LogType;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.Map;

public class FlexContentComponent {

    private static final String LOGGER_CUSTOM       = LoggerCustom.class.getTypeName();
    private static final String LOGGER_HTTP         = LoggerHttp.class.getTypeName();
    private static final String LOGGER_CUMULATIVE   = LoggerCumulative.class.getTypeName();

    private static final String KEY_TYPE                = "type";
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
    }

    private String getMessage(ILoggingEvent event) {
        Object[] arguments = event.getArgumentArray();
        if(arguments.length > 1) {
            return arguments[1].toString();
        }

        return null;
    }

}
