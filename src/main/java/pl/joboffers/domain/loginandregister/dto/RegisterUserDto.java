package pl.joboffers.domain.loginandregister.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record RegisterUserDto(@NotEmpty(message = "username should not be empty")
                              @NotNull(message = "username should not be null")
                              String username,
                              @NotEmpty(message = "password should not be empty")
                              @NotNull(message = "password should not be null")
                              @Size(min = 8, message = "password should have minimum 8 characters")
                              String password,
                              @NotEmpty(message = "email should not be empty")
                              @NotNull(message = "email name should not be null")
                              @Email(message = "email should be valid")
                              String email
) {
}
