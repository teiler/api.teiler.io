package io.teiler.server.endpoints;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import io.teiler.server.dto.Compensation;
import io.teiler.server.endpoints.util.EndpointUtil;
import io.teiler.server.services.CompensationService;
import io.teiler.server.util.HomebrewGson;
import io.teiler.server.util.exceptions.PayerProfiteerConflictException;
import io.teiler.server.util.exceptions.TransactionNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Controller for Compensation-related endpoints.
 *
 * @author pbaechli
 */
@Controller
public class CompensationEndpointController implements EndpointController {

    private static final int DEFAULT_QUERY_LIMIT = 20;
    private static final String COMPENSATION_ID_PARAM = ":compensationid";
    private static final String BASE_URL = GlobalEndpointController.URL_VERSION + "/groups/"
        + EndpointUtil.GROUP_ID_PARAM + "/compensations";
    private static final String URL_WITH_COMPENSATION_ID = BASE_URL + "/" + COMPENSATION_ID_PARAM;

    @Autowired
    private CompensationService compensationService;

    @Override
    public void register() {
        post(BASE_URL, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            Compensation requestCompensation = HomebrewGson.getInstance().fromJson(req.body(), Compensation.class);
            Compensation newCompensation = compensationService.createCompensation(requestCompensation, groupId);
            return HomebrewGson.getInstance().toJson(newCompensation);
        });

        get(BASE_URL, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            long limit = EndpointUtil.readLimit(req, DEFAULT_QUERY_LIMIT);
            List<Compensation> compensations = compensationService.getLastCompensations(groupId, limit);
            return HomebrewGson.getInstance().toJson(compensations);
        });

        get(URL_WITH_COMPENSATION_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            int compensationId = Integer.parseInt(req.params(COMPENSATION_ID_PARAM));
            Compensation compensation = compensationService.getCompensation(groupId, compensationId);
            return HomebrewGson.getInstance().toJson(compensation);
        });

        put(URL_WITH_COMPENSATION_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            int compensationId = Integer.parseInt(req.params(COMPENSATION_ID_PARAM));
            Compensation changedCompensation = HomebrewGson.getInstance().fromJson(req.body(), Compensation.class);
            Compensation compensation = compensationService
                .editCompensation(groupId, compensationId, changedCompensation);
            return HomebrewGson.getInstance().toJson(compensation);
        });

        delete(URL_WITH_COMPENSATION_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            int compensationId = Integer.parseInt(req.params(COMPENSATION_ID_PARAM));
            compensationService.deleteCompensation(groupId, compensationId);
            return "";
        });

        exception(TransactionNotFoundException.class, (e, request, response) ->
            EndpointUtil.prepareErrorResponse(response, 404, e));

        exception(PayerProfiteerConflictException.class, (e, request, response) ->
            EndpointUtil.prepareErrorResponse(response, 409, e));
    }

}
