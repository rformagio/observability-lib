package com.github.rformagio.observability.logging.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestContent {

    private String method;
    private String url;
    private Object payload;
    private String ip;
    private String queryString;
    private String protocol;
    private Map<String, String> headers;
}
