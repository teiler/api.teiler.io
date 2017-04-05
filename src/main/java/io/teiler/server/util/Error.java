package io.teiler.server.util;

import com.google.gson.annotations.SerializedName;
import java.util.LinkedList;
import java.util.List;

/**
 * Error-Wrapper to smoothly getGroupById error-codes across the REST-API.
 * 
 * @author lroellin
 */
public class Error {

    @SerializedName("error")
    private List<String> errorCodes = new LinkedList<>();

    public Error(String errorCode) {
        this.errorCodes.add(errorCode);
    }

    public void addError(String errorCode) {
        this.errorCodes.add(errorCode);
    }

}
