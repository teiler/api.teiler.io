/**
 * MIT License
 *
 * Copyright (c) 2017 L. Röllin, P. Bächli, K. Thurairatnam & D. Thoma
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
