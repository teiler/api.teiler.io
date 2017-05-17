package io.teiler.server.endpoints;

import static spark.Spark.get;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import io.teiler.server.dto.Compensation;
import io.teiler.server.endpoints.util.EndpointUtil;
import io.teiler.server.services.SuggestedCompensationsService;
import io.teiler.server.util.HomebrewGson;

/**
 * Controller for suggested compensations.
 *
 * @author dthoma
 */
@Controller
public class SuggestedCompensationsEndpointController implements EndpointController {

    private static final String BASE_URL = GlobalEndpointController.URL_VERSION + "/groups/"
        + EndpointUtil.GROUP_ID_PARAM + "/settleup";

    @Autowired
    private SuggestedCompensationsService suggestedCompensationService;

    @Override
    public void register() {
        get(BASE_URL, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            List<Compensation> suggestedCompensations = suggestedCompensationService
                .getSuggestedCompensations(groupId);
            return HomebrewGson.getInstance().toJson(suggestedCompensations);
        });
    }
    
}
