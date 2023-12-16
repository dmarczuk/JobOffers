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

    public Set<Offer> fetchAllOffersAndSaveAllIfNotExists() { //method to fetch offers from server
        Set<Offer> jobOffers = fetchOffers();
        List<Offer> all = offerRepository.findAll();
        Set<Offer> offersToSave = filterNonExistingOffer(jobOffers);
        try {
            List<Offer> all2 = offerRepository.findAll();
            List<Offer> savedOffers = offerRepository.saveAll(offersToSave);
            // podwojne zapisywanie do bazy??? Dlaczego?? (przy dwukrotnym dodawaniu 2 tych samych ofert)
            List<Offer> all3 = offerRepository.findAll();
            return new HashSet<>(savedOffers);
        } catch (OfferDuplicateException duplicateException) {
            throw new SaveOfferException(duplicateException.getMessage()); // add jobOffers to argument
        }
    }

    private Set<Offer> fetchOffers() {
        return offerFetcher.fetchOffers().stream()
                .map(OfferMapper::mapperJobOfferResponseToOffer)
                .collect(Collectors.toSet());
    }

    private Set<Offer> filterNonExistingOffer(Set<Offer> jobOffers) {
        return jobOffers.stream()
                .filter(offer -> !offerRepository.existsByOfferUrl(offer.offerUrl()))
                .collect(Collectors.toSet());
    }
}
