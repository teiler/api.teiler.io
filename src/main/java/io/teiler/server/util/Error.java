package io.teiler.server.util;

import com.google.gson.annotations.SerializedName;

public class Error {
    @SerializedName("error")
    private String errorCode;

    public Error(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {

        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
