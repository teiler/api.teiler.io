package io.teiler.api.endpoint;

import static spark.Spark.before;
import static spark.Spark.get;

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
        before((req, res) -> LOGGER.debug("API call to '" + req.pathInfo() + "'"));
        before((req, res) -> {
            String path = req.pathInfo();
            if (path.endsWith("/")) {
                String newPath = path.substring(0, path.length() - 1);
                LOGGER.debug("Call to '" + path + "' redirected to '" + newPath + "'");
                res.redirect(newPath);
            }
        });

        get("/hello", (req, res) -> "Hello World");
        get("/hello/:name", (req, res) -> "Hello " + req.params(":name"));
    }

}
