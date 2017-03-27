package io.teiler.api.endpoint;

import static spark.Spark.before;
import static spark.Spark.get;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import io.teiler.server.persistence.repositories.DummyRepository;

@Component
public class Dummy implements Endpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dummy.class);
    
    @Autowired
    private DummyRepository dummyRepository;
    
    private Gson gson;

    public Dummy() {
        register();
        gson = new Gson();
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

        get("/dummy/all/txt", (req, res) -> dummyRepository.getAll());
        get("/dummy/all/json", (req, res) -> gson.toJson(dummyRepository.getAll()));
        //get("/hello/:name", (req, res) -> "Hello " + req.params(":name"));
    }

}
