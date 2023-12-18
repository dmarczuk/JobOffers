package pl.joboffers.infrastructure.offer.controller;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
record OfferNotFoundErrorResponse(String message, HttpStatus status) {

}
