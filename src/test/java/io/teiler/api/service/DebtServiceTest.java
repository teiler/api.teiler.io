package io.teiler.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.teiler.server.Tylr;
import io.teiler.server.dto.Group;
import io.teiler.server.dto.Person;
import io.teiler.server.services.DebtService;
import io.teiler.server.services.GroupService;
import io.teiler.server.services.PersonService;

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
    private PersonService personService;

    @Autowired
    private GroupService groupService;

    @Test
    public void testReturnZeroBalanceWithOnePersonAndNoneTransactions() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        String groupId = testGroup.getId();
        Person firstPerson = personService.createPerson(groupId, FIRST_PERSON_NAME);
        debtService.getDebt(groupId);
    }

}
