package com.funix.asm02.service.role;

import com.funix.asm02.dao.RoleRepository;
import com.funix.asm02.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RoleServiceImpl implements RoleService{
    private RoleRepository roleRepository;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findById(int theId) {
        Optional<Role> result = roleRepository.findById(theId);
        Role role = null;
        if(result.isPresent()){
            role = result.get();
        }else{
            throw new RuntimeException("Did not find the Role id:"+ theId);
        }

        return role;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void deleteById(int theId) {
        roleRepository.deleteById(theId);
    }
}
