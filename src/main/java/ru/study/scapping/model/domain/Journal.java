package ru.study.scapping.model.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 511)
    private String name;

    public Journal(String name) {
        this.name = name;
    }
}
