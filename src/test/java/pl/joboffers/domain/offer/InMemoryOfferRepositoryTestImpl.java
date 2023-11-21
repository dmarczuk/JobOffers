package pl.joboffers.domain.offer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOfferRepositoryTestImpl implements OfferRepository {
    Map<Integer, Offer> inMemoryDatabase = new ConcurrentHashMap<>();
    @Override
    public Offer save(Offer offer) {
        inMemoryDatabase.put(offer.getId(), offer);
        return offer;
    }

    @Override
    public Offer findById(Integer id) {
        return inMemoryDatabase.get(id);
    }
}
