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

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.gson.annotations.SerializedName;

import io.teiler.server.dto.Person;
import io.teiler.server.util.TimeUtil;

/**
 * Entity representing an entry of the <code>group</code>-table.
 *
 * @author lroellin
 * @author pbaechli
 */
@Entity
@Table(name = "`person`")
public class PersonEntity {
    /* We need to duplicate the serialised names here, since the list of people is a list of
     * PersonEntities. This should only be needed on nested properties. */

    @Id
    @SerializedName("id")
    @SequenceGenerator(
        name = "person_id_seq",
        sequenceName = "person_id_seq",
        allocationSize = 1)
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "person_id_seq")
    @Column(name = "id")
    private Integer id;

    @NotNull
    @SerializedName("name")
    @Column(name = "name")
    private String name;

    @Column(name = "`group`")
    private String groupId;

    @NotNull
    @Column(name = "active")
    private Boolean active;

    @NotNull
    @SerializedName("update-time")
    @Column(name = "update_time")
    private Timestamp updateTime;

    @NotNull
    @SerializedName("create-time")
    @Column(name = "create_time")
    private Timestamp createTime;

    public PersonEntity() { /* intentionally empty */ }

    /**
     * Converts a {@link Person} to a {@link PersonEntity}.
     *
     * @param person {@link Person}
     */
    public PersonEntity(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.active = person.isActive();
        this.updateTime = TimeUtil.convertToTimestamp(person.getUpdateTime());
        this.createTime = TimeUtil.convertToTimestamp(person.getCreateTime());
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
     * Converts this {@link PersonEntity} to a {@link Person}.
     *
     * @return {@link Person}
     */
    public Person toPerson() {
        return new Person(
            getId(),
            getName(),
            getActive(),
            TimeUtil.convertToLocalDateTime(getUpdateTime()),
            TimeUtil.convertToLocalDateTime(getCreateTime()));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        return this.toPerson().toString();
    }

}
