package weatherdata.processors;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import weatherdata.DataSource;

import java.util.HashMap;
import java.util.Map;

public class CreateUriProcessorTest {
    private Exchange mockExchange = Mockito.mock(Exchange.class);
    private Message mockInMsg = Mockito.mock(Message.class);
    private Message mockOutMsg = Mockito.mock(Message.class);

    private Map<DataSource, String> uriMap = new HashMap<DataSource, String>();
    private Map<DataSource, String> timeRangeMap = new HashMap<DataSource, String>();
    private CreateUriProcessor processor = new CreateUriProcessor();

    @Before
    public void setup() {
        when(mockExchange.getIn()).thenReturn(mockInMsg);
        when(mockExchange.getOut()).thenReturn(mockOutMsg);
        //when(mockOutMsg.getBody(DataSource.class)).

        // Mock values for uri
        uriMap.put(DataSource.AIRCRAFTREPORT, "abcd");
        uriMap.put(DataSource.AIRSIGMET, "");
        uriMap.put(DataSource.METAR, null);
        uriMap.put(DataSource.TAF, "xxxx?hoursBeforeNow={hoursBeforeNow}");

        timeRangeMap.put(DataSource.TAF, "12");

        processor.setDataSourceTimeRanges(timeRangeMap);
        processor.setDataSourceUris(uriMap);

    }

    @Test(expected = DataSourceUriException.class)
    public void shouldThrowExceptionForNoUriForSource() {
       when(mockInMsg.getBody(DataSource.class)).thenReturn(DataSource.STATION);
       processor.process(mockExchange);
    }

    @Test(expected = DataSourceUriException.class)
    public void shouldThrowExceptionForNullOrEmptyUriForSource() {
        when(mockInMsg.getBody(DataSource.class)).thenReturn(DataSource.AIRSIGMET);
        try {
            processor.process(mockExchange);
            Assert.fail("Should throw exception here");
        } catch (DataSourceUriException e) { }

        when(mockInMsg.getBody(DataSource.class)).thenReturn(DataSource.METAR);
        processor.process(mockExchange);
    }

    @Test
    public void shouldReturnUriForSource() {
        // With replacement
        when(mockInMsg.getBody(DataSource.class)).thenReturn(DataSource.TAF);
        processor.process(mockExchange);
        verify(mockOutMsg).setBody("xxxx?hoursBeforeNow=12");

        when(mockInMsg.getBody(DataSource.class)).thenReturn(DataSource.AIRCRAFTREPORT);
        processor.process(mockExchange);
        verify(mockOutMsg).setBody("abcd");

    }
}
