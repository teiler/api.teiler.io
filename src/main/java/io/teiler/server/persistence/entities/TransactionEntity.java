package io.teiler.server.persistence.entities;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

import io.teiler.server.dto.TransactionType;

/**
 * Entity representing an entry of the <code>transaction</code>-table.
 *
 * @author pbaechli
 */
@Entity
@Table(name = "`transaction`")
@Inheritance
@DiscriminatorColumn(name = "type")
public class TransactionEntity {

    @Id
    @SequenceGenerator(name = "transaction_id_seq",
        sequenceName = "transaction_id_seq",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "transaction_id_seq")
    @Column(name = "id")
    private Integer id;

    @Transient
    @Formula("SELECT SUM(p.share) FROM ProfiteerEntity p WHERE p.expenseId = id")
    private Integer amount;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", updatable = false, insertable = false)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "payer")
    private PersonEntity payer;
    
    @NotNull
    @Column(name = "update_time")
    private Timestamp updateTime;

    @NotNull
    @Column(name = "create_time")
    private Timestamp createTime;
    
    public TransactionEntity() { /* intentionally empty */ }
    
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public PersonEntity getPayer() {
        return payer;
    }

    public void setPayer(PersonEntity payer) {
        this.payer = payer;
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
