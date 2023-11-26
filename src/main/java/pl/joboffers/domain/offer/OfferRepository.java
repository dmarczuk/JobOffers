package pl.joboffers.domain.offer;


import java.util.Set;

interface OfferRepository {

    Offer save(Offer offer);

    Offer findById(Integer id);

    Set<Offer> findAll();

}
