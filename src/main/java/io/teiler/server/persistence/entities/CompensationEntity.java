package io.teiler.server.persistence.entities;

import io.teiler.server.dto.Compensation;
import io.teiler.server.dto.TransactionType;
import io.teiler.server.util.TimeUtil;
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