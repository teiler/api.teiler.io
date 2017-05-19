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
import io.teiler.server.dto.Compensation;
import io.teiler.server.persistence.entities.CompensationEntity;
import io.teiler.server.persistence.entities.QCompensationEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Provides database-related operations for Compensations.
 *
 * @author pbaechli
 */
@Repository
public class CompensationRepository {

    @Autowired
    private EntityManager entityManager;

    /**
     * Creates a new {@link CompensationEntity} and returns it.
     *
     * @param compensation {@link Compensation}
     * @return {@link CompensationEntity}
     */
    @Transactional
    public CompensationEntity create(Compensation compensation) {
        CompensationEntity compensationEntity = new CompensationEntity(compensation);
        entityManager.persist(compensationEntity);
        return compensationEntity;
    }

    /**
     * Returns a {@link CompensationEntity} with the Id.
     *
     * @param id Id of the Compensation
     * @return {@link CompensationEntity}
     */
    public CompensationEntity getById(int id) {
        return new JPAQuery<CompensationEntity>(entityManager).from(QCompensationEntity.compensationEntity)
            .where(QCompensationEntity.compensationEntity.id.eq(id))
            .fetchOne();
    }

    /**
     * Returns an {@link CompensationEntity} with the given Id and Group-Id.<br>
     * <i>Note:</i> The Group of the Payer of the Compensation has to to match the given Group.
     *
     * @param groupId Id of the Group of the Payer
     * @param compensationId Id of the Compensation
     * @return {@link CompensationEntity}
     */
    public CompensationEntity getByGroupIdAndCompensationId(String groupId, int compensationId) {
        return new JPAQuery<CompensationEntity>(entityManager).from(QCompensationEntity.compensationEntity)
            .where(QCompensationEntity.compensationEntity.id.eq(compensationId))
            .where(QCompensationEntity.compensationEntity.payer.groupId.eq(groupId))
            .fetchOne();
    }

    /**
     * Returns a {@link List} of {@link CompensationEntity} in the Group with the given Id
     * ordered by <code>update-time</code> descending.<br>
     * <i>Note:</i> The Compensation has to exist within the given Group.
     *
     * @param groupId Id of the Group
     * @param limit Maximum amount of Compensations to fetch
     * @return {@link List} of {@link CompensationEntity}
     */
    public List<CompensationEntity> getCompensationsByGroupIdAndOrderedByUpdateTimeDesc(String groupId, long limit) {
        return new JPAQuery<CompensationEntity>(entityManager).from(QCompensationEntity.compensationEntity)
            .where(QCompensationEntity.compensationEntity.payer.groupId.eq(groupId))
            .where(QCompensationEntity.compensationEntity.profiteers.any().person.groupId.eq(groupId))
            .orderBy(QCompensationEntity.compensationEntity.updateTime.desc())
            .limit(limit)
            .fetch();
    }

    /**
     * Updates a already persisted {@link CompensationEntity} with the given values.
     *
     * @param compensationId Id of the Compensation
     * @param changedCompensation {@link Compensation} containing the new values
     * @return {@link CompensationEntity} containing the new values
     */
    @Transactional
    public CompensationEntity editCompensation(int compensationId, Compensation changedCompensation) {
        CompensationEntity compensation = new CompensationEntity(changedCompensation);
        compensation.setId(compensationId);

        entityManager.merge(compensation);

        return compensation;
    }

    /**
     * Deletes the {@link CompensationEntity} with the given Id.
     *
     * @param compensationId Id of the Compensation
     */
    @Transactional
    public void deleteCompensation(int compensationId) {
        CompensationEntity compensation = getById(compensationId);
        entityManager.remove(compensation);
    }

}
