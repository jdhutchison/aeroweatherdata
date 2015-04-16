package weatherdata.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import weatherdata.data.airsigmet.Request;

public class OldAirSigMetDataProcessor implements Processor {

    private static Request lastRequestData;

    @Override
    public void process(Exchange exchange) throws Exception {
        Request newData = exchange.getIn().getBody(Request.class);

        // Check if there is old data to use
        if (lastRequestData != null) {

            // Do comparisons between old and new

            // Store the new data so it becomes the old data for next time
            lastRequestData = newData;
        }

        // The new data is automatically passed on to the rest of the route
    }
}
