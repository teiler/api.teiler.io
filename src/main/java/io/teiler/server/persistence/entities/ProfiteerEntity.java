package io.teiler.server.persistence.entities;

import io.teiler.server.dto.Profiteer;
import io.teiler.server.util.TimeUtil;
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
    private Integer transactionId;

    @NotNull
    @Column(name = "share")
    private Integer share;

    @NotNull
    @Column(name = "update_time")
    private Timestamp updateTime;

    @NotNull
    @Column(name = "create_time")
    private Timestamp createTime;

    public ProfiteerEntity() { /* intentionally empty */ }

    /**
     * Creates a ProfiteerEntity from a given DTO profiteer.
     *
     * @param profiteer The DTO profiteer
     */
    public ProfiteerEntity(Profiteer profiteer) {
        this.person = new PersonEntity(profiteer.getPerson());
        this.share = profiteer.getShare();
        this.transactionId = profiteer.getTransactionId();
        this.updateTime = TimeUtil.convertToTimestamp(profiteer.getUpdateTime());
        this.createTime = TimeUtil.convertToTimestamp(profiteer.getCreateTime());
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
            createTime = updateTime;
        }
    }

    /**
     * Converts this {@link ProfiteerEntity} to a {@link Profiteer}.
     *
     * @return {@link Profiteer}
     */
    public Profiteer toProfiteer() {
        return new Profiteer(
            transactionId,
            person.toPerson(),
            share,
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

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
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

    @Override
    public String toString() {
        return this.toProfiteer().toString();
    }
}
