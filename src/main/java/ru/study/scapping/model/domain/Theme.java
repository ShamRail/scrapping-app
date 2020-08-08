package ru.study.scapping.model.domain;

import lombok.*;

import javax.persistence.*;

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

    @Column(length = 511)
    private String name;

    @Column(length = 511)
    private String translation;

    public Theme(String name, String translation) {
        this.name = name;
        this.translation = translation;
    }

    public String nameWithTranslation() {
        return String.format("%s | %s", name, translation);
    }

    public static Theme idStub(int id) {
        Theme theme = new Theme();
        theme.id = id;
        return theme;
    }

}
