package com.funix.asm02.entity;

import com.funix.asm02.common.Field;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="address")
    private String address;

    @Column(name="description")
    private String description;

    @Column(name="email", unique = true)
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*" + "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",
                       message = "Email format is incorrect")
    @NotNull(message = "is required")
    private String email;

    @Column(name="full_name")
    @NotNull(message = "is required")
    @Size(min=1, message="Contain at least 1 characters")
    private String fullName;

    @Column(name="image")
    private String image;

    @Column(name="password")
    @Size(min=8, message="Contain at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
                   message = "Password must contain at least one uppercase letter and one number")
    @NotNull(message = "is required")
    private String password;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="verification_code", updatable = false)
    private String verificationCode;

    @Column(name="status")
    private int status;
    /*
    0: Chưa cấp quyền truy cập
    1: Đã cấp quyền truy cập
     */

    @Column(name="enable")
    private Boolean enable;

    @Column(name="role_id")
    private int roleId;
    /*
    1: Applicant
    2: Recruiter
     */

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE,
                          CascadeType.PERSIST, CascadeType.REFRESH},
               mappedBy = "user")
    private List<CV> cvs;

    @OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.EAGER,
            mappedBy = "user")
    private Set<UserRole> userRoles;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
               fetch = FetchType.LAZY, mappedBy = "user")
    private Set<SaveJob> saveJobs;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
               fetch = FetchType.LAZY, mappedBy = "user")
    private Set<FollowCompany> followCompanies;

    @OneToMany(cascade = CascadeType.ALL,
               fetch = FetchType.LAZY, mappedBy = "user")
    private Set<ApplyPost> applyPosts;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Company company;

    public User(){}

    @Transient
    public String getImagePath(){
        if(image==null) return null;
        Path path = Paths.get("uploads", Field.IMAGE, String.valueOf(id),image);
        return path.toString();
    }

}
