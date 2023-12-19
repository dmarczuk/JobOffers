package pl.joboffers.apivalidationerror;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.infrastructure.apivalidation.ApiValidationErrorDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_400_bad_request_when_request_does_not_have_offer() throws Exception {
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
    public void should_return_400_bad_request_when_request_add_duplicate_offer_url() throws Exception {
        //given
        mockMvc.perform(post("/offers")
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

//      when
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
        MvcResult mvcResult = performGetResultWithAddOffer.andExpect(status().isBadRequest()).andReturn();
//        String json = mvcResult.getResponse().getContentAsString();
//        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
//
//        //then
//        assertThat(result.messages()).contains("offer url should not be null");
    }
}


















