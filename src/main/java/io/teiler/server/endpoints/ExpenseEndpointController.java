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
import io.teiler.server.util.exceptions.ValueLessThanOrEqualToZeroException;

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

        exception(ValueLessThanOrEqualToZeroException.class, (e, request, response) ->
            EndpointUtil.prepareErrorResponse(response, 406, e));
    }

}
