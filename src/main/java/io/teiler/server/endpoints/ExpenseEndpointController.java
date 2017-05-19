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

import io.teiler.server.dto.Expense;
import io.teiler.server.endpoints.util.EndpointUtil;
import io.teiler.server.services.ExpenseService;
import io.teiler.server.util.HomebrewGson;
import io.teiler.server.util.exceptions.SharesNotAddingUpException;
import io.teiler.server.util.exceptions.TransactionNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Controller for Expense-related endpoints.
 *
 * @author pbaechli
 */
@Controller
public class ExpenseEndpointController implements EndpointController {

    private static final int DEFAULT_QUERY_LIMIT = 20;
    private static final String EXPENSE_ID_PARAM = ":expenseid";
    private static final String BASE_URL = GlobalEndpointController.URL_VERSION + "/groups/"
        + EndpointUtil.GROUP_ID_PARAM + "/expenses";
    private static final String URL_WITH_EXPENSE_ID = BASE_URL + "/" + EXPENSE_ID_PARAM;
    
    @Autowired
    private ExpenseService expenseService;

    @Override
    public void register() {
        post(BASE_URL, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            Expense requestExpense = HomebrewGson.getInstance().fromJson(req.body(), Expense.class);
            Expense newExpense = expenseService.createExpense(requestExpense, groupId);
            return HomebrewGson.getInstance().toJson(newExpense);
        });

        get(BASE_URL, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            long limit = EndpointUtil.readLimit(req, DEFAULT_QUERY_LIMIT);
            List<Expense> expenses = expenseService.getLastExpenses(groupId, limit);
            return HomebrewGson.getInstance().toJson(expenses);
        });

        get(URL_WITH_EXPENSE_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            int expenseId = Integer.parseInt(req.params(EXPENSE_ID_PARAM));
            Expense expense = expenseService.getExpense(groupId, expenseId);
            return HomebrewGson.getInstance().toJson(expense);
        });

        put(URL_WITH_EXPENSE_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            int expenseId = Integer.parseInt(req.params(EXPENSE_ID_PARAM));
            Expense changedExpense = HomebrewGson.getInstance().fromJson(req.body(), Expense.class);
            Expense expense = expenseService.editExpense(groupId, expenseId, changedExpense);
            return HomebrewGson.getInstance().toJson(expense);
        });

        delete(URL_WITH_EXPENSE_ID, (req, res) -> {
            String groupId = EndpointUtil.readGroupId(req);
            int expenseId = Integer.parseInt(req.params(EXPENSE_ID_PARAM));
            expenseService.deleteExpense(groupId, expenseId);
            return "";
        });

        exception(TransactionNotFoundException.class, (e, request, response) ->
            EndpointUtil.prepareErrorResponse(response, 404, e));

        exception(SharesNotAddingUpException.class, (e, request, response) ->
            EndpointUtil.prepareErrorResponse(response, 406, e));
    }

}
