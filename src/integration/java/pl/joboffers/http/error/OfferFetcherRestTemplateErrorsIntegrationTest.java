package pl.joboffers.http.error;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.joboffers.domain.offer.OfferFetchable;
import pl.joboffers.infrastructure.offer.http.OfferFetcherRestTemplateConfigurationProperties;


import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class OfferFetcherRestTemplateErrorsIntegrationTest {

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();
    OfferFetcherRestTemplateConfigurationProperties properties = new OfferFetcherRestTemplateConfigurationProperties(1500, 1000, "http://localhost", wireMockServer.getPort());
    OfferFetchable remoteOfferFetcherClient = new OfferFetcherRestTemplateErrorIntegrationTestConfig(properties).remoteOfferFetcherClient(wireMockServer.getPort());

    @Test
    void should_return_500_exception_when_fault_connection_reset_by_peer() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)
                ));

        //when
        Throwable throwable = catchThrowable(() -> remoteOfferFetcherClient.fetchOffers());

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    void should_return_401_exception_when_http_service_returning_unauthorized_status() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.UNAUTHORIZED.value())
                ));

        //when
        Throwable throwable = catchThrowable(() -> remoteOfferFetcherClient.fetchOffers());

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("401 UNAUTHORIZED");
    }

    @Test
    void should_return_500_exception_when_fault_empty_response() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withFault(Fault.EMPTY_RESPONSE)
                ));

        //when
        Throwable throwable = catchThrowable(() -> remoteOfferFetcherClient.fetchOffers());

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    void should_return_500_exception_when_fault_malformed_response_chunk() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
                ));

        //when
        Throwable throwable = catchThrowable(() -> remoteOfferFetcherClient.fetchOffers());

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    void should_return_500_exception_when_fault_random_data_then_close() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)
                ));

        //when
        Throwable throwable = catchThrowable(() -> remoteOfferFetcherClient.fetchOffers());

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    void should_return_404_exception_when_http_service_returning_not_found_status() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.NOT_FOUND.value())
                ));

        //when
        Throwable throwable = catchThrowable(() -> remoteOfferFetcherClient.fetchOffers());

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("404 NOT_FOUND");
    }

    @Test
    void should_return_500_exception_when_response_delay_is_5000ms_and_client_has_1500ms_read_timeout() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(giveTwoOffers())
                        .withFixedDelay(5000)
                ));

        //when
        Throwable throwable = catchThrowable(() -> remoteOfferFetcherClient.fetchOffers());

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    void should_return_204_exception_when_status_is_204_no_content() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NO_CONTENT.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(giveTwoOffers())
                ));

        //when
        Throwable throwable = catchThrowable(() -> remoteOfferFetcherClient.fetchOffers());

        //then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("204 NO_CONTENT");
    }

    private String giveTwoOffers() {
        return """
                  [
                      {
                          "id": "1000",
                          "title": "Junior Java Developer",
                          "company": "BlueSoft Sp. z o.o.",
                          "salary": "7 000 – 9 000 PLN",
                          "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"
                     },
                     {
                          "id": "2000",
                          "title": "Java (CMS) Developer",
                          "company": "Efigence SA",
                          "salary": "16 000 – 18 000 PLN",
                          "offerUrl": "https://nofluffjobs.com/pl/job/java-cms-developer-efigence-warszawa-b4qs8loh"
                     }]""".trim();
    }
}
























