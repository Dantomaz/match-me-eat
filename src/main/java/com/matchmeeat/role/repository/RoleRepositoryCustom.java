package com.matchmeeat.role.repository;

import com.matchmeeat.role.entity.Role;

import java.util.List;

public interface RoleRepositoryCustom {

    List<Role> findUserRoles(String username);
}
