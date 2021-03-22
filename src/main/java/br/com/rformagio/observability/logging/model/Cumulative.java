package br.com.rformagio.observability.logging.model;

import br.com.rformagio.utils.LogLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cumulative {

    private LocalDateTime initTime;
    private LocalDateTime endTime;
    private Duration duration;
    @JsonIgnore
    private String description;
    private Map<String, Object> properties;
    private Map<String, Object> messages;
    private Map<String, Object> errors;
    private Map<String, List<String>> exceptions;
    private LogLevel level;
}
