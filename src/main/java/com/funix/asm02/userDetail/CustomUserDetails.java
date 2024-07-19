package com.funix.asm02.userDetail;

import com.funix.asm02.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class CustomUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;
    private User user;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails() {
    }
    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public String getUsername() {
        return user.getEmail();
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
        return user.getEnable();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFullName(){
        return user.getFullName();
    }
    public Integer getUserId(){
        return user.getId();
    }

}
