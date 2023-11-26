package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.OfferResponseDto;
import pl.joboffers.domain.offer.exceptions.OfferDuplicateException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryOfferRepositoryTestImpl implements OfferRepository {
    Map<String, Offer> inMemoryDatabase = new ConcurrentHashMap<>();
    @Override
    public Offer save(Offer offer) {
        long count = inMemoryDatabase.values().stream()
                .filter(offerInDatabase -> offerInDatabase.offerUrl().equals(offer.offerUrl()))
                .count();
        if(count != 0) {
            throw new OfferDuplicateException("Offer url [" + offer.offerUrl() + "] already exist in database");
        } else {
            String id = UUID.randomUUID().toString();
            Offer savedOffer = new Offer(id, offer.companyName(), offer.position(), offer.salary(), offer.offerUrl());
            inMemoryDatabase.put(savedOffer.id(), savedOffer);
            return savedOffer;
        }
    }

    @Override
    public Set<Offer> saveAll(Set<Offer> offers) {
        return offers.stream()
                .map(this::save)
                .collect(Collectors.toSet());
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
