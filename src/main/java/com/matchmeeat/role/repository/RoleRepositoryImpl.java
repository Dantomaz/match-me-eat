package com.matchmeeat.role.repository;

import com.matchmeeat.role.entity.QRole;
import com.matchmeeat.role.entity.Role;
import com.matchmeeat.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RoleRepositoryImpl implements RoleRepositoryCustom {

    private static final QUser USER = QUser.user;
    private static final QRole ROLE = QRole.role;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Role> findUserRoles(String username) {
        return jpaQueryFactory
            .select(ROLE)
            .from(ROLE)
            .join(USER).on(USER.roles.contains(ROLE))
            .where(USER.username.eq(username))
            .fetch();
    }
}
