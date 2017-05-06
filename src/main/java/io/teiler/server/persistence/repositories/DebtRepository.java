package io.teiler.server.persistence.repositories;

import io.teiler.server.persistence.entities.DebtEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

/**
 * Provides database-related operations for debts.
 *
 * @author dthoma
 */
@Repository
public class DebtRepository {

    static final String STATEMENT_SQLMAP = "GET_DEBT_QUERY";

    @Autowired
    private EntityManager entityManager;

    /**
     * Returns a {@link List} of {@link DebtEntity} in the Group with the given Id.
     *
     * @param groupId Id of the Group
     * @return {@link List} of {@link DebtEntity}
     */
    public List<DebtEntity> get(String groupId) {
        String sql =
                "SELECT" +
                "    person.id AS person, " +
                "    transactions.credit - profiteers.debt AS balance " +
                "FROM person " +
                "LEFT JOIN ( " +
                "    SELECT  " +
                "        transaction.payer, " +
                "        SUM(profiteer.share) AS credit " +
                "    FROM transaction" +
                "    LEFT JOIN profiteer ON profiteer.transaction=transaction.id " +
                "    LEFT JOIN person ON person.id=profiteer.person " +
                "    WHERE person.\"group\"=?1 " +
                "    GROUP BY transaction.payer " +
                ") AS transactions ON transactions.payer=person.id " +
                "LEFT JOIN ( " +
                "    SELECT  " +
                "        profiteer.person, " +
                "        SUM(profiteer.share) AS debt " +
                "    FROM profiteer " +
                "    LEFT JOIN person ON person.id=profiteer.person " +
                "    WHERE person.\"group\"=?1 " +
                "    GROUP BY profiteer.person " +
                ") AS profiteers ON profiteers.person=person.id ";

        Query query = entityManager.createNativeQuery(sql, STATEMENT_SQLMAP);
        query.setParameter(1, groupId);

        return query.getResultList();
    }
}
