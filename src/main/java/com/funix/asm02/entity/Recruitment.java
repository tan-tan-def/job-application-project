package com.funix.asm02.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Data
@Entity
@Table(name="recruitment")
public class Recruitment {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="address")
    private String address;

    //Address without accents and without uppercase for searching
    @Column(name="address_to_search")
    private String addressToSearch;

    @Column(name="created_at")
    private String createdAt;

    @Column(name="description")
    private String description;

    @Column(name="experience")
    private String experience;

    //The number of applicant
    @Column(name="quantity")
    private int quantity;

    @Column(name="rank_person")
    private String rankPerson;

    @Column(name="salary")
    private long salary;

    @Column(name="status")
    private int status;
    /*
    0: Đã hết hạn
    1: Còn hạn sử dụng
     */

    @Column(name="title")
    private String title;

    //Type without accents and without uppercase for searching
    @Column(name="title_to_search")
    private String titleToSearch;

    //Fulltime or Partime
    @Column(name="type")
    private String type;

    //Save the number of job applicants
    @Column(name="total_apply_people")
    private int totalApplyPeople;

    @Column(name="deadline")
    private String deadline;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name="category_id")
    @ToString.Exclude
    private Category category;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name="company_id")
    private Company company;

    @OneToMany(cascade = CascadeType.ALL,
               fetch = FetchType.LAZY, mappedBy = "recruitment")
    private Set<SaveJob> saveJobs;

    @OneToMany(cascade = CascadeType.ALL,
               fetch = FetchType.LAZY, mappedBy = "recruitment")
    private Set<ApplyPost> applyPosts;

    @Transient
    public String getSalaryFormatted(){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(salary);
        return formattedNumber +" đồng";
    }
    @Transient
    public String getDeadlineFormatted() {
        LocalDate ld;
        if (deadline.matches("\\d{4}-\\d{2}-\\d{2}")) {
            ld = LocalDate.parse(deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            ld = LocalDate.parse(deadline, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return ld.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
