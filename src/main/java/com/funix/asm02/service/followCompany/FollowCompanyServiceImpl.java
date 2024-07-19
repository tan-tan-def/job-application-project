package com.funix.asm02.service.followCompany;

import com.funix.asm02.common.Field;
import com.funix.asm02.dao.FollowCompanyRepository;
import com.funix.asm02.entity.Company;
import com.funix.asm02.entity.FollowCompany;
import com.funix.asm02.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowCompanyServiceImpl implements FollowCompanyService{
    private FollowCompanyRepository followCompanyRepository;
    public Page<FollowCompany> typeOfSearch(List<FollowCompany> list, Integer pageNo){
        List<FollowCompany> list1 = list;
        Pageable pageable = PageRequest.of(pageNo-1, Field.PAGE_SIZE_5);
        Integer start = (int) pageable.getOffset();
        Integer end = (int)((pageable.getOffset()+pageable.getPageSize()) > list.size() ? list.size() :  (pageable.getOffset() + pageable.getPageSize()));
        list = list.subList(start,end);
        return new PageImpl<FollowCompany>(list, pageable, list1.size());
    }
    @Autowired
    public FollowCompanyServiceImpl(FollowCompanyRepository followCompanyRepository) {
        this.followCompanyRepository = followCompanyRepository;
    }
    @Override
    public Page<FollowCompany> findAllByUser(User user, Integer pageNo) {
        List<FollowCompany> followCompanies = followCompanyRepository.findByUser(user);
        return typeOfSearch(followCompanies, pageNo);
    }

    @Override
    public void delete(int id) {
        followCompanyRepository.deleteById(id);
    }

    @Override
    public void deleteById(int theId) {
        followCompanyRepository.deleteById(theId);
    }

    @Override
    public FollowCompany save(FollowCompany followCompany) {
        return followCompanyRepository.save(followCompany);
    }

    @Override
    public List<FollowCompany> findAllByCompany(Company company) {
        return followCompanyRepository.findByCompany(company);
    }

    @Override
    public FollowCompany findByUserAndCompany(User user, Company company) {
        return followCompanyRepository.findByUserAndCompany(user,company);
    }
}
