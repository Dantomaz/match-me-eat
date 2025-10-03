package com.matchmeeat.auth.controller;

import com.matchmeeat.auth.dto.LoginRequestDto;
import com.matchmeeat.auth.dto.RegistrationRequestDto;
import com.matchmeeat.auth.dto.ResendEmailRequestDto;
import com.matchmeeat.auth.service.AuthenticationService;
import com.matchmeeat.auth.token.dto.TokensDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Value("${ENV_CLIENT_URL}")
    private String clientUrl;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegistrationRequestDto registrationRequest) {
        authenticationService.registerUser(registrationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyUserEmail(@RequestParam(name = "token") String verificationToken) {
        try {
            authenticationService.verifyUserEmail(verificationToken);
        } catch (Exception exception) {
            URI failureUri = URI.create(clientUrl + "/verify-email/failure");
            return redirectAfterEmailVerification(failureUri);
        }

        URI successUri = URI.create(clientUrl + "/verify-email/success");
        return redirectAfterEmailVerification(successUri);
    }

    private ResponseEntity<Void> redirectAfterEmailVerification(URI uri) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @PostMapping("/resend-email")
    public ResponseEntity<Void> resendVerificationEmail(@RequestBody ResendEmailRequestDto resendEmailRequest) {
        authenticationService.resendVerificationEmail(resendEmailRequest.email());
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
