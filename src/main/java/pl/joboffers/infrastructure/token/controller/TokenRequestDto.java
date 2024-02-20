package pl.joboffers.infrastructure.token.controller;

import javax.validation.constraints.NotBlank;

public record TokenRequestDto(
        @NotBlank(message = "{Username.not.blank}")
        String username,
        @NotBlank(message = "{password.not.blank")
        String password
) {
}
