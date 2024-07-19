package com.funix.asm02.service.saveJob;

import com.funix.asm02.common.Field;
import com.funix.asm02.dao.SaveJobRepository;
import com.funix.asm02.entity.Recruitment;
import com.funix.asm02.entity.SaveJob;
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
public class SaveJobServiceImpl implements SaveJobService{
    private SaveJobRepository saveJobRepository;
    @Autowired
    public SaveJobServiceImpl(SaveJobRepository saveJobRepository) {
        this.saveJobRepository = saveJobRepository;
    }
    public Page<SaveJob> typeOfSearch(List<SaveJob> list, Integer pageNo){
        List<SaveJob> list1 = list;
        Pageable pageable = PageRequest.of(pageNo-1, Field.PAGE_SIZE_5);
        Integer start = (int) pageable.getOffset();
        Integer end = (int)((pageable.getOffset()+pageable.getPageSize()) > list.size() ? list.size() :  (pageable.getOffset() + pageable.getPageSize()));
        list = list.subList(start,end);
        return new PageImpl<SaveJob>(list, pageable, list1.size());
    }

    @Override
    public List<SaveJob> findAll() {
        return saveJobRepository.findAll();
    }

    @Override
    public SaveJob findById(int theId) {
        Optional<SaveJob> result = saveJobRepository.findById(theId);
        SaveJob saveJob = null;
        if(result.isPresent()){
            saveJob = result.get();
        }else{
            throw new RuntimeException("Did not find the Job id "+theId);
        }
        return saveJob;
    }

    @Override
    public SaveJob save(SaveJob saveJob) {
        return saveJobRepository.save(saveJob);
    }

    @Override
    public void deleteById(int theId) {
        saveJobRepository.deleteById(theId);
    }

    @Override
    public Page<SaveJob> findAllByUser(User user, Integer pageNo) {
        List<SaveJob> saveJobs = saveJobRepository.findByUser(user);
        return typeOfSearch(saveJobs,pageNo);
    }

    @Override
    public SaveJob findByUserRecruitment(User user, Recruitment recruitment) {
        return saveJobRepository.findByUserRecruitment(user,recruitment);
    }

    @Override
    public List<SaveJob> findByRecruitment(Recruitment recruitment) {
        return saveJobRepository.findByRecruitment(recruitment);
    }

    @Override
    public void deleteAll(List<SaveJob> saveJobs) {
        saveJobRepository.deleteAll(saveJobs);
    }
}
