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

    public List<Offer> fetchAllOffersAndSaveAllIfNotExists() { //method to fetch offers from server
        List<Offer> jobOffers = fetchOffers();
        List<Offer> all = offerRepository.findAll();
        List<Offer> offersToSave = filterNonExistingOffer(jobOffers);
        try {
            List<Offer> all2 = offerRepository.findAll();
            List<Offer> savedOffers = offerRepository.saveAll(offersToSave);
            // podwojne zapisywanie do bazy??? Dlaczego?? (przy dwukrotnym dodawaniu 2 tych samych ofert)
            List<Offer> all3 = offerRepository.findAll();
            return savedOffers;
        } catch (OfferDuplicateException duplicateException) {
            throw new SaveOfferException(duplicateException.getMessage()); // add jobOffers to argument
        }
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
