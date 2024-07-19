package com.funix.asm02.service.applyPost;

import com.funix.asm02.common.Field;
import com.funix.asm02.dao.ApplyPostRepository;
import com.funix.asm02.entity.ApplyPost;
import com.funix.asm02.entity.Company;
import com.funix.asm02.entity.Recruitment;
import com.funix.asm02.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ApplyPostServiceImpl implements ApplyPostService{
    private ApplyPostRepository applyPostRepository;
    @Autowired
    public ApplyPostServiceImpl(ApplyPostRepository applyPostRepository) {
        this.applyPostRepository = applyPostRepository;
    }

    public Page<ApplyPost> paginateApplyPosts(List<ApplyPost> list, Integer pageNo){
        List<ApplyPost> list1 = list;
        Pageable pageable = PageRequest.of(pageNo-1, Field.PAGE_SIZE_5);
        Integer start = (int) pageable.getOffset();
        Integer end = (int)((pageable.getOffset()+pageable.getPageSize()) > list.size() ? list.size() :  (pageable.getOffset() + pageable.getPageSize()));
        list = list.subList(start,end);
        return new PageImpl<ApplyPost>(list, pageable, list1.size());
    }

    @Override
    public Page<ApplyPost> findAllByCompany(Integer pageNo, Company company) {
        List<ApplyPost> applyPosts = applyPostRepository.findAllByCompany(company);
        return paginateApplyPosts(applyPosts, pageNo);
    }

    @Override
    public List<ApplyPost> findAllByCompany(Company company) {
        return applyPostRepository.findAllByCompany(company);
    }

    @Override
    public ApplyPost saveApplyPost(ApplyPost applyPost) {
        return applyPostRepository.save(applyPost);
    }

    @Override
    public ApplyPost findById(Integer theId) {
        Optional<ApplyPost> result = applyPostRepository.findById(theId);
        ApplyPost applyPost = null;
        if(result.isPresent()){
            applyPost = result.get();
        }
        return applyPost;
    }

    @Override
    public void deleteById(Integer theId) {
        applyPostRepository.deleteById(theId);
    }

    @Override
    public Page<ApplyPost> findAllByRecruitment(Recruitment recruitment, Integer pageNo) {
        List<ApplyPost> applyPosts = applyPostRepository.findByRecruitment(recruitment);
        return paginateApplyPosts(applyPosts, pageNo);
    }

    @Override
    public Page<ApplyPost> findAllByUser(User user, Integer pageNo) {
        List<ApplyPost> applyPosts = applyPostRepository.findByUser(user);
        return paginateApplyPosts(applyPosts,pageNo);
    }

    @Override
    public List<ApplyPost> findAllByUser(User user) {
        return applyPostRepository.findByUser(user);
    }

    @Override
    public ApplyPost findByRecruitmentIdUserId(int idRecruitment, int idUser) {
        return applyPostRepository.findByUserIdAndRecruitmentId(idRecruitment, idUser);
    }

    @Override
    public List<ApplyPost> findAllByRecruitment(Recruitment recruitment) {
        return applyPostRepository.findByRecruitment(recruitment);
    }

    @Override
    public void deleteAll(List<ApplyPost>applyPosts) {
        applyPostRepository.deleteAll(applyPosts);
    }

    @Override
    public List<ApplyPost> findAll() {
        return applyPostRepository.findAll();
    }

}
