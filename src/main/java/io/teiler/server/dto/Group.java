package io.teiler.server.dto;

import com.google.gson.annotations.SerializedName;
import io.teiler.server.persistence.entities.GroupEntity;

/**
 * Class holding all information about a group.
 * 
 * @author lroellin
 */
public class Group {

    @SerializedName("group-uuid")
    private String uuid;

    @SerializedName("name")
    private String name;

    public Group(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public Group(GroupEntity group) {
        this.uuid = group.getUuid();
        this.name = group.getName();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
