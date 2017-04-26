package io.teiler.api.service;

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
import io.teiler.server.dto.Compensation;
import io.teiler.server.dto.Group;
import io.teiler.server.dto.Person;
import io.teiler.server.dto.Profiteer;
import io.teiler.server.util.exceptions.PayerNotFoundException;
import io.teiler.server.util.exceptions.PayerProfiteerConflictException;
import io.teiler.server.util.exceptions.ProfiteerNotFoundException;
import io.teiler.server.util.exceptions.TransactionNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tylr.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"local.server.port=4567"})
@AutoConfigureTestDatabase
public class CompensationServiceTest {

    private static final String TEST_GROUP_NAME = "Testgruppe";

    private static final Integer TEST_COMPENSATION_AMOUNT = 4200;
    private static final String TEST_PAYER = "Hans";
    private static final String TEST_PROFITEER_1 = "Paul";
    private static final String TEST_PROFITEER_2 = "Peter";

    @Autowired
    private CompensationService compensationService;
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private PersonService personService;

    @Test
    public void testCreateCompensation() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayer = personService.createPerson(testGroup.getId(), TEST_PAYER);
        Person testProfiteerPerson = personService.createPerson(testGroup.getId(), TEST_PROFITEER_1);
        Profiteer testProfiteer = new Profiteer(null, testProfiteerPerson, TEST_COMPENSATION_AMOUNT);
        
        Compensation testCompensation = new Compensation(null, TEST_COMPENSATION_AMOUNT, testPayer, testProfiteer);
        Compensation testCompensationResponse = compensationService.createCompensation(testCompensation, testGroup.getId());

        Assert.assertEquals(TEST_COMPENSATION_AMOUNT, testCompensationResponse.getAmount());
        Assert.assertEquals(testPayer.getId(), testCompensationResponse.getPayer().getId());

        Assert.assertNotNull(testCompensationResponse.getProfiteer());
    }

    @Test(expected = PayerProfiteerConflictException.class)
    public void testCreateCompensationWithSamePayerAndProfiteer() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayerAndProfiteer = personService.createPerson(testGroup.getId(), TEST_PAYER);
        Profiteer testProfiteer = new Profiteer(null, testPayerAndProfiteer, TEST_COMPENSATION_AMOUNT);
        
        Compensation testCompensation = new Compensation(null, TEST_COMPENSATION_AMOUNT, testPayerAndProfiteer, testProfiteer);
        
        compensationService.createCompensation(testCompensation, testGroup.getId());
    }

    @Test(expected = PayerNotFoundException.class)
    public void testCreateCompensationWithPayerFromDifferentGroup() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);

        Group differentGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person differentGroupPerson = personService.createPerson(differentGroup.getId(), TEST_PAYER);

        Profiteer testProfiteer = new Profiteer(null, differentGroupPerson, TEST_COMPENSATION_AMOUNT);

        Compensation testCompensation = new Compensation(null, TEST_COMPENSATION_AMOUNT, differentGroupPerson, testProfiteer);

        compensationService.createCompensation(testCompensation, testGroup.getId());
    }

    @Test(expected = ProfiteerNotFoundException.class)
    public void testCreateCompensationWithProfiteerFromDifferentGroup() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPerson = personService.createPerson(testGroup.getId(), TEST_PAYER);

        Group differentGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person differentProfiteer = personService.createPerson(differentGroup.getId(), TEST_PROFITEER_1);

        Profiteer testProfiteer = new Profiteer(null, differentProfiteer, TEST_COMPENSATION_AMOUNT);

        Compensation testCompensation = new Compensation(null, TEST_COMPENSATION_AMOUNT, testPerson, testProfiteer);

        compensationService.createCompensation(testCompensation, testGroup.getId());
    }

    @Test
    public void testViewCompensation() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayer = personService.createPerson(testGroup.getId(), TEST_PAYER);
        Person testProfiteerPerson = personService.createPerson(testGroup.getId(), TEST_PROFITEER_1);
        Profiteer testProfiteer = new Profiteer(null, testProfiteerPerson, TEST_COMPENSATION_AMOUNT);

        Compensation testCompensation = new Compensation(null, TEST_COMPENSATION_AMOUNT, testPayer, testProfiteer);

        Compensation testCompensationResponse = compensationService.createCompensation(testCompensation, testGroup.getId());
        Compensation viewResponse = compensationService.getCompensation(testGroup.getId(), testCompensationResponse.getId());

        Assert.assertEquals(TEST_COMPENSATION_AMOUNT, viewResponse.getAmount());
        Assert.assertEquals(testPayer.getId(), viewResponse.getPayer().getId());

        Assert.assertNotNull(viewResponse.getProfiteer());
    }

    @Test(expected = TransactionNotFoundException.class)
    public void testViewExpenseFromDifferentGroup() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayer = personService.createPerson(testGroup.getId(), TEST_PAYER);
        Person testProfiteerPerson = personService.createPerson(testGroup.getId(), TEST_PROFITEER_1);
        
        Profiteer testProfiteer = new Profiteer(null, testProfiteerPerson, TEST_COMPENSATION_AMOUNT);

        Compensation testCompensation = new Compensation(null, TEST_COMPENSATION_AMOUNT, testPayer, testProfiteer);

        compensationService.createCompensation(testCompensation, testGroup.getId());

        Group differentGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person differentPayer = personService.createPerson(differentGroup.getId(), TEST_PAYER);
        Person differentProfiteerPerson = personService.createPerson(differentGroup.getId(), TEST_PROFITEER_1);

        Profiteer differentProfiteer = new Profiteer(null, differentProfiteerPerson, TEST_COMPENSATION_AMOUNT);

        Compensation differentCompensation = new Compensation(null, TEST_COMPENSATION_AMOUNT, differentPayer, differentProfiteer);
        Compensation differentCompensationResponse = compensationService.createCompensation(differentCompensation, differentGroup.getId());

        compensationService.getCompensation(testGroup.getId(), differentCompensationResponse.getId());
    }

    @Test
    public void testEditCompensationAmountAndShare() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayer = personService.createPerson(testGroup.getId(), TEST_PAYER);
        Person testProfiteerPerson = personService.createPerson(testGroup.getId(), TEST_PROFITEER_1);
        
        Profiteer testProfiteer = new Profiteer(null, testProfiteerPerson, TEST_COMPENSATION_AMOUNT);

        Compensation testCompensation = new Compensation(null, TEST_COMPENSATION_AMOUNT, testPayer, testProfiteer);
        Compensation testCompensationResponse = compensationService.createCompensation(testCompensation, testGroup.getId());

        testCompensationResponse.setAmount(1000);
        testCompensationResponse.getProfiteer().setShare(1000);

        Compensation testComensationEditResponse = compensationService.editCompensation(testGroup.getId(), testCompensationResponse.getId(), testCompensationResponse);
        
        Assert.assertEquals(Integer.valueOf(1000), testComensationEditResponse.getAmount());
        Assert.assertEquals(Integer.valueOf(1000), testComensationEditResponse.getProfiteer().getShare());
    }

    @Test
    public void testEditCompensationChangeProfiteer() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayer = personService.createPerson(testGroup.getId(), TEST_PAYER);
        Person testProfiteerPerson1 = personService.createPerson(testGroup.getId(), TEST_PROFITEER_1);
        Person testProfiteerPerson2 = personService.createPerson(testGroup.getId(), TEST_PROFITEER_2);

        Profiteer testProfiteer1 = new Profiteer(null, testProfiteerPerson1, TEST_COMPENSATION_AMOUNT - 50); // slighly hacky, but hey, it works
        Profiteer testProfiteer2 = new Profiteer(null, testProfiteerPerson2, TEST_COMPENSATION_AMOUNT);

        Compensation testCompensation = new Compensation(null, TEST_COMPENSATION_AMOUNT, testPayer, testProfiteer1);
        Compensation testCompensationResponse = compensationService.createCompensation(testCompensation, testGroup.getId());

        testCompensationResponse.setProfiteer(testProfiteer2);

        Compensation testEditedCompensation = compensationService.editCompensation(testGroup.getId(), testCompensationResponse.getId(), testCompensationResponse);

        Assert.assertEquals(TEST_COMPENSATION_AMOUNT, testEditedCompensation.getAmount());
        Assert.assertEquals(testPayer.getId(), testEditedCompensation.getPayer().getId());

        Assert.assertNotNull(testEditedCompensation.getProfiteer());
        Assert.assertEquals(testProfiteer2.getPerson().getId(), testEditedCompensation.getProfiteer().getPerson().getId());
    }

    @Test
    public void testLastCompensationsOrderIsCorrect() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayer = personService.createPerson(testGroup.getId(), TEST_PAYER);
        Person testProfiteerPerson = personService.createPerson(testGroup.getId(), TEST_PROFITEER_1);
        
        Profiteer testProfiteer = new Profiteer(null, testProfiteerPerson, TEST_COMPENSATION_AMOUNT);

        Compensation testCompensation1 = new Compensation(null, TEST_COMPENSATION_AMOUNT - 50, testPayer, testProfiteer); // slighly hacky, but hey, it works
        Compensation testCompensation2 = new Compensation(null, TEST_COMPENSATION_AMOUNT, testPayer, testProfiteer);

        compensationService.createCompensation(testCompensation1, testGroup.getId());
        compensationService.createCompensation(testCompensation2, testGroup.getId());

        List<Compensation> compensations = compensationService.getLastCompensations(testGroup.getId(), 3);

        Assert.assertEquals(TEST_COMPENSATION_AMOUNT, compensations.get(0).getAmount());
    }

    @Test(expected = TransactionNotFoundException.class)
    public void testDeleteExpense() {
        Group testGroup = groupService.createGroup(TEST_GROUP_NAME);
        Person testPayer = personService.createPerson(testGroup.getId(), TEST_PAYER);
        Person testProfiteerPerson = personService.createPerson(testGroup.getId(), TEST_PROFITEER_1);
        Profiteer testProfiteer = new Profiteer(null, testProfiteerPerson, TEST_COMPENSATION_AMOUNT);

        Compensation testCompensation = new Compensation(null, TEST_COMPENSATION_AMOUNT, testPayer, testProfiteer);
        Compensation testCompensationResponse = compensationService.createCompensation(testCompensation, testGroup.getId());

        compensationService.deleteCompensation(testGroup.getId(), testCompensationResponse.getId());

        compensationService.getCompensation(testGroup.getId(), testCompensationResponse.getId());
    }

}
