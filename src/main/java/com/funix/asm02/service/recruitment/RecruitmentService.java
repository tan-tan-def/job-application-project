package com.funix.asm02.service.recruitment;

import com.funix.asm02.entity.Category;
import com.funix.asm02.entity.Company;
import com.funix.asm02.entity.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecruitmentService {
    List<Recruitment> findAllAndSort();
    List<Recruitment> findAllByCategory(Category category);
    List<Recruitment> findAll();
    Recruitment saveRecruitment(Recruitment recruitment);
    Recruitment findById(int id);
    void deleteRecruitment(int id);
    Page<Recruitment> searchTitle(String keySearch, Integer pageNo);
    Page<Recruitment> searchAddress(String keySearch, Integer pageNo);
    Page<Recruitment> searchCompany(String keySearch, Integer pageNo);
    Page<Recruitment> findAllByCompany(Company company, Integer pageNo);
    Page<Recruitment> searchCategory(Category category, Integer pageNo);
    Page<Recruitment> findAllRecruitmentPageableAndSort(Integer pageNo);
}
