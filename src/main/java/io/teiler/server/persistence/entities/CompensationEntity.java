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
package io.teiler.server.persistence.entities;

import io.teiler.server.dto.Compensation;
import io.teiler.server.util.TimeUtil;
import io.teiler.server.util.enums.TransactionType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Entity representing a Compensation-entry of the <code>transaction</code>-table.
 *
 * @author pbaechli
 */
@Entity
@DiscriminatorValue("COMPENSATION")
public class CompensationEntity extends TransactionEntity {

    public CompensationEntity() { /* intentionally empty */ }

    /**
     * Creates a new CompensationEntity from a DTO compensation.
     *
     * @param compensation The DTO compensation
     */
    public CompensationEntity(Compensation compensation) {
        super.setId(compensation.getId());
        super.setPayer(new PersonEntity(compensation.getPayer()));
        super.setUpdateTime(TimeUtil.convertToTimestamp(compensation.getUpdateTime()));
        super.setCreateTime(TimeUtil.convertToTimestamp(compensation.getCreateTime()));
        super.setTransactionType(TransactionType.COMPENSATION);
    }

    /**
     * Converts this {@link CompensationEntity} to a {@link Compensation}.
     *
     * @return {@link Compensation}
     */
    public Compensation toCompensation() {
        return new Compensation(getId(), getAmount(), getPayer().toPerson(),
            TimeUtil.convertToLocalDateTime(getUpdateTime()),
            TimeUtil.convertToLocalDateTime(getCreateTime()),
            getProfiteer().toProfiteer().getPerson());
    }

    public ProfiteerEntity getProfiteer() {
        // TODO maybe make this even better
        return getProfiteers().get(0);
    }

    @Override
    public String toString() {
        return this.toCompensation().toString();
    }

}
