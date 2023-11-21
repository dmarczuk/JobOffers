package pl.joboffers.domain.offer;

import java.util.ArrayList;
import java.util.List;

public class OfferFacade {

    public List<Offer> findAllOffers() {
        return new ArrayList<>();
    }

    public Offer findOfferById(Integer id) {
        return new Offer(1, "url");
    }

    public String saveOffer(Offer offer) {
        return "success";
    }

    public String fetchAllOffersAndSaveAllIfNotExists(List<Offer> offers) {
        return "success";
    }
}
