package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
class OfferService {

    private OfferFetchable offerFetcher;
    private OfferRepository offerRepository;

    public List<Offer> fetchAllOffersAndSaveAllIfNotExists() {
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
