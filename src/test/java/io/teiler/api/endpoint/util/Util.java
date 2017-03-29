package io.teiler.api.endpoint.util;

import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class Util {
    @LocalServerPort
    private int port;

    public int getPort() {
        return port;
    }

    /**
     * This method simplifies generating a request prefilled with our header.
     * @param headerContent Content of our <code>X-Teiler-GroupID header</code>
     * @return Prefilled HTTP entity with our headers.
     */
    public static HttpEntity getHttpEntityWithHeader(String headerContent) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("X-Teiler-GroupID", headerContent);
        HttpEntity entity = new HttpEntity(null, requestHeaders);
        return entity;
    }

}
