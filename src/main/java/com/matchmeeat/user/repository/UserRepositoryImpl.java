package com.matchmeeat.user.repository;

import com.matchmeeat.user.entity.QUser;
import com.matchmeeat.user.entity.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private static final QUser USER = QUser.user;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void updateUserVerified(String verificationToken) {
        jpaQueryFactory
            .update(USER)
            .set(USER.verified, true)
            .where(USER.verificationToken.eq(verificationToken))
            .execute();
    }

    @Override
    public User findUserByEmail(String email) {
        return jpaQueryFactory
            .select(Projections.constructor(User.class, USER.username, USER.email, USER.verified))
            .from(USER)
            .where(USER.email.eq(email))
            .fetchOne();
    }

    @Override
    public void updateVerificationToken(String email, String newVerificationToken) {
        jpaQueryFactory
            .update(USER)
            .set(USER.verificationToken, newVerificationToken)
            .where(USER.email.eq(email))
            .execute();
    }
}
