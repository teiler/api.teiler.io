package io.teiler.server.util;

import com.google.gson.annotations.SerializedName;

public class Error {
    @SerializedName("error")
    private String error;

    public Error(String error) {
        this.error = error;
    }

    public String getError() {

        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
