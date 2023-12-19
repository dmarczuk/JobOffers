package pl.joboffers.apivalidationerror;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_400_bad_request_when_request_does_not_have_offer() throws Exception {
        ResultActions performGetResultWithAddOffer = mockMvc.perform(post("/offers")
                .content("""
                        {
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON)
        );
        performGetResultWithAddOffer.andExpect(status().isBadRequest()).andReturn();
    }
}
