package io.teiler.api.endpoint;

import static spark.Spark.get;
import static spark.Spark.before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HelloWorld {

    private static Logger LOGGER = LoggerFactory.getLogger(HelloWorld.class);
    
    public HelloWorld() {
        register();
    }
    
    public void register() {
        before((req, res) -> LOGGER.info("API call to " + req.pathInfo()));
        
        get("/hello", (req, res) -> "Hello World");
        get("/hello/:name", (req, res) -> "Hello " + req.params(":name"));
    }
    
}
