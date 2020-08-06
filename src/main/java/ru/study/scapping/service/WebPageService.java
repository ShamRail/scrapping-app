package ru.study.scapping.service;

import ru.study.scapping.model.domain.KeyWord;
import ru.study.scapping.model.domain.Theme;
import ru.study.scapping.model.domain.WebPage;
import ru.study.scapping.model.dto.WebPageDTO;

import java.util.List;

public interface WebPageService {

    WebPage save(WebPageDTO webPageDTO);

    List<WebPage> saveAll(List<WebPageDTO> webPageDTOS);

    List<WebPage> findAllByThemeAndKeyWord(Theme theme, KeyWord keyWord);

    List<WebPage> findAllByThemeAndKeyWords(Theme theme, List<KeyWord> keyWords);

}
