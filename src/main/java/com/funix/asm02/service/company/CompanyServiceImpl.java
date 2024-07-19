package com.funix.asm02.service.company;

import com.funix.asm02.dao.CompanyRepository;
import com.funix.asm02.entity.Company;
import com.funix.asm02.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService{
    private CompanyRepository companyRepository;
    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> findAllAndSort() {
        return companyRepository.findAllByNameCompanyNotNullAndSortByTotalApplyPeopleDesc();
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company findById(int theId) {
        Optional<Company> result = companyRepository.findById(theId);
        Company company = null;
        if(result.isPresent()){
            company = result.get();
        }else{
            throw new RuntimeException("Did not find the Company id" + theId);
        }
        return company;
    }

    @Override
    public void deleteById(int theId) {
        companyRepository.deleteById(theId);
    }

    @Override
    public Company findByUser(User user) {
        return companyRepository.findByUser(user);
    }

    @Override
    public Company findByEmail(String email) {
        return companyRepository.findByEmail(email);
    }
}
