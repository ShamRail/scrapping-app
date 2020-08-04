package ru.study.scapping.model.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "name"})
@ToString
@Entity
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String translation;

    public Theme(String name, String translation) {
        this.name = name;
        this.translation = translation;
    }
}
