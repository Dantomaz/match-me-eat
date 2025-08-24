package com.matchmeeat.auth.controller;

import com.matchmeeat.auth.dto.LoginRequestDto;
import com.matchmeeat.auth.dto.LoginResponseDto;
import com.matchmeeat.auth.dto.RegistrationRequestDto;
import com.matchmeeat.auth.dto.RegistrationResponseDto;
import com.matchmeeat.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerUser(@Valid @RequestBody RegistrationRequestDto registrationRequest) {
        return ResponseEntity.ok(authenticationService.registerUser(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticateUser(loginRequest));
    }
}
