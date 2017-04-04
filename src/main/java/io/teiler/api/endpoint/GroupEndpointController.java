package io.teiler.api.endpoint;

import static spark.Spark.get;
import static spark.Spark.post;

import com.google.gson.Gson;
import io.teiler.api.service.GroupService;
import io.teiler.server.dto.Group;
import io.teiler.server.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Controller for Group-related endpoints.
 * 
 * @author lroellin
 */
@Controller
public class GroupEndpointController implements EndpointController {


    private Gson gson = GsonUtil.getHomebrewGson();

    @Autowired
    private GroupService groupService;

    @Override
    public void register() {
        post("/v1/groups", (req, res) -> {
            Group requestGroup = gson.fromJson(req.body(), Group.class);
            Group newGroup = groupService.createGroup(requestGroup.getName());
            return gson.toJson(newGroup);
        });

        get("/v1/groups/:id", (req, res) -> {
            String id = req.params(":id");
            Group requestGroup = groupService.viewGroup(id);
            return gson.toJson(requestGroup);
        });
    }

}