package pl.joboffers.infrastructure.offer.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.Series.*;

public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        final HttpStatus statusCode = httpResponse.getStatusCode();
        final Series series = statusCode.series();
        if (series == SERVER_ERROR) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Error while using http client");
        } else if (series == CLIENT_ERROR) {
            if (statusCode == NOT_FOUND) {
                throw new ResponseStatusException(NOT_FOUND);
            } else if (statusCode == UNAUTHORIZED) {
                throw new ResponseStatusException(UNAUTHORIZED);
            }
        }
    }
}
