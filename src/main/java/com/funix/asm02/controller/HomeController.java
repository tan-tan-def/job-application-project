package com.funix.asm02.controller;

import com.funix.asm02.common.Field;
import com.funix.asm02.entity.*;
import com.funix.asm02.service.applyPost.ApplyPostService;
import com.funix.asm02.service.category.CategoryService;
import com.funix.asm02.service.company.CompanyService;
import com.funix.asm02.service.cv.CVService;
import com.funix.asm02.service.followCompany.FollowCompanyService;
import com.funix.asm02.service.recruitment.RecruitmentService;
import com.funix.asm02.service.saveJob.SaveJobService;
import com.funix.asm02.service.uploadFile.UploadFileService;
import com.funix.asm02.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class HomeController {
    private UserService userService;
    private RecruitmentService recruitmentService;
    private CompanyService companyService;
    private CategoryService categoryService;
    private CVService cvService;
    private FollowCompanyService followCompanyService;
    private ApplyPostService applyPostService;
    private SaveJobService saveJobService;
    private UploadFileService uploadFileService;
    private int numberToShowSwal=1;

    public void setPaginationAttributes(Model theModel, Page<?> list, Integer pageNo){
        theModel.addAttribute("currentPage", pageNo);
        theModel.addAttribute("totalPages", list.getTotalPages());
    }

    @Autowired
    public HomeController(UserService userService, RecruitmentService recruitmentService, CompanyService companyService,
                          CategoryService categoryService, CVService cvService, UploadFileService uploadFileService,
                          FollowCompanyService followCompanyService, ApplyPostService applyPostService, SaveJobService saveJobService){
        this.userService = userService;
        this.recruitmentService = recruitmentService;
        this.companyService = companyService;
        this.categoryService = categoryService;
        this.cvService = cvService;
        this.uploadFileService = uploadFileService;
        this.followCompanyService = followCompanyService;
        this.applyPostService = applyPostService;
        this.saveJobService = saveJobService;
    }
    public void showSwalLogin(Model theModel,Principal principal){
        if(principal != null) {
            //The logic for displaying swal after each successful login
            theModel.addAttribute("numberToShowSwal",numberToShowSwal);
            numberToShowSwal += 1;

            User user = userService.userAuthentication(principal);
            CV cv = cvService.findByUserAndStatus(user, Field.CV_STATUS_1);
            theModel.addAttribute("cv",cv);
        }else{
            //The logic for displaying swal after each successful login
            numberToShowSwal=0;
            theModel.addAttribute("numberToShowSwal",numberToShowSwal);
            numberToShowSwal+=1;
        }
    }
    @RequestMapping("/home")
    public String home(Model theModel, Principal principal){
        //Show number of candidate
        List<User> numberCandidate = userService.findByRoleId(Field.ID_OF_APPLICANT);
        theModel.addAttribute("numberCandidate", numberCandidate.size());

        //Show number of company
        List<User> numberCompany = userService.findByRoleId(Field.ID_OF_RECRUITER);
        theModel.addAttribute("numberCompany", numberCompany.size());

        //Show the list of category
        for(Category category:categoryService.findAll()){
            category.setNumberChoose();
            categoryService.saveCategory(category);
        }
        List<Category> categories = categoryService.findAllAndSort();
        theModel.addAttribute("categories", categories);

        //show the list of recruitment
        List<Recruitment> recruitments = recruitmentService.findAllAndSort();
        theModel.addAttribute("recruitments", recruitments);
        theModel.addAttribute("numberRecruitment", recruitments.size());

        //show the list of company
        List<Company> companies = companyService.findAllAndSort();
        theModel.addAttribute("companies", companies);
        showSwalLogin(theModel,principal);
            return "general/home";
    }
    @GetMapping("/detail-post/{id}")
    public String showRecruitmentDetail(@PathVariable("id") int id, Model theModel, Principal principal,
                                        @RequestParam(name="pageNo", defaultValue = "1") Integer pageNo){
        Recruitment recruitment = recruitmentService.findById(id);
        theModel.addAttribute("recruitment", recruitment);

        Page<Recruitment> listRelated = recruitmentService.findAllRecruitmentPageableAndSort(pageNo);
        theModel.addAttribute("listRelated", listRelated);
        setPaginationAttributes(theModel, listRelated, pageNo);

        if(principal!=null){
            User user = userService.userAuthentication(principal);
            SaveJob saveJob = saveJobService.findByUserRecruitment(user,recruitment);
            theModel.addAttribute("saveJob", saveJob);
        }

        theModel.addAttribute("role", "user");
        return "general/detail-post";
    }
    @GetMapping("/detail-company/{id}")
    public String showDetailCompany(@PathVariable("id") Integer id, Model theModel, Principal principal){
        Company company = companyService.findById(id);
        theModel.addAttribute("company", company);

        if(principal!=null){
            User user = userService.userAuthentication(principal);
            FollowCompany followCompany = followCompanyService.findByUserAndCompany(user,company);
            theModel.addAttribute("followCompany", followCompany);
        }

        return "general/detail-company";
    }
    @PostMapping("/upload-avatar")
    public @ResponseBody String uploadAvatar(@RequestParam("email") String email, @RequestParam("file")MultipartFile file){
        User user = userService.findByEmail(email);
        if(!file.isEmpty()){
            user.setImage(file.getOriginalFilename());
            uploadFileService.store(file, Field.IMAGE, user.getId());
            userService.saveUser(user);
            //System.out.println("Lớp HomeController" + user.getImagePath());
            return "/"+user.getImagePath();
        }
        return "Error";
    }
    @GetMapping("/category")
    public String showCategory(Model theModel){
        List<Category> categories = categoryService.findAllAndSort();
        theModel.addAttribute("categories",categories);
        return "general/list-job";
    }
    @PostMapping("/show-modal-apply")
    public @ResponseBody String showModalApply( @RequestParam("idRe") Integer idRe, Principal principal){
        if(principal==null){
            return "false";
        }
        User user = userService.userAuthentication(principal);
        ApplyPost applyPost = applyPostService.findByRecruitmentIdUserId(idRe, user.getId());
        if(applyPost!=null){
            return "duplicate";
        }
        return "true";
    }
}
