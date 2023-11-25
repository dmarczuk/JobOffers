package pl.joboffers.domain.offer;


import java.util.Optional;
import java.util.Set;

interface OfferRepository {

    Offer save(Offer offer);

    Set<Offer> saveAll(Set<Offer> offers);

    Optional<Offer> findById(String id);

    Set<Offer> findAll();

}
