package pl.joboffers.controller.error;

import org.junit.jupiter.api.Test;
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
import pl.joboffers.infrastructure.apivalidation.ApiValidationErrorOneMessageDto;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserAlreadyExistErrorIntegrationTest extends BaseIntegrationTest {


    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    public void should_return_409_conflict_message_when_user_try_to_register_and_username_already_exist_in_database() throws Exception {
        //step 1 - user registered in database
        // given && when && then
        ResultActions userRegisterAction = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword",
                        "email": "some@email"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //step 2 - user who already exist in database try to register
        // given && when
        ResultActions failedRegisterAction = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword",
                        "email": "some@email"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        MvcResult mvcResult = failedRegisterAction.andExpect(status().isConflict()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorOneMessageDto result = objectMapper.readValue(json, ApiValidationErrorOneMessageDto.class);

        //then
        assertAll(
                () -> assertThat(result.message()).isEqualTo("User already exist in database"),
                () -> assertThat(result.status()).isEqualTo(HttpStatus.CONFLICT)
        );
    }
}
