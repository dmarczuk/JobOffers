package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponse;
import pl.joboffers.domain.offer.dto.OfferRequestDto;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

class OfferMapper {

    public static Offer mapperOfferRequestDtoToOffer(OfferRequestDto requestDto) {
        return Offer.builder()
                .companyName(requestDto.company())
                .position(requestDto.position())
                .salary(requestDto.salary())
                .offerUrl(requestDto.offerUrl())
                .build();
    }

    public static OfferResponseDto mapperOfferToOfferResponseDto(Offer offer) {
        return new OfferResponseDto(
                offer.id(),
                offer.companyName(),
                offer.salary(),
                offer.position(),
                offer.offerUrl());
    }


    public static Offer mapperJobOfferResponseToOffer(JobOfferResponse jobOfferResponse) {
        return Offer.builder()
                .companyName(jobOfferResponse.company())
                .position(jobOfferResponse.title())
                .salary(jobOfferResponse.salary())
                .offerUrl(jobOfferResponse.offerUrl())
                .build();
    }
}
