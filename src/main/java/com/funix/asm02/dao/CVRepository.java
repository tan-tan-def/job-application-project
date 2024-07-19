package com.funix.asm02.dao;

import com.funix.asm02.entity.CV;
import com.funix.asm02.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CVRepository extends JpaRepository<CV, Integer> {
    CV findByStatus(int statusId);

    List<CV> findByUser(User user);
}
