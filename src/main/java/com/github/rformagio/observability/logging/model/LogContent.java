package com.github.rformagio.observability.logging.model;

import com.github.rformagio.observability.utils.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogContent implements Serializable {

    @Setter(AccessLevel.NONE)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime timestamp;
    private String level;
    private String applicationName;
    private String correlationId;
    private String type;
    private String threadName;
    private String loggerName;
    private String message;
    private List<String> exception;
    //@JsonRawValue
    private Object metaData;
    private RequestContent requestContent;
    private ResponseContent responseContent;

    public void setTimestamp() {
        this.timestamp = LocalDateTime.now();
    }
}
