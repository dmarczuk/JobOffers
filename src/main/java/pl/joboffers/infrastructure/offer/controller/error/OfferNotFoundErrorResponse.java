package pl.joboffers.infrastructure.offer.controller.error;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
record OfferNotFoundErrorResponse(String message, HttpStatus status) {

}
