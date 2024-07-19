package com.funix.asm02.dao;

import com.funix.asm02.entity.Company;
import com.funix.asm02.entity.Recruitment;
import com.funix.asm02.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Company findByUser(User user);
    Company findByEmail(String email);
    @Query("SELECT c FROM Company c WHERE c.nameCompany IS NOT NULL ORDER BY c.totalApplyPeople DESC")
    List<Company> findAllByNameCompanyNotNullAndSortByTotalApplyPeopleDesc();
}
