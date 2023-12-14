package pl.joboffers.domain.offer;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
@Repository
interface OfferRepository extends MongoRepository<Offer, String> {

   // Offer save(Offer offer);

    //Set<Offer> saveAll(Set<Offer> offers);

    Optional<Offer> findById(String id);

    //List<Offer> findAll();

    boolean existsOfferByOfferUrl(String url);



}
