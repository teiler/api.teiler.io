package io.teiler.api.endpoint;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import io.teiler.api.service.CompensationService;
import io.teiler.server.dto.Compensation;
import io.teiler.server.util.Error;
import io.teiler.server.util.GsonUtil;
import io.teiler.server.util.Normalize;
import io.teiler.server.util.exceptions.PersonNotFoundException;
import io.teiler.server.util.exceptions.TransactionNotFoundException;

/**
 * Controller for Compensation-related endpoints.
 * 
 * @author pbaechli
 */
@Controller
public class CompensationEndpointController implements EndpointController {

    private static final int DEFAULT_QUERY_LIMIT = 20;
    private static final String COMPENSATION_ID_PARAM = ":compensationid";
    private static final String LIMIT_PARAM = "limit";
    private static final String BASE_URL = GlobalEndpointController.URL_VERSION + "/groups/:groupid/compensations";
    private static final String URL_WITH_COMPENSATION_ID = BASE_URL + "/" + COMPENSATION_ID_PARAM;
    private Gson gson = GsonUtil.getHomebrewGson();
    
    @Autowired
    private CompensationService compensationService;

    @Override
    public void register() {
        post(BASE_URL, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalize.normalizeGroupId(groupId);
            Compensation requestCompensation = gson.fromJson(req.body(), Compensation.class);
            Compensation newCompensation = compensationService.createCompensation(requestCompensation, groupId);
            return gson.toJson(newCompensation);
        });

        get(BASE_URL, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalize.normalizeGroupId(groupId);
            String limitString = req.queryParams(LIMIT_PARAM);
            long limit = DEFAULT_QUERY_LIMIT;
            if (limitString != null) {
                limit = Long.parseLong(limitString);
            }
            List<Compensation> compensations = compensationService.getLastCompensations(groupId, limit);
            return gson.toJson(compensations);
        });
        
        get(URL_WITH_COMPENSATION_ID, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalize.normalizeGroupId(groupId);
            int compensationId = Integer.parseInt(req.params(COMPENSATION_ID_PARAM));
            Compensation compensation = compensationService.getCompensation(groupId, compensationId);
            return gson.toJson(compensation);
        });

        put(URL_WITH_COMPENSATION_ID, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalize.normalizeGroupId(groupId);
            int compensationId = Integer.parseInt(req.params(COMPENSATION_ID_PARAM));
            Compensation changedCompensation = gson.fromJson(req.body(), Compensation.class);
            Compensation compensation = compensationService.editCompensation(groupId, compensationId, changedCompensation);
            return gson.toJson(compensation);
        });

        delete(URL_WITH_COMPENSATION_ID, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalize.normalizeGroupId(groupId);
            int compensationId = Integer.parseInt(req.params(COMPENSATION_ID_PARAM));
            compensationService.deleteCompensation(groupId, compensationId);
            return "";
        });

        exception(TransactionNotFoundException.class, (e, request, response) -> {
            response.status(404);
            Error error = new Error(e.getMessage());
            response.body(gson.toJson(error));
        });

        exception(PersonNotFoundException.class, (e, request, response) -> {
            response.status(404);
            Error error = new Error(e.getMessage());
            response.body(gson.toJson(error));
        });
        
        exception(PersonNotFoundException.class, (e, request, response) -> {
            response.status(409);
            Error error = new Error(e.getMessage());
            response.body(gson.toJson(error));
        });
    }

}
