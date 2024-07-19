package com.funix.asm02.service.recruitment;

import com.funix.asm02.common.Field;
import com.funix.asm02.dao.RecruitmentRepository;
import com.funix.asm02.entity.ApplyPost;
import com.funix.asm02.entity.Category;
import com.funix.asm02.entity.Company;
import com.funix.asm02.entity.Recruitment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecruitmentServiceImpl implements RecruitmentService{
    private RecruitmentRepository recruitmentRepository;
    @Autowired
    public RecruitmentServiceImpl(RecruitmentRepository recruitmentRepository) {
        this.recruitmentRepository = recruitmentRepository;
    }
    @Override
    public List<Recruitment> findAllAndSort() {
        Sort sort = Sort.by(
                Sort.Order.desc("totalApplyPeople"),
                Sort.Order.desc("salary"),
                Sort.Order.desc("quantity")
        );
        return recruitmentRepository.findAll(sort);
    }

    @Override
    public List<Recruitment> findAllByCategory(Category category) {

        return recruitmentRepository.findByCategory(category);
    }

    @Override
    public List<Recruitment> findAll() {
        return recruitmentRepository.findAll();
    }

    @Override
    public Recruitment saveRecruitment(Recruitment recruitment) {
        return recruitmentRepository.save(recruitment);
    }

    @Override
    public Recruitment findById(int theId) {
        Optional<Recruitment> result = recruitmentRepository.findById(theId);
        Recruitment recruitment = null;
        if(result.isPresent()){
            recruitment = result.get();
        }else{
            throw new RuntimeException("Did not find the Recruitment id "+theId);
        }
        return recruitment;
    }

    @Override
    public void deleteRecruitment(int theId) {
        recruitmentRepository.deleteById(theId);
    }

    public Page<Recruitment> typeOfSearch( List<Recruitment> list, Integer pageNo){
        List<Recruitment> list1 = list;
        Pageable pageable = PageRequest.of(pageNo-1, Field.PAGE_SIZE_5);
        Integer start = (int) pageable.getOffset();
        Integer end = (int)((pageable.getOffset()+pageable.getPageSize()) > list.size() ? list.size() :  (pageable.getOffset() + pageable.getPageSize()));
        list = list.subList(start,end);
        return new PageImpl<Recruitment>(list, pageable, list1.size());
    }
    @Override
    public Page<Recruitment> searchTitle(String keySearch, Integer pageNo) {
        List<Recruitment> list = recruitmentRepository.searchTitle(keySearch);
        return typeOfSearch(list, pageNo);
    }
    @Override
    public Page<Recruitment> searchAddress(String keySearch, Integer pageNo) {
        List<Recruitment> list = recruitmentRepository.searchAddress(keySearch);
        return typeOfSearch(list, pageNo);
    }
    @Override
    public Page<Recruitment> searchCompany(String keySearch, Integer pageNo) {
        List<Recruitment> list = recruitmentRepository.searchCompany(keySearch);
        return typeOfSearch(list, pageNo);
    }

    @Override
    public Page<Recruitment> findAllByCompany(Company company, Integer pageNo) {
        List<Recruitment> list = recruitmentRepository.findByCompany(company);
        return typeOfSearch(list, pageNo);
    }

    @Override
    public Page<Recruitment> searchCategory(Category category, Integer pageNo) {
        List<Recruitment> list = recruitmentRepository.findByCategory(category);
        return typeOfSearch(list,pageNo);
    }

    @Override
    public Page<Recruitment> findAllRecruitmentPageableAndSort(Integer pageNo){
        Sort sort = Sort.by(
                Sort.Order.desc("totalApplyPeople"),
                Sort.Order.desc("salary"),
                Sort.Order.desc("quantity")
        );
        List<Recruitment> recruitments = recruitmentRepository.findAll(sort);
        return typeOfSearch(recruitments,pageNo);
    }
}
