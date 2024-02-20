package pl.joboffers.infrastructure.token.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.joboffers.infrastructure.security.jwt.JwtAuthenticatorFacade;

@RestController
@AllArgsConstructor
public class TokenController {

    private JwtAuthenticatorFacade jwtAuthenticatorFacade;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(TokenRequestDto tokenRequest) {
        final JwtResponseDto jwtResponse = jwtAuthenticatorFacade.authenticateAndGenerateToken(tokenRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}
