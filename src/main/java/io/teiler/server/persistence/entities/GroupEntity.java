package io.teiler.server.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "`group`") // f*** PSQL
public class GroupEntity {
	
    @Id
    @Column(name = "uuid")
    private String uuid;
    
    @NotNull
    @Column(name = "name")
    private String name;
    
    public GroupEntity() { /* intentionally empty */ }

    public GroupEntity(String uuid, String name) {
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
