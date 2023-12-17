package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;
import pl.joboffers.domain.offer.exceptions.OfferNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository offerRepository;
    private final OfferService offerService;

    public List<OfferResponseDto> findAllOffers() {
        return offerRepository.findAll()
                .stream()
                .map(OfferMapper::mapperOfferToOfferResponseDto)
                .collect(Collectors.toList());
    }

    public OfferResponseDto findOfferById(String id) {
        return offerRepository.findById(id)
                .map(OfferMapper::mapperOfferToOfferResponseDto)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found"));
    }

    public OfferResponseDto saveOffer(OfferRequestDto offerRequestDto) {
        final Offer offer = OfferMapper.mapperOfferRequestDtoToOffer(offerRequestDto);
        Offer offerSaved = offerRepository.save(offer);
        return OfferMapper.mapperOfferToOfferResponseDto(offerSaved);
    }

    public List<OfferResponseDto> fetchAllOffersAndSaveAllIfNotExists() {
        return offerService.fetchAllOffersAndSaveAllIfNotExists()
                .stream()
                .map(OfferMapper::mapperOfferToOfferResponseDto)
                .collect(Collectors.toList());
    }
}