package com.matchmeeat.role.service;

import com.matchmeeat.role.RoleEnum;
import com.matchmeeat.role.entity.Role;

import java.util.List;

public interface RoleService {

    Role findRole(RoleEnum name);

    List<Role> findRoles(List<RoleEnum> names);

    List<Role> findUserRoles(String username);
}
