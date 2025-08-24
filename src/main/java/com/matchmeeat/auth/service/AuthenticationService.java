package com.matchmeeat.auth.service;

import com.matchmeeat.auth.dto.LoginRequestDto;
import com.matchmeeat.auth.dto.RegistrationRequestDto;
import com.matchmeeat.auth.token.dto.TokensDto;
import com.matchmeeat.auth.token.entity.RefreshToken;
import com.matchmeeat.auth.token.service.RefreshTokenService;
import com.matchmeeat.exception.customexceptions.InvalidRefreshTokenException;
import com.matchmeeat.exception.customexceptions.RefreshTokenExpiredException;
import com.matchmeeat.exception.customexceptions.RefreshTokenRevokedException;
import com.matchmeeat.exception.customexceptions.ValidationException;
import com.matchmeeat.role.RoleEnum;
import com.matchmeeat.role.entity.Role;
import com.matchmeeat.role.service.RoleService;
import com.matchmeeat.user.entity.User;
import com.matchmeeat.user.service.UserService;
import com.matchmeeat.utils.SecureTokenGenerator;
import com.matchmeeat.utils.UserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final RefreshTokenService refreshTokenService;
    @Value("${custom.refresh-token.ttl}")
    private Duration ttl;

    /**
     * Authenticate (login) a user and return access token and refresh token
     *
     * @param loginRequest username and password
     * @return {@link TokensDto} - access token (jwt) and refresh token (opaque - UUID)
     */
    public TokensDto authenticateUser(LoginRequestDto loginRequest) {
        UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken.unauthenticated(
            loginRequest.username(),
            loginRequest.password()
        );
        Authentication authentication = authenticationManager.authenticate(authToken);
        String accessToken = jwtService.generateToken(authentication.getName());
        String refreshToken = generateRefreshToken(authentication.getName());
        return new TokensDto(accessToken, refreshToken);
    }

    /**
     * Generate new refresh token and save it to the database
     *
     * @param username username for which the token will be generated for
     * @return refresh token value (UUID)
     */
    private String generateRefreshToken(String username) {
        User user = userService.findUserByUsername(username);
        RefreshToken refreshToken = RefreshToken.create(user, ttl);
        refreshTokenService.save(refreshToken);
        return refreshToken.getToken();
    }

    public void verifyUserEmail(String verificationToken) {
        userService.verifyUserEmail(verificationToken);
    }

    /**
     * Register a new user and return verification token
     *
     * @param user user credentials and e-mail to register
     * @return optional e-mail verification token
     */
    @Transactional
    public Optional<String> registerUser(RegistrationRequestDto user) {
        validateUser(user);
        return UserUtils.userProvidedEmail(user) ? registerUserWithEmail(user) : registerUserWithoutEmail(user);
    }

    private Optional<String> registerUserWithEmail(RegistrationRequestDto user) {
        Role role = roleService.findRole(RoleEnum.ROLE_USER);
        User newUser = new User(user.username().strip(), user.email().strip(), passwordEncoder.encode(user.password()), role);
        String verificationToken = SecureTokenGenerator.generateToken();
        newUser.setVerificationToken(verificationToken);
        userService.save(newUser);
        return Optional.of(verificationToken);
    }

    /**
     * Register user without email, so no e-mail verification token is needed
     *
     * @param user user to register
     * @return {@link Optional#empty()} because there's no verification token required
     */
    private Optional<String> registerUserWithoutEmail(RegistrationRequestDto user) {
        Role role = roleService.findRole(RoleEnum.ROLE_USER);
        User newUser = new User(user.username().strip(), passwordEncoder.encode(user.password()), role);
        userService.save(newUser);
        return Optional.empty();
    }

    private void validateUser(RegistrationRequestDto user) {
        HashMap<String, String> errors = new HashMap<>();

        if (UserUtils.userProvidedEmail(user) && userService.existsByEmail(user.email())) {
            errors.put("email", "Email [%s] is already taken".formatted(user.email()));
        }

        if (userService.existsByUsername(user.username())) {
            errors.put("username", "Username [%s] is already taken".formatted(user.username()));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(HttpStatus.CONFLICT, errors);
        }
    }

    public TokensDto refreshAccessToken(String refreshToken) {
        RefreshToken refreshTokenFromDB = refreshTokenService.findByToken(refreshToken)
            .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token is invalid"));
        validateRefreshToken(refreshTokenFromDB);
        String username = refreshTokenFromDB.getUser().getUsername();
        String accessToken = jwtService.generateToken(username);
        String newRefreshToken = generateRefreshToken(username);
        refreshTokenFromDB.revoke();
        return new TokensDto(accessToken, newRefreshToken);
    }

    private void validateRefreshToken(RefreshToken refreshToken) {
        if (refreshToken.isExpired()) {
            throw new RefreshTokenExpiredException("Refresh token has expired");
        }

        if (refreshToken.isRevoked()) {
            throw new RefreshTokenRevokedException("Refresh token has been revoked");
        }
    }
}
