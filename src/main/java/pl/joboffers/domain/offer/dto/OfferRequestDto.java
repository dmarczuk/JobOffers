package pl.joboffers.domain.offer.dto;

public record OfferRequestDto(String company,
                              String salary,
                              String position,
                              String offerUrl) { }