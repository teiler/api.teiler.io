package io.teiler.api.service;

import io.teiler.server.dto.*;
import io.teiler.server.services.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.teiler.server.Tylr;

import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"local.server.port=4567"})
@ActiveProfiles("test")
public class DebtServiceTest {

    private static final String FIRST_PERSON_NAME = "Johannes";
    private static final String SECOND_PERSON_NAME = "Rudolf";
    private static final String THIRD_PERSON_NAME = "Sepp";
    private static final String TEST_GROUP_NAME = "Testgroup";

    @Autowired
    private DebtService debtService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private CompensationService compensationService;

    @Autowired
    private PersonService personService;

    @Autowired
    private GroupService groupService;

    @Test
    public void testReturnCorrectBalance() {
        final int share = 5;

        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();

        Person firstPerson = personService.createPerson(groupId, FIRST_PERSON_NAME);
        Person secondPerson = personService.createPerson(groupId, SECOND_PERSON_NAME);
        Person thirdPerson = personService.createPerson(groupId, THIRD_PERSON_NAME);

        List<Profiteer> firstExpenseProfiteers = new LinkedList<>();
        firstExpenseProfiteers.add(new Profiteer(null, firstPerson, share));
        firstExpenseProfiteers.add(new Profiteer(null, secondPerson, share));
        firstExpenseProfiteers.add(new Profiteer(null, thirdPerson, share));

        Expense firstExpense = new Expense(null, 3 * share, firstPerson, "Test", firstExpenseProfiteers);
        expenseService.createExpense(firstExpense, groupId);

        for (Debt debt : debtService.getDebt(groupId)) {
            if (debt.getPerson() == firstPerson.getId()) {
                Assert.assertEquals(10, debt.getBalance().intValue());
            } else {
                Assert.assertEquals(-5, debt.getBalance().intValue());
            }
        }
    }

    @Test
    public void testReturnZeroBalanceWithOnePersonAndNoneTransactions() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();

        personService.createPerson(groupId, FIRST_PERSON_NAME);

        List<Debt> debts = debtService.getDebt(groupId);
        Assert.assertEquals(0, debts.get(0).getBalance().intValue());
    }

}
