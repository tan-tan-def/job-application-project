package com.funix.asm02.entity;

import com.funix.asm02.common.DateTimeUntil;
import com.funix.asm02.common.Field;
import jakarta.persistence.*;
import lombok.Data;

import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Entity
@Table(name="apply_post")
public class ApplyPost {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="recruitment_id", referencedColumnName = "id")
    private Recruitment recruitment;

    @Column(name="name_cv")
    private String nameCv;

    @Column(name="text")
    private String text;

    @Column(name="created_at")
    private String createdAt;

    @Column(name="status")
    private  int status;
    /*
    0: Chưa duyệt CV
    1: Đã duyệt CV
     */

    public ApplyPost(User user, Recruitment recruitment, String nameCv, String text, int status) {
        this.user = user;
        this.recruitment = recruitment;
        this.nameCv = nameCv;
        this.text = text;
        this.createdAt = DateTimeUntil.date();
        this.status=status;
    }
    @Transient
    public String getNameCvPath(){
        if(nameCv==null) return null;
        Path path = Paths.get("uploads", Field.CV, String.valueOf(user.getId()), nameCv);
        return path.toString();
    }
    public ApplyPost() {
    }

}
