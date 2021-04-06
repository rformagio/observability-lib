package com.github.rformagio.observability.filter;

import com.github.rformagio.observability.utils.Constants;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

public class CorrelationIdFilter implements Filter {

   // private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
   // private static final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";

    private String correlationIdHeaderName;

    public CorrelationIdFilter(String correlationIdHeaderName) {
        this.correlationIdHeaderName = correlationIdHeaderName;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        MDC.put(Constants.CORRELATION_ID_KEY_NAME, getCorrelationId(httpServletRequest));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getCorrelationId(final HttpServletRequest request) {
        String correlationId = request.getHeader(correlationIdHeaderName);
        if(StringUtils.isEmpty(correlationId)) {
            correlationId = generateUniqueCorrelationId();
        }
        return correlationId;
    }

    private String generateUniqueCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
