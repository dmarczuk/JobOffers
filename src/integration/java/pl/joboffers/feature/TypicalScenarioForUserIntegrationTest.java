package pl.joboffers.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.domain.offer.OfferFetchable;
import pl.joboffers.domain.offer.dto.JobOfferResponse;
import pl.joboffers.domain.offer.dto.OfferResponseDto;
import pl.joboffers.infrastructure.offer.scheduler.OfferFetcherScheduler;

import java.time.Duration;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.*;

//@EnableConfigurationProperties
public class TypicalScenarioForUserIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OfferFetchable offerFetchable;

    @Autowired
    OfferFetcherScheduler scheduler;

    @Test
    public void should_user_go_by_whole_path() { //?? name of test ??????

//      step 1: there are no offers in external HTTP server (http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers)
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(giveTwoOffers())
                ));
        //when
        //Set<JobOfferResponse> jobOfferResponses = offerFetchable.fetchOffers();

        //then

//      step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        //given

        //when
        Set<OfferResponseDto> savedOffers = scheduler.fetchAllOffersAndSaveAllIfNotExists();
        assertThat(savedOffers.size()).isEqualTo(2);

        Set<OfferResponseDto> savedDuplicateOffers = scheduler.fetchAllOffersAndSaveAllIfNotExists();
        assertThat(savedDuplicateOffers.size()).isEqualTo(0);

        //then


//      step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
//      step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
//      step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
//      step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
//      step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
//      step 8: there are 2 new offers in external HTTP server
//      step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
//      step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000
//      step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
//      step 12: user made GET /offers/1000 and system returned OK(200) with offer
//      step 13: there are 2 new offers in external HTTP server
//      step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
//      step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000


    }

    private String giveZeroOffer() {
        return "[]";
    }

    private String giveTwoOffers() {
        return """
                  [
                      {
                          "title": "Junior Java Developer",
                          "company": "BlueSoft Sp. z o.o.",
                          "salary": "7 000 – 9 000 PLN",
                          "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"
                     },
                     {
                          "title": "Java (CMS) Developer",
                          "company": "Efigence SA",
                          "salary": "16 000 – 18 000 PLN",
                          "offerUrl": "https://nofluffjobs.com/pl/job/java-cms-developer-efigence-warszawa-b4qs8loh"
                     }]""".trim();
    }
}
