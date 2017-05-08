package io.teiler.server.endpoints;

import static spark.Spark.get;

import io.teiler.server.dto.Debt;
import io.teiler.server.util.Normalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import io.teiler.server.services.DebtService;
import io.teiler.server.util.GsonUtil;

import java.util.List;

/**
 * Controller for Compensation-related endpoints.
 *
 * @author dthoma
 */
@Controller
public class DebtEndpointController implements EndpointController {

    private static final String BASE_URL = GlobalEndpointController.URL_VERSION + "/groups/:groupid/debts";
    private Gson gson = GsonUtil.getHomebrewGson();

    @Autowired
    private DebtService debtService;

    @Override
    public void register() {
        get(BASE_URL, (req, res) -> {
            String groupID = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupID = Normalize.normalizeGroupId(groupID);
            List<Debt> debts = debtService.getDebts(groupID);
            return gson.toJson(debts);
        });
    }

}