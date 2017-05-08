package io.teiler.server.config;

import static spark.Spark.port;
import static spark.Spark.ipAddress;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.teiler.server.endpoints.EndpointController;

/**
 * Holds the configuration and programmatic setup required for Spark.
 * 
 * @author pbaechli
 */
@Configuration
@EnableTransactionManagement
public class SparkConfiguration {

    @Value("${server.ip}")
    private String ip;

    @Value("${server.port}")
    private int port;

    @Autowired
    private List<EndpointController> endpoints = new ArrayList<>();

    @PostConstruct
    public void init() {
        ipAddress(ip);
        port(port);
    }

    @Bean
    CommandLineRunner sparkRunner() {
        return args -> endpoints.stream().forEach(EndpointController::register);
    }

}
