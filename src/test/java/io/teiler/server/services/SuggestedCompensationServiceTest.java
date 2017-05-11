package io.teiler.server.services;

import io.teiler.server.Tylr;
import io.teiler.server.dto.Compensation;
import io.teiler.server.dto.Group;
import io.teiler.server.dto.Person;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"local.server.port=4567"})
@ActiveProfiles("test")
public class SuggestedCompensationServiceTest {

    private static final String TEST_GROUP_NAME = "Testgroup";

    @Autowired
    private SuggestedCompensationService suggestedCompensationService;

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
            people.add(personService.createPerson(group.getId(), "Person " + i));
        }

        Person payer = people.get(0);
        Person profiteer = people.get(people.size() - 1);

        Compensation compensation = new Compensation(null, share, payer, profiteer);
        compensationService.createCompensation(compensation, group.getId());

        List<Compensation> suggestedCompensations = suggestedCompensationService.getSuggestedCompensations(group.getId());
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
            people.add(personService.createPerson(group.getId(), "Person " + i));
        }

        Compensation firstCompensation = new Compensation(null, 10, people.get(0), people.get(2));
        compensationService.createCompensation(firstCompensation, group.getId());

        Compensation secondCompensation = new Compensation(null, 8, people.get(1), people.get(2));
        compensationService.createCompensation(secondCompensation, group.getId());

        List<Compensation> suggestedCompensations = suggestedCompensationService.getSuggestedCompensations(group.getId());

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
        List<Compensation> suggestedCompensations = suggestedCompensationService.getSuggestedCompensations(group.getId());

        Assert.assertEquals(0, suggestedCompensations.size());
    }

    @Test
    public void testGroupWithoutTransactions() {
        Group group = groupService.createGroup(TEST_GROUP_NAME);
        List<Compensation> suggestedCompensations = suggestedCompensationService.getSuggestedCompensations(group.getId());

        for (int i = 0; i < 5; i++) {
            personService.createPerson(group.getId(), "Person " + i);
        }

        Assert.assertEquals(0, suggestedCompensations.size());
    }
}
