package com.funix.asm02.controller;

import com.funix.asm02.common.Field;
import com.funix.asm02.entity.*;
import com.funix.asm02.service.applyPost.ApplyPostService;
import com.funix.asm02.service.company.CompanyService;
import com.funix.asm02.service.cv.CVService;
import com.funix.asm02.service.followCompany.FollowCompanyService;
import com.funix.asm02.service.recruitment.RecruitmentService;
import com.funix.asm02.service.saveJob.SaveJobService;
import com.funix.asm02.service.uploadFile.UploadFileService;
import com.funix.asm02.service.user.UserService;
import com.funix.asm02.userDetail.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/applicant")
public class ApplicantController {
    private UserService userService;
    private SaveJobService saveJobService;
    private ApplyPostService applyPostService;
    private FollowCompanyService followCompanyService;
    private RecruitmentService recruitmentService;
    private CompanyService companyService;
    private CVService cvService;
    private UploadFileService uploadFileService;

    public void setPaginationAttributes(Model theModel, Page<?> list, Integer pageNo){
        theModel.addAttribute("currentPage", pageNo);
        theModel.addAttribute("totalPages", list.getTotalPages());
    }
    public void changeTotalApplyPeople(Recruitment recruitment){
        //Set total apply people for this recruitment
        List<ApplyPost> applyPostList = applyPostService.findAllByRecruitment(recruitment);
        recruitment.setTotalApplyPeople(applyPostList.size());
        recruitmentService.saveRecruitment(recruitment);

        //Set total apply people for this company
        Company company = companyService.findById(recruitment.getCompany().getId());
        List<ApplyPost> applyPostList1 = applyPostService.findAllByCompany(company);
        company.setTotalApplyPeople(applyPostList1.size());
        companyService.saveCompany(company);
    }
    /*
    Table of contents(row, content):
          :Show profile's applicant
          :Update profile's applicant
          :Apply job with new CV
          :Apply Job with updated CV
          :Save a new job
          :Follow company
          :Display the list of jobs being saved
          :Delete the save job
          :Display the list of applying job
          :Delete the apply job
          :Display the list following company
          :Unfollow company
          :Job postings of the company
          :Delete the CV
     */
    @Autowired
    public ApplicantController(UserService userService, SaveJobService saveJobService, RecruitmentService recruitmentService, CVService cvService,
                               ApplyPostService applyPostService, FollowCompanyService followCompanyService, CompanyService companyService,
                               UploadFileService uploadFileService) {
        this.userService = userService;
        this.saveJobService = saveJobService;
        this.applyPostService = applyPostService;
        this.followCompanyService = followCompanyService;
        this.recruitmentService = recruitmentService;
        this.companyService = companyService;
        this.uploadFileService = uploadFileService;
        this.cvService = cvService;
    }

    //Show profile's applicant
    @GetMapping("/profile")
    public String showProfileApplicant(Model theModel, @RequestParam(name="id-recruitment", defaultValue = "-1") Integer recruitmentId,
                                       Principal principal, @RequestParam(name="apply", defaultValue = "") String apply,
                                       @RequestParam(name = "message", defaultValue = "") String message){
        User user = userService.userAuthentication(principal);
        theModel.addAttribute("userInformation", user);

        theModel.addAttribute("apply", apply);
        theModel.addAttribute("idRe", recruitmentId);

        CV cv = cvService.findByUserAndStatus(user, Field.CV_STATUS_1);
        theModel.addAttribute("Cv", cv);
        theModel.addAttribute("message", message);
        return "general/profile";
    }
    //Delete the CV
    @PostMapping("/deleteCv")
    public String deleteCV(@RequestParam("idCv") int idCv, Principal principal){
        User user = userService.userAuthentication(principal);
        uploadFileService.delete(idCv, Field.CV, user.getId());
        cvService.deleteById(idCv);
        return "redirect:/applicant/profile";
    }

    //Upload cv
    @PostMapping("/upload-cv")
    public @ResponseBody String uploadCV(@RequestParam("file") MultipartFile file, Principal principal){
        User user = userService.userAuthentication(principal);
        if(!file.isEmpty()){
            CV cvStatusOne = cvService.findByUserAndStatus(user, Field.CV_STATUS_1);
            if(cvStatusOne!=null){
                cvStatusOne.setStatus(Field.CV_STATUS_0);
            }
            CV cv = new CV(user,file.getOriginalFilename(),Field.CV_STATUS_1);
            cvService.save(cv);
            uploadFileService.store(file,Field.CV, user.getId());
            return "/"+ cv.getFileNamePath();
        }
        return "false";
    }
    //Update profile's applicant
    @PostMapping("/update-profile")
    public String updateProfileApplicant(@ModelAttribute("userInformation") User user, Principal principal,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         RedirectAttributes redirectAttributes){

        User mainUser = userService.userAuthentication(principal);
        mainUser.setAddress(user.getAddress());
        mainUser.setDescription(user.getDescription());
        mainUser.setPhoneNumber(user.getPhoneNumber());
        mainUser.setEmail(user.getEmail());
        mainUser.setFullName(user.getFullName());
        userService.saveUser(mainUser);

        redirectAttributes.addAttribute("message","Cập nhật cá nhân thành công");
        customUserDetails.setUser(mainUser);
        return "redirect:/applicant/profile";
    }
    //Apply job with new CV
    @PostMapping("/apply-job")
    public @ResponseBody String applyJobCheck(@RequestParam("idRe") Integer idRe, @RequestParam("text") String text,
                                         @RequestParam("file") MultipartFile file, Principal principal){
        if(principal==null){
            return "false";
        }
        //find user and recruitment
        User user = userService.userAuthentication(principal);
        Recruitment recruitment = recruitmentService.findById(idRe);
        String fileName = file.getOriginalFilename();

        //save applyPost
        ApplyPost applyPost = new ApplyPost(user,recruitment,fileName,text, Field.APPLY_POST_NOT_REVIEWED);
        applyPostService.saveApplyPost(applyPost);

        //save cv
        CV cv = new CV(user,fileName, 0);
        cvService.save(cv);

        //upload file int src/main/resources/static/cv/**
        uploadFileService.store(file,Field.CV,user.getId());

        changeTotalApplyPeople(recruitment);
        return "true";
    }


    //Apply Job with updated CV
    @PostMapping("/apply-job1")
    public @ResponseBody String applyJob1(@RequestParam("idRe") Integer idRe, @RequestParam("text") String text, Principal principal){
        if(principal==null){
            return "false";
        }
        String fileName="empty";

        //Find the User and Recruitment
        User user = userService.userAuthentication(principal);
        Recruitment recruitment = recruitmentService.findById(idRe);

        CV cv = cvService.findByUserAndStatus(user,Field.CV_STATUS_1);
        if(cv!=null){
            fileName = cv.getFileName();
        }
        if(fileName.equals("empty")){
            return "emptyCv";
        }

        //save applyPost
        ApplyPost applyPost = new ApplyPost(user,recruitment,fileName,text, Field.APPLY_POST_NOT_REVIEWED);
        applyPostService.saveApplyPost(applyPost);

        changeTotalApplyPeople(recruitment);
        return "true";
    }
    //Save a new job
    @PostMapping("/save-job")
    public @ResponseBody String saveJob(@RequestParam("idRe") Integer idRe, Principal principal){
        //Not logged in
        if(principal==null){
            return "false";
        }

        //Find user and recruitment
        User user = userService.userAuthentication(principal);
        Recruitment recruitment = recruitmentService.findById(idRe);

        //Find SaveJob with user and recruitment
        SaveJob saveJobFind = saveJobService.findByUserRecruitment(user,recruitment);

        //Save or delete SaveJob
        if(saveJobFind==null){
            SaveJob saveJob = new SaveJob(recruitment, user);
            saveJobService.save(saveJob);
            return "true";
        }else{
            saveJobService.deleteById(saveJobFind.getId());
            return "duplicate";
        }
    }
    //Follow company
    @PostMapping("/follow-company")
    public @ResponseBody String followCompany(@RequestParam("idCompany") Integer idCompany, Principal principal){
        if(principal==null){
            return "false";
        }

        User user = userService.userAuthentication(principal);
        Company company = companyService.findById(idCompany);

        FollowCompany followCompanyFind = followCompanyService.findByUserAndCompany(user, company);

        if(followCompanyFind==null){
            FollowCompany followCompany = new FollowCompany(user,company);
            followCompanyService.save(followCompany);
            return "true";
        }else{
            followCompanyService.delete(followCompanyFind.getId());
            return "duplicate";
        }
    }
    //Display the list of jobs being saved
    @GetMapping("/list-save-job")
    public String listSaveJob(Principal principal, Model theModel,
                              @RequestParam(name="pageNo",defaultValue = "1") Integer pageNo){
        User user = userService.userAuthentication(principal);
        Page<SaveJob> saveJobs = saveJobService.findAllByUser(user, pageNo);

        theModel.addAttribute("saveJobList", saveJobs);
        setPaginationAttributes(theModel,saveJobs, pageNo);
        return "applicant/list-save-job";
    }
    //Delete the save job
    @PostMapping("/delete-save-job")
    public String deleteSaveJob(@RequestParam("idSaveJob") Integer id){
        saveJobService.deleteById(id);
        return "redirect:/applicant/list-save-job";
    }
    //Display the list of applying job
    @GetMapping("/get-list-apply")
    public String getListApplyJob(Principal principal, Model theModel,
                                  @RequestParam(name="pageNo", defaultValue = "1") Integer pageNo){
        User user = userService.userAuthentication(principal);
        Page<ApplyPost> applyPosts = applyPostService.findAllByUser(user, pageNo);

        theModel.addAttribute("applyPostList", applyPosts);
        setPaginationAttributes(theModel,applyPosts, pageNo);
        return "applicant/list-apply-job";
    }
    //Delete the apply job
    @PostMapping("/delete-apply-post")
    public String deleteApplyPost(@RequestParam("idApplyPost") Integer id){
        Recruitment recruitment = applyPostService.findById(id).getRecruitment();
        applyPostService.deleteById(id);
        changeTotalApplyPeople(recruitment);
        return "redirect:/applicant/get-list-apply";
    }
    //Display the list following company
    @GetMapping("/get-list-company")
    public String getListCompany(Principal principal, Model theModel,
                                 @RequestParam(name="pageNo", defaultValue = "1") Integer pageNo){
        User user = userService.userAuthentication(principal);
        Page<FollowCompany> followCompanies = followCompanyService.findAllByUser(user, pageNo);

        theModel.addAttribute("followCompanyList", followCompanies);
        setPaginationAttributes(theModel, followCompanies, pageNo);
        return "applicant/list-follow-company";
    }
    //Delete follow company
    @PostMapping("/delete-follow")
    public String deleteFollowCompany(@RequestParam("idFollowCompany") Integer id){
        followCompanyService.delete(id);
        return "redirect:/applicant/get-list-company";
    }

    //Job postings of the company
    @GetMapping("/company-post/{id}")
    public String companyPost(@PathVariable("id") Integer id, Model theModel,
                              @RequestParam(name="pageNo", defaultValue = "1") Integer pageNo){
        Company company = companyService.findById(id);
        Page<Recruitment> recruitments = recruitmentService.findAllByCompany(company, pageNo);

        theModel.addAttribute("company", company);
        theModel.addAttribute("recruitments", recruitments);

        setPaginationAttributes(theModel,recruitments,pageNo);

        return "applicant/post-company";
    }
}
