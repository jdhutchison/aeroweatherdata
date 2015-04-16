package weatherdata.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * A simple processor that allows an arbitrary object to be inserted into the
 * exchange. This allows weatherdata.data/objects from a regular Spring context to be
 * inserted into a Camel route.
 */
public class PopulateExchangeProcessor implements Processor {
    private Object payload;

    public PopulateExchangeProcessor(Object payload) {
        this.payload = payload;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getOut().setBody(payload);
    }
}
