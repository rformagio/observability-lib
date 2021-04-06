package com.github.rformagio.observability.config;

import com.github.rformagio.observability.filter.CorrelationIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ConfigFilter {

    @Bean
    @DependsOn("configProperties")
    public FilterRegistrationBean<CorrelationIdFilter> correlationIdFilterBean(ConfigProperties configProperties) {
       // ConfigProperties configProperties = ConfigApplication.getConfigProperties();
        FilterRegistrationBean<CorrelationIdFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new CorrelationIdFilter(configProperties.getCorrelationIdFilter().getHeaderName()));
        for( String resource: configProperties.getCorrelationIdFilter().getResources()) {
            registrationBean.addUrlPatterns(resource);
        }



        registrationBean.setOrder(configProperties.getCorrelationIdFilter().getOrder());
        registrationBean.setEnabled(configProperties.getCorrelationIdFilter().isEnabled());

        return registrationBean;
    }

}
