package ru.study.scapping.model.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class KeyWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String translation;

    @ManyToOne(fetch = FetchType.EAGER)
    private Theme theme;

    public KeyWord(String name, String translation) {
        this.name = name;
        this.translation = translation;
    }

    public KeyWord(String name, String translation, Theme theme) {
        this.name = name;
        this.translation = translation;
        this.theme = theme;
    }
}
