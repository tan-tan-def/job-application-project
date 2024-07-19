package com.funix.asm02.dao;

import com.funix.asm02.entity.User;
import com.funix.asm02.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    @Query("SELECT ur FROM UserRole ur WHERE ur.user = :user")
    Set<UserRole> findUserRolesByUser(@Param("user") User user);
}
