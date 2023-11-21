package pl.joboffers.domain.offer;


interface OfferRepository {

    Offer save(Offer offer);

    Offer findById(Integer id);

}
