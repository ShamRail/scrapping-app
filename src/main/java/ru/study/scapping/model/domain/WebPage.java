package ru.study.scapping.model.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = {"id", "link", "site"})
@ToString
@Entity
public class WebPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String site;

    @Column(length = 511)
    private String link;

    @Column(length = 2000)
    private String snippet;

    private String language;

    @ManyToOne(fetch = FetchType.EAGER)
    private KeyWord keyWord;

    @ManyToOne
    private Theme theme;

}
