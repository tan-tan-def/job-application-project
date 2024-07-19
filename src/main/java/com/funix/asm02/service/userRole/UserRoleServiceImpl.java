package com.funix.asm02.service.userRole;

import com.funix.asm02.dao.UserRoleRepository;
import com.funix.asm02.entity.User;
import com.funix.asm02.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserRoleServiceImpl implements UserRoleService{
    private UserRoleRepository userRoleRepository;
    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public List<UserRole> findAll() {
        return userRoleRepository.findAll();
    }

    @Override
    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole findById(int theId) {
        Optional<UserRole> result = userRoleRepository.findById(theId);
        UserRole userRole = null;
        if(result.isPresent()){
            userRole=result.get();
        }else{
            throw new RuntimeException("Did not find the UserRole: "+theId);
        }
        return userRole;
    }

    @Override
    public void deleteById(int theId) {
        userRoleRepository.deleteById(theId);
    }

    @Override
    public Set<UserRole> findUserRolesByUser(User user) {
        return userRoleRepository.findUserRolesByUser(user);
    }
}
