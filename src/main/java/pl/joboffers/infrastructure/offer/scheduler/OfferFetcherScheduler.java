package pl.joboffers.infrastructure.offer.scheduler;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.offer.OfferFacade;

@Component
@AllArgsConstructor
public class OfferFetcherScheduler {

    private final OfferFacade offerFacade;
    @Scheduled
    public void f() {
        offerFacade.fetchAllOffersAndSaveAllIfNotExists();
    }
}
