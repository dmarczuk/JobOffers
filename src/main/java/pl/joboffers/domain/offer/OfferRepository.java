package pl.joboffers.domain.offer;


import java.util.Optional;
import java.util.Set;

interface OfferRepository {

    Offer save(Offer offer);

    Optional<Offer> findById(Integer id);

    Set<Offer> findAll();

}
