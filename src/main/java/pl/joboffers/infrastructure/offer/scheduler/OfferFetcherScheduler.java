package pl.joboffers.infrastructure.offer.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.offer.OfferFacade;

@Component
@AllArgsConstructor
@Log4j2
public class OfferFetcherScheduler {

    private final OfferFacade offerFacade;
   // private final SchedulerConfigurationProperties properties;
    @Scheduled(cron = "${joboffers.offer.cron}")
    public void fetchOffers() {
        log.info("scheduler started");
        offerFacade.fetchAllOffersAndSaveAllIfNotExists();
    }
}
