package io.teiler.api.service;

import io.teiler.server.Tylr;
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

import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"local.server.port=4567"})
@ActiveProfiles("test")
public class SuggestedCompensationServiceTest {

    private static final String TEST_GROUP_NAME = "Testgroup";

    @Autowired
    private SuggestCompensationService suggestCompensationService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private CompensationService compensationService;

    @Autowired
    private PersonService personService;

    @Autowired
    private GroupService groupService;

    @Test
    public void testReturnCorrectSuggestions() {
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

        List<SuggestedCompensation> suggestedCompensations = suggestCompensationService.getSuggestedCompensations(group.getId());
        SuggestedCompensation suggestedCompensation = suggestedCompensations.get(0);

        Assert.assertEquals(payer.getId(), suggestedCompensation.getProfiteer().getId());
        Assert.assertEquals(profiteer.getId(), suggestedCompensation.getPayer().getId());
        Assert.assertEquals(share, suggestedCompensation.getAmount().intValue());
    }
}
