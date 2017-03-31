package io.teiler.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("io.teiler")
public class Tylr {

    public static void main(String[] args) {
        SpringApplication.run(Tylr.class, args);
    }
    
}
