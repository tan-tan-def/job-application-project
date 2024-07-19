package com.funix.asm02.service.category;


import com.funix.asm02.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAllAndSort();
    Category saveCategory(Category category);
    Category findById(int theId);
    void deleteCategory(int theId);
    void deleteAll();
    List<Category> findAll();

}
