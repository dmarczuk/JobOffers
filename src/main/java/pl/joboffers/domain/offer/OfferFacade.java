package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;
import pl.joboffers.domain.offer.exceptions.OfferDuplicateException;
import pl.joboffers.domain.offer.exceptions.OfferNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository offerRepository;
    private final OfferService offerService;

    @Cacheable("jobOffers")
    public List<OfferResponseDto> findAllOffers() {
        return offerRepository.findAll()
                .stream()
                .map(OfferMapper::mapperOfferToOfferResponseDto)
                .collect(Collectors.toList());
    }

    public OfferResponseDto findOfferById(String id) {
        return offerRepository.findById(id)
                .map(OfferMapper::mapperOfferToOfferResponseDto)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id " + id + " not found"));
    }

    public OfferResponseDto saveOffer(OfferRequestDto offerRequestDto) {
        final Offer offer = OfferMapper.mapperOfferRequestDtoToOffer(offerRequestDto);
        try {
            Offer offerSaved = offerRepository.save(offer);
            return OfferMapper.mapperOfferToOfferResponseDto(offerSaved);
        } catch (DuplicateKeyException e) {
            throw new OfferDuplicateException("Offer url already exist");
        }
    }

    public List<OfferResponseDto> fetchAllOffersAndSaveAllIfNotExists() {
        return offerService.fetchAllOffersAndSaveAllIfNotExists()
                .stream()
                .map(OfferMapper::mapperOfferToOfferResponseDto)
                .collect(Collectors.toList());
    }
}