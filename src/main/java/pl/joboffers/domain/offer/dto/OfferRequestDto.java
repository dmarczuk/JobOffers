package pl.joboffers.domain.offer.dto;

public record OfferRequestDto(String id,
                              String title,
                              String company,
                              String salary,
                              String offerUrl) { }