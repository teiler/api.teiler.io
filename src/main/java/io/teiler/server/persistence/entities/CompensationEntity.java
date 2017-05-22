package io.teiler.server.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import io.teiler.server.dto.Compensation;
import io.teiler.server.util.TimeUtil;
import io.teiler.server.util.enums.TransactionType;

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
        return getProfiteers().get(0);
    }

    @Override
    public String toString() {
        return this.toCompensation().toString();
    }

}
