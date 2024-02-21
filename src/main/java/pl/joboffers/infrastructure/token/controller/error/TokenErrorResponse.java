package pl.joboffers.infrastructure.token.controller.error;

import org.springframework.http.HttpStatus;

public record TokenErrorResponse(String name, HttpStatus status) {
}
