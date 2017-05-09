package io.teiler.server.endpoints;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;
import io.teiler.server.dto.Group;
import io.teiler.server.endpoints.util.EndpointUtil;
import io.teiler.server.services.GroupService;
import io.teiler.server.util.Error;
import io.teiler.server.util.GsonUtil;
import io.teiler.server.util.exceptions.CurrencyNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Controller for Group-related endpoints.
 *
 * @author lroellin
 */
@Controller
public class GroupEndpointController implements EndpointController {

    private static final String BASE_URL = GlobalEndpointController.URL_VERSION + "/groups";
    private static final String URL_WITH_GROUP_ID = BASE_URL + "/:groupid";

    private Gson gson = GsonUtil.getHomebrewGson();

    @Autowired
    private GroupService groupService;

    @Override
    public void register() {
        post(BASE_URL, (req, res) -> {
            Group requestGroup = gson.fromJson(req.body(), Group.class);
            Group newGroup = groupService.createGroup(requestGroup.getName());
            return gson.toJson(newGroup);
        });

        get(URL_WITH_GROUP_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            Boolean activeOnly = EndpointUtil.readActive(req, true);
            Group requestGroup = groupService.viewGroup(groupId, activeOnly);
            return gson.toJson(requestGroup);
        });

        put(URL_WITH_GROUP_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            Group changedGroup = gson.fromJson(req.body(), Group.class);
            Group group = groupService.editGroup(groupId, changedGroup);
            return gson.toJson(group);
        });

        delete(URL_WITH_GROUP_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            groupService.deleteGroup(groupId);
            return "";
        });

        exception(CurrencyNotValidException.class, (e, request, response) -> {
            response.status(416);
            Error error = new Error(e.getMessage());
            response.body(gson.toJson(error));
        });
    }

}
