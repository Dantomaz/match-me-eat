package com.matchmeeat.auth.service;

import com.matchmeeat.auth.dto.LoginRequestDto;
import com.matchmeeat.auth.dto.LoginResponseDto;
import com.matchmeeat.auth.dto.RegistrationRequestDto;
import com.matchmeeat.auth.dto.RegistrationResponseDto;
import com.matchmeeat.auth.mapper.AuthMapper;
import com.matchmeeat.exception.ValidationException;
import com.matchmeeat.user.entity.User;
import com.matchmeeat.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;

    public LoginResponseDto authenticateUser(LoginRequestDto loginRequest) {
        UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken.unauthenticated(
            loginRequest.username(),
            loginRequest.password()
        );
        Authentication authentication = authenticationManager.authenticate(authToken);
        String token = jwtService.generateToken(loginRequest.username());
        return new LoginResponseDto(token);
    }

    @Transactional
    public RegistrationResponseDto registerUser(RegistrationRequestDto user) {
        validateUser(user);
        User newUser = new User(user.username(), user.email(), passwordEncoder.encode(user.password()));
        User savedUser = userRepository.save(newUser);
        return authMapper.userToRegistrationResponseDto(savedUser);
    }

    private void validateUser(RegistrationRequestDto user) {
        HashMap<String, String> errors = new HashMap<>();

        if (userRepository.existsByEmail(user.email())) {
            errors.put("email", "Email [%s] is already taken".formatted(user.email()));
        }

        if (userRepository.existsByUsername(user.username())) {
            errors.put("username", "Username [%s] is already taken".formatted(user.username()));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(HttpStatus.CONFLICT, errors);
        }
    }
}
