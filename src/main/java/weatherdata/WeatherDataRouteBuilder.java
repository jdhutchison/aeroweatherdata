package weatherdata;

import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.Main;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;
import weatherdata.processors.DataSourceUriException;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines Camel routes to be added to the Camel context for weather weatherdata.data processing.
 */
@Component
public class WeatherDataRouteBuilder extends SpringRouteBuilder {

    // Defining route start/endpoints
    private static final String ALL_SOURCES_ROUTE = "direct:AllSources";
    private static final String SOURCE_ROUTE_PREFIX = "direct:Source-";
    private static final String HOURS_BEFORE_TOKEN = "{hoursBeforeNow}";

    // Holds individual JAXB formatters for each data source package
    private final Map<DataSource, JaxbDataFormat> FORMATTERS = new HashMap<DataSource, JaxbDataFormat>();

    // Used to put which data source is being processed
    public static final String DATA_SOURCE_PROPERTY = "dataSource";

    private Map<DataSource,String> dataSourceUris;
    private Map<DataSource, String> dataSourceTimeRanges;
    private String routeSchedule;
    private String messageQueue;

    /**
     * Allow this route to be run as an application for Servicemix debugging
     */
    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }


    @PostConstruct
    public void postConstruct() {
        for (DataSource dataSource : DataSource.values()) {
            JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
            jaxbDataFormat.setContextPath("weatherdata.data." + dataSource.name().toLowerCase());
            FORMATTERS.put(dataSource, jaxbDataFormat);
        }
    }

    @Override
    public void configure() throws Exception {

        from("quartz2://getAllData?cron=" + routeSchedule).id("WeatherDataSchedule").to(ALL_SOURCES_ROUTE).end();

        // When called, iterates over the list of sources to scrape
        from(ALL_SOURCES_ROUTE).id("ProcessAllSourcesRoute").log("Processing all weatherdata data sources")
                .to(SOURCE_ROUTE_PREFIX + DataSource.AIRCRAFTREPORT, SOURCE_ROUTE_PREFIX + DataSource.AIRSIGMET,
                        SOURCE_ROUTE_PREFIX + DataSource.METAR, SOURCE_ROUTE_PREFIX + DataSource.STATION,
                        SOURCE_ROUTE_PREFIX + DataSource.TAF);

        // Processing for inidividual data sources. At the end of the route the data is a POJO, and can be used
        // as needed.
        from(SOURCE_ROUTE_PREFIX + DataSource.AIRCRAFTREPORT).id(SOURCE_ROUTE_PREFIX + DataSource.AIRCRAFTREPORT)
                .to(getUriForRoute(DataSource.AIRCRAFTREPORT)).unmarshal(FORMATTERS.get(DataSource.AIRCRAFTREPORT)) // <- POJO available here
                .marshal().json(JsonLibrary.Jackson).to("activemq:topic:" + DataSource.AIRCRAFTREPORT + "//" + messageQueue);

        from(SOURCE_ROUTE_PREFIX + DataSource.AIRSIGMET).id(SOURCE_ROUTE_PREFIX + DataSource.AIRSIGMET)
                .to(getUriForRoute(DataSource.AIRSIGMET)).unmarshal(FORMATTERS.get(DataSource.AIRSIGMET)) // <- POJO available here
                // The line below performs operations on the plain Java object, not XML or JSON
                // .process(objectTransformationProcessor)
                .marshal().json(JsonLibrary.Jackson).to("activemq:topic:" + DataSource.AIRSIGMET + "//" + messageQueue);

        from(SOURCE_ROUTE_PREFIX + DataSource.METAR).id(SOURCE_ROUTE_PREFIX + DataSource.METAR)
                .to(getUriForRoute(DataSource.METAR)).unmarshal(FORMATTERS.get(DataSource.METAR)) // <- POJO available here
                .marshal().json(JsonLibrary.Jackson).to("activemq:topic:" + DataSource.METAR + "//" + messageQueue);

        from(SOURCE_ROUTE_PREFIX + DataSource.STATION).id(SOURCE_ROUTE_PREFIX + DataSource.STATION)
                .to(getUriForRoute(DataSource.STATION)).unmarshal(FORMATTERS.get(DataSource.STATION)) // <- POJO available here
                .marshal().json(JsonLibrary.Jackson).to("activemq:topic:" + DataSource.STATION + "//" + messageQueue);

        from(SOURCE_ROUTE_PREFIX + DataSource.TAF).id(SOURCE_ROUTE_PREFIX + DataSource.TAF)
                .to(getUriForRoute(DataSource.TAF)).unmarshal(FORMATTERS.get(DataSource.TAF)) // <- POJO available here
                .marshal().json(JsonLibrary.Jackson).to("activemq:topic:" + DataSource.TAF + "//" + messageQueue);
    }

    /**
     * Gets a URI with the correct parameters for a given data source. The hours ago parametre is set
     * where required.
     *
     * @param source which data source to get the URI for.
     * @return a proper URI with the time period set.
     */
    String getUriForRoute(DataSource source) {
        String uri = dataSourceUris.get(source);
        // Check there is a URI to process
        if (uri == null || uri.trim().length() == 0) {
            throw new DataSourceUriException(source);
        }

        if (dataSourceTimeRanges.containsKey(source)) {
            uri = uri.replace(HOURS_BEFORE_TOKEN, dataSourceTimeRanges.get(source));
        }
        return uri;
    }

    public void setRouteSchedule(String routeSchedule) {
        this.routeSchedule = routeSchedule;
    }

    public void setMessageQueue(String messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void setDataSourceUris(Map<DataSource, String> dataSourceUris) {
        this.dataSourceUris = dataSourceUris;
    }

    public void setDataSourceTimeRanges(Map<DataSource, String> dataSourceTimeRanges) {
        this.dataSourceTimeRanges = dataSourceTimeRanges;
    }
}
