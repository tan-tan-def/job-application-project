package com.funix.asm02.dao;

import com.funix.asm02.entity.Recruitment;
import com.funix.asm02.entity.SaveJob;
import com.funix.asm02.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaveJobRepository extends JpaRepository<SaveJob, Integer> {
    List<SaveJob> findByUser(User user);
    List<SaveJob> findByRecruitment(Recruitment recruitment);
    @Query("SELECT sj FROM SaveJob sj WHERE sj.recruitment = :recruitment AND sj.user = :user")
    SaveJob findByUserRecruitment(@Param("user") User user, @Param("recruitment") Recruitment recruitment);

}
