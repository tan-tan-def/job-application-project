package com.funix.asm02.service.category;

import com.funix.asm02.dao.CategoryRepository;
import com.funix.asm02.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
    private CategoryRepository categoryRepository;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAllAndSort() {
        Sort sort = Sort.by(Sort.Direction.DESC, "numberChoose");
        return categoryRepository.findAll(sort);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category findById(int theId) {
        Optional<Category> result = categoryRepository.findById(theId);
        Category category = null;
        if(result.isPresent()){
            category = result.get();
        }else{
            throw new RuntimeException("Did not find the Category id "+theId);
        }

        return category;
    }

    @Override
    public void deleteCategory(int theId) {
        categoryRepository.deleteById(theId);
    }

    @Override
    public void deleteAll() {
        categoryRepository.deleteAll();
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

}
