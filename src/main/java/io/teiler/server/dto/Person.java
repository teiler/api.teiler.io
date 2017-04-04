package io.teiler.server.dto;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;

/**
 * Class holding all information about a group.
 * 
 * @author lroellin
 */
public class Person {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("update-time")
    private LocalDateTime updateTime;

    @SerializedName("create-time")
    private LocalDateTime createTime;

    // Usual constructor
    public Person(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // Constructor with all fields (useful for converting)
    public Person(Integer id, String name, LocalDateTime updateTime, LocalDateTime createTime) {
        this.id = id;
        this.name = name;
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
