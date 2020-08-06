package ru.study.scapping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.study.scapping.model.domain.KeyWord;
import ru.study.scapping.model.domain.Theme;
import ru.study.scapping.model.domain.WebPage;
import ru.study.scapping.model.dto.WebPageDTO;
import ru.study.scapping.repo.KeyWordRepo;
import ru.study.scapping.repo.ThemeRepo;
import ru.study.scapping.repo.WebPageRepo;
import ru.study.scapping.service.translation.TranslationService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WebPageServiceImpl implements WebPageService {

    @Autowired
    private ThemeRepo themeRepo;

    @Autowired
    private KeyWordRepo keyWordRepo;

    @Autowired
    private WebPageRepo webPageRepo;

    @Override
    public WebPage save(WebPageDTO webPageDTO) {
        return webPageRepo.save(fromDTO(webPageDTO));
    }

    private WebPage fromDTO(WebPageDTO webPageDTO) {
        WebPage webPage = new WebPage();

        webPage.setLink(webPageDTO.getLink());
        webPage.setTitle(webPageDTO.getTitle());
        webPage.setLanguage(webPageDTO.getLanguage());
        webPage.setSite(webPageDTO.getSite());
        webPage.setSnippet(webPageDTO.getSnippet());

        Theme theme = saveTheme(webPageDTO);
        webPage.setTheme(theme);
        webPage.setKeyWord(saveKeyWord(webPageDTO, theme));

        return webPage;
    }

    private Theme saveTheme(WebPageDTO webPageDTO) {
        if ("#".equals(webPageDTO.getTheme())) {
            return themeRepo.findAll().get(0);
        } else {
            Theme theme = new Theme(webPageDTO.getTheme(), webPageDTO.getThemeTranslation());
            themeRepo.save(theme);
            return theme;
        }
    }

    private KeyWord saveKeyWord(WebPageDTO webPageDTO, Theme theme) {
        Optional<KeyWord> okw = keyWordRepo.findByName(webPageDTO.getKeyWord());
        KeyWord keyWord;
        if (okw.isPresent()) {
            keyWord = okw.get();
        } else {
            keyWord = new KeyWord(webPageDTO.getKeyWord(), webPageDTO.getKeyWordTranslation(), theme);
            keyWord = keyWordRepo.save(keyWord);
        }
        return keyWord;
    }

    @Override
    public List<WebPage> saveAll(List<WebPageDTO> webPageDTOS) {
        return webPageDTOS.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Override
    public List<WebPage> findAllByThemeAndKeyWord(Theme theme, KeyWord keyWord) {
        return webPageRepo.findByThemeAndKeyWord(theme, keyWord);
    }

    @Override
    public List<WebPage> findAllByThemeAndKeyWords(Theme theme, List<KeyWord> keyWords) {
        return keyWords.stream()
                .flatMap(kw -> findAllByThemeAndKeyWord(theme, kw).stream())
                .collect(Collectors.toList());
    }
}
