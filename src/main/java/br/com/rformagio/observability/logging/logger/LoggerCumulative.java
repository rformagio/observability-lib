package br.com.rformagio.observability.logging.logger;

import br.com.rformagio.observability.logging.model.Cumulative;
import br.com.rformagio.utils.LogLevel;
import br.com.rformagio.utils.LogType;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class LoggerCumulative {

    private Cumulative cumulative;

    private LoggerCumulative(LogLevel level) {
        this.cumulative = new Cumulative();
        cumulative.setLevel(level);
        cumulative.setInitTime(LocalDateTime.now());
    }

   public static LoggerCumulative init(LogLevel level) {
       return new LoggerCumulative(level);
   }


    public void log() {
        cumulative.setEndTime(LocalDateTime.now());
        cumulative.setDuration(Duration.between(cumulative.getEndTime(), cumulative.getInitTime()));
        terminate();
    }

    public void logDescription(String description) {
        cumulative.setDescription(description);
    }

    public void addMessage(String message, Object object) {
        cumulative.getMessages().put(message, object);
    }

    public void addError(String error, Object object) {
        cumulative.getErrors().put(error, object);
    }

    public void addProperty(String key, String value) {
        cumulative.getProperties().put(key, value);
    }

    public void addException(String description, Exception exception) {
        cumulative.getExceptions().put(description, getExceptions(exception.getStackTrace()));
    }

    private List<String> getExceptions(StackTraceElement[] ste) {

        if( Objects.nonNull(ste)) {
            return Arrays.stream(ste)
                    .map(it -> "at " + it.toString())
                    .collect(Collectors.toList());
        }

        return null;
    }

    private void terminate() {
        String type = LogType.CUMULATIVE.name();
        switch (cumulative.getLevel()) {
            case INFO:
                log.info(type, cumulative); break;
            case WARN:
                log.warn(type, cumulative); break;
            case DEBUG:
                log.debug(type, cumulative); break;
            case ERROR:
                log.error(type, cumulative); break;
            case TRACE:
                log.trace(type, cumulative); break;
        }
    }

}
