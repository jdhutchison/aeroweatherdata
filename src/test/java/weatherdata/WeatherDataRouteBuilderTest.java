package weatherdata;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@MockEndpoints("direct:ToMessageQueue")
@ContextConfiguration(locations = "classpath:weatherdata-context.xml")
public class WeatherDataRouteBuilderTest {//extends AbstractJUnit4SpringContextTests {

    @Autowired
    WeatherDataRouteBuilder routeBuilder;

    @Autowired
    private CamelContext camelContext;

    //@EndpointInject(uri = "direct:ToMessageQueue", context = "camelContext")
    //private MockEndpoint mock;

    @Produce(uri = "direct:Source-AIRCRAFTREPORT")
    protected ProducerTemplate template;

    @Produce(uri = "direct:Source-AIRSIGMET")
    protected ProducerTemplate airSigTemplate;

    @Produce(uri = "direct:Source-METAR")
    protected ProducerTemplate metarTemplate;

    @Produce(uri = "direct:Source-STATION")
    protected ProducerTemplate stationTemplate;

    @Produce(uri = "direct:Source-TAF")
    protected ProducerTemplate tafTemplate;
    //@Test
    public void testShouldSendDataToQueue() throws InterruptedException {
        //mock.expectedBodyReceived().arrives();
        template.sendBody("");
        //mock.assertIsSatisfied();
    }

    @Test
    public void testHookForMaven() {

    }
}
