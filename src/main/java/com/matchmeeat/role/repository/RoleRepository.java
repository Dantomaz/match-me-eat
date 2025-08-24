package com.matchmeeat.role.repository;

import com.matchmeeat.role.RoleEnum;
import com.matchmeeat.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID>, RoleRepositoryCustom {

    Role findByName(RoleEnum name);

    List<Role> findAllByNameIn(List<RoleEnum> names);
}
