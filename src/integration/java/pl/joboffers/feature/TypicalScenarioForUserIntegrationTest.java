package pl.joboffers.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;
import pl.joboffers.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import pl.joboffers.infrastructure.offer.scheduler.OfferFetcherScheduler;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TypicalScenarioForUserIntegrationTest extends BaseIntegrationTest {

//    @Autowired
//    OfferFetchable offerFetchable;

    @Autowired
    OfferFetcherScheduler scheduler;

//    @Autowired
//    OfferFacade offerFacade;

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("joboffers.offer.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("joboffers.offer.http.client.config.port", () -> wireMockServer.getPort());
    }

    @Test
    public void should_user_go_by_whole_happy_path() throws Exception {

//      step 1: there are no offers in external HTTP server (http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers)
        //given && when && then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(giveZeroOffer())
                ));


//      step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        //given && when && then
        List<OfferResponseDto> savedZeroOffers = scheduler.fetchAllOffersAndSaveAllIfNotExists();
        assertThat(savedZeroOffers.size()).isEqualTo(0);


//      step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        //given && when
        ResultActions failedLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        failedLoginRequest
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("""
                        {
                        "name": "Bad Credentials",
                        "status": "UNAUTHORIZED"
                        }
                        """.trim()));


//      step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
        // given && when
        ResultActions failedGetOffersRequest = mockMvc.perform(post("/offers")
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        failedGetOffersRequest.andExpect(status().isForbidden());

//      step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
        // given && when
        ResultActions registerAction = mockMvc.perform(post("/register")
                        .content("""
                                {
                                "username": "someUser",
                                "password": "somePassword",
                                "email": "some@email"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON_VALUE));

        MvcResult registerActionResult = registerAction.andExpect(status().isCreated()).andReturn();
        String registerActionResultJson = registerActionResult.getResponse().getContentAsString();
        RegistrationResultDto registrationResultDto = objectMapper.readValue(registerActionResultJson, RegistrationResultDto.class);
        assertAll(
                () -> assertThat(registrationResultDto.username()).isEqualTo("someUser"),
                () -> assertThat(registrationResultDto.created()).isTrue(),
                () -> assertThat(registrationResultDto.id()).isNotNull()
        );

        // then
//      step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        //given && when
        ResultActions successfulLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword",
                        "email": "some@email"
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        MvcResult mvcResult1 = successfulLoginRequest.andExpect(status().isOk()).andReturn();
        String json = mvcResult1.getResponse().getContentAsString();
        JwtResponseDto jwtResponse = objectMapper.readValue(json, JwtResponseDto.class);
        String token = jwtResponse.token();
        assertAll(
                () -> assertThat(jwtResponse.username()).isEqualTo("someUser"),
                () -> assertThat(jwtResponse.token()).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$"))
        );

//      step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
        //given
        String offersUrl = "/offers";
        //when
        ResultActions performGetResultWithOffers = mockMvc.perform(get(offersUrl)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        MvcResult mvcResult = performGetResultWithOffers.andExpect(status().isOk()).andReturn();
        String jsonWithOffers = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<OfferResponseDto> jobOffers = objectMapper.readValue(jsonWithOffers, new TypeReference<>(){
        });
        assertThat(jobOffers.size()).isEqualTo(0);


//      step 8: there are 2 new offers in external HTTP server
        //given && when && then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(giveTwoOffers())
                ));


//      step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
        //when
        List<OfferResponseDto> savedTwoOffers = scheduler.fetchAllOffersAndSaveAllIfNotExists();

        //then
        assertThat(savedTwoOffers.size()).isEqualTo(2);


//      step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000
        //when
        ResultActions performGetResultWithTwoOffers = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
        );
        MvcResult mvcResultWithTwoOffers = performGetResultWithTwoOffers.andExpect(status().isOk()).andReturn();
        String jsonWithTwoOffers = mvcResultWithTwoOffers.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<OfferResponseDto> twoOffers = objectMapper.readValue(jsonWithTwoOffers, new TypeReference<>(){

        });

//        then
        assertAll(
                () -> assertThat(twoOffers.size()).isEqualTo(2),
                () -> assertThat(twoOffers).containsExactlyInAnyOrder(
                        firstOffer(savedTwoOffers.get(0).id()),
                        secondOffer(savedTwoOffers.get(1).id())
                )
        );


//      step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        //when
        ResultActions performGetResultsWithNotExistingId = mockMvc.perform(get("/offers/9999")
                .header("Authorization", "Bearer " + token)
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
        //given
        String idOfFirstAddedOffers = twoOffers.get(0).id();
        //when
        ResultActions performGetResultsWithExistingId = mockMvc.perform(get("/offers/" + idOfFirstAddedOffers)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        MvcResult mvcResultWithOneOffers = performGetResultsWithExistingId.andExpect(status().isOk()).andReturn();
//                .andExpect(content().json("""
//                        {
//                        "message": "Offer with id 1000 not found",
//                        "status": "NOT_FOUND"
//                        }
//                        """.trim())
        String jsonWithOneOffer = mvcResultWithOneOffers.getResponse().getContentAsString(StandardCharsets.UTF_8);
        OfferResponseDto oneOffer = objectMapper.readValue(jsonWithOneOffer, new TypeReference<>(){

        });
        assertAll(
                () -> assertThat(oneOffer).isNotNull(),
                () -> assertThat(oneOffer).isEqualTo(firstOffer(savedTwoOffers.get(0).id()))
        );

//      step 13: there are 2 new offers in external HTTP server
        //given && when && then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(giveFourOffers())
                ));


//      step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
        //when
        List<OfferResponseDto> savedTwoNewOffers = scheduler.fetchAllOffersAndSaveAllIfNotExists();

        //then
        assertThat(savedTwoNewOffers.size()).isEqualTo(2);


//      step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000
        //when
        ResultActions performGetResultWithTwoNewOffers = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
        );
        MvcResult mvcResultWithFourOffers = performGetResultWithTwoNewOffers.andExpect(status().isOk()).andReturn();
        String jsonWithFourOffers = mvcResultWithFourOffers.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<OfferResponseDto> fourOffers = objectMapper.readValue(jsonWithFourOffers, new TypeReference<>(){

        });

//        then
        assertAll(
                () -> assertThat(fourOffers.size()).isEqualTo(4),
                () -> assertThat(fourOffers).containsExactlyInAnyOrder(
                        firstOffer(savedTwoOffers.get(0).id()),
                        secondOffer(savedTwoOffers.get(1).id()),
                        thirdOffer(savedTwoNewOffers.get(0).id()),
                        fourthOffer(savedTwoNewOffers.get(1).id())
                )
        );

//      step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer as body and system returned CREATED(201) with saved offer
        //when
        ResultActions performGetResultWithAddOffer = mockMvc.perform(post("/offers")
                .header("Authorization", "Bearer " + token)
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
        //when
        ResultActions performGetResultWithOfferAddedByUser = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
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

    private String giveFourOffers() {
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
                     },
                     {
                          "title": "Junior Java Developer",
                          "company": "Sollers Consulting",
                          "salary": "7 500 – 11 500 PLN",
                          "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-sollers-consulting-warszawa-s6et1ucc"
                      },
                      {
                          "title": "Junior Full Stack Developer",
                          "company": "Vertabelo S.A.",
                          "salary": "7 000 – 9 000 PLN",
                          "offerUrl": "https://nofluffjobs.com/pl/job/junior-full-stack-developer-vertabelo-remote-k7m9xpnm"
                      }]""".trim();
    }

    private OfferResponseDto firstOffer(String id) {
        return new OfferResponseDto(id, "BlueSoft Sp. z o.o.", "7 000 – 9 000 PLN",
                "Junior Java Developer", "https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre");
    }

    private OfferResponseDto secondOffer(String id) {
        return new OfferResponseDto(id, "Efigence SA", "16 000 – 18 000 PLN",
                "Java (CMS) Developer", "https://nofluffjobs.com/pl/job/java-cms-developer-efigence-warszawa-b4qs8loh");
    }

    private OfferResponseDto thirdOffer(String id) {
        return new OfferResponseDto(id, "Sollers Consulting", "7 500 – 11 500 PLN",
                "Junior Java Developer", "https://nofluffjobs.com/pl/job/junior-java-developer-sollers-consulting-warszawa-s6et1ucc");
    }

    private OfferResponseDto fourthOffer(String id) {
        return new OfferResponseDto(id, "Vertabelo S.A.", "7 000 – 9 000 PLN",
                "Junior Full Stack Developer", "https://nofluffjobs.com/pl/job/junior-full-stack-developer-vertabelo-remote-k7m9xpnm");
    }
}
