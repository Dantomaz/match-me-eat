package com.matchmeeat.user.service;

import com.matchmeeat.exception.ValidationException;
import com.matchmeeat.user.dto.UserDto;
import com.matchmeeat.user.entity.User;
import com.matchmeeat.user.mapper.UserMapper;
import com.matchmeeat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto getUserByUsername(final String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User account does not exist"));

        validateUser(user);
        return userMapper.userToUserDto(user);
    }

    private void validateUser(final User user) {
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
}
