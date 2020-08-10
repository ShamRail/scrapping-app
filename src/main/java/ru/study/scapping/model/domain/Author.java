package ru.study.scapping.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(length = 511)
    private String name;

    public Author(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private List<Publication> publications = new LinkedList<>();

    public static Author idStub(int id) {
        Author author = new Author();
        author.setId(id);
        return author;
    }

}
