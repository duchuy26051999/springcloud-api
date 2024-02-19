package com.example.authservice.dto;

import com.example.authservice.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MyUserPrincipal implements UserDetails {
    private Long id;

    private String username;

    private User user;

    private List<GrantedAuthority> grantedAuthoritys;

    public MyUserPrincipal(User user, List<GrantedAuthority> list) {
        super();
        this.id = user.getId();
        this.username = user.getUsername();
        this.user = user;
        this.grantedAuthoritys = list;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnable();
    }

    public boolean isDelete() {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    public List<GrantedAuthority> getGrantedAuthoritys() {
        return this.grantedAuthoritys;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }
}
