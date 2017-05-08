package io.teiler.server.dto;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;

/**
 * Class holding all information about a group.
 *
 * @author lroellin
 * @author pbaechli
 */
public class Person {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("active")
    private boolean active;

    @SerializedName("update-time")
    private LocalDateTime updateTime;

    @SerializedName("create-time")
    private LocalDateTime createTime;

    /**
     * Usual constructor.
     *
     * @param id Id of the Person
     * @param name Name of the Person
     */
    public Person(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.active = true;
    }

    /**
     * Constructor with all fields (useful for converting).
     *
     * @param id Id of the Person
     * @param name Name of the Person
     * @param active Whether the Person is active or not
     * @param updateTime {@link LocalDateTime} marking the last update of the Person
     * @param createTime {@link LocalDateTime} marking the creation of the Person
     */
    public Person(Integer id, String name, boolean active, LocalDateTime updateTime, LocalDateTime createTime) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.updateTime = updateTime;
        this.createTime = createTime;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", active=" + active + ", updateTime="
            + updateTime + ", createTime=" + createTime + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Person person = (Person) o;

        if (active != person.active) {
            return false;
        }
        if (id != null ? !id.equals(person.id) : person.id != null) {
            return false;
        }
        return name != null ? name.equals(person.name) : person.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }
}
