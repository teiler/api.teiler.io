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

import com.querydsl.jpa.impl.JPAQuery;
import io.teiler.server.dto.Profiteer;
import io.teiler.server.persistence.entities.ProfiteerEntity;
import io.teiler.server.persistence.entities.QProfiteerEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
     * @param profiteerPersonId Id of the Profiteer-Person
     */
    @Transactional
    public void deleteProfiteerByTransactionIdAndProfiteerPersonId(int transactionId, int profiteerPersonId) {
        ProfiteerEntity deleteEntity = getByTransactionIdAndProfiteerPersonId(transactionId, profiteerPersonId);
        entityManager.remove(deleteEntity);
    }

    /**
     * Deletes the {@link ProfiteerEntity} with the given Ids.
     *
     * @param transactionId Id of the Transaction
     * @param profiteerPersonIds Id of the Profiteer-Person
     */
    @Transactional
    public void deleteProfiteerByTransactionIdAndProfiteerPersonIdList(int transactionId,
        List<Integer> profiteerPersonIds) {
        profiteerPersonIds.forEach(id -> deleteProfiteerByTransactionIdAndProfiteerPersonId(transactionId, id));
    }

}
