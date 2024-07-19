package com.funix.asm02.userDetail;

import com.funix.asm02.entity.User;
import com.funix.asm02.entity.UserRole;
import com.funix.asm02.service.user.UserService;
import com.funix.asm02.service.userRole.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailService implements UserDetailsService  {
    private UserService userService;
    private UserRoleService userRoleService;
    @Autowired
    public CustomUserDetailService(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if(user==null){
            throw new UsernameNotFoundException("User not found");
        }
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Set<UserRole> userRoles = user.getUserRoles();
        for(UserRole userRole:userRoles){
            grantedAuthorities.add((new SimpleGrantedAuthority(userRole.getRole().getName())));
        }
        return new CustomUserDetails(user, grantedAuthorities);
    }
}
