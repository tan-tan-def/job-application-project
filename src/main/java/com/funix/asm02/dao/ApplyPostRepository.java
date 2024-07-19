package com.funix.asm02.dao;

import com.funix.asm02.entity.ApplyPost;
import com.funix.asm02.entity.Company;
import com.funix.asm02.entity.Recruitment;
import com.funix.asm02.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyPostRepository extends JpaRepository<ApplyPost, Integer> {
    @Query("SELECT ap FROM  ApplyPost ap WHERE ap.recruitment.company = :company")
    List<ApplyPost> findAllByCompany(@Param("company") Company company);
    List<ApplyPost> findByRecruitment(Recruitment recruitment);
    List<ApplyPost> findByUser(User user);
    @Query("SELECT ap FROM ApplyPost ap WHERE ap.recruitment.id = :idRecruitment AND ap.user.id = :idUser")
    ApplyPost findByUserIdAndRecruitmentId(@Param("idRecruitment") int idRecruitment, @Param("idUser") int idUser);
}
