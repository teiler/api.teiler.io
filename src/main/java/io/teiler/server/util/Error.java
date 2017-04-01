package io.teiler.server.util;

import com.google.gson.annotations.SerializedName;

/**
 * Error-Wrapper to smoothly get error-codes across the REST-API.
 * 
 * @author lroellin
 */
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
