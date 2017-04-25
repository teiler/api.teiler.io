package io.teiler.server.persistence.repositories;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;

import io.teiler.server.dto.Profiteer;
import io.teiler.server.persistence.entities.ProfiteerEntity;
import io.teiler.server.persistence.entities.QProfiteerEntity;

/**
 * Provides database-related operations for Profiteers.
 * 
 * @author pbaechli
 */
@Repository
public class ProfiteerRepository {

    @Autowired
    private EntityManager entityManager;
    
    /**
     * Creates a new {@link ProfiteerEntity} and returns it.
     * 
     * @param share {@link Profiteer}
     * @return {@link ProfiteerEntity}
     */
    @Transactional
    public ProfiteerEntity create(Profiteer share) {
        ProfiteerEntity profiteerEntity = new ProfiteerEntity(share);
        entityManager.persist(profiteerEntity);
        return profiteerEntity;
    }
    
    /**
     * Returns the {@link ProfiteerEntity} with the given Id.
     * 
     * @param profiteerId Id of the Profiteer
     * @return {@link ProfiteerEntity}
     */
    public ProfiteerEntity getById(int profiteerId) {
        return new JPAQuery<ProfiteerEntity>(entityManager).from(QProfiteerEntity.profiteerEntity)
            .where(QProfiteerEntity.profiteerEntity.id.eq(profiteerId))
            .fetchOne();
    }
    
    /**
     * Returns the {@link ProfiteerEntity} with the given Person- and Transaction-Id.
     * <i>Note:</i> The Profiteer has to exist within the given Group.
     * 
     * @param transactionId Id of the Transaction
     * @param profiteerPersonId Id of the Profiteer-Person
     * @return {@link ProfiteerEntity}
     */
    public ProfiteerEntity getByTransactionIdAndProfiteerPersonId(int transactionId, int profiteerPersonId) {
        return new JPAQuery<ProfiteerEntity>(entityManager).from(QProfiteerEntity.profiteerEntity)
            .where(QProfiteerEntity.profiteerEntity.person.id.eq(profiteerPersonId))
            .where(QProfiteerEntity.profiteerEntity.transactionId.eq(transactionId))
            .fetchOne();
    }
    
    /**
     * Updates a already persisted {@link ProfiteerEntity} with the given values.
     * 
     * @param profiteerId Id of the Profiteer
     * @param changedProfiteer {@link Profiteer} containing the new values
     * @return {@link ProfiteerEntity} containing the new values
     */
    @Transactional
    public ProfiteerEntity editProfiteer(int profiteerId, Profiteer changedProfiteer) {
        ProfiteerEntity profiteer = getById(profiteerId);
        profiteer.setShare(changedProfiteer.getShare());
        entityManager.persist(profiteer);
        return profiteer;
    }
    
    /**
     * Deletes the {@link ProfiteerEntity} with the given Id.
     * 
     * @param transactionId Id of the Transaction
     * @param profiteerId Id of the Profiteer-Person
     */
    @Transactional
    public void deleteProfiteerByTransactionIdAndProfiteerPersonId(int transactionId, int profiteerPersonId) {
        entityManager
            .createQuery("DELETE FROM ProfiteerEntity p WHERE p.transactionId = :transactionId AND p.person.id = :profiteerPersonId")
            .setParameter("transactionId", transactionId)
            .setParameter("profiteerPersonId", profiteerPersonId)
            .executeUpdate();
    }
    
}
