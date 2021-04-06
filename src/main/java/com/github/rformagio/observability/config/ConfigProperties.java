package com.github.rformagio.observability.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Data
@Configuration("configProperties")
@ConfigurationProperties(prefix = "app.observability")
public class ConfigProperties {

    private String applicationName;

    private CorrelationIdFilterProp correlationIdFilter;

}
