package io.teiler.server.services;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.teiler.server.Tylr;
import io.teiler.server.dto.Compensation;
import io.teiler.server.dto.Debt;
import io.teiler.server.dto.Expense;
import io.teiler.server.dto.Group;
import io.teiler.server.dto.Person;
import io.teiler.server.dto.Profiteer;

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
    public void testReturnCorrectBalanceWithExpense() {
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
            if (debt.getPerson().getId() == firstPerson.getId()) {
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


    @Test
    public void testReturnNothingWithNoPersonInGroup() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();

        List<Debt> debts = debtService.getDebt(groupId);
        Assert.assertEquals(0, debts.size());
    }

    @Test
    public void testReturnCorrectBalanceWithCompensation() {
        final int personCount = 5;
        final int share = 10;

        Group group = groupService.createGroup(TEST_GROUP_NAME);
        List<Person> people = new LinkedList<>();

        for (int i = 0; i < personCount; i++) {
            people.add(personService.createPerson(group.getId(), "Person " + i));
        }

        Person payer = people.get(0);
        Person profiteer = people.get(people.size() - 1);

        Compensation compensation = new Compensation(null, share, payer, profiteer);
        compensationService.createCompensation(compensation, group.getId());

        List<Debt> debts = debtService.getDebt(group.getId());
        for(Debt debt : debts) {
            if (debt.getPerson().getId() == payer.getId()) {
                Assert.assertEquals(share, debt.getBalance().intValue());
            } else if (debt.getPerson().getId() == profiteer.getId()) {
                Assert.assertEquals(-share, debt.getBalance().intValue());
            } else {
                Assert.assertEquals(0, debt.getBalance().intValue());
            }
        }
    }

    @Test
    public void testReturnCorrectBalanceWithExpenseAndCompensation() {
        final int compensationShare = 5;
        final int expenseShare = 10;

        Group group = groupService.createGroup(TEST_GROUP_NAME);

        Person firstPerson = personService.createPerson(group.getId(), FIRST_PERSON_NAME);
        Person secondPerson = personService.createPerson(group.getId(), SECOND_PERSON_NAME);

        Compensation compensation = new Compensation(null, compensationShare, firstPerson, secondPerson);
        compensationService.createCompensation(compensation, group.getId());

        List<Profiteer> profiteers = new LinkedList<>();
        profiteers.add(new Profiteer(null, firstPerson, expenseShare));
        profiteers.add(new Profiteer(null, secondPerson, expenseShare));

        Expense firstExpense = new Expense(null, 2 * expenseShare, secondPerson, "Test", profiteers);
        expenseService.createExpense(firstExpense, group.getId());

        List<Debt> debts = debtService.getDebt(group.getId());
        Assert.assertEquals(-5, debts.get(0).getBalance().intValue());
        Assert.assertEquals(5, debts.get(1).getBalance().intValue());
    }

}
