package io.teiler.server.persistence.entities;

import io.teiler.server.dto.Person;
import io.teiler.server.util.TimeUtil;
import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Entity representing an entry of the <code>group</code>-table.
 *
 * @author lroellin
 */
@Entity
@Table(name = "`person`") // f*** PSQL
public class PersonEntity {

    @Id
    @Column(name = "id")
    private String id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "update_time")
    private Timestamp updateTime;

    @NotNull
    @Column(name = "create_time")
    private Timestamp createTime;

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        updateTime = new Timestamp(Instant.now().toEpochMilli());
        if (createTime == null) {
            createTime = new Timestamp(Instant.now().toEpochMilli());
        }
    }

    public PersonEntity() { /* intentionally empty */ }

    public PersonEntity(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.updateTime = TimeUtil.convertToTimestamp(person.getUpdateTime());
        this.createTime = TimeUtil.convertToTimestamp(person.getCreateTime());
    }

    public Person toPerson() {
        return new Person(
            this.getId(),
            this.getName(),
            TimeUtil.convertToLocalDateTime(this.getUpdateTime()),
            TimeUtil.convertToLocalDateTime(this.getCreateTime()));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
