package com.matchmeeat.user.repository;

import com.matchmeeat.user.entity.User;

public interface UserRepositoryCustom {

    void updateUserVerified(String verificationToken);

    User findUserByEmail(String email);

    void updateVerificationToken(String email, String newVerificationToken);
}
