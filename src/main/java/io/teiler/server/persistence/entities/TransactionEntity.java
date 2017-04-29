package io.teiler.server.persistence.entities;

import io.teiler.server.dto.TransactionType;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
    
    @OneToMany(mappedBy = "transactionId", fetch = FetchType.EAGER, orphanRemoval = false, cascade = CascadeType.REMOVE)
    private List<ProfiteerEntity> profiteers;
    
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
            createTime = updateTime;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return profiteers.stream().map(ProfiteerEntity::getShare).mapToInt(Integer::intValue).sum();
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
        return new Timestamp(updateTime.getTime());
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime != null ? new Timestamp(updateTime.getTime()) : null;
    }

    public Timestamp getCreateTime() {
        return new Timestamp(createTime.getTime());
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = updateTime != null ? new Timestamp(createTime.getTime()) : null;
    }
    
    public List<ProfiteerEntity> getProfiteers() {
        return profiteers;
    }

    public void setProfiteers(List<ProfiteerEntity> profiteers) {
        this.profiteers = profiteers;
    }
    
}
