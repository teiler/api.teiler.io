package io.teiler.server.endpoints;

/**
 * This interface provides a common base for all REST-Endpoint-Controllers.
 * 
 * @author pbaechli
 */
public interface EndpointController {

    /**
     * Within this method all REST-Endpoints provided by the respective controller are to be
     * initialised.
     */
    void register();

}
