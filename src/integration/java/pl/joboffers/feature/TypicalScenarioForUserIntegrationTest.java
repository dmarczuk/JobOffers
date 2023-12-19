package pl.joboffers.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.domain.offer.OfferFetchable;
import pl.joboffers.domain.offer.dto.OfferResponseDto;
import pl.joboffers.infrastructure.offer.scheduler.OfferFetcherScheduler;
import java.nio.charset.StandardCharsets;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TypicalScenarioForUserIntegrationTest extends BaseIntegrationTest {

    @Autowired
    OfferFetchable offerFetchable;

    @Autowired
    OfferFetcherScheduler scheduler;

    @Test
    public void should_user_go_by_whole_happy_path() throws Exception { //?? name of test ??????

//      step 1: there are no offers in external HTTP server (http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers)
        //given && when  && then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(giveTwoOffers())
                ));


//      step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        //given && when && then
        List<OfferResponseDto> savedOffers = scheduler.fetchAllOffersAndSaveAllIfNotExists();
        assertThat(savedOffers.size()).isEqualTo(2);

        List<OfferResponseDto> savedDuplicateOffers = scheduler.fetchAllOffersAndSaveAllIfNotExists();
        assertThat(savedDuplicateOffers.size()).isEqualTo(0);


//      step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
//      step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
//      step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
//      step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC


//      step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
        //given
        //when
        ResultActions performGetResultWithOffers = mockMvc.perform(get("/offers")
//                .content(giveTwoOffers())
                .contentType(MediaType.APPLICATION_JSON)
        );
        MvcResult mvcResult = performGetResultWithOffers.andExpect(status().isOk()).andReturn();
        String jsonWithOffers = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<OfferResponseDto> jobOffers = objectMapper.readValue(jsonWithOffers, new TypeReference<>(){

        });
//        List<JobOfferResponse> jobOffers = jobOffersResponseDto.jobOffers();

//        then
        assertAll(
   //             () -> assertThat(jobOffers.size()).isEqualTo(2),
                () -> assertThat(jobOffers.get(0).offerUrl()).isEqualTo("https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"),
                () -> assertThat(jobOffers.get(0).salary()).isEqualTo("7 000 – 9 000 PLN"),
                () -> assertThat(jobOffers.get(1).offerUrl()).isEqualTo("https://nofluffjobs.com/pl/job/java-cms-developer-efigence-warszawa-b4qs8loh"),
                () -> assertThat(jobOffers.get(1).salary()).isEqualTo( "16 000 – 18 000 PLN")
        );



//      step 8: there are 2 new offers in external HTTP server
//      step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
//      step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000


//      step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        //given
        //when
        ResultActions performGetResultsWithNotExistingId = mockMvc.perform(get("/offers/9999")
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        performGetResultsWithNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json("""
                                {
                                "message": "Offer with id 9999 not found",
                                "status": "NOT_FOUND"
                                }
                                """.trim())
                );

//      step 12: user made GET /offers/1000 and system returned OK(200) with offer
//      step 13: there are 2 new offers in external HTTP server
//      step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
//      step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000


//      step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer as body and system returned CREATED(201) with saved offer
        //when
        ResultActions performGetResultWithAddOffer = mockMvc.perform(post("/offers")
                .content("""           
                      {
                          "company": "testCompany",
                          "salary": "testSalary",
                          "position": "testPosition",
                          "offerUrl": "testUrl"
                     }
                     """.trim())
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        String createdOfferJson = performGetResultWithAddOffer.andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OfferResponseDto parsedCreatedOfferJson = objectMapper.readValue(createdOfferJson, OfferResponseDto.class);
        String id = parsedCreatedOfferJson.id();
        assertAll(
                () -> assertThat(parsedCreatedOfferJson.offerUrl()).isEqualTo("testUrl"),
                () -> assertThat(parsedCreatedOfferJson.company()).isEqualTo("testCompany"),
                () -> assertThat(parsedCreatedOfferJson.salary()).isEqualTo("testSalary"),
                () -> assertThat(parsedCreatedOfferJson.position()).isEqualTo("testPosition"),
                () -> assertThat(id).isNotNull()
        );

//      step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 1 offer
        //given
        //when
        ResultActions performGetResultWithOfferAddedByUser = mockMvc.perform(get("/offers")
//                .content(giveTwoOffers())
                        .contentType(MediaType.APPLICATION_JSON)
        );
        MvcResult mvcResultFromUser = performGetResultWithOfferAddedByUser.andExpect(status().isOk()).andReturn();
        String jsonWithOffersFromUser = mvcResultFromUser.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<OfferResponseDto> jobOffersFromUser = objectMapper.readValue(jsonWithOffersFromUser, new TypeReference<>(){

        });
        List<OfferResponseDto> addedOffer = jobOffersFromUser
                .stream()
                .filter(offer -> offer.offerUrl().equals("testUrl"))
                .toList();
        assertAll(
                () -> assertThat(addedOffer.size()).isEqualTo(1),
                () -> assertThat(addedOffer.get(0).offerUrl()).isEqualTo("testUrl"),
                () -> assertThat(addedOffer.get(0).salary()).isEqualTo("testSalary")
        );

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
