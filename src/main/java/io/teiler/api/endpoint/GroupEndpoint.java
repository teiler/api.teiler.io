package io.teiler.api.endpoint;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;

import io.teiler.api.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class GroupEndpoint implements Endpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupEndpoint.class);


    @Autowired
    GroupService groupService;

    @Override
    public void register() {
        before((req, res) -> LOGGER.debug("API call to '" + req.pathInfo() + "'"));

        post("/v1/group", (req, res) -> {
            return groupService.handlePost(req);
        });

        get("/v1/group", (req, res) -> {
            return groupService.handleGet(req);
        });


    }
}
