package com.matchmeeat.auth.controller;

import com.matchmeeat.auth.dto.LoginRequestDto;
import com.matchmeeat.auth.dto.RegistrationRequestDto;
import com.matchmeeat.auth.dto.RegistrationResponseDto;
import com.matchmeeat.auth.service.AuthenticationService;
import com.matchmeeat.auth.token.dto.TokensDto;
import com.matchmeeat.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerUser(
        @Valid @RequestBody RegistrationRequestDto registrationRequest,
        HttpServletRequest request
    ) {
        return authenticationService.registerUser(registrationRequest)
            .map(verificationToken -> ServletUtils.getVerificationUrl(request, verificationToken))
            // TODO send verification e-mail (generate token with SecureTokenGenerator.generateToken512())
            //  and make the button redirect to this verificationUrl instead of just returning it
            .map(verificationUrl -> ResponseEntity.ok(new RegistrationResponseDto(verificationUrl)))
            .orElseGet(() -> ResponseEntity.ok().build());
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyUserEmail(@RequestParam(name = "token") String verificationToken) {
        authenticationService.verifyUserEmail(verificationToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokensDto> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticateUser(loginRequest));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokensDto> refreshAccessToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok(authenticationService.refreshAccessToken(refreshToken));
    }
}
