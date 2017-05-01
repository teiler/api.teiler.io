package io.teiler.api.service;

import io.teiler.server.Tylr;
import io.teiler.server.dto.Expense;
import io.teiler.server.dto.Group;
import io.teiler.server.dto.Person;
import io.teiler.server.dto.Profiteer;
import io.teiler.server.service.ExpenseService;
import io.teiler.server.service.GroupService;
import io.teiler.server.service.PersonService;
import io.teiler.server.util.exceptions.PayerInactiveException;
import io.teiler.server.util.exceptions.PayerNotFoundException;
import io.teiler.server.util.exceptions.ProfiteerInactiveException;
import io.teiler.server.util.exceptions.ProfiteerNotFoundException;
import io.teiler.server.util.exceptions.SharesNotAddingUpException;
import io.teiler.server.util.exceptions.TransactionNotFoundException;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"local.server.port=4567"})
@AutoConfigureTestDatabase
public class ExpenseServiceTest {

    private static final String TEST_GROUP_NAME = "Testgruppe";

    private static final String TEST_EXPENSE_TITLE = "Testausgabe";
    private static final Integer TEST_EXPENSE_AMOUNT = 4200;
    private static final String TEST_PAYER_AND_PROFITEER = "Hans";
    private static final Integer TEST_PAYER_AND_PROFITEER_SHARE = 1200;
    private static final String TEST_PROFITEER_1 = "Paul";
    private static final Integer TEST_PROFITEER_1_SHARE = 1500;
    private static final String TEST_PROFITEER_2 = "Peter";
    private static final Integer TEST_PROFITEER_2_SHARE = 1500;

    @Autowired
    private ExpenseService expenseService;
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private PersonService personService;

    @Test
    public void testCreateExpenseWithSingleProfiteer() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayerAndProfiteer = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, testPayerAndProfiteer, TEST_PAYER_AND_PROFITEER_SHARE));

        Expense testExpense = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE, testPayerAndProfiteer, TEST_EXPENSE_TITLE, testProfiteers);

        Expense testExpenseResponse = expenseService.createExpense(testExpense, testGroup.getId());

        Assert.assertEquals(TEST_EXPENSE_TITLE, testExpenseResponse.getTitle());
        Assert.assertEquals(TEST_PAYER_AND_PROFITEER_SHARE, testExpenseResponse.getAmount());
        Assert.assertEquals(testPayerAndProfiteer.getId(), testExpenseResponse.getPayer().getId());

        Assert.assertFalse(testExpenseResponse.getProfiteers().isEmpty());
        Assert.assertEquals(testProfiteers.size(), testExpenseResponse.getProfiteers().size());
    }

    @Test
    public void testCreateExpenseWithMultipleProfiteers() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayerAndProfiteer = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);
        Person testProfiteer1 = personService.createPerson(testGroup.getId(), TEST_PROFITEER_1);
        Person testProfiteer2 = personService.createPerson(testGroup.getId(), TEST_PROFITEER_2);

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, testPayerAndProfiteer, TEST_PAYER_AND_PROFITEER_SHARE));
        testProfiteers.add(new Profiteer(null, testProfiteer1, TEST_PROFITEER_1_SHARE));
        testProfiteers.add(new Profiteer(null, testProfiteer2, TEST_PROFITEER_2_SHARE));

        Expense testExpense = new Expense(null, TEST_EXPENSE_AMOUNT, testPayerAndProfiteer, TEST_EXPENSE_TITLE, testProfiteers);

        Expense testExpenseResponse = expenseService.createExpense(testExpense, testGroup.getId());

        Assert.assertEquals(TEST_EXPENSE_TITLE, testExpenseResponse.getTitle());
        Assert.assertEquals(TEST_EXPENSE_AMOUNT, testExpenseResponse.getAmount());
        Assert.assertEquals(testPayerAndProfiteer.getId(), testExpenseResponse.getPayer().getId());
        
        Assert.assertFalse(testExpenseResponse.getProfiteers().isEmpty());
        Assert.assertEquals(testProfiteers.size(), testExpenseResponse.getProfiteers().size());
    }


    @Test(expected = SharesNotAddingUpException.class)
    public void testCreateExpenseSharesDontAddUp() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayerAndProfiteer = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);
        Person testProfiteer1 = personService.createPerson(testGroup.getId(), TEST_PROFITEER_1);
        Person testProfiteer2 = personService.createPerson(testGroup.getId(), TEST_PROFITEER_2);

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, testPayerAndProfiteer, TEST_PAYER_AND_PROFITEER_SHARE + 100));
        testProfiteers.add(new Profiteer(null, testProfiteer1, TEST_PROFITEER_1_SHARE + 100));
        testProfiteers.add(new Profiteer(null, testProfiteer2, TEST_PROFITEER_2_SHARE + 100));

        Expense testExpense = new Expense(null, TEST_EXPENSE_AMOUNT, testPayerAndProfiteer, TEST_EXPENSE_TITLE, testProfiteers);

        expenseService.createExpense(testExpense, testGroup.getId());
    }

    @Test(expected = PayerNotFoundException.class)
    public void testCreateExpenseWithPayerFromDifferentGroup() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);

        Group differentGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person differentGroupPerson = personService.createPerson(differentGroup.getId(), TEST_PAYER_AND_PROFITEER);

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, differentGroupPerson, TEST_PAYER_AND_PROFITEER_SHARE));

        Expense testExpense = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE, differentGroupPerson, TEST_EXPENSE_TITLE, testProfiteers);

        expenseService.createExpense(testExpense, testGroup.getId());
    }

    @Test(expected = ProfiteerNotFoundException.class)
    public void testCreateExpenseWithProfiteerFromDifferentGroup() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPerson = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);

        Group differentGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person differentProfiteer = personService.createPerson(differentGroup.getId(), TEST_PROFITEER_1);

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, differentProfiteer, TEST_PAYER_AND_PROFITEER_SHARE));

        Expense testExpense = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE, testPerson, TEST_EXPENSE_TITLE, testProfiteers);

        expenseService.createExpense(testExpense, testGroup.getId());
    }

    @Test
    public void testViewExpense() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayerAndProfiteer = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);
        Person testProfiteer1 = personService.createPerson(testGroup.getId(), TEST_PROFITEER_1);
        Person testProfiteer2 = personService.createPerson(testGroup.getId(), TEST_PROFITEER_2);

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, testPayerAndProfiteer, TEST_PAYER_AND_PROFITEER_SHARE));
        testProfiteers.add(new Profiteer(null, testProfiteer1, TEST_PROFITEER_1_SHARE));
        testProfiteers.add(new Profiteer(null, testProfiteer2, TEST_PROFITEER_2_SHARE));

        Expense testExpense = new Expense(null, TEST_EXPENSE_AMOUNT, testPayerAndProfiteer, TEST_EXPENSE_TITLE, testProfiteers);

        Expense testExpenseResponse = expenseService.createExpense(testExpense, testGroup.getId());
        Expense viewResponse = expenseService.getExpense(testGroup.getId(), testExpenseResponse.getId());

        Assert.assertEquals(TEST_EXPENSE_TITLE, viewResponse.getTitle());
        Assert.assertEquals(TEST_EXPENSE_AMOUNT, viewResponse.getAmount());
        Assert.assertEquals(testPayerAndProfiteer.getId(), viewResponse.getPayer().getId());

        Assert.assertFalse(viewResponse.getProfiteers().isEmpty());
        Assert.assertEquals(testProfiteers.size(), viewResponse.getProfiteers().size());
    }

    @Test(expected = TransactionNotFoundException.class)
    public void testViewExpenseFromDifferentGroup() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayerAndProfiteer = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, testPayerAndProfiteer, TEST_PAYER_AND_PROFITEER_SHARE));

        Expense testExpense = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE, testPayerAndProfiteer, TEST_EXPENSE_TITLE, testProfiteers);

        expenseService.createExpense(testExpense, testGroup.getId());

        Group differentGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person differentPerson = personService.createPerson(differentGroup.getId(), TEST_PAYER_AND_PROFITEER);

        List<Profiteer> testDifferentProfiteers = new LinkedList<>();
        testDifferentProfiteers.add(new Profiteer(null, differentPerson, TEST_PAYER_AND_PROFITEER_SHARE));

        Expense testDifferentExpense = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE, differentPerson, TEST_EXPENSE_TITLE, testDifferentProfiteers);

        Expense testDifferentExpenseResponse = expenseService.createExpense(testDifferentExpense, differentGroup.getId());

        expenseService.getExpense(testGroup.getId(), testDifferentExpenseResponse.getId());
    }

    @Test
    public void testEditExpenseTitle() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayerAndProfiteer = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, testPayerAndProfiteer, TEST_PAYER_AND_PROFITEER_SHARE));

        Expense testExpense = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE, testPayerAndProfiteer, TEST_EXPENSE_TITLE, testProfiteers);

        Expense testExpenseResponse = expenseService.createExpense(testExpense, testGroup.getId());

        testExpenseResponse.setTitle("Getrocknete Bananen");

        Expense changedExpense = expenseService.editExpense(testGroup.getId(), testExpenseResponse.getId(), testExpenseResponse);

        Assert.assertEquals("Getrocknete Bananen", changedExpense.getTitle());
    }

    @Test
    public void testEditExpenseAmountAndShares() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayerAndProfiteer = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, testPayerAndProfiteer, TEST_PAYER_AND_PROFITEER_SHARE));

        Expense testExpense = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE, testPayerAndProfiteer, TEST_EXPENSE_TITLE, testProfiteers);

        Expense testExpenseResponse = expenseService.createExpense(testExpense, testGroup.getId());

        testExpenseResponse.setAmount(1000);
        // The "get" part is a bit hacky but it's just too much code otherwise...
        testProfiteers.get(0).setShare(1000);

        testExpenseResponse.setProfiteers(testProfiteers);

        expenseService.editExpense(testGroup.getId(), testExpenseResponse.getId(), testExpenseResponse);
    }

    @Test(expected = SharesNotAddingUpException.class)
    public void testEditExpenseAmountAndSharesWrongly() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayerAndProfiteer = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, testPayerAndProfiteer, TEST_PAYER_AND_PROFITEER_SHARE));

        Expense testExpense = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE, testPayerAndProfiteer, TEST_EXPENSE_TITLE, testProfiteers);

        Expense testExpenseResponse = expenseService.createExpense(testExpense, testGroup.getId());

        testExpenseResponse.setAmount(1000);
        // The "get" part is a bit hacky but it's just too much code otherwise...
        testProfiteers.get(0).setShare(1000+100);

        testExpenseResponse.setProfiteers(testProfiteers);

        expenseService.editExpense(testGroup.getId(), testExpenseResponse.getId(), testExpenseResponse);
    }

    @Test
    public void testEditExpenseNewProfiteer() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayerAndProfiteer = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);
        Person testProfiteer1 = personService.createPerson(testGroup.getId(), TEST_PROFITEER_1);
        Person testProfiteer2 = personService.createPerson(testGroup.getId(), TEST_PROFITEER_2);

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, testPayerAndProfiteer, TEST_PAYER_AND_PROFITEER_SHARE));
        testProfiteers.add(new Profiteer(null, testProfiteer1, TEST_PROFITEER_1_SHARE));

        Expense testExpense = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE + TEST_PROFITEER_1_SHARE, testPayerAndProfiteer, TEST_EXPENSE_TITLE, testProfiteers);

        Expense testExpenseResponse = expenseService.createExpense(testExpense, testGroup.getId());

        testProfiteers.add(new Profiteer(null, testProfiteer2, TEST_PROFITEER_2_SHARE));
        testExpenseResponse.setProfiteers(testProfiteers);
        testExpenseResponse.setAmount(testExpenseResponse.getAmount() + TEST_PROFITEER_2_SHARE);

        Expense testEditedExpense = expenseService.editExpense(testGroup.getId(), testExpenseResponse.getId(), testExpenseResponse);

        Assert.assertEquals(TEST_EXPENSE_TITLE, testEditedExpense.getTitle());
        Assert.assertEquals(TEST_EXPENSE_AMOUNT, testEditedExpense.getAmount());
        Assert.assertEquals(testPayerAndProfiteer.getId(), testEditedExpense.getPayer().getId());

        Assert.assertFalse(testEditedExpense.getProfiteers().isEmpty());
        Assert.assertEquals(testProfiteers.size(), testEditedExpense.getProfiteers().size());
    }

    @Test
    public void testLastExpensesOrderIsCorrect() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayerAndProfiteer = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, testPayerAndProfiteer, TEST_PAYER_AND_PROFITEER_SHARE));

        Expense testExpense = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE, testPayerAndProfiteer, TEST_EXPENSE_TITLE, testProfiteers);
        Expense testExpense2 = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE, testPayerAndProfiteer, TEST_EXPENSE_TITLE + " zwei", testProfiteers);

        expenseService.createExpense(testExpense, testGroup.getId());
        expenseService.createExpense(testExpense2, testGroup.getId());

        List<Expense> expenses = expenseService.getLastExpenses(testGroup.getId(), 3);

        Assert.assertEquals(TEST_EXPENSE_TITLE + " zwei", expenses.get(0).getTitle());
    }

    @Test(expected = TransactionNotFoundException.class)
    public void testDeleteExpense() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayerAndProfiteer = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, testPayerAndProfiteer, TEST_PAYER_AND_PROFITEER_SHARE));

        Expense testExpense = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE, testPayerAndProfiteer, TEST_EXPENSE_TITLE, testProfiteers);

        Expense testExpenseResponse = expenseService.createExpense(testExpense, testGroup.getId());

        expenseService.deleteExpense(testGroup.getId(), testExpenseResponse.getId());

        expenseService.getExpense(testGroup.getId(), testExpenseResponse.getId());
    }

    @Test(expected = PayerInactiveException.class)
    public void testCreateExpenseWithInactivePayer() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayer = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);
        Person testProfiteer = personService.createPerson(testGroup.getId(), TEST_PROFITEER_1);

        personService.deactivatePerson(testGroup.getId(), testPayer.getId());

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, testPayer, TEST_PAYER_AND_PROFITEER_SHARE));
        testProfiteers.add(new Profiteer(null, testProfiteer, TEST_PROFITEER_1_SHARE));

        Expense testExpense = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE +
            TEST_PROFITEER_1_SHARE, testPayer, TEST_EXPENSE_TITLE, testProfiteers);

        expenseService.createExpense(testExpense, testGroup.getId());
    }

    @Test(expected = ProfiteerInactiveException.class)
    public void testCreateExpenseWithInactiveProfiteer() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayer = personService.createPerson(testGroup.getId(), TEST_PAYER_AND_PROFITEER);
        Person testProfiteer = personService.createPerson(testGroup.getId(), TEST_PROFITEER_1);

        personService.deactivatePerson(testGroup.getId(), testProfiteer.getId());

        List<Profiteer> testProfiteers = new LinkedList<>();
        testProfiteers.add(new Profiteer(null, testPayer, TEST_PAYER_AND_PROFITEER_SHARE));
        testProfiteers.add(new Profiteer(null, testProfiteer, TEST_PROFITEER_1_SHARE));

        Expense testExpense = new Expense(null, TEST_PAYER_AND_PROFITEER_SHARE +
            TEST_PROFITEER_1_SHARE, testPayer, TEST_EXPENSE_TITLE, testProfiteers);

        expenseService.createExpense(testExpense, testGroup.getId());
    }

}
