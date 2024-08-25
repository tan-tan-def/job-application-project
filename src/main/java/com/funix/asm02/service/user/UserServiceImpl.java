package com.funix.asm02.service.user;

import com.funix.asm02.common.Field;
import com.funix.asm02.common.RandomString;
import com.funix.asm02.dao.UserRepository;
import com.funix.asm02.entity.Role;
import com.funix.asm02.entity.User;
import com.funix.asm02.userDetail.CustomUserDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private JavaMailSender javaMailSender;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int theId) {
        Optional<User> result = userRepository.findById(theId);
        User user = null;
        if(result.isPresent()){
            user = result.get();
        }else{
            throw new RuntimeException("Did not find the User id "+theId);
        }
        return user;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(int theId) {
        userRepository.deleteById(theId);
    }

    @Override
    public User findByVerificationCode(String verificationCode) {
        return userRepository.findByVerificationCode(verificationCode);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findByRoleId(Integer theId) {
        return userRepository.findByRoleId(theId);
    }

    @Override
    public User userAuthentication(Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        User user = findById(userDetails.getUserId());
        return user;
    }

    @Override
    public String registerUser(User user, Role role) {
        String roleUser="";
        //Transfer password to BCrypt and save RoleId
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePassword = encoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user.setRoleId(role.getId());

        if(role.getName().equals(Field.RECRUITER)){
            user.setEnable(false);
            user.setStatus(Field.USER_STATUS_NO_ENABLE_0);
            roleUser = Field.RECRUITER;
        }else{
            user.setEnable(true);
            user.setStatus(Field.USER_STATUS_ENABLE_1);
            roleUser = Field.APPLICANT;
        }

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);

        //save User
        saveUser(user);
        return roleUser;
    }
    @Override
    public void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String subject = "Please verify your change password";
        String senderName = "Wizardry Employment";
        String mailContent = "<p>Dear "+user.getFullName()+",</p>";
        mailContent += "<p> Please click the link below to verify to your registration:<p>";

        String verifyURL = siteURL + "/verify?code="+user.getVerificationCode();
        mailContent += "<h3><a href=\"" + verifyURL + "\">VERIFY</a></h3>";

        mailContent += "<p>Thank you<br>Royal Center for Wizardry Employment</p>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("phapthuathoanggia@gmail.com",senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent, true);

        javaMailSender.send(message);

    }
    @Override
    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if(user==null||user.getEnable()){
            return false;
        }else{
            user.setEnable(true);
            user.setStatus(Field.USER_STATUS_ENABLE_1);
            saveUser(user);
            return true;
        }
    }
}
