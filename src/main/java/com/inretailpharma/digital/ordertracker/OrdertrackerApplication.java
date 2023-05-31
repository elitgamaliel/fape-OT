package com.inretailpharma.digital.ordertracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Clase principal, anotada como SpringBootApplication
 *
 * @author
 */
@SpringBootApplication
@EnableSwagger2
public class OrdertrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdertrackerApplication.class, args);
    }
}
