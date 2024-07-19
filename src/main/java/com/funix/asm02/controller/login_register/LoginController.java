package com.funix.asm02.controller.login_register;

import com.funix.asm02.common.Field;
import com.funix.asm02.common.Utility;
import com.funix.asm02.entity.User;
import com.funix.asm02.service.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

@Controller
public class LoginController {
    private UserService userService;
    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }
    @RequestMapping("/")
    public String main(){
        return "redirect:/user/home";
    }

    @RequestMapping("/login")
    public String login(@RequestParam(name = "verification",  defaultValue = "false") boolean verification, Model theModel){
        String message = verification ? "Verify Success !!!" : "";
        theModel.addAttribute("message_verification", message);
        return "login-register/login";
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, RedirectAttributes redirectAttributes){
        boolean verification = userService.verify(code);
        redirectAttributes.addAttribute("verification", verification);
        return "redirect:/login";
    }
    @PostMapping("/confirm-account")
    public String confirmAccount(@RequestParam("email") String email, HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {
        User user = userService.findByEmail(email);
        String siteURL = Utility.getSiteURL(request);

        userService.sendVerificationEmail(user, siteURL);

        return "redirect:/register/profile/"+user.getId();
    }

}
