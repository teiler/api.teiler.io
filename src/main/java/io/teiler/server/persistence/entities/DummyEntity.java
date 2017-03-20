package io.teiler.server.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dummy")
public class DummyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "text")
    @NotNull
    private String text;

    @Column(name = "additional_info")
    @NotNull
    private String additionalInfo;

    public DummyEntity() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DummyEntity other = (DummyEntity) obj;
        if (additionalInfo == null) {
            if (other.additionalInfo != null) {
                return false;
            }
        } else if (!additionalInfo.equals(other.additionalInfo)) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!text.equals(other.text)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DummyEntity [id=" + id + ", text=" + text + ", additionalInfo=" + additionalInfo
                + "]";
    }
    
}
