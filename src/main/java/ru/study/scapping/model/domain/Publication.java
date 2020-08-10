package ru.study.scapping.model.domain;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@NoArgsConstructor
@EqualsAndHashCode(of = {
    "id", "link", "title"
})
@Getter
@Setter
@Entity
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 1000)
    private String title;

    @Column(length = 1000)
    private String link = "#";

    @Column(length = 3500)
    private String snippet;

    private Integer year;

    @ManyToOne
    private Theme theme;

    @ManyToOne
    private KeyWord keyWord;

    @ManyToOne(fetch = FetchType.EAGER)
    private Journal journal;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "publications_authours",
            joinColumns = { @JoinColumn(name = "author_id") },
            inverseJoinColumns = { @JoinColumn(name = "publication_id") })
    private List<Author> authors = new LinkedList<>();

    public String authorsSeparated() {
        return authors.stream()
                .map(Author::getName)
                .collect(Collectors.joining(", "));
    }

}
