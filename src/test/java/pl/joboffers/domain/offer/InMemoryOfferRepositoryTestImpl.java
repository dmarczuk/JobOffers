package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.exceptions.OfferUrlAlreadyExistException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryOfferRepositoryTestImpl implements OfferRepository {
    Map<Integer, Offer> inMemoryDatabase = new ConcurrentHashMap<>();
    @Override
    public Offer save(Offer offer) {
        if(inMemoryDatabase.containsValue(offer.offerUrl())) {
            throw new OfferUrlAlreadyExistException("Offer url already exist in database");
        } else {
            inMemoryDatabase.put(offer.id(), offer);
            return offer;
        }
    }

    @Override
    public Offer findById(Integer id) {
        return inMemoryDatabase.get(id);
    }

    @Override
    public Set<Offer> findAll() {
        return new HashSet<>(inMemoryDatabase.values());
    }
}
