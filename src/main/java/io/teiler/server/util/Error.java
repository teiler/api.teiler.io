package io.teiler.server.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.LinkedList;
import java.util.List;

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
