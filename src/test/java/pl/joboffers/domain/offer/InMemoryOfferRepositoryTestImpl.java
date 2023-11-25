package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.exceptions.OfferDuplicateException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOfferRepositoryTestImpl implements OfferRepository {
    Map<String, Offer> inMemoryDatabase = new ConcurrentHashMap<>();
    @Override
    public Offer save(Offer offer) {
        if(inMemoryDatabase.containsKey(offer.id())) {
            throw new OfferDuplicateException("Offer url already exist in database");
        } else {
            inMemoryDatabase.put(offer.id(), offer);
            return offer;
        }
    }

    @Override
    public Optional<Offer> findById(String id) {
        return Optional.ofNullable(inMemoryDatabase.get(id));
    }

    @Override
    public Set<Offer> findAll() {
        return new HashSet<>(inMemoryDatabase.values());
    }
}
