package br.com.rformagio.observability.logging.logger;

import br.com.rformagio.observability.logging.model.RequestContent;
import br.com.rformagio.observability.logging.model.ResponseContent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LoggerHttp {

    @Pointcut("(@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PatchMapping)) " +
            " && @annotation(br.com.rformagio.observability.annotation.EnableRequestResponseLogging)")
    public void doAction() {}


    @Before("doAction()")
    public void beforeAction(JoinPoint joinPoint) {

        RequestContent requestContent = new RequestContent();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme());
        sb.append("://");
        sb.append(request.getServerName());
        sb.append(":");
        sb.append(request.getServerPort());
        sb.append(request.getContextPath());
        sb.append(request.getRequestURI());

        requestContent.setUrl(sb.toString());
        requestContent.setQueryString(request.getQueryString());
        requestContent.setMethod(request.getMethod());
        requestContent.setIp(request.getRemoteAddr());
        requestContent.setProtocol(request.getProtocol());

        requestContent.setHeaders(getHeadersInfo(request));
        requestContent.setPayload(joinPoint.getArgs());

        log.info("REQUEST", requestContent);

    }

    @AfterReturning(value = "doAction()", returning = "result")
    public void afterAction(JoinPoint joinPoint, Object result) {

        ResponseContent responseContent = new ResponseContent();

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getResponse();

        responseContent.setStatus(String.valueOf(response.getStatus()));
        responseContent.setHeaders(getHeadersInfo(response));
        responseContent.setBody(result);
        log.info("RESPONSE", responseContent);
    }

    private  Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private  Map<String, String> getHeadersInfo(HttpServletResponse response) {

        Map<String, String> map;

        Collection<String> headerNames = response.getHeaderNames();
        map = headerNames.stream().collect(Collectors.toMap( String::trim, response::getHeader));

        return map;
    }

}
