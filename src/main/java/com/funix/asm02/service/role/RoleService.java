package com.funix.asm02.service.role;

import com.funix.asm02.entity.Role;

import java.util.List;

public interface RoleService {
    Role saveRole(Role role);
    Role findById(int theId);
    List<Role> findAll();
    void deleteById(int theId);
}
