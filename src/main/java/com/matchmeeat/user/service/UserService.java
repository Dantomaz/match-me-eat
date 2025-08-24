package com.matchmeeat.user.service;

import com.matchmeeat.user.entity.User;

public interface UserService {

    User findUserByUsername(String username);

    User save(User user);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void verifyUserEmail(String verificationToken);
}
