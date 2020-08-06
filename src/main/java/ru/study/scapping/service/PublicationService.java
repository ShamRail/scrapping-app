package ru.study.scapping.service;

import ru.study.scapping.model.domain.Author;
import ru.study.scapping.model.domain.KeyWord;
import ru.study.scapping.model.domain.Publication;
import ru.study.scapping.model.domain.Theme;
import ru.study.scapping.model.dto.PublicationDTO;

import java.util.List;

public interface PublicationService {

    Publication save(PublicationDTO publicationDTO);

    List<Publication> saveAll(List<PublicationDTO> publicationDTOS);

    List<Publication> findByThemeAndKeyWord(Theme theme, KeyWord keyWord);

    List<Publication> findByThemeAndKeyWordAndAuthors(Theme theme, KeyWord keyWord, List<Author> authorsID);

    List<Publication> findByThemeAndKeyWordsAndAuthors(Theme theme, List<KeyWord> keyWords, List<Author> authorsID);

}
