package com.matchmeeat.privilege.entity;

import com.matchmeeat.base.entity.BaseEntity;
import com.matchmeeat.privilege.PrivilegeEnum;
import com.matchmeeat.role.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Privilege.TABLE_NAME)
public class Privilege extends BaseEntity implements GrantedAuthority {

    public static final String TABLE_NAME = "privileges";

    @Column(nullable = false, unique = true)
    @Enumerated(value = EnumType.STRING)
    private PrivilegeEnum name;

    @ManyToMany(mappedBy = "privileges")
    private List<Role> roles;

    @Override
    public String getAuthority() {
        return name.name();
    }
}
