package com.funix.asm02.controller.login_register;

import com.funix.asm02.common.Field;
import com.funix.asm02.common.Utility;
import com.funix.asm02.entity.Company;
import com.funix.asm02.entity.Role;
import com.funix.asm02.entity.User;
import com.funix.asm02.entity.UserRole;
import com.funix.asm02.service.company.CompanyService;
import com.funix.asm02.service.redis.RedisService;
import com.funix.asm02.service.role.RoleService;
import com.funix.asm02.service.user.UserService;
import com.funix.asm02.service.userRole.UserRoleService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;


@Controller
@RequestMapping("/register")
public class RegisterController {
    private UserService userService;
    private RoleService roleService;
    private UserRoleService userRoleService;
    private CompanyService companyService;
    private RedisService redisService;
    @Autowired
    public RegisterController(UserService userService, RoleService roleService, UserRoleService userRoleService, CompanyService companyService, RedisService redisService){
        this.userService = userService;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
        this.companyService = companyService;
        this.redisService = redisService;
    }
    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
    @GetMapping("/show-form-for-register")
    public String showFormForRegister(Model theModel){
        theModel.addAttribute("newUser", new User());
        List<Role> roles = roleService.findAll();
        theModel.addAttribute("roles",roles);
        return "login-register/register";
    }
    @PostMapping("/sign-up")
    public String saveUser(@Valid @ModelAttribute("newUser") User user,
                           BindingResult theBindingResult,
                           @RequestParam("roleId") Integer roleId,
                           HttpServletRequest request,
                           Model theModel
    ) throws MessagingException, UnsupportedEncodingException {
        String roleUser = "";
        if(!theBindingResult.hasErrors()) {
            Role role = roleService.findById(roleId);

            redisService.saveUser(user.getEmail(),user);
            roleUser = userService.registerUser(user,role);

            String siteURL = Utility.getSiteURL(request);

            userService.sendVerificationEmail(user, siteURL);

            //Save role and user of UserRole
            UserRole userRole = new UserRole(user,role);
            userRoleService.save(userRole);
        }
        if(roleUser.equals(Field.RECRUITER)){
            theModel.addAttribute("userInformation", user);

            //save Company
            Company company = new Company();
            company.setUser(user);
            companyService.saveCompany(company);
            return "general/profile";
        }else if(roleUser.equals("")){
            theModel.addAttribute("message", "Not Empty");
            return "login-register/register";
        }
        return "redirect:/login";
    }
    @GetMapping("/profile/{id}")
    public String showAnnouncement(@PathVariable int id, Model theModel){
        User user = userService.findById(id);
        theModel.addAttribute("userInformation", user);
        theModel.addAttribute("confirm_await", true);
        theModel.addAttribute("message", "Đã gửi email xác thực, vui lòng kiểm tra email");
        return "general/profile";
    }
    @GetMapping("/check-duplicate-email")
    public @ResponseBody String checkDuplicateEmail(@RequestParam("email") String email) {
        User duplicateUser = userService.findByEmail(email);
        return (duplicateUser!=null)?"duplicate":"unique";
    }
}


