package pl.joboffers.controller.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.infrastructure.apivalidation.ApiValidationErrorOneMessageDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OfferUrlDuplicateErrorIntegrationTest extends BaseIntegrationTest {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }


    @Test
    @WithMockUser
    public void should_return_409_conflict_when_user_add_duplicate_offer_url() throws Exception {
        // step 1
        //given && when
        ResultActions perform = mockMvc.perform(post("/offers")
                .content("""
                        {
                            "company": "testCompany",
                            "salary": "testSalary",
                            "position": "testPosition",
                            "offerUrl": "testUrl"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
        );
        // then
        perform.andExpect(status().isCreated());

        // step 2
        // given && when
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
        // then
        performGetResultWithAddOffer.andExpect(status().isConflict());
        MvcResult mvcResult = performGetResultWithAddOffer.andExpect(status().isConflict()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorOneMessageDto result = objectMapper.readValue(json, ApiValidationErrorOneMessageDto.class);

        //then
        assertAll(
                () -> assertThat(result.message()).isEqualTo("Offer url already exist"),
                () -> assertThat(result.status()).isEqualTo(HttpStatus.CONFLICT)
        );
    }
}
