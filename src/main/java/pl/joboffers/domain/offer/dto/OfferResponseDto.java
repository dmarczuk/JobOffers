package pl.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferResponseDto(String id,
                               String company,
                               String salary,
                               String position,
                               String offerUrl) { }
