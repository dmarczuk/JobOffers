package pl.joboffers.domain.offer.dto;

import lombok.Builder;

@Builder
public record JobOfferResponse(String title,
                               String companyName,
                               String salary,
                               String offerUrl) { }
