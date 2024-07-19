package com.funix.asm02.service.company;

import com.funix.asm02.entity.Company;
import com.funix.asm02.entity.Recruitment;
import com.funix.asm02.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CompanyService {
    List<Company> findAllAndSort();
    List<Company> findAll();
    Company saveCompany(Company company);
    Company findById(int theId);
    void deleteById(int theId);
    Company findByUser(User user);
    Company findByEmail(String email);
}
