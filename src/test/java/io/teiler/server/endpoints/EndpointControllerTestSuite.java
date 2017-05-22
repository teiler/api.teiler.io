package io.teiler.server.endpoints;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    GroupEndpointControllerTest.class,
    PeopleEndpointControllerTest.class
})
public class EndpointControllerTestSuite {

    @ClassRule
    public static EndpointTestResources res = EndpointTestResources.getInstance();
    
}
