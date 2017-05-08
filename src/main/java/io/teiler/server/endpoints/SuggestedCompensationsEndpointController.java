package io.teiler.server.endpoints;

import com.google.gson.Gson;
import io.teiler.server.dto.SuggestedCompensation;
import io.teiler.server.services.SuggestCompensationService;
import io.teiler.server.util.GsonUtil;
import io.teiler.server.util.Normalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

import static spark.Spark.get;

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
            String groupID = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupID = Normalizer.normalizeGroupId(groupID);
            List<SuggestedCompensation> suggestedCompensations = suggestCompensationService.getSuggestedCompensations(groupID);
            return gson.toJson(suggestedCompensations);
        });
    }
}
