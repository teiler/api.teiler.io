package io.teiler.server.services;

import io.teiler.server.Tylr;
import io.teiler.server.dto.Compensation;
import io.teiler.server.dto.Expense;
import io.teiler.server.dto.Group;
import io.teiler.server.dto.Person;
import io.teiler.server.dto.Profiteer;
import java.util.Arrays;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"local.server.port=4567"})
@ActiveProfiles("test")
public class SuggestedCompensationServiceTest {

    private static final String TEST_GROUP_NAME = "Testgroup";
    private static final String TEST_NAME_PREFIX = "Person ";

    @Autowired
    private SuggestedCompensationService suggestedCompensationService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private CompensationService compensationService;

    @Autowired
    private PersonService personService;

    @Autowired
    private GroupService groupService;

    /*
    Means that the balance of the person with the highest Debt is smaller than the balance of the person
    with the highest credit. This should test the if part of the algorithm.
     */
    @Test
    public void testReturnCorrectSuggestionsWithSameDebtAsCredit() {
        final int personCount = 5;
        final int share = 10;

        Group group = groupService.createGroup(TEST_GROUP_NAME);
        List<Person> people = new LinkedList<>();

        for (int i = 0; i < personCount; i++) {
            people.add(personService.createPerson(group.getId(), TEST_NAME_PREFIX + i));
        }

        Person payer = people.get(0);
        Person profiteer = people.get(people.size() - 1);

        Compensation compensation = new Compensation(null, share, payer, profiteer);
        compensationService.createCompensation(compensation, group.getId());

        List<Compensation> suggestedCompensations = suggestedCompensationService
            .getSuggestedCompensations(group.getId());
        Compensation suggestedCompensation = suggestedCompensations.get(0);

        Assert.assertEquals(payer.getId(), suggestedCompensation.getProfiteer().getId());
        Assert.assertEquals(profiteer.getId(), suggestedCompensation.getPayer().getId());
        Assert.assertEquals(share, suggestedCompensation.getAmount().intValue());
    }

    /*
    Means that the balance of the person with the highest debt is higher than the the balance of the person
    with the highest credit. This should the the else part of the algorithm.
     */
    @Test
    public void testReturnCorrectSuggestionsWithMoreDebtThanCredit() {
        Group group = groupService.createGroup(TEST_GROUP_NAME);

        List<Person> people = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            people.add(personService.createPerson(group.getId(), TEST_NAME_PREFIX + i));
        }

        Compensation firstCompensation = new Compensation(null, 10, people.get(0), people.get(2));
        compensationService.createCompensation(firstCompensation, group.getId());

        Compensation secondCompensation = new Compensation(null, 8, people.get(1), people.get(2));
        compensationService.createCompensation(secondCompensation, group.getId());

        List<Compensation> suggestedCompensations = suggestedCompensationService
            .getSuggestedCompensations(group.getId());

        Assert.assertEquals(2, suggestedCompensations.size());

        for (Compensation suggestedCompensation : suggestedCompensations) {
            if (suggestedCompensation.getProfiteer().getId() == people.get(0).getId()) {
                Assert.assertEquals(10, suggestedCompensation.getAmount().intValue());
                Assert.assertEquals(people.get(2).getId(), suggestedCompensation.getPayer().getId());
            } else {
                Assert.assertEquals(8, suggestedCompensation.getAmount().intValue());
                Assert.assertEquals(people.get(1).getId(), suggestedCompensation.getProfiteer().getId());
                Assert.assertEquals(people.get(2).getId(), suggestedCompensation.getPayer().getId());
            }
        }
    }

    @Test
    public void testGroupWithoutPeople() {
        Group group = groupService.createGroup(TEST_GROUP_NAME);
        List<Compensation> suggestedCompensations = suggestedCompensationService
            .getSuggestedCompensations(group.getId());

        Assert.assertEquals(0, suggestedCompensations.size());
    }

    @Test
    public void testGroupWithoutTransactions() {
        Group group = groupService.createGroup(TEST_GROUP_NAME);
        List<Compensation> suggestedCompensations = suggestedCompensationService
            .getSuggestedCompensations(group.getId());

        for (int i = 0; i < 5; i++) {
            personService.createPerson(group.getId(), TEST_NAME_PREFIX + i);
        }

        Assert.assertEquals(0, suggestedCompensations.size());
    }

    @Test
    public void testCompensationsCancelEachOtherOut() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        Person firstPerson = personService.createPerson(groupId, "Richi");
        Person secondPerson = personService.createPerson(groupId, "Heiri");
        Compensation firstCompensation = new Compensation(null, 500, firstPerson, secondPerson);
        Compensation secondCompensation = new Compensation(null, 500, secondPerson, firstPerson);
        compensationService.createCompensation(firstCompensation, groupId);
        compensationService.createCompensation(secondCompensation, groupId);
        List<Compensation> suggestedCompensations = suggestedCompensationService.getSuggestedCompensations(groupId);
        Assert.assertEquals(0, suggestedCompensations.size());
    }

    @Test
    public void testSameBalancesWork() {
        // This test checks that the @tk-codes bug is fixed ;)
        Group group = groupService.createGroup("May 13");
        Person hello = personService.createPerson(group.getId(), "Hello");
        Person bye = personService.createPerson(group.getId(), "Bye");
        Person vanakkam = personService.createPerson(group.getId(), "Vanakkam");
        Person ciao = personService.createPerson(group.getId(), "Ciao");

        Profiteer helloProfiteer = new Profiteer(null, hello, 250);
        Profiteer byeProfiteer = new Profiteer(null, bye, 250);
        Profiteer vanakkamProfiteer = new Profiteer(null, vanakkam, 250);
        Profiteer ciaoProfiteer = new Profiteer(null, ciao, 250);
        List<Profiteer> profiteers = new LinkedList<>(
            Arrays.asList(helloProfiteer, byeProfiteer, vanakkamProfiteer, ciaoProfiteer));
        Expense expense = new Expense(null, 1000, hello, "May 13", profiteers);
        expenseService.createExpense(expense, group.getId());
        List<Compensation> suggestedCompensations = suggestedCompensationService
            .getSuggestedCompensations(group.getId());
        Assert.assertEquals(3, suggestedCompensations.size());
        for (Compensation suggestedCompensation : suggestedCompensations) {
            Assert.assertEquals(250, suggestedCompensation.getAmount().longValue());
            Assert.assertEquals(hello.getId(), suggestedCompensation.getProfiteer().getId());
        }
    }
}
