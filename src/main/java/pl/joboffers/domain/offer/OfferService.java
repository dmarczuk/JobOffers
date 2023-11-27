package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import pl.joboffers.domain.offer.exceptions.OfferDuplicateException;
import pl.joboffers.domain.offer.exceptions.SaveOfferException;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
class OfferService {

    private OfferFetchable offerFetcher;
    private OfferRepository offerRepository;

    public Set<Offer> fetchAllOffersAndSaveAllIfNotExists() { //method to fetch offers from server
        Set<Offer> jobOffers = fetchOffers();
        Set<Offer> offerToSave = filterNonExistingOffer(jobOffers);
        try {
            return offerRepository.saveAll(offerToSave);
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
                .filter(offer -> !offerRepository.offerExist(offer))
                .collect(Collectors.toSet());
    }
}
