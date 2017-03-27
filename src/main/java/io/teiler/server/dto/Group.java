package io.teiler.server.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lroellin on 27.03.17.
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
