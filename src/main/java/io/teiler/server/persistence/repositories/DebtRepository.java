package io.teiler.server.persistence.repositories;

import com.querydsl.jpa.impl.JPAQuery;
import io.teiler.server.dto.Debt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Provides database-related operations for debts.
 *
 * @author dthoma
 */
@Repository
public class DebtRepository {

    @Autowired
    private EntityManager entityManager;

    /**
     * Returns a {@link List} of {@link Debt} in the Group with the given Id.
     *
     * @param groupId Id of the Group
     * @return {@link List} of {@link Debt}
     */
    public List<Debt> get(String groupId) {
        Query q = entityManager.createNativeQuery("SELECT a.id, a.version, a.firstname, a.lastname FROM Author a", Debt.class);
        
    }

}
