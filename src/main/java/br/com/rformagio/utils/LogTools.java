package br.com.rformagio.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LogTools {

    public static List<String> getExceptions(ILoggingEvent event) {

        if(Objects.nonNull(event.getThrowableProxy())){
            return Arrays
                    .stream(event.getThrowableProxy().getStackTraceElementProxyArray())
                    .map(StackTraceElementProxy::getSTEAsString)
                    .collect(Collectors.toList());
        }

        return null;
    }

}
