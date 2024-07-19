package com.funix.asm02.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="save_job")
public class SaveJob {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="recruitment_id", referencedColumnName = "id")
    private Recruitment recruitment;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    public SaveJob(Recruitment recruitment, User user) {
        this.recruitment = recruitment;
        this.user = user;
    }
    public SaveJob() {
    }
}
