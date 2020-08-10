package ru.study.scapping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.study.scapping.model.domain.Author;
import ru.study.scapping.model.domain.KeyWord;
import ru.study.scapping.model.domain.Publication;
import ru.study.scapping.model.domain.Theme;
import ru.study.scapping.model.dto.PublicationRequestDTO;
import ru.study.scapping.service.KeyWordService;
import ru.study.scapping.service.PublicationService;
import ru.study.scapping.service.ThemeService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("app/publications")
public class PublicationController {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private KeyWordService keyWordService;

    private Theme defaultTheme;

    private List<KeyWord> defaultKeyWords;

    @PostConstruct
    private void init() {
        defaultTheme = themeService.findFirst();
        defaultKeyWords = publicationService.findAvailableKeyWords();
    }

    @GetMapping
    public String publicationPage(
            @RequestParam(required = false, name = "themeID") Integer themeId,
            @RequestParam(required = false, name = "kwIDs") List<Integer> kwIDs,
            @RequestParam(required = false, name = "athIDs") List<Integer> athIDs,
            Model model) {

        // на страницу всегда грузим тему и ключевые слова
        // по умолчанию добавляем пустой список публикаций

        model.addAttribute("requestDTO", new PublicationRequestDTO());
        model.addAttribute("theme", defaultTheme);
        model.addAttribute("keyWords", defaultKeyWords);
        model.addAttribute("publications", List.of());

        // если запросили тему и ключевые слова, то
        // проверяем
        // -- если указали одно id автора и оно равно 0, то грузим по теме и ключевым словам
        // -- иначе грузим по авторам

        if (themeId != null && kwIDs != null && athIDs != null) {
            Theme theme = Theme.idStub(themeId);
            List<KeyWord> keyWords = kwIDs.stream().map(KeyWord::idStub).collect(Collectors.toList());
            List<Publication> publications;
            if (athIDs.size() == 1 && athIDs.get(0) == 0) {
                publications = publicationService.findByThemeAndKeyWords(theme, keyWords);
            } else {
                List<Author> searchingAuthors = athIDs.stream().map(Author::idStub).collect(Collectors.toList());
                publications = publicationService.findByThemeAndKeyWordsAndAuthors(theme, keyWords, searchingAuthors);
            }
            model.addAttribute("publications", publications);
            model.addAttribute("searchingKeyWords", keyWordService.findAllByIds(kwIDs));
            model.addAttribute("searchingAuthors",
                    publications.stream()
                            .flatMap(p -> p.getAuthors().stream())
                            .filter(a -> athIDs.contains(a.getId()) || athIDs.get(0) == 0)
                            .distinct()
                            .collect(Collectors.toList())
            );
        }

        return "publication";
    }

    @PostMapping
    public String handlePost(@ModelAttribute("requestDTO") PublicationRequestDTO requestDTO) {
        String themeID = String.valueOf(requestDTO.getThemeID());
        String kwIDs = requestDTO.getKwIDs().stream().map(String::valueOf).collect(Collectors.joining(","));
        String athIDs = requestDTO.getAthIDs().stream().map(String::valueOf).collect(Collectors.joining(","));
        String url = String.format("/app/publications?themeID=%s&kwIDs=%s&athIDs=%s", themeID, kwIDs, athIDs);
        return "redirect:" + url;
    }



}
