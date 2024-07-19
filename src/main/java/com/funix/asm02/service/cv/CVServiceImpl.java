package com.funix.asm02.service.cv;

import com.funix.asm02.dao.CVRepository;
import com.funix.asm02.entity.CV;
import com.funix.asm02.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CVServiceImpl implements CVService{
    private CVRepository cvRepository;

    @Autowired
    public CVServiceImpl(CVRepository cvRepository) {
        this.cvRepository = cvRepository;
    }

    @Override
    public CV save(CV cv) {
        return cvRepository.save(cv);
    }

    @Override
    public CV findByStatus(int statusId) {
        return cvRepository.findByStatus(statusId);
    }

    @Override
    public void deleteById(int theId) {
        cvRepository.deleteById(theId);
    }

    @Override
    public CV findByUserAndStatus(User user, int status) {
        for(CV cv: user.getCvs()){
            if(cv.getStatus()==status){
                return cv;
            }
        }
        return null;
    }

    @Override
    public CV findById(int id) {
        Optional<CV> result = cvRepository.findById(id);
        CV cv = null;
        if(result.isPresent()){
            cv = result.get();
        }else{
            throw new RuntimeException("Did not find the User id "+id);
        }
        return cv;
    }

}
