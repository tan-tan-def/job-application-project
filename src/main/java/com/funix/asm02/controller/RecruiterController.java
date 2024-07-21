package com.funix.asm02.controller;

import com.funix.asm02.common.DateTimeUntil;
import com.funix.asm02.common.Field;
import com.funix.asm02.common.RemoveVietnameseSigns;
import com.funix.asm02.entity.*;
import com.funix.asm02.service.applyPost.ApplyPostService;
import com.funix.asm02.service.category.CategoryService;
import com.funix.asm02.service.company.CompanyService;
import com.funix.asm02.service.recruitment.RecruitmentService;
import com.funix.asm02.service.saveJob.SaveJobService;
import com.funix.asm02.service.uploadFile.UploadFileService;
import com.funix.asm02.service.user.UserService;
import com.funix.asm02.userDetail.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/recruiter")
public class RecruiterController {
    private ApplyPostService applyPostService;
    private RecruitmentService recruitmentService;
    private CategoryService categoryService;
    private UserService userService;
    private CompanyService companyService;
    private SaveJobService saveJobService;
    private UploadFileService uploadFileService;


    /*
        Table of contents(row, content)
       : Candidates who have applied to a specific company
       : Show the form to post a new recruitment
       : Add recruitment and increase a numberChoose's Category
       : Show the form of company's profile
       : Update profile of Recruiter
       : Update profile of Company
       : Display job postings of a company
       : Delete the post
       : Show the detail of recruitment
       : Display detailed information of the job recruitment
       : Edit and save the job recruitment
     ? : Show the list of candidate
     ? : Logic to post a job
     ? : Update the information of company and recruiter
     ? : CRUD Logic for a Job Posting
       :
     */

    @Autowired
    public RecruiterController(ApplyPostService applyPostService, RecruitmentService recruitmentService,
                               CategoryService categoryService, UserService userService, SaveJobService saveJobService,
                               CompanyService companyService, UploadFileService uploadFileService) {
        this.applyPostService = applyPostService;
        this.recruitmentService = recruitmentService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.companyService = companyService;
        this.uploadFileService = uploadFileService;
        this.saveJobService = saveJobService;
    }
    public void setPaginationAttributes(Model theModel, Page<?> list, Integer pageNo){
        theModel.addAttribute("currentPage", pageNo);
        theModel.addAttribute("totalPages", list.getTotalPages());
    }
    //Candidates who have applied to a specific company
    @GetMapping("/list-candidates-of-company")
    public String listCandidates(Model theModel, Principal principal,
                                 @RequestParam(name="pageNo", defaultValue = "1") Integer pageNo){
        // Extract companyId from CustomUserDetails obtained from Authentication principal
        User user = userService.userAuthentication(principal);

        // Retrieve a paginated list of ApplyPost candidates for a specific company
        Page<ApplyPost> listCandidateOfCompany = applyPostService.findAllByCompany(pageNo, user.getCompany());

        //Add attribute
        theModel.addAttribute("list", listCandidateOfCompany);
        setPaginationAttributes(theModel, listCandidateOfCompany, pageNo);
        return "recruiter/list-candidates";
    }
    //Show the form to post a new recruitment
    @GetMapping("/post-job")
    public String postJob(Model theModel){
        theModel.addAttribute("recruitment", new Recruitment());
        List<Category> categories = categoryService.findAllAndSort();
        theModel.addAttribute("categories", categories);
        return "recruiter/post-job";
    }

    //Add recruitment and increase a numberChoose's Category
    @PostMapping("/add-recruitment")
    public String add(@ModelAttribute("recruitment") Recruitment recruitment, Principal principal,
                      RedirectAttributes redirectAttributes){

        User user = userService.userAuthentication(principal);

        recruitment.setTotalApplyPeople(0);
        recruitment.setStatus(Field.RECRUITMENT_STATUS_NOT_EXPIRED_1);
        recruitment.setTitleToSearch(RemoveVietnameseSigns.removeVietnameseSignsAndLowerCase(recruitment.getTitle()));
        recruitment.setAddressToSearch(RemoveVietnameseSigns.removeVietnameseSignsAndLowerCase(recruitment.getAddress()));
        recruitment.setCreatedAt(DateTimeUntil.date());
        recruitment.setCompany(user.getCompany());

        recruitmentService.saveRecruitment(recruitment);

        redirectAttributes.addFlashAttribute("message", "Đăng tuyển thành công");

        return "redirect:/recruiter/list-post";
    }
    @PostMapping("/show-swal-post-job")
    public @ResponseBody String showSwalPostJob(Principal principal){
        User user = userService.userAuthentication(principal);
        if(user.getCompany().getNameCompany()==null){
            return "false";
        }
        return "true";
    }
    //Show the form of company's profile
    @GetMapping("/profile")
    public String showProfileCompanyAndRecruiter(Model theModel, Principal principal,
                                                 @ModelAttribute(name="message") String message){
        User user = userService.userAuthentication(principal);
        theModel.addAttribute("userInformation", user);

        Company company = companyService.findById(user.getCompany().getId());
        theModel.addAttribute("companyInformation", company);
        theModel.addAttribute("message", message);

        return "general/profile";
    }

    //Update profile of Recruiter
    @PostMapping("/update-recruiter")
    public String updateProfileRecruiter(@ModelAttribute("userInformation") User user, Principal principal,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         RedirectAttributes redirectAttributes){
        User mainUser = userService.userAuthentication(principal);
        mainUser.setEmail(user.getEmail());
        mainUser.setFullName(user.getFullName());
        mainUser.setAddress(user.getAddress());
        mainUser.setPhoneNumber(user.getPhoneNumber());
        mainUser.setDescription(user.getDescription());

        userService.saveUser(mainUser);

        customUserDetails.setUser(mainUser);

        redirectAttributes.addFlashAttribute("message", "Cập nhật cá nhân thành công");
        return "redirect:/recruiter/profile";
    }

    //Update profile of Company
    @PostMapping("/update-company")
    public String updateProfileCompany(@ModelAttribute("companyInformation") Company company,
                                       Principal principal, RedirectAttributes redirectAttributes){
        Company mainCompany = companyService.findById(company.getId());

        mainCompany.setEmail(company.getEmail());
        mainCompany.setNameCompany(company.getNameCompany());
        mainCompany.setNameCompanyToSearch(RemoveVietnameseSigns.removeVietnameseSignsAndLowerCase(company.getNameCompany()));
        mainCompany.setAddress(company.getAddress());
        mainCompany.setPhoneNumber(company.getPhoneNumber());
        mainCompany.setDescription(company.getDescription());

        companyService.saveCompany(mainCompany);

        redirectAttributes.addFlashAttribute("message", "Cập nhật công ty thành công");

        return "redirect:/recruiter/profile";
    }
    //Upload logo company
    @PostMapping("/upload-logo-company")
    public @ResponseBody String uploadLogoCompany(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        User user = userService.userAuthentication(principal);
        if(!file.isEmpty()){
            Company company = companyService.findByUser(user);
            String urlImage = uploadFileService.upload(file);
//            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//            company.setLogo(fileName);
            company.setLogo(urlImage);

//            uploadFileService.store(file,Field.IMAGE,user.getId());
            companyService.saveCompany(company);
            return company.getLogo();
        }
        return "Error";
    }
    //Display job postings of a company
    @GetMapping("/list-post")
    public String listPost(Model theModel, Principal principal, @ModelAttribute(name="message") String message,
                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo){
        User user = userService.userAuthentication(principal);
        Company company = companyService.findByUser(user);
        Page<Recruitment> recruitments = recruitmentService.findAllByCompany(company, pageNo);

        theModel.addAttribute("list", recruitments);
        theModel.addAttribute("message", message);
        setPaginationAttributes(theModel, recruitments, pageNo);
        return "recruiter/list-post";
    }
    //Delete the post
    @PostMapping("/delete")
    public String deletePost(@RequestParam("idRecruitment") Integer id){
        Recruitment recruitment = recruitmentService.findById(id);

        //Delete all applyPosts of this recruitment
        List<ApplyPost> applyPosts = applyPostService.findAllByRecruitment(recruitment);
        if(!applyPosts.isEmpty()){
            applyPostService.deleteAll(applyPosts);

            Company company = recruitment.getCompany();
            List<ApplyPost> applyPostList = applyPostService.findAllByCompany(company);
            company.setTotalApplyPeople(applyPostList.size());
            companyService.saveCompany(company);
        }

        //Delete all saveJob of this recruitment
        List<SaveJob> saveJobs = saveJobService.findByRecruitment(recruitment);
        saveJobService.deleteAll(saveJobs);

        //Delete recruitment
        recruitmentService.deleteRecruitment(id);

        return "redirect:/recruiter/list-post";
    }
    //Show the detail of recruitment
    @GetMapping("/detail-post/{id}")
    public String detail(Model theModel, @PathVariable("id") Integer id, Principal principal,
                         @RequestParam(name="pageNo", defaultValue = "1") Integer pageNo){
        Recruitment recruitment = recruitmentService.findById(id);
        theModel.addAttribute("recruitment", recruitment);

        User user = userService.userAuthentication(principal);
        if(user.getCompany().getId()==recruitment.getCompany().getId()){
            Page<ApplyPost> applyPosts = applyPostService.findAllByRecruitment(recruitment, pageNo);
            theModel.addAttribute("applyPosts", applyPosts);

            theModel.addAttribute("show", true);
            setPaginationAttributes(theModel, applyPosts, pageNo);
        }else{
            Page<Recruitment> recruitments = recruitmentService.findAllRecruitmentPageableAndSort(pageNo);
            theModel.addAttribute("listRelated", recruitments);
            theModel.addAttribute("show", false);
            setPaginationAttributes(theModel, recruitments, pageNo);
        }
        theModel.addAttribute("role","recruiter");

        return "general/detail-post";
    }
    //Display detailed information of the job recruitment
    @GetMapping("/edit-post/{id}")
    public String editPost(@PathVariable("id") Integer id, Model theModel){
        Recruitment recruitment = recruitmentService.findById(id);
        theModel.addAttribute("recruitment",recruitment);
        List<Category> categories = categoryService.findAllAndSort();
        theModel.addAttribute("categories",categories);
        return "recruiter/edit-job";
    }
    //Edit and save the job recruitment
    @PostMapping("/edit")
    public String edit(@ModelAttribute("recruitment") Recruitment recruitment, RedirectAttributes redirectAttributes){
        Recruitment mainRecruitment = recruitmentService.findById(recruitment.getId());
        //Save the recruitment
        mainRecruitment.setAddress(recruitment.getAddress());
        mainRecruitment.setAddressToSearch(RemoveVietnameseSigns.removeVietnameseSignsAndLowerCase(recruitment.getAddress()));
        mainRecruitment.setDeadline(recruitment.getDeadline());
        mainRecruitment.setDescription(recruitment.getDescription());
        mainRecruitment.setExperience(recruitment.getExperience());
        mainRecruitment.setQuantity(recruitment.getQuantity());
        mainRecruitment.setSalary(recruitment.getSalary());
        mainRecruitment.setTitle(recruitment.getTitle());
        mainRecruitment.setTitleToSearch(RemoveVietnameseSigns.removeVietnameseSignsAndLowerCase(recruitment.getTitle()));
        mainRecruitment.setType(recruitment.getType());
        mainRecruitment.setCategory(recruitment.getCategory());

        recruitmentService.saveRecruitment(mainRecruitment);

        redirectAttributes.addFlashAttribute("message","Cập nhật thành công");

        return "redirect:/recruiter/list-post";
    }
    //Approve the applicant's CV
    @GetMapping("/approve")
    public String approve(@RequestParam("userId") int userId,
                          @RequestParam("recruitmentId") int recruitmentId) {
        ApplyPost applyPost = applyPostService.findByRecruitmentIdUserId(recruitmentId, userId);
        applyPost.setStatus(Field.APPLY_POST_REVIEWED);
        applyPostService.saveApplyPost(applyPost);

        return "redirect:/recruiter/detail-post/"+recruitmentId;
    }
}
