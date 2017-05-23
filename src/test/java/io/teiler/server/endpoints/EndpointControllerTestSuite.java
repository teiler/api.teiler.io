package io.teiler.server.endpoints;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    GroupEndpointControllerTest.class,
    PersonEndpointControllerTest.class
})
public class EndpointControllerTestSuite {
    
    private EndpointControllerTestSuite() { /* intentionally empty */ }

    @ClassRule
    public static final EndpointTestResources res = EndpointTestResources.getInstance();
    
}
