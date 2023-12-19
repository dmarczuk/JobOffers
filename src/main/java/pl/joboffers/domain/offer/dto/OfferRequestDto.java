package pl.joboffers.domain.offer.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record OfferRequestDto(@NotEmpty(message = "company name should not be empty")
                              @NotNull(message = "company name should not be null")
                              String company,
                              @NotEmpty(message = "salary should not be empty")
                              @NotNull(message = "salary should not be null")
                              String salary,
                              @NotEmpty(message = "position should not be empty")
                              @NotNull(message = "position should not be null")
                              String position,
                              @NotEmpty(message = "offer url should not be empty")
                              @NotNull(message = "offer url should not be null")
                              String offerUrl) { }