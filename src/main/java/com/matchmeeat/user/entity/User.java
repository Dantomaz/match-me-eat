package com.matchmeeat.user.entity;

import com.matchmeeat.base.entity.Audit;
import com.matchmeeat.role.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = User.TABLE_NAME)
@Entity
public class User extends Audit implements UserDetails {

    public static final String TABLE_NAME = "users";

    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "verification_token", unique = true)
    private String verificationToken;

    @Column(nullable = false)
    private boolean verified = false;

    @Getter(value = AccessLevel.NONE)
    @Column(name = "account_enabled", nullable = false)
    private boolean accountEnabled = true;

    @Getter(value = AccessLevel.NONE)
    @Column(name = "account_expired", nullable = false)
    private boolean accountExpired = false;

    @Getter(value = AccessLevel.NONE)
    @Column(name = "account_locked", nullable = false)
    private boolean accountLocked = false;

    @Getter(value = AccessLevel.NONE)
    @Column(name = "credentials_expired", nullable = false)
    private boolean credentialsExpired = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    /**
     * Used for creating new user without e-mail in the system
     *
     * @param username username
     * @param password encoded password
     * @param role     role
     */
    public User(String username, String password, Role role) {
        this(username, null, password, List.of(role));
    }

    /**
     * Used for creating new user with e-mail in the system
     *
     * @param username username
     * @param email    email
     * @param password encoded password
     * @param role     role
     */
    public User(String username, String email, String password, Role role) {
        this(username, email, password, List.of(role));
    }

    /**
     * Used for creating new user in the system
     *
     * @param username username
     * @param email    email
     * @param password encoded password
     * @param roles    roles
     */
    public User(String username, String email, String password, List<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isEnabled() {
        return accountEnabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }
}
