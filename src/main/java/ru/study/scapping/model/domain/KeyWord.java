package ru.study.scapping.model.domain;


import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = {"id", "name"})
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

    public String nameWithTranslation() {
        return String.format("%s | %s", name, translation);
    }

    public static KeyWord idStub(int id) {
        KeyWord keyWord = new KeyWord();
        keyWord.id = id;
        return keyWord;
    }

}
