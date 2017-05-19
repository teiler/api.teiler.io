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

import io.teiler.server.dto.Group;
import io.teiler.server.endpoints.util.EndpointUtil;
import io.teiler.server.services.GroupService;
import io.teiler.server.util.HomebrewGson;
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
    private static final String URL_WITH_GROUP_ID = BASE_URL + "/" + EndpointUtil.GROUP_ID_PARAM;

    @Autowired
    private GroupService groupService;

    @Override
    public void register() {
        post(BASE_URL, (req, res) -> {
            Group requestGroup = HomebrewGson.getInstance().fromJson(req.body(), Group.class);
            Group newGroup = groupService.createGroup(requestGroup.getName());
            return HomebrewGson.getInstance().toJson(newGroup);
        });

        get(URL_WITH_GROUP_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            Boolean activeOnly = EndpointUtil.readActive(req, true);
            Group requestGroup = groupService.viewGroup(groupId, activeOnly);
            return HomebrewGson.getInstance().toJson(requestGroup);
        });

        put(URL_WITH_GROUP_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            Group changedGroup = HomebrewGson.getInstance().fromJson(req.body(), Group.class);
            Group group = groupService.editGroup(groupId, changedGroup);
            return HomebrewGson.getInstance().toJson(group);
        });

        delete(URL_WITH_GROUP_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            groupService.deleteGroup(groupId);
            return "";
        });

        exception(CurrencyNotValidException.class, (e, request, response) ->
            EndpointUtil.prepareErrorResponse(response, 416, e));
    }

}
