package com.api.estudo.models;


import com.api.estudo.enums.UserType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "USERS")
@Getter
@Setter
public class RaceUser implements UserDetails {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "userid")
    private Integer userId;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "idoriginal")
    private Integer originalId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(type);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
