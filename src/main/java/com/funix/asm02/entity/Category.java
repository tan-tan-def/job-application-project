package com.funix.asm02.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="category")
public class Category {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="number_choose")
    private int numberChoose;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
               mappedBy = "category")
    @ToString.Exclude
    List<Recruitment> recruitments;

    public Category(String name) {
        this.name = name;
        this.recruitments = new ArrayList<>();
        this.numberChoose = 0;
    }

    public Category() {
    }

    public void setNumberChoose() {
        this.numberChoose = recruitments.size();
    }
}
