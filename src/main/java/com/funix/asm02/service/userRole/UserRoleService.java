package com.funix.asm02.service.userRole;

import com.funix.asm02.entity.User;
import com.funix.asm02.entity.UserRole;

import java.util.List;
import java.util.Set;


public interface UserRoleService {
    List<UserRole> findAll();
    UserRole save(UserRole userRole);
    UserRole findById(int theId);
    void deleteById(int theId);
    Set<UserRole> findUserRolesByUser(User user);
}
