package com.github.rformagio.observability.config;

import com.github.rformagio.observability.logging.logger.LoggerHttp;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@ComponentScan(basePackageClasses = {ConfigProperties.class, ConfigFilter.class, LoggerHttp.class})
@Configuration
public class ConfigApplication implements ApplicationContextAware {

    private static ApplicationContext context;

    private static ConfigProperties configProperties;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ConfigApplication.context = applicationContext;
        configProperties = ConfigApplication.context.getBean(ConfigProperties.class);
    }

    public static ConfigProperties getConfigProperties() {
        if (Objects.nonNull(configProperties)) {
            return configProperties;
        } else {
            return new ConfigProperties();
        }
    }
}
