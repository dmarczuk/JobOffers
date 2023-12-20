package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.joboffers.domain.offer.exceptions.OfferDuplicateException;
import pl.joboffers.domain.offer.exceptions.SaveOfferException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
class OfferService {

    private OfferFetchable offerFetcher;
    private OfferRepository offerRepository;

    public List<Offer> fetchAllOffersAndSaveAllIfNotExists() { //method to fetch offers from server (problem z podwojnym zapisywaniem)
        List<Offer> jobOffers = fetchOffers();
        List<Offer> offersToSave = filterNonExistingOffer(jobOffers);
        return offerRepository.saveAll(offersToSave);
    }

    private List<Offer> fetchOffers() {
        return offerFetcher.fetchOffers().stream()
                .map(OfferMapper::mapperJobOfferResponseToOffer)
                .collect(Collectors.toList());
    }

    private List<Offer> filterNonExistingOffer(List<Offer> jobOffers) {
        return jobOffers.stream()
                .filter(offer -> !offerRepository.existsByOfferUrl(offer.offerUrl()))
                .collect(Collectors.toList());
    }
}
