package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository offerRepository;

    public Set<Offer> findAllOffers() {
        return offerRepository.findAll();
    }

    public Offer findOfferById(Integer id) {
        return offerRepository.findById(id);
    }

    public String saveOffer(Offer offer) {
        if (findOfferById(offer.getId()) == null) {
            offerRepository.save(offer);
            return "success";
        } else {
            return "offer exist in database";
        }
    }

    public String fetchAllOffersAndSaveAllIfNotExists(List<Offer> offers) {
        return "success";
    }
}
