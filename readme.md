# Observability Library

I hope this library can help with some metrics and logging features. For while, 
only logging features is available.
It uses *Spring boot*.

The logging feature uses logback and it allows you to log data in a flexible JSON layout. It can be very useful for Splunk. 


### How to use

For start, you must put in your dependencies:
```xml
<dependency>
    <groupId>com.github.rformagio</groupId>
    <artifactId>observability-lib</artifactId>
    <version>1.1</version>
</dependency>
```
 
To enable, put the annotation *EnableObservabiltyLib* in your Application class.

``` java
@EnableObservabiltyLib
@SpringBootApplication
public class TesteApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesteApplication.class, args);
    }

}

``` 

In your class:

```
import static LoggerCustom.*;
```

And so....you have some choices:

```
logInfoObject( "name", Object);
```

Or 

```
logErrorObject( "name", Object);
```

Or 

```
logInfoProperty( "key", "value");
```

Or 

```
logErrorProperty( "key", "value");
```

It's possible to put some message too:

```
logInfoObject( "name", Object, "Message here !!!");
```

Or 

```
logInfoProperty( "key", "value", "Your message here!");
```

You can do some configuration in your *application.yaml* :
``` yaml
app:
  observability:
    applicationName: 'teste-api'
    correlationIdFilter:
      enabled: true
      order: 1
      headerName: 'X-CORRELATION_ID'
      resources:
        - '/api/hello/*'
        - '/api/teste/'
```

You also can log your Request/Response. Just put the annotation *@EnableRequestResponseLogging* in your method. For exemplo:

``` java
 @GetMapping("/{msg}")
    @ResponseStatus(HttpStatus.FOUND)
    @EnableRequestResponseLogging
    public String hello(@PathVariable("msg") String msg ) {  

...

```

Some fields are fixed:

```
{
  "timestamp" : "2021-03-21 16:56",
  "level" : "INFO",
  "applicationName" : "testeApplication",
  "threadName" : "http-nio-8081-exec-1",
  "loggerName" : "org.springframework.web.servlet.DispatcherServlet",
  "type" : "DEFAULT",
  "message" : "Completed initialization in 17 ms"
}
```

There area four types:

DEFAULT: when you use Sl4j method, like ```log.info() ```

CUSTOM: when you use some method, like ``` logInfoObject()```

REQUEST or RESPONSE: when the application is logging *requests* or *responses*.


Some examples:

```
{
  "timestamp" : "2021-03-21 16:56",
  "level" : "INFO",
  "applicationName" : "teste-api",
  "threadName" : "http-nio-8081-exec-5",
  "loggerName" : "LoggerHttp",
  "type" : "REQUEST",
  "request" : {
    "method" : "GET",
    "url" : "http://localhost:8081/api/hello/Rodrigo",
    "payload" : [
      "Rodrigo"
    ],
    "ip" : "0:0:0:0:0:0:0:1",
    "protocol" : "HTTP/1.1",
    "headers" : {
      "postman-token" : "a1b9743a-5fb6-436e-a9c0-b99f6e6441fc",
      "host" : "localhost:8081",
      "connection" : "keep-alive",
      "cache-control" : "no-cache",
      "accept-encoding" : "gzip, deflate, br",
      "user-agent" : "PostmanRuntime/7.26.10",
      "accept" : "*/*"
    }
  }
}
```


Or if you do: 
```
  logInfoProperty( "idSystem", "12345-09", "My property !!!");
```
Output will be:

```
{
  "timestamp" : "2021-03-21 16:56",
  "level" : "INFO",
  "applicationName" : "testeApplication",
  "threadName" : "http-nio-8081-exec-1",
  "loggerName" : "org.springframework.web.servlet.DispatcherServlet",
  "type" : "CUSTOM",
  "message" : "My property !!!"
  "idSystem" : "12345-09"  
}
```
If you have some object:

```
public class Person {

    private String name;
    private String doc;
    private String address;
}
```

You can do:

```
logInfoObject("person", p, "Person found!!!")
```

```
{
  "timestamp" : "2021-03-22 12:16",
  "level" : "INFO",
  "applicationName" : "teste-api",
  "threadName" : "http-nio-8081-exec-5",
  "loggerName" : "HelloController",
  "type" : "CUSTOM",
  "person" : {
    "name" : "Rodrigo",
    "doc" : "3214234234",
    "address" : "Rua ADADAS"
  }
}
```
 In a 1.1 version, you also can enable the Correlation Id Filter:
``` yaml
app:
  observability:
    applicationName: 'teste-api'
    correlationIdFilter:
      enabled: true
      order: 1
      headerName: 'X-CORRELATION_ID'
      resources:
        - '/api/hello/*'
        - '/api/teste/'

```

You can config a header name, where your correlation id comes from. The order field is 
the spring boot load order. And the resources are the urls you want to filter.


