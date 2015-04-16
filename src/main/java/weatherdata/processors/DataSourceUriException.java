package weatherdata.processors;

import weatherdata.DataSource;

/**
 * This error is thrown when the {@link weatherdata.processors.CreateUriProcessor} is asked
 * to format the URI for an unknown datasource. This is due to a configuration error.
 */
public class DataSourceUriException extends RuntimeException {
    public DataSourceUriException(DataSource source) {
        super("Unable to find a URI set for (unknown) data source: " + source.name().toLowerCase());
    }
}
