package org.apache.http.infra;

/**
 * A facade of restful service. Invoke startup() method to start listen a port to provide Restful API.
 */
public interface RestfulService {

    /**
     * Start RESTFul service.
     */
    void startup();

    /**
     * Shutdown RESTFul service.
     */
    void shutdown();
}
