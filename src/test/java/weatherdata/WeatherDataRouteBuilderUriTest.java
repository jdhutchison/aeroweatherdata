package weatherdata;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import weatherdata.processors.DataSourceUriException;

import java.util.HashMap;
import java.util.Map;

public class WeatherDataRouteBuilderUriTest {

    private Map<DataSource, String> uriMap = new HashMap<DataSource, String>();
    private Map<DataSource, String> timeRangeMap = new HashMap<DataSource, String>();

    private WeatherDataRouteBuilder routeBuilder = new WeatherDataRouteBuilder();

    @Before
    public void setup() {
        // Mock values for uri
        uriMap.put(DataSource.AIRCRAFTREPORT, "abcd");
        uriMap.put(DataSource.AIRSIGMET, "");
        uriMap.put(DataSource.METAR, null);
        uriMap.put(DataSource.TAF, "xxxx?hoursBeforeNow={hoursBeforeNow}");

        timeRangeMap.put(DataSource.TAF, "12");

        routeBuilder.setDataSourceTimeRanges(timeRangeMap);
        routeBuilder.setDataSourceUris(uriMap);

    }

    @Test(expected = DataSourceUriException.class)
    public void shouldThrowExceptionForNoUriForSource() {
        routeBuilder.getUriForRoute(DataSource.STATION);
    }

    @Test(expected = DataSourceUriException.class)
    public void shouldThrowExceptionForNullOrEmptyUriForSource() {
        try {
            routeBuilder.getUriForRoute(DataSource.AIRSIGMET);
            Assert.fail("Should throw exception here");
        } catch (DataSourceUriException e) { }

        routeBuilder.getUriForRoute(DataSource.METAR);
    }

    @Test
    public void shouldReturnUriForSource() {
        // With replacement
        String uri = routeBuilder.getUriForRoute(DataSource.TAF);
        Assert.assertEquals("xxxx?hoursBeforeNow=12", uri);

        uri = routeBuilder.getUriForRoute(DataSource.AIRCRAFTREPORT);
        Assert.assertEquals("abcd", uri);

    }
}
