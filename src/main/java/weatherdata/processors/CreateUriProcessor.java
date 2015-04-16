package weatherdata.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Required;
import weatherdata.DataSource;
import weatherdata.WeatherDataRouteBuilder;

import java.util.Map;

/**
 * Given the URIs for each data source, and the time ranges, produces a uri
 * that is to be used to fetch data from its source.
 */
public class CreateUriProcessor implements Processor {
    private static final String HOURS_BEFORE_TOKEN = "{hoursBeforeNow}";

    private Map<DataSource,String> dataSourceUris;
    private Map<DataSource, String> dataSourceTimeRanges;

    @Override
    public void process(Exchange exchange) throws DataSourceUriException {
        DataSource source = exchange.getIn().getBody(DataSource.class);
        String uri = dataSourceUris.get(source);

        // Check there is a URI to process
        if (uri == null || uri.trim().length() == 0) {
            throw new DataSourceUriException(source);
        }

        if (dataSourceTimeRanges.containsKey(source)) {
            uri = uri.replace(HOURS_BEFORE_TOKEN, dataSourceTimeRanges.get(source));
        }

        // Place the source in the header of the exchange, the uri in the out message
        exchange.setProperty(WeatherDataRouteBuilder.DATA_SOURCE_PROPERTY, source);
        exchange.getOut().setBody(uri);
    }

    @Required
    public void setDataSourceUris(Map<DataSource, String> dataSourceUris) {
        this.dataSourceUris = dataSourceUris;
    }

    @Required
    public void setDataSourceTimeRanges(Map<DataSource, String> dataSourceTimeRanges) {
        this.dataSourceTimeRanges = dataSourceTimeRanges;
    }
}
