package io.teiler.server.endpoints;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;
import io.teiler.server.dto.Expense;
import io.teiler.server.services.ExpenseService;
import io.teiler.server.util.Error;
import io.teiler.server.util.GsonUtil;
import io.teiler.server.util.Normalizer;
import io.teiler.server.util.exceptions.PersonNotFoundException;
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
    private static final String LIMIT_PARAM = "limit";
    private static final String BASE_URL = GlobalEndpointController.URL_VERSION + "/groups/:groupid/expenses";
    private static final String URL_WITH_EXPENSE_ID = BASE_URL + "/" + EXPENSE_ID_PARAM;
    private Gson gson = GsonUtil.getHomebrewGson();
    @Autowired
    private ExpenseService expenseService;

    @Override
    public void register() {
        post(BASE_URL, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalizer.normalizeGroupId(groupId);
            Expense requestExpense = gson.fromJson(req.body(), Expense.class);
            Expense newExpense = expenseService.createExpense(requestExpense, groupId);
            return gson.toJson(newExpense);
        });

        get(BASE_URL, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalizer.normalizeGroupId(groupId);
            String limitString = req.queryParams(LIMIT_PARAM);
            long limit = DEFAULT_QUERY_LIMIT;
            if (limitString != null) {
                limit = Long.parseLong(limitString);
            }
            List<Expense> expenses = expenseService.getLastExpenses(groupId, limit);
            return gson.toJson(expenses);
        });

        get(URL_WITH_EXPENSE_ID, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalizer.normalizeGroupId(groupId);
            int expenseId = Integer.parseInt(req.params(EXPENSE_ID_PARAM));
            Expense expense = expenseService.getExpense(groupId, expenseId);
            return gson.toJson(expense);
        });

        put(URL_WITH_EXPENSE_ID, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalizer.normalizeGroupId(groupId);
            int expenseId = Integer.parseInt(req.params(EXPENSE_ID_PARAM));
            Expense changedExpense = gson.fromJson(req.body(), Expense.class);
            Expense expense = expenseService.editExpense(groupId, expenseId, changedExpense);
            return gson.toJson(expense);
        });

        delete(URL_WITH_EXPENSE_ID, (req, res) -> {
            String groupId = req.params(GroupEndpointController.GROUP_ID_PARAM);
            groupId = Normalizer.normalizeGroupId(groupId);
            int expenseId = Integer.parseInt(req.params(EXPENSE_ID_PARAM));
            expenseService.deleteExpense(groupId, expenseId);
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

        exception(SharesNotAddingUpException.class, (e, request, response) -> {
            response.status(406);
            Error error = new Error(e.getMessage());
            response.body(gson.toJson(error));
        });
    }

}
