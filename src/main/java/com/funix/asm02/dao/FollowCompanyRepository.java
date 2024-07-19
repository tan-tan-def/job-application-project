package com.funix.asm02.dao;

import com.funix.asm02.entity.Company;
import com.funix.asm02.entity.FollowCompany;
import com.funix.asm02.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowCompanyRepository extends JpaRepository<FollowCompany,Integer> {
    List<FollowCompany> findByUser(User user);
    List<FollowCompany> findByCompany(Company company);
    @Query("SELECT fc FROM FollowCompany fc WHERE fc.user = :user AND fc.company = :company")
    FollowCompany findByUserAndCompany(@Param("user") User user, @Param("company") Company company);
}
