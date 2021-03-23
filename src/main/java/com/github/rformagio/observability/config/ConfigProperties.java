package com.github.rformagio.observability.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
public class ConfigProperties {

    @Value("${app.observability.applicationName:null}")
    private String applicationName;

    //@Value("${app.observability.correlationIdHeaderName:null}")
    private String correlationIdHeaderName;

    @Value("${app.observability.filter.correlationId.enabled:false}")
    private boolean filterCorrelationIdEnabled;

    @Value("${app.observability.filter.correlationId.resources:}#{T(java.util.Collections).emptyList()}")
    private List<String> filterCorrelationIdResources;

    //@Value("${app.observability.filter.requestResponseLogging.enabled:false}")
    private boolean filterRequestResponseLoggingEnabled;

    //@Value("${app.observability.filter.requestResponseLogging.resources}")
    private List<String> filterRequestResponseLoggingResources;

}
