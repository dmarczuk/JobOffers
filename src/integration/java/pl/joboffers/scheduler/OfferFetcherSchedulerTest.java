package pl.joboffers.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.JobOffersApplication;
import pl.joboffers.domain.offer.OfferFetchable;

import java.time.Duration;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = JobOffersApplication.class, properties = "scheduling.enabled=true")
public class OfferFetcherSchedulerTest extends BaseIntegrationTest {

    @SpyBean
    OfferFetchable remoteOfferClient;

    @Test
    public void should_run_offer_fetcher_scheduler_minimum_two_times() {
        await()
                .atMost(Duration.ofSeconds(3))
                .untilAsserted(() -> verify(remoteOfferClient, times(1)).fetchOffers());
    }
}
