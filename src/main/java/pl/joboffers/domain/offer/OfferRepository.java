package pl.joboffers.domain.offer;


import com.mongodb.client.MongoDatabase;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface OfferRepository extends MongoRepository<Offer, String> {

    Offer save(Offer offer);

    Set<Offer> saveAll(Set<Offer> offers);

    Optional<Offer> findById(String id);

    List<Offer> findAll();

    boolean offerExist(Offer offer);

}
