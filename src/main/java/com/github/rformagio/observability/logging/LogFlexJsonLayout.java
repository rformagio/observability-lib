package com.github.rformagio.observability.logging;

import com.github.rformagio.observability.config.ConfigApplication;
import com.github.rformagio.observability.config.ConfigProperties;
import com.github.rformagio.observability.utils.Constants;
import com.github.rformagio.observability.utils.CustomLocalDateTimeDeserializer;
import com.github.rformagio.observability.utils.CustomLocalDateTimeSerializer;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.rformagio.observability.utils.LogTools;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class LogFlexJsonLayout extends LayoutBase<ILoggingEvent> {

    private static final String NEW_LINE = "\n";

    private static final String KEY_TIME_STAMP          = "timestamp";
    private static final String KEY_LEVEL               = "level";
    private static final String KEY_APPLICATION_NAME    = "applicationName";
    private static final String KEY_CORRELATION_ID      = "correlationId";
    private static final String KEY_EXCEPTION           = "exception";
    private static final String KEY_THREAD_NAME         = "threadName";

    private final ObjectMapper mapper;

    private final DefaultPrettyPrinter prettyPrinter;

    private boolean indentOutput = false;

    public void setIndentOutput(boolean indentOutput) {
        this.indentOutput = indentOutput;
    }

    public LogFlexJsonLayout() {
        prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

        mapper = new Jackson2ObjectMapperBuilder()
                .findModulesViaServiceLoader(true)
                .serializerByType(LocalDateTime.class, new CustomLocalDateTimeSerializer())
                .deserializerByType(LocalDateTime.class, new CustomLocalDateTimeDeserializer())
                .build();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    }

    @Override
    public void start() {
        if(indentOutput) {
            mapper.writer(prettyPrinter);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        super.start();
    }


    @Override
    public String doLayout(ILoggingEvent event) {

        ConfigProperties configProperties = ConfigApplication.getConfigProperties();
        Map<String, Object> logContent = new LinkedHashMap<>();
        StringBuilder stb = new StringBuilder();

        logContent.put(KEY_TIME_STAMP, LocalDateTime.now());
        logContent.put(KEY_LEVEL, event.getLevel().levelStr);
        logContent.put(KEY_APPLICATION_NAME, configProperties.getApplicationName());
        logContent.put(KEY_THREAD_NAME, event.getThreadName());
        logContent.put(KEY_EXCEPTION, LogTools.getExceptions(event));
        logContent.put(KEY_CORRELATION_ID, event.getMDCPropertyMap().get(Constants.CORRELATION_ID_KEY_NAME));

        try {
            FlexContentComponent
                    .forLogger(event.getLoggerName())
                    .fillContent(logContent, event);

            stb.append(toJson(logContent));

        } catch (JsonProcessingException jpe){
            stb.append(getErrorMessage(jpe));
        }

        stb.append(NEW_LINE);
        return stb.toString();
    }

    private String toJson(Map<String, Object> map) throws JsonProcessingException {
            //return mapper.writer(prettyPrinter).writeValueAsString(map);
        return mapper.writeValueAsString(map);
    }

    private String getErrorMessage(JsonProcessingException jpe) {
        StringBuilder stb = new StringBuilder();
        return  stb.append("{\n\t\"Error\": \"Error transforming Json\",\n\t\"Exception\":\"")
                .append(jpe.getMessage())
                .append("\"}\n").toString();
    }
}
