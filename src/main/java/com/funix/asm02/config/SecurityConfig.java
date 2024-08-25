package com.funix.asm02.config;

import com.funix.asm02.userDetail.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(c->c.disable())
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/*","/user/**", "/register/**", "/search/**","/uploads/**",
                                        "/verify","/confirm-account", "/applicant/follow-company",
                                        "/applicant/apply-job2", "/applicant/apply-job1", "/applicant/save-job")
                                        .permitAll()
                                .requestMatchers("/recruiter/**").hasAuthority("RECRUITER")
                                .requestMatchers("/applicant/**").hasAuthority("APPLICANT")
                                .anyRequest().authenticated()
                )
                .formLogin(
                        login -> login
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/")
                                .permitAll()
                )
                .logout(
                        logout ->logout
                                .permitAll()
                                .logoutSuccessUrl("/")
                );
        return http.build();
    }
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static");

        Path pathDir = Paths.get(".","uploads");
        String pathUploads = pathDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/" + pathUploads + "/");
    }
    @Bean
    WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers("/static/**", "/assets/**");
    }

}
