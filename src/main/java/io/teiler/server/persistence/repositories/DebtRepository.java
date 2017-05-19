/**
 * MIT License
 *
 * Copyright (c) 2017 L. Röllin, P. Bächli, K. Thurairatnam & D. Thoma
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.teiler.server.persistence.repositories;

import io.teiler.server.persistence.entities.DebtEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Provides database-related operations for debts.
 *
 * @author dthoma
 */
@Repository
public class DebtRepository {

    public static final String STATEMENT_SQLMAP = "GET_DEBT_QUERY";

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
            "SELECT"
                + "    person.id AS person, "
                + "    COALESCE(transactions.credit, 0) - COALESCE(profiteers.debt, 0) AS balance "
                + "FROM person "
                + "LEFT JOIN ( "
                + "    SELECT  "
                + "        transaction.payer, "
                + "        SUM(profiteer.share) AS credit "
                + "    FROM transaction"
                + "    LEFT JOIN profiteer ON profiteer.transaction=transaction.id "
                + "    LEFT JOIN person ON person.id=profiteer.person "
                + "    WHERE person.\"group\"=?1 "
                + "    GROUP BY transaction.payer "
                + ") AS transactions ON transactions.payer=person.id "
                + "LEFT JOIN ( "
                + "    SELECT  "
                + "        profiteer.person, "
                + "        SUM(profiteer.share) AS debt "
                + "    FROM profiteer "
                + "    LEFT JOIN person ON person.id=profiteer.person "
                + "    WHERE person.\"group\"=?1 "
                + "    GROUP BY profiteer.person "
                + ") AS profiteers ON profiteers.person=person.id "
                + "WHERE person.\"group\"=?1 ";

        Query query = entityManager.createNativeQuery(sql, STATEMENT_SQLMAP);
        query.setParameter(1, groupId);

        return query.getResultList();
    }

}
