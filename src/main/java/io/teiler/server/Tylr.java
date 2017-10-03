package io.teiler.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * As of now, the only purpose of this class is to fire up the whole application.
 *
 * @author pbaechli
 */
@SpringBootApplication
@ComponentScan("io.teiler")
public class Tylr {

    public static void main(String[] args) {
        SpringApplication.run(Tylr.class, args);
    }

}
