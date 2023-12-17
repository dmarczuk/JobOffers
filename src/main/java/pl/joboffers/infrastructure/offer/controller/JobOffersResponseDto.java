package pl.joboffers.infrastructure.offer.controller;

import lombok.Builder;
import pl.joboffers.domain.offer.dto.JobOfferResponse;
import pl.joboffers.domain.offer.dto.OfferResponseDto;

import java.util.List;
import java.util.Set;

@Builder
public record JobOffersResponseDto(List<OfferResponseDto> jobOffers) {
}
