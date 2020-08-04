package ru.study.scapping.model.domain;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

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

    private String title;

    private String link = "#";

    @Column(length = 2000)
    private String snippet;

    private Integer year;

    @ManyToOne
    private Theme theme;

    @ManyToOne
    private KeyWord keyWord;

    @ManyToOne(fetch = FetchType.EAGER)
    private Journal journal;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "publications_authours",
            joinColumns = { @JoinColumn(name = "author_id") },
            inverseJoinColumns = { @JoinColumn(name = "publication_id") })
    private List<Author> authors = new LinkedList<>();

}
