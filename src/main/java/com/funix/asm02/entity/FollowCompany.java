package com.funix.asm02.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="follow_company")
public class FollowCompany {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    public FollowCompany(User user, Company company) {
        this.user = user;
        this.company = company;
    }

    public FollowCompany() {
    }
}
