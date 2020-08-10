package ru.study.scapping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.study.scapping.model.domain.Author;
import ru.study.scapping.model.domain.KeyWord;
import ru.study.scapping.model.domain.Theme;
import ru.study.scapping.service.PublicationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("app/publications/authors")
public class AuthorController {

    @Autowired
    private PublicationService publicationService;

    private static final Theme DEFAULT_THEME = Theme.idStub(1);

    @GetMapping
    public List<Author> authorsByKeyWords(
            @RequestParam("ids") List<Integer> ids) {
        List<KeyWord> keyWords = ids.stream().map(KeyWord::idStub).collect(Collectors.toList());
        return  publicationService.findByThemeAndKeyWords(DEFAULT_THEME, keyWords).stream()
                .flatMap(p -> p.getAuthors().stream())
                .distinct()
                .collect(Collectors.toList());

    }

}
