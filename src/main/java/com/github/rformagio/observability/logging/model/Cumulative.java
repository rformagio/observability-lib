package com.github.rformagio.observability.logging.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.rformagio.observability.utils.LogLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Cumulative {

    private LocalDateTime initTime;
    private LocalDateTime endTime;
    private Duration duration;
   // @JsonIgnore
    private String description;
    private Map<String, Object> properties = new LinkedHashMap<>();
    private Map<String, Object> messages =  new LinkedHashMap<>();
    private Map<String, Object> errors =  new LinkedHashMap<>();
    private Map<String, List<String>> exceptions =  new LinkedHashMap<>();
    private LogLevel level;
}
