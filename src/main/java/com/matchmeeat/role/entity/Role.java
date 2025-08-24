package com.matchmeeat.role.entity;

import com.matchmeeat.base.entity.BaseEntity;
import com.matchmeeat.privilege.entity.Privilege;
import com.matchmeeat.role.RoleEnum;
import com.matchmeeat.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@ToString(callSuper = true, of = "name")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Role.TABLE_NAME)
public class Role extends BaseEntity implements GrantedAuthority {

    public static final String TABLE_NAME = "roles";

    @Column(nullable = false, unique = true)
    @Enumerated(value = EnumType.STRING)
    private RoleEnum name;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    private List<Privilege> privileges;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @Override
    public String getAuthority() {
        return name.name();
    }
}
