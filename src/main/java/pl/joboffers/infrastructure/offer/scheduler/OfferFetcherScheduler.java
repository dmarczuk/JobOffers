package pl.joboffers.infrastructure.offer.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.offer.OfferFacade;
fimport pl.joboffers.domain.offer.dto.OfferResponseDto;

import java.util.Set;

@Component
@AllArgsConstructor
@Log4j2
public class OfferFetcherScheduler {

    private final OfferFacade offerFacade;
    @Scheduled(cron = "${joboffers.offer.scheduler.delayTime}")
    public Set<OfferResponseDto> fetchOffers() {
        log.info("scheduler started");
        return offerFacade.fetchAllOffersAndSaveAllIfNotExists();
    }
}
