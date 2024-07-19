package com.funix.asm02;


import com.funix.asm02.common.Field;
import com.funix.asm02.entity.Category;
import com.funix.asm02.entity.Role;
import com.funix.asm02.service.category.CategoryService;
import com.funix.asm02.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DataSeeder implements CommandLineRunner {
    private RoleService roleService;
    private CategoryService categoryService;
    @Autowired
    public DataSeeder(RoleService roleService, CategoryService categoryService) {
        this.roleService = roleService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        createRole();
        createCategory();
    }
    public void createRole(){
        Role applicantRole = new Role(Field.APPLICANT);
        Role recruiterRole = new Role(Field.RECRUITER);
        List<Role> roles = roleService.findAll();
        if(roles.isEmpty()){
            roleService.saveRole(applicantRole);
            roleService.saveRole(recruiterRole);
        }
    }
    public void createCategory(){
        List<String> categoryNames = Arrays.asList("Java", "PHP", "C#", "Python", "JavaScript", "Ruby", "Swift", "C++", "FullStacks", "AI");
        Set<String> anotherCategoryNames = new HashSet<>();
        anotherCategoryNames.addAll(categoryNames);
        List<Category> categories = categoryService.findAllAndSort();
        if(categories.size() != categoryNames.size()){
            for (String category:anotherCategoryNames){
                Category category1 = new Category(category);
                categoryService.saveCategory(category1);
            }
        }
    }


}
