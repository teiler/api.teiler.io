package io.teiler.server.util;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Error wrapper to smoothly get error codes across the REST API.
 *
 * @author lroellin
 */
public class Error {

    @Expose
    @SerializedName("error")
    private List<String> errorCodes = new LinkedList<>();

    public Error(String errorCode) {
        this.errorCodes.add(errorCode);
    }

}
