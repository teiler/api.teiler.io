package io.teiler.server.persistence.repositories;

import io.teiler.server.persistence.entities.DebtEntity;
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
     * Returns a {@link List} of {@link DebtEntity} in the Group with the given Id.
     *
     * @param groupId Id of the Group
     * @return {@link List} of {@link DebtEntity}
     */
    public List<DebtEntity> get(String groupId) {
        Query query = entityManager.createNativeQuery(
                "SELECT\n" +
                        "    person.id AS person,\n" +
                        "    transactions.credit - profiteers.debt AS balance\n" +
                        "FROM person\n" +
                        "LEFT JOIN (\n" +
                        "    SELECT \n" +
                        "        transaction.payer,\n" +
                        "        SUM(profiteer.share) AS credit\n" +
                        "    FROM transaction\n" +
                        "    LEFT JOIN profiteer ON profiteer.transaction=transaction.id\n" +
                        "    LEFT JOIN person ON person.id=profiteer.person\n" +
                        "    WHERE person.\"group\"='1'\n" +
                        "    GROUP BY transaction.payer\n" +
                        ") AS transactions ON transactions.payer=person.id\n" +
                        "LEFT JOIN (\n" +
                        "    SELECT \n" +
                        "        profiteer.person,\n" +
                        "        SUM(profiteer.share) AS debt\n" +
                        "    FROM profiteer\n" +
                        "    LEFT JOIN person ON person.id=profiteer.person\n" +
                        "    WHERE person.\"group\"='1'\n" +
                        "    GROUP BY profiteer.person\n" +
                        ") AS profiteers ON profiteers.person=person.id",
                DebtEntity.class);

        return (List<DebtEntity>) query.getResultList();

    }

}
