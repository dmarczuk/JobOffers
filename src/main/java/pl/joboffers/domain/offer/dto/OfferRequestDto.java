package pl.joboffers.domain.offer.dto;

public record OfferRequestDto(Integer id,
                              String title,
                              String company,
                              String salary,
                              String offerUrl) { }