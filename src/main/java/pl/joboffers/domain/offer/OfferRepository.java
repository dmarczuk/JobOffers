package pl.joboffers.domain.offer;

import org.springframework.stereotype.Repository;

public interface OfferRepository extends Repository {

    Offer save(Offer offer);

    Offer findById(Integer id);

}
