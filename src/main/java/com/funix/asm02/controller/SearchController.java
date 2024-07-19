package com.funix.asm02.controller;

import com.funix.asm02.common.Field;
import com.funix.asm02.common.RemoveVietnameseSigns;
import com.funix.asm02.entity.Category;
import com.funix.asm02.entity.Recruitment;
import com.funix.asm02.entity.User;
import com.funix.asm02.service.category.CategoryService;
import com.funix.asm02.service.recruitment.RecruitmentService;
import com.funix.asm02.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {
    private UserService userService;
    private RecruitmentService recruitmentService;
    private CategoryService categoryService;
    private final String SEARCH_RECRUITMENT = "search-recruitment";
    private final String SEARCH_ADDRESS = "search-address";
    private final String SEARCH_COMPANY = "search-company";
    private final String SEARCH_CATEGORY = "search-category";
    @Autowired
    public SearchController(UserService userService, RecruitmentService recruitmentService, CategoryService categoryService) {
        this.userService = userService;
        this.recruitmentService = recruitmentService;
        this.categoryService = categoryService;
    }
    public void show(Model theModel, String keySearch, Page<?> list, Integer pageNo, String result){
        //Show number of candidate
        List<User> numberCandidate = userService.findByRoleId(Field.ID_OF_APPLICANT);
        theModel.addAttribute("numberCandidate", numberCandidate.size());
        //Show number of company
        List<User> numberCompany = userService.findByRoleId(Field.ID_OF_RECRUITER);
        theModel.addAttribute("numberCompany", numberCompany.size());
        //Show number of recruitment
        List<Recruitment> numberRecruitment = recruitmentService.findAllAndSort();
        theModel.addAttribute("numberRecruitment", numberRecruitment.size());
        //Show the key search
        theModel.addAttribute("keySearch", keySearch);
        //Show the list
        theModel.addAttribute("list", list);
        theModel.addAttribute("totalPages", list.getTotalPages());
        theModel.addAttribute("currentPage", pageNo);
        //show the active link
        theModel.addAttribute("resultSearch", result);
    }
    @RequestMapping("/search-recruitment")
    public String searchRecruitment(Model theModel, @ModelAttribute("keySearch") String keySearch
                                    ,@RequestParam(name="pageNo", defaultValue = "1") Integer pageNo){
        String anotherKeySearch = RemoveVietnameseSigns.removeVietnameseSignsAndLowerCase(keySearch);
        Page<Recruitment> list = recruitmentService.searchTitle(anotherKeySearch, pageNo);
        show(theModel, keySearch, list, pageNo, SEARCH_RECRUITMENT);
        return "general/result-search";
    }
    @RequestMapping("/search-company")
    public String searchCompany(Model theModel, @ModelAttribute("keySearch") String keySearch
                                ,@RequestParam(name="pageNo", defaultValue = "1") Integer pageNo){
        String anotherKeySearch = RemoveVietnameseSigns.removeVietnameseSignsAndLowerCase(keySearch);
        Page<Recruitment> list = recruitmentService.searchCompany(anotherKeySearch,pageNo);
        show(theModel, keySearch, list, pageNo, SEARCH_COMPANY);
        return "general/result-search";
    }
    @RequestMapping("/search-address")
    public String searchAddress(Model theModel, @ModelAttribute("keySearch") String keySearch
                                ,@RequestParam(name="pageNo", defaultValue = "1") Integer pageNo){
        String anotherKeySearch = RemoveVietnameseSigns.removeVietnameseSignsAndLowerCase(keySearch);
        Page<Recruitment> list = recruitmentService.searchAddress(anotherKeySearch, pageNo);
        show(theModel, keySearch, list, pageNo, SEARCH_ADDRESS);
        return "general/result-search";
    }
    @GetMapping("/search-category")
    public String searchCategory(@ModelAttribute("categoryId") int id, Model theModel,
                                 @RequestParam(name="pageNo", defaultValue = "1") int pageNo){
        Category category = categoryService.findById(id);
        Page<Recruitment> list = recruitmentService.searchCategory(category,pageNo);
        show(theModel, category.getName(),list,pageNo,SEARCH_CATEGORY);
        theModel.addAttribute("categoryId", category.getId());
        return "general/result-search";
    }
    @RequestMapping("/search-another-recruitment")
    public String searchRecruitmentFirst(RedirectAttributes redirectAttributes, @RequestParam("keySearch") String keySearch) {
        redirectAttributes.addAttribute("keySearch", keySearch);
        return "redirect:/search/search-recruitment";
    }
    @RequestMapping("/search-another-company")
    public String searchCompany(RedirectAttributes redirectAttributes, @RequestParam("keySearch") String keySearch){
        redirectAttributes.addAttribute("keySearch", keySearch);
        return "redirect:/search/search-company";
    }
    @RequestMapping("/search-another-address")
    public String searchAddress(RedirectAttributes redirectAttributes, @RequestParam("keySearch") String keySearch){
        redirectAttributes.addAttribute("keySearch", keySearch);
        return "redirect:/search/search-address";
    }
    @GetMapping("/search-another-category")
    public String searchCategory(RedirectAttributes redirectAttributes, @RequestParam("categoryId") int id){
        redirectAttributes.addAttribute("categoryId", id);
        return "redirect:/search/search-category";
    }
}
