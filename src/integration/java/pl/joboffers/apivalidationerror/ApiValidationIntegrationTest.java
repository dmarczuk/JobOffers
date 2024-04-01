package pl.joboffers.apivalidationerror;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.infrastructure.apivalidation.ApiValidationErrorDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationIntegrationTest extends BaseIntegrationTest {

    @Test
    @WithMockUser
    public void should_return_400_bad_request_when_request_does_not_have_correct_offer() throws Exception {
        //when
        ResultActions performGetResultWithAddOffer = mockMvc.perform(post("/offers")
                .content("""
                        {
                        "company": "testCompany",
                        "position": "",
                        "salary": ""
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
        );
        MvcResult mvcResult = performGetResultWithAddOffer.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);

        //then
        assertThat(result.messages()).containsExactlyInAnyOrder(
                "offer url should not be null",
                "offer url should not be empty",
                "salary should not be empty",
                "position should not be empty"
        );
    }

    @Test
    public void should_not_register_user_with_invalid_username() throws Exception {
        // given && when
        ResultActions failedRegisterAction = mockMvc.perform(post("/register")
                .content("""
                                {
                                "username": "",
                                "password": "somePassword"
                                }
                                """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        MvcResult mvcResult = failedRegisterAction.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);

        //then
        assertAll(
                () -> assertThat(result.messages().contains("username should not be empty")).isTrue(),
                () -> assertThat(result.messages().contains("email should not be empty")).isTrue(),
                () -> assertThat(result.messages().contains("email name should not be null")).isTrue(),
                () -> assertThat(result.status()).isEqualTo(HttpStatus.BAD_REQUEST)
        );
    }

    @Test
    public void should_not_register_user_with_invalid_password() throws Exception {
        // given && when
        ResultActions failedRegisterAction = mockMvc.perform(post("/register")
                .content("""
                                {
                                "username": "someUser",
                                "password": "short",
                                "email": "email@com"
                                }
                                """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        MvcResult mvcResult = failedRegisterAction.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);

        //then
        assertAll(
                () -> assertThat(result.messages().contains("password should have minimum 8 characters")).isTrue(),
                () -> assertThat(result.status()).isEqualTo(HttpStatus.BAD_REQUEST)
        );
    }

    @Test
    public void should_not_register_user_with_invalid_email() throws Exception {
        // given && when
        ResultActions failedRegisterAction = mockMvc.perform(post("/register")
                .content("""
                                {
                                "username": "someUser",
                                "password": "somePassword",
                                "email": "invalidEmail"
                                }
                                """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        MvcResult mvcResult = failedRegisterAction.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);

        //then
        assertAll(
                () -> assertThat(result.messages().contains("email should be valid")).isTrue(),
                () -> assertThat(result.status()).isEqualTo(HttpStatus.BAD_REQUEST)
        );
    }
}
