package com.matchmeeat.role.service;

import com.matchmeeat.role.RoleEnum;
import com.matchmeeat.role.entity.Role;
import com.matchmeeat.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findRole(RoleEnum role) {
        return roleRepository.findByName(role);
    }

    @Override
    public List<Role> findRoles(List<RoleEnum> roles) {
        return roleRepository.findAllByNameIn(roles);
    }

    @Override
    public List<Role> findUserRoles(String username) {
        return roleRepository.findUserRoles(username);
    }
}
