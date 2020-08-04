package ru.study.scapping.model.domain;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "name"})
@ToString(of = {"id", "name"})
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    public Author(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "authors")
    private List<Publication> publications = new LinkedList<>();

}
