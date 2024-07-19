package com.funix.asm02.service.saveJob;

import com.funix.asm02.entity.Recruitment;
import com.funix.asm02.entity.SaveJob;
import com.funix.asm02.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SaveJobService {
    List<SaveJob> findAll();
    SaveJob findById(int theId);
    SaveJob save(SaveJob saveJob);
    void deleteById(int theId);
    Page<SaveJob> findAllByUser(User user, Integer pageNo);
    SaveJob findByUserRecruitment(User user, Recruitment recruitment);
    List<SaveJob> findByRecruitment(Recruitment recruitment);
    void deleteAll(List<SaveJob> saveJobs);
}
