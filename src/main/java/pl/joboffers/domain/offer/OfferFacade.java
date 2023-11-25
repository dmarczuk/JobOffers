package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;
import pl.joboffers.domain.offer.exceptions.OfferNotFoundException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository offerRepository;
    private final OfferService offerService;

    public Set<OfferResponseDto> findAllOffers() {
        return offerRepository.findAll()
                .stream()
                .map(offer -> new OfferResponseDto(offer.id(), true, offer.offerUrl()))
                .collect(Collectors.toSet());
    }

    public OfferResponseDto findOfferById(String id) {
        return offerRepository.findById(id)
                .map(offer -> new OfferResponseDto(offer.id(), true, offer.offerUrl()))// add other parameters
                .orElseThrow(() -> new OfferNotFoundException("Offer not found"));
    }

    public OfferResponseDto saveOffer(OfferRequestDto offerRequestDto) {
        final Offer offer = Offer.builder()
                .id(offerRequestDto.id())
                .title(offerRequestDto.title())
                .company(offerRequestDto.company())
                .salary(offerRequestDto.salary())
                .offerUrl(offerRequestDto.offerUrl())
                .build();
        Offer offerSaved = offerRepository.save(offer);
        return new OfferResponseDto(offerSaved.id(), true, offerSaved.offerUrl());
    }

    public Set<OfferResponseDto> fetchAllOffersAndSaveAllIfNotExists() {

        return new HashSet<>();
    }
}