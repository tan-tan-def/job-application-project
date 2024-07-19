package com.funix.asm02.service.cv;

import com.funix.asm02.entity.CV;
import com.funix.asm02.entity.User;

import java.util.List;

public interface CVService {
    CV save(CV cv);
    CV findByStatus(int statusId);
    void deleteById(int theId);
    CV findByUserAndStatus(User user, int Status);
    CV findById(int id);
}
