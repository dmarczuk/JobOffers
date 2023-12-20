package pl.joboffers.infrastructure.apivalidation;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ApiValidationErrorOneMessageDto(String message, HttpStatus status) {
}
