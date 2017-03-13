package io.teiler.server.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.teiler.api.endpoint.HelloWorld;

@Configuration
public class SparkConfiguration {
    
    @Bean
    CommandLineRunner sparkRunner() {
        return args -> new HelloWorld();
    }
    
}
