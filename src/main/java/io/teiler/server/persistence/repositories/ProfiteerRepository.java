package io.teiler.server.persistence.repositories;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import io.teiler.server.dto.Share;
import io.teiler.server.persistence.entities.ProfiteerEntity;

/**
 * Provides database-related operations for Profiteers.
 * 
 * @author pbaechli
 */
@Repository
public class ProfiteerRepository {

    @Autowired
    private EntityManager entityManager;
    
    @Transactional
    public ProfiteerEntity create(Share share) {
        ProfiteerEntity profiteerEntity = new ProfiteerEntity(share);
        entityManager.persist(profiteerEntity);
        return profiteerEntity;
    }
    
}
