package io.teiler.api.endpoint;

import static spark.Spark.get;
import static spark.Spark.post;

import com.google.gson.Gson;
import io.teiler.api.service.GroupService;
import io.teiler.server.dto.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GroupEndpointController implements EndpointController {

    private Gson gson = new Gson();

    private static final String GROUP_ID_HEADER = "X-Teiler-GroupID";

    @Autowired
    private GroupService groupService;

    @Override
    public void register() {
        post("/v1/group", (req, res) -> {
            Group requestGroup = gson.fromJson(req.body(), Group.class);
            return groupService.createGroup(requestGroup.getName());
        });

        get("/v1/group", (req, res) -> {
            String authorizationHeader = req.headers(GROUP_ID_HEADER);
            return groupService.viewGroup(authorizationHeader);
        });
    }
    
}
