package com.funix.asm02.entity;

import com.funix.asm02.common.Field;
import jakarta.persistence.*;
import lombok.Data;

import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Entity
@Table(name="cv")
public class CV {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="file_name")
    private String fileName;

    @Column(name="status")
    private int status;
    /*
    0: CV ứng tuyển thông thường
    1: CV được cập nhật trong profile
     */

    public CV(User user, String fileName, int status) {
        this.user = user;
        this.fileName = fileName;
        this.status = status;
    }

    public CV() {
    }
    @Transient
    public String getFileNamePath(){
        if(fileName==null) return null;
        Path path = Paths.get("uploads", Field.CV, String.valueOf(user.getId()),fileName);
        //System.out.println(path);
        return path.toString();
    }
}
