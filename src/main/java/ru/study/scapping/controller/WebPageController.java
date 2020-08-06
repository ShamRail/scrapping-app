package ru.study.scapping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.study.scapping.model.domain.KeyWord;
import ru.study.scapping.model.domain.Theme;
import ru.study.scapping.model.domain.WebPage;
import ru.study.scapping.model.dto.WPRequestDTO;
import ru.study.scapping.service.KeyWordService;
import ru.study.scapping.service.ThemeService;
import ru.study.scapping.service.WebPageService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("app/pages")
public class WebPageController {

    @Autowired
    private KeyWordService keyWordService;

    @Autowired
    private ThemeService themeService;

    @Autowired
    private WebPageService webPageService;

    @GetMapping
    public String webPagesPage(
            Model model,
            @RequestParam(value = "theme", required = false) Integer themeId,
            @RequestParam(value = "ids", required = false) List<Integer> ids) {
        model.addAttribute("requestDTO", new WPRequestDTO());
        model.addAttribute("theme", themeService.findFirst());
        model.addAttribute("keyWords", keyWordService.findAll());
        if (themeId == null || (ids == null || ids.isEmpty())) {
            model.addAttribute("webPages", List.of());
        } else {
            List<KeyWord> keyWords = ids.stream().map(KeyWord::idStub).collect(Collectors.toList());
            List<WebPage> webPages = webPageService.findAllByThemeAndKeyWords(Theme.idStub(themeId), keyWords);
            List<KeyWord> searchingKeyWords = webPages.stream()
                    .map(WebPage::getKeyWord)
                    .distinct()
                    .collect(Collectors.toList());;
            model.addAttribute("searchingKeyWords", searchingKeyWords);
            model.addAttribute("webPages", webPages);
        }
        return "pages";
    }

    @PostMapping
    public String filter(@ModelAttribute("requestDTO") WPRequestDTO wpRequestDTO) {
        Integer themeId = wpRequestDTO.getThemeId();
        String ids = wpRequestDTO.getKeyWordsIDs().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return "redirect:/app/pages" + String.format("?theme=%d&ids=%s", themeId, ids);
    }

}
