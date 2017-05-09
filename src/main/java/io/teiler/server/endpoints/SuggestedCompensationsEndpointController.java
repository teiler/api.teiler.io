package io.teiler.server.endpoints;

import static spark.Spark.get;

import com.google.gson.Gson;
import io.teiler.server.dto.Compensation;
import io.teiler.server.endpoints.util.GroupIdReader;
import io.teiler.server.services.SuggestCompensationService;
import io.teiler.server.util.GsonUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Controller for suggested payments.
 *
 * @author dthoma
 */
@Controller
public class SuggestedCompensationsEndpointController implements EndpointController {

    private static final String BASE_URL = GlobalEndpointController.URL_VERSION + "/groups/:groupid/settleup";
    private Gson gson = GsonUtil.getHomebrewGson();

    @Autowired
    private SuggestCompensationService suggestCompensationService;

    @Override
    public void register() {
        get(BASE_URL, (req, res) -> {
            String groupId = GroupIdReader.getGroupId(req);
            List<Compensation> suggestedCompensations = suggestCompensationService
                .getSuggestedCompensations(groupId);
            return gson.toJson(suggestedCompensations);
        });
    }
}
