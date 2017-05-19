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

import io.teiler.server.dto.Group;
import io.teiler.server.dto.Person;
import io.teiler.server.util.TimeUtil;
import io.teiler.server.util.enums.Currency;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
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

    public GroupEntity() { /* intentionally empty */ }

    /**
     * Converts a {@link GroupEntity} to a {@link Group}.
     *
     * @param group {@link Group}
     */
    public GroupEntity(Group group) {
        List<PersonEntity> peopleEntities;
        if (group.getPeople() != null) {
            peopleEntities =
                group.getPeople().stream().map(PersonEntity::new).collect(Collectors.toList());

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
     * Converts this {@link GroupEntity} to a {@link Group}.
     *
     * @return {@link Group}
     */
    public Group toGroup() {
        List<Person> dtoPeople;
        if (getPeople() != null) {
            dtoPeople =
                getPeople().stream().map(PersonEntity::toPerson).collect(Collectors.toList());
        } else {
            dtoPeople = null;
        }
        return new Group(
            getId(),
            getName(),
            getCurrency(),
            dtoPeople,
            TimeUtil.convertToLocalDateTime(getUpdateTime()),
            TimeUtil.convertToLocalDateTime(getCreateTime()));
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
        return new Timestamp(createTime.getTime());
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = updateTime != null ? new Timestamp(createTime.getTime()) : null;
    }

    public Timestamp getUpdateTime() {
        return new Timestamp(updateTime.getTime());
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime != null ? new Timestamp(updateTime.getTime()) : null;
    }

    @Override
    public String toString() {
        return this.toGroup().toString();
    }

}
