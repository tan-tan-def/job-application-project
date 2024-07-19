package com.funix.asm02.service.followCompany;

import com.funix.asm02.entity.Company;
import com.funix.asm02.entity.FollowCompany;
import com.funix.asm02.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FollowCompanyService {
    Page<FollowCompany> findAllByUser(User user, Integer pageNo);
    void delete(int id);
    void deleteById(int theId);
    FollowCompany save(FollowCompany followCompany);
    List<FollowCompany> findAllByCompany(Company company);
    FollowCompany findByUserAndCompany(User user, Company company);
}
