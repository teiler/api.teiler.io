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
        final Integer SHARE = 5;

        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();

        Person firstPerson = personService.createPerson(groupId, FIRST_PERSON_NAME);
        Person secondPerson = personService.createPerson(groupId, SECOND_PERSON_NAME);
        Person thirdPerson = personService.createPerson(groupId, THIRD_PERSON_NAME);

        List<Profiteer> firstExpenseProfiteers = new LinkedList<>();
        firstExpenseProfiteers.add(new Profiteer(null, firstPerson, SHARE));
        firstExpenseProfiteers.add(new Profiteer(null, secondPerson, SHARE));
        firstExpenseProfiteers.add(new Profiteer(null, thirdPerson, SHARE));

        Expense firstExpense = new Expense(null, 3 * SHARE, firstPerson, "Test", firstExpenseProfiteers);
        expenseService.createExpense(firstExpense, groupId);

        System.out.println(firstPerson.getId());
        System.out.println(debtService.getDebt(groupId).toString());

        for (Debt debt : debtService.getDebt(groupId)) {
            if (debt.getPerson() == firstPerson.getId()) {
                Assert.assertEquals(new Integer(10), debt.getBalance());
            } else {
                Assert.assertEquals(new Integer(-5), debt.getBalance());
            }
        }
    }

    @Test
    public void testReturnZeroBalanceWithOnePersonAndNoneTransactions() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();

        Person firstPerson = personService.createPerson(groupId, FIRST_PERSON_NAME);

        List<Debt> debts = debtService.getDebt(groupId);
        Assert.assertEquals(new Integer(0), debts.get(0).getBalance());
    }

}
