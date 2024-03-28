package pl.joboffers.cache.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.domain.offer.OfferFacade;
import pl.joboffers.infrastructure.loginandregister.controller.dto.JwtResponseDto;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;


public class RedisOfferCacheIntegrationTest extends BaseIntegrationTest {

    @SpyBean
    OfferFacade offerFacade;

    @Autowired
    CacheManager cacheManager;

    @Container
    private static final GenericContainer<?> REDIS;

    static {
        REDIS = new GenericContainer<>("redis").withExposedPorts(6379);
        REDIS.start();
    }

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.redis.port", () -> REDIS.getFirstMappedPort().toString());
        registry.add("spring.cache.type", () -> "redis");
        registry.add("spring.cache.redis.time-to-live", () -> "PT1S");
    }

    @Test
    public void should_save_offers_to_cache_and_then_invalidate_by_time_to_live() throws Exception {
        // step 1: someUser register with somePassword
        // given && when
        ResultActions registerAction = mockMvc.perform(post("/register")
                .content("""
                                {
                                "username": "someUser",
                                "password": "somePassword"
                                }
                                """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        registerAction.andExpect(status().isCreated());

        //step 2: login
        //given && when
        ResultActions successfulLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        MvcResult mvcResult1 = successfulLoginRequest.andExpect(status().isOk()).andReturn();
        String json = mvcResult1.getResponse().getContentAsString();
        JwtResponseDto jwtResponse = objectMapper.readValue(json, JwtResponseDto.class);
        String token = jwtResponse.token();

        //step 3: should save to cache request
        //given && when
        mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        verify(offerFacade, times(1)).findAllOffers();
        assertThat(cacheManager.getCacheNames().contains("jobOffers")).isTrue();

        // step 4: cache should be invalidated
        // given && when && then
        await()
                .atMost(Duration.ofSeconds(4))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    mockMvc.perform(get("/offers")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON));
                    verify(offerFacade, atLeast(2)).findAllOffers();
                    }
                );
    }


}
