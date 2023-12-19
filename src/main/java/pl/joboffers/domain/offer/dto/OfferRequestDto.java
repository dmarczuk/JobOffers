package pl.joboffers.domain.offer.dto;

import javax.validation.constraints.NotNull;

public record OfferRequestDto(String company,
                              String salary,
                              String position,
                              @NotNull(message = "offer url should not be null")
                              String offerUrl) { }