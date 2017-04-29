package io.teiler.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.teiler.server.dto.Compensation;
import io.teiler.server.dto.Person;
import io.teiler.server.dto.Profiteer;
import io.teiler.server.persistence.entities.CompensationEntity;
import io.teiler.server.persistence.entities.ProfiteerEntity;
import io.teiler.server.persistence.repositories.CompensationRepository;
import io.teiler.server.persistence.repositories.ProfiteerRepository;
import io.teiler.server.util.exceptions.ProfiteerNotFoundException;

/**
 * Provides service-methods for Compensations.
 * 
 * @author pbaechli
 */
@Service
public class CompensationService {

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private ProfiteerRepository profiteerRepository;

    @Autowired
    private GroupUtil groupUtil;

    @Autowired
    private CompensationUtil compensationUtil;

    @Autowired
    private TransactionUtil transactionUtil;

    /**
     * Creates a new {@link Compensation}.<br>
     * <i>Note:</i> The Payer and Profiteer may not be the same Person.
     * 
     * @param compensation {@link Compensation} containing the information to be persisted
     * @return {@link Compensation} containing the data freshly fetched from the database
     */
    public Compensation createCompensation(Compensation compensation, String groupId) {
        groupUtil.checkIdExists(groupId);
        transactionUtil.checkPayerBelongsToThisGroup(groupId, compensation.getPayer().getId());

        Person profiteerPerson = compensation.getProfiteer();
        compensationUtil.checkPayerAndProfiteerAreNotEqual(compensation, profiteerPerson);
        transactionUtil.checkProfiteerBelongsToThisGroup(groupId, profiteerPerson.getId());

        CompensationEntity compensationEntity = compensationRepository.create(compensation);
        
        Profiteer profiteer = new Profiteer(
                compensationEntity.getId(), profiteerPerson, compensation.getAmount());
        profiteerRepository.create(profiteer);

        return compensationRepository.getById(compensationEntity.getId()).toCompensation();
    }

    /**
     * Returns a {@link Compensation} with the given Id and Group-Id.
     * 
     * @param groupId Id of the Group
     * @param compensationId Id of the Compensation
     * @return {@link Compensation}
     */
    public Compensation getCompensation(String groupId, int compensationId) {
        groupUtil.checkIdExists(groupId);
        compensationUtil.checkCompensationExists(compensationId);
        compensationUtil.checkCompensationBelongsToThisGroup(groupId, compensationId);

        CompensationEntity compensation = compensationRepository.getByGroupIdAndCompensationId(groupId, compensationId);
        return compensation.toCompensation();
    }

    /**
     * Returns a {@link List} of {@link Compensation} in the Group with the given Id sorted
     * descending by the <code>update-time</code>.<br>
     * <i>Note:</i> The Group of the Payer of the Compensation has to to match the given Group.
     * 
     * @param groupId Id of the Group
     * @param limit Maximum amount of Compensation to fetch
     * @return {@link List} of {@link Compensation}
     */
    public List<Compensation> getLastCompensations(String groupId, long limit) {
        groupUtil.checkIdExists(groupId);

        List<CompensationEntity> compensation = compensationRepository.getCompensationsByGroupIdAndOrderedByUpdateTimeDesc(groupId, limit);
        return compensation.stream().map(CompensationEntity::toCompensation).collect(Collectors.toList());
    }

    /**
     * Updates and already created Compensation with the given values.<br>
     * <i>Note:</i> The Compensation has to exist within the given Group
     * and the Payer and Profiteer may not be the same Person.
     * 
     * @param groupId Id of the Group
     * @param compensationId Id of the Compensation
     * @param changedCompensation {@link Compensation} containing the new values
     * @return {@link Compensation} containing the new values
     */
    public Compensation editCompensation(String groupId, int compensationId, Compensation changedCompensation) {
        groupUtil.checkIdExists(groupId);
        compensationUtil.checkCompensationExists(compensationId);
        compensationUtil.checkCompensationBelongsToThisGroup(groupId, compensationId);
        transactionUtil
            .checkPayerBelongsToThisGroup(groupId, changedCompensation.getPayer().getId());

        Person changedProfiteerPerson = changedCompensation.getProfiteer();
        compensationUtil.checkPayerAndProfiteerAreNotEqual(changedCompensation, changedProfiteerPerson);
        transactionUtil
            .checkProfiteerBelongsToThisGroup(groupId, changedProfiteerPerson.getId());

        compensationRepository.editCompensation(compensationId, changedCompensation);
        CompensationEntity compensationEntity = compensationRepository.getById(compensationId);
        
        try {
            // first we check if the profiteer changed by looking him up in the group
            transactionUtil.checkProfiteerExistsInThisTransaction(
                    compensationEntity.getId(), changedProfiteerPerson.getId());

            // profiteer was not changed => update
            ProfiteerEntity profiteerEntity = profiteerRepository.getByTransactionIdAndProfiteerPersonId(
                    compensationEntity.getId(), changedProfiteerPerson.getId());
            
            Profiteer changeddProfiteer = new Profiteer(
                    compensationEntity.getId(), changedProfiteerPerson, changedCompensation.getAmount());
            profiteerRepository.editProfiteer(profiteerEntity.getId(), changeddProfiteer);
        }
        catch (ProfiteerNotFoundException e) {
            // does not yet exist => delete the existing one and create a new one
            profiteerRepository.deleteProfiteerByTransactionIdAndProfiteerPersonId(
                    compensationEntity.getId(), compensationEntity.getProfiteer().getPerson().getId());
            
            Profiteer changeddProfiteer = new Profiteer(
                    compensationEntity.getId(), changedProfiteerPerson, changedCompensation.getAmount());
            profiteerRepository.create(changeddProfiteer);
        }

        return compensationRepository.getById(compensationEntity.getId()).toCompensation();
    }

    /**
     * Deletes the Compensation with the given Id and Group-Id.<br>
     * <i>Note:</i> The Compensation has to exist within the given Group.
     * 
     * @param groupId Id of the Group
     * @param compensationId Id of the Compensation
     */
    public void deleteCompensation(String groupId, int compensationId) {
        groupUtil.checkIdExists(groupId);
        compensationUtil.checkCompensationExists(compensationId);
        compensationUtil.checkCompensationBelongsToThisGroup(groupId, compensationId);

        compensationRepository.deleteCompensation(compensationId);
    }

}
