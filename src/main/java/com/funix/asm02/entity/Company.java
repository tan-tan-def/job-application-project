package com.funix.asm02.entity;

import com.funix.asm02.common.Field;
import com.funix.asm02.service.applyPost.ApplyPostService;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name="company")
public class Company {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="address")
    private String address;

    @Column(name="description")
    private String description;

    @Column(name="email")
    private String email;

    @Column(name="logo")
    private String logo;

    @Column(name="name_company")
    private String nameCompany;

    //Name's company without accents and without uppercase for searching
    @Column(name="name_company_to_search")
    private String nameCompanyToSearch;

    @Column(name="phone_number")
    private String phoneNumber;

    //Save the number of job applicants
    @Column(name="total_apply_people")
    private int totalApplyPeople;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
               mappedBy = "company")
    private List<Recruitment> recruitments;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
               fetch = FetchType.LAZY, mappedBy = "company")
    private Set<FollowCompany> followCompanies;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @Transient
    public String getLogoPath(){
        if(logo==null) return null;
        Path path = Paths.get("uploads", Field.IMAGE, String.valueOf(user.getId()),logo);
        return path.toString();
    }
}
