package io.teiler.server.dto;

import com.google.gson.annotations.SerializedName;

public enum Currency {
    @SerializedName("chf")
    chf ("chf"),
    @SerializedName("eur")
    eur ("eur");

    private final String name;

    private Currency(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
