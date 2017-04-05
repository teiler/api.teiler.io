package io.teiler.server.persistence.entities;

import io.teiler.server.dto.Currency;
import io.teiler.server.dto.Group;
import io.teiler.server.dto.Person;
import io.teiler.server.util.TimeUtil;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "`group`") // f*** PSQL
public class GroupEntity {
    @Id
    @Column(name = "id")
    private String id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @OneToMany(targetEntity = PersonEntity.class, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "`group`", referencedColumnName = "id")
    private List<PersonEntity> people;

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

    public GroupEntity() { /* intentionally empty */ }

    public GroupEntity(Group group) {
        List<PersonEntity> peopleEntities = new LinkedList<>();
        if(group.getPeople() != null) {
            for (Person person : group.getPeople()) {
                peopleEntities.add(new PersonEntity(person));
            }
        } else {
            peopleEntities = null;
        }
        this.id = group.getId();
        this.name = group.getName();
        this.currency = group.getCurrency();
        this.people = peopleEntities;
        this.updateTime = TimeUtil.convertToTimestamp(group.getUpdateTime());
        this.createTime = TimeUtil.convertToTimestamp(group.getCreateTime());
    }

    public Group toGroup() {
        List<Person> people = new LinkedList<>();
        if(this.getPeople() != null) {
            for(PersonEntity personEntity : this.getPeople()) {
                people.add(personEntity.toPerson());
            }
        } else {
            people = null;
        }
        return new Group(
            this.getId(),
            this.getName(),
            this.getCurrency(),
            people,
            TimeUtil.convertToLocalDateTime(this.getUpdateTime()),
            TimeUtil.convertToLocalDateTime(this.getCreateTime()));
    }

    public void addPerson(PersonEntity person) {
        people.add(person);
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public List<PersonEntity> getPeople() {
        return people;
    }

    public void setPeople(List<PersonEntity> people) {
        this.people = people;
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
