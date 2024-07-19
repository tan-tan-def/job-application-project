package com.funix.asm02.dao;

import com.funix.asm02.entity.Category;
import com.funix.asm02.entity.Company;
import com.funix.asm02.entity.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Integer> {
    @Query("SELECT r FROM Recruitment r WHERE r.addressToSearch LIKE %?1% ORDER BY r.totalApplyPeople DESC, r.salary DESC, r.quantity DESC")
    List<Recruitment> searchAddress(String keySearch);
    @Query("SELECT r FROM Recruitment r WHERE r.titleToSearch LIKE %?1% ORDER BY r.totalApplyPeople DESC, r.salary DESC, r.quantity DESC")
    List<Recruitment> searchTitle(String keySearch);
    @Query("SELECT r FROM Recruitment r WHERE r.company.nameCompanyToSearch LIKE %?1% ORDER BY r.totalApplyPeople DESC, r.salary DESC, r.quantity DESC")
    List<Recruitment> searchCompany(String keySearch);
    List<Recruitment> findByCompany(Company company);
    @Query("SELECT r FROM Recruitment r WHERE r.category = ?1 ORDER BY r.totalApplyPeople DESC, r.salary DESC, r.quantity DESC")
    List<Recruitment> findByCategory(Category category);
}
