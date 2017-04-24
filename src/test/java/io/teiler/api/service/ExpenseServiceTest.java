package io.teiler.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.teiler.server.Tylr;
import io.teiler.server.dto.Expense;
import io.teiler.server.dto.Group;
import io.teiler.server.dto.Person;
import io.teiler.server.dto.Share;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"local.server.port=4567"})
@AutoConfigureTestDatabase
public class ExpenseServiceTest {

    private static final String TEST_GROUP_NAME = "Testgruppe";

    private static final String TEST_EXPENSE_TITLE = "Testausgabe";
    private static final Integer TEST_EXPENSE_AMOUNT = 4200;
    private static final Person TEST_PAYER = new Person(-1, "Hans");
    private static final Person TEST_PROFITEER_1 = TEST_PAYER;
    private static final Person TEST_PROFITEER_2 = new Person(-2, "Paul");
    private static final Person TEST_PROFITEER_3 = new Person(-3, "Peter");
    private static List<Share> testShares = Collections.emptyList();

    @Autowired
    private ExpenseService expenseService;
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private PersonService personService;

    @Test
    public void testCreateExpenseWithSingleProfiteer() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        
        TEST_PROFITEER_1.setId(personService.createPerson(testGroup.getId(), TEST_PROFITEER_1.getName()).getId());
        TEST_PROFITEER_2.setId(personService.createPerson(testGroup.getId(), TEST_PROFITEER_2.getName()).getId());
        TEST_PROFITEER_3.setId(personService.createPerson(testGroup.getId(), TEST_PROFITEER_3.getName()).getId());
        
        // We have to set the shares here in order to have the correct Person-Ids in the Profiteers.
        testShares = getTestShares();
        
        Expense testExpense = new Expense(null, TEST_EXPENSE_AMOUNT, TEST_PAYER, TEST_EXPENSE_TITLE,
            testShares);
        Expense expense = expenseService.createExpense(testExpense, testGroup.getId());

        Assert.assertEquals(TEST_EXPENSE_TITLE, expense.getTitle());
        Assert.assertEquals(TEST_EXPENSE_AMOUNT, expense.getAmount());
        Assert.assertEquals(TEST_PAYER.getId(), expense.getPayer().getId());
        
        Assert.assertFalse(expense.getShares().isEmpty());
        Assert.assertEquals(testShares.size(), expense.getShares().size());
    }

    private static List<Share> getTestShares() {
        List<Share> shares = new ArrayList<>(3);
        shares.add(new Share(null, TEST_PROFITEER_1, 0.3));
        shares.add(new Share(null, TEST_PROFITEER_2, 0.3));
        shares.add(new Share(null, TEST_PROFITEER_3, 0.4));
        return shares;
    }

}
