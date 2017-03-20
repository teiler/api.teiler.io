package io.teiler.server.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.teiler.api.endpoint.Endpoint;

@Configuration
@EnableTransactionManagement
public class SparkConfiguration {
    
    @Autowired
    private List<Endpoint> endpoints = new ArrayList<>();

    @Bean
    CommandLineRunner sparkRunner() {
        return args -> endpoints.stream().forEach(e -> e.register());
    }

}
