package br.com.rformagio.observability.logging.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseContent {

    private String status;
    private String protocol;
    private Object body;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> headers;
}
