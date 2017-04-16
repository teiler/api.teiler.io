package io.teiler.server.persistence.entities;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.teiler.server.dto.Share;
import io.teiler.server.util.TimeUtil;

/**
 * Entity representing an entry of the <code>profiteer</code>-table.
 *
 * @author pbaechli
 */
@Entity
@Table(name = "`profiteer`")
public class ProfiteerEntity {

    @Id
    @SequenceGenerator(name = "profiteer_id_seq",
        sequenceName = "profiteer_id_seq",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "profiteer_id_seq")
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "person")
    private PersonEntity person;
    
    @Column(name = "transaction")
    private Integer expenseId;
    
    @NotNull
    @Column(name = "factor")
    private Double factor;
    
    @NotNull
    @Column(name = "update_time")
    private Timestamp updateTime;

    @NotNull
    @Column(name = "create_time")
    private Timestamp createTime;
    
    public ProfiteerEntity() { /* intentionally empty */ }
    
    public ProfiteerEntity(Share share) {
        this.person = new PersonEntity(share.getProfiteer());
        this.factor = share.getFactor();
        this.expenseId = share.getExpenseId();
        this.updateTime = TimeUtil.convertToTimestamp(share.getUpdateTime());
        this.createTime = TimeUtil.convertToTimestamp(share.getCreateTime());
    }
    
    /**
     * Sets the update-time and creation-time to {@link Instant#now()}.
     * <br>
     * <i>Note:</i> The creation-time will only be set if it has not been set previously. 
     */
    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        updateTime = new Timestamp(Instant.now().toEpochMilli());
        if (createTime == null) {
            createTime = new Timestamp(Instant.now().toEpochMilli());
        }
    }
    
    /**
     * Converts this {@link ProfiteerEntity} to a {@link Share}.
     * 
     * @return {@link Share}
     */
    public Share toShare() {
        return new Share(
            expenseId,
            person.toPerson(),
            factor,
            TimeUtil.convertToLocalDateTime(getUpdateTime()),
            TimeUtil.convertToLocalDateTime(getCreateTime()));
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
}
