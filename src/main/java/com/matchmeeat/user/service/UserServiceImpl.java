package com.matchmeeat.user.service;

import com.matchmeeat.exception.customexceptions.ValidationException;
import com.matchmeeat.user.dto.UserDto;
import com.matchmeeat.user.entity.User;
import com.matchmeeat.user.repository.UserRepository;
import com.matchmeeat.utils.UserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User with username [%s] not found".formatted(username)));
        validateUser(user);
        return user;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void verifyUserEmail(String verificationToken) {
        boolean canBeVerified = userRepository.existsByVerificationTokenAndVerifiedFalse(verificationToken);
        if (!canBeVerified) {
            throw new ValidationException(HttpStatus.GONE, HttpStatus.GONE.getReasonPhrase(), "Invalid or expired verification token");
        }
        userRepository.updateUserVerified(verificationToken);
    }

    private void validateUser(User user) {
        if (UserUtils.userProvidedEmail(user) && !user.isVerified()) {
            throw new ValidationException(
                HttpStatus.FORBIDDEN,
                "Account not verified",
                "Your account has not been verified yet. Please check your e-mail."
            );
        }

        if (!user.isEnabled()) {
            throw new ValidationException(HttpStatus.FORBIDDEN, "Account disabled", "Your account has been disabled. Contact support.");
        }

        if (!user.isAccountNonExpired()) {
            throw new ValidationException(HttpStatus.FORBIDDEN, "Account expired", "Your account has expired. Please renew or contact support.");
        }

        if (!user.isAccountNonLocked()) {
            throw new ValidationException(
                HttpStatus.FORBIDDEN,
                "Account locked",
                "Your account is locked due to too many failed login attempts. Try again later or reset your password."
            );
        }

        if (!user.isCredentialsNonExpired()) {
            throw new ValidationException(HttpStatus.FORBIDDEN, "Password expired", "Your password has expired. Please reset your password.");
        }
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user.isVerified()) {
            throw new ValidationException(
                HttpStatus.FORBIDDEN,
                "Account already verified",
                "Your account has been already verified. You can log in now."
            );
        }

        return new UserDto(user.getUsername(), user.getEmail());
    }

    @Override
    public void updateVerificationToken(String email, String newVerificationToken) {
        userRepository.updateVerificationToken(email, newVerificationToken);
    }
}
