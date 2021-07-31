package com.hendisantika.zipkinservice1;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class SpringBootZipkinExampleService1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootZipkinExampleService1Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

@RestController
class ExampleController {

    private static final Log log = LogFactory.getLog(ExampleController.class);
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test")
    public String testZipkin() throws URISyntaxException {
        log.info("calling service!!");
        String message = restTemplate.getForObject(new URI("http://localhost:8081/test2"), String.class);
        log.info("message from service 2: " + message);
        return "working!!";
    }
}
