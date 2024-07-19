package com.funix.asm02.service.user;

import com.funix.asm02.entity.Role;
import com.funix.asm02.entity.User;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(int theId);
    User saveUser(User user);
    void deleteUser(int theId);
    User findByVerificationCode(String verificationCode);
    User findByEmail(String email);
    List<User> findByRoleId(Integer theId);
    User userAuthentication(Principal principal);
    String registerUser(User user, Role role);
    void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException;
    boolean verify(String verificationCode);
}
