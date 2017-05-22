package io.teiler.server.endpoints;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Spark;

class EndpointTestResources extends ExternalResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointTestResources.class);
    
    private int count = getAmountOfEndpointTests();
    private static EndpointTestResources instance;
    
    private EndpointTestResources() { /* intentionally empty */ }
    
    public static EndpointTestResources getInstance() {
        if (instance == null) {
            instance = new EndpointTestResources();
        }
        return instance;
    }
    
    private static int getAmountOfEndpointTests() {
        Reflections reflections = new Reflections("io.teiler.server.endpoints");
        Set<Class<? extends BaseEndpointControllerTest>> allClasses =
                reflections.getSubTypesOf(BaseEndpointControllerTest.class);
        
        LOGGER.trace("Amount of test classes: " + allClasses.size());
        
        int amountOfTestMethods = 0;
        for (Class<? extends BaseEndpointControllerTest> c : allClasses) {
            for (Method m : c.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Test.class)) {
                    amountOfTestMethods++;
                }
            }
        }
        
        LOGGER.trace("Amount of test methods: " + amountOfTestMethods);
        
        return amountOfTestMethods;
    }
    
    @Override
    protected void after() {
        if (count == 0) {
            LOGGER.debug("STAWP!");
            Spark.stop();
            LOGGER.debug("Do you know how fast you were going?");
        }
        count--;
        LOGGER.debug("after() has been called (" + count + " left)");
        LOGGER.debug("count is now = " + count);
    }
    
}