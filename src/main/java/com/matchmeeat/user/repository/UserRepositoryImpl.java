package com.matchmeeat.user.repository;

import com.matchmeeat.user.entity.QUser;
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
}
