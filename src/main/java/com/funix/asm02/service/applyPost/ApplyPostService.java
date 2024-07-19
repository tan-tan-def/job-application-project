package com.funix.asm02.service.applyPost;

import com.funix.asm02.entity.ApplyPost;
import com.funix.asm02.entity.Company;
import com.funix.asm02.entity.Recruitment;
import com.funix.asm02.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ApplyPostService {
    Page<ApplyPost> findAllByCompany(Integer PageNo, Company company);
    List<ApplyPost> findAllByCompany(Company company);
    ApplyPost saveApplyPost(ApplyPost applyPost);
    ApplyPost findById(Integer theId);
    void deleteById(Integer theId);
    Page<ApplyPost> findAllByRecruitment(Recruitment recruitment, Integer pageNo);
    Page<ApplyPost> findAllByUser(User user, Integer pageNo);
    List<ApplyPost> findAllByUser(User user);
    ApplyPost findByRecruitmentIdUserId(int idRecruitment, int idUser);
    List<ApplyPost> findAllByRecruitment(Recruitment recruitment);
    void deleteAll(List<ApplyPost>applyPosts);
    List<ApplyPost> findAll();
}
