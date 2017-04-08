package io.teiler.server.dto;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class holding all information about a group.
 * 
 * @author lroellin
 */
public class Group {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("currency")
    private Currency currency;

    @SerializedName("people")
    private List<Person> people;

    @SerializedName("update-time")
    private LocalDateTime updateTime;

    @SerializedName("create-time")
    private LocalDateTime createTime;

    // Usual constructor
    public Group(String id, String name, Currency currency) {
        this.id = id;
        this.name = name;
        this.currency = currency;
    }

    // Constructor with all fields (useful for converting)
    public Group(String id, String name, Currency currency,
        List<Person> people, LocalDateTime updateTime, LocalDateTime createTime) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.people = people;
        this.updateTime = updateTime;
        this.createTime = createTime;
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

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
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
}
