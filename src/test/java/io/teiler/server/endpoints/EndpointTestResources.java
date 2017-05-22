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
    
    private static int count = 0;
    private static EndpointTestResources instance;
    
    private EndpointTestResources() {
        count = getAmountOfEndpointTests();
    }
    
    public static EndpointTestResources getInstance() {
        if (count == 0) {
            instance = new EndpointTestResources();
        }
        return instance;
    }
    
    private int getAmountOfEndpointTests() {
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
        count--;
        LOGGER.debug("after() has been called (" + count + " left)");
        LOGGER.debug("count is now = " + count);
        if (count == 0) {
            LOGGER.debug("STAWP!");
            Spark.stop();
            LOGGER.debug("Do you know how fast you were going?");
        }
    }
    
}