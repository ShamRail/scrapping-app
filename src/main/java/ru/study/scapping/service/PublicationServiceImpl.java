package ru.study.scapping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.study.scapping.model.domain.*;
import ru.study.scapping.model.dto.PublicationDTO;
import ru.study.scapping.repo.*;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PublicationServiceImpl implements PublicationService {

    @Autowired
    private PublicationRepo publicationRepo;

    @Autowired
    private ThemeRepo themeRepo;

    @Autowired
    private JournalRepo journalRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @Autowired
    private KeyWordRepo keyWordRepo;

    @PostConstruct
    public void initTheme() {
        if (themeRepo.findAll().isEmpty()) {
            Theme theme = new Theme(
                    "The functioning of cyber-physical systems and their development",
                    "Функционирование киберфизических систем и их разработка"
            );
            themeRepo.save(theme);
        }
    }

    @Override
    public Publication save(PublicationDTO publicationDTO) {
        return publicationRepo.save(fromDTO(publicationDTO));
    }

    private Publication fromDTO(PublicationDTO publicationDTO) {

        Publication publication = new Publication();
        publication.setLink(publicationDTO.getLink());
        publication.setSnippet(publicationDTO.getSnippet());
        publication.setYear(publicationDTO.getYear());
        publication.setTitle(publicationDTO.getTitle());

        publication.setTheme(saveTheme(publicationDTO));
        publication.setJournal(saveJournal(publicationDTO));
        publication.setAuthors(saveAuthors(publicationDTO));
        publication.setKeyWord(saveKeyWord(publicationDTO, publication.getTheme()));

        return publication;
    }

    private Theme saveTheme(PublicationDTO publicationDTO) {
        if ("#".equals(publicationDTO.getTheme())) {
            return themeRepo.findAll().get(0);
        } else {
            Theme theme = new Theme(publicationDTO.getTheme(), publicationDTO.getThemeTranslation());
            themeRepo.save(theme);
            return theme;
        }
    }

    private Journal saveJournal(PublicationDTO publicationDTO) {
        Journal journal = new Journal();
        if (!"Unknown".equals(publicationDTO.getJournal())) {
            Optional<Journal> oj = journalRepo.findByName(publicationDTO.getJournal());
            if (oj.isPresent()) {
                journal = oj.get();
            } else {
                journal = new Journal(publicationDTO.getJournal());
                journalRepo.save(journal);
            }
        }
        return journal;
    }

    private KeyWord saveKeyWord(PublicationDTO publicationDTO, Theme theme) {
        Optional<KeyWord> okw = keyWordRepo.findByName(publicationDTO.getKeyWord());
        KeyWord keyWord;
        if (okw.isPresent()) {
            keyWord = okw.get();
        } else {
            keyWord = new KeyWord(publicationDTO.getKeyWord(), publicationDTO.getKeyWordTranslation(), theme);
            keyWord = keyWordRepo.save(keyWord);
        }
        return keyWord;
    }

    private List<Author> saveAuthors(PublicationDTO publicationDTO) {
        List<Author> result = new ArrayList<>();
        List<Author> dbAuthors = authorRepo.findByNameIn(publicationDTO.getAuthors());
        List<Author> notSaved = new ArrayList<>();
        for (String authorName : publicationDTO.getAuthors()) {
            boolean contains = false;
            for (Author author : dbAuthors) {
                if (Objects.equals(author.getName(), authorName)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                Author author = new Author(authorName);
                authorRepo.save(author);
                notSaved.add(author);
            }
        }
        result.addAll(dbAuthors);
        result.addAll(notSaved);
        return result;
    }

    @Override
    public List<Publication> saveAll(List<PublicationDTO> publicationDTOS) {
        return publicationDTOS.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Override
    public List<Publication> findByThemeAndKeyWord(Theme theme, KeyWord keyWord) {
        return publicationRepo.findWithAllByThemeAndKeyWord(theme, keyWord);
    }

    @Override
    public List<Publication> findByThemeAndKeyWords(Theme theme, List<KeyWord> keyWords) {
        return keyWords.stream()
                .flatMap(kw -> findByThemeAndKeyWord(theme, kw).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<KeyWord> findAvailableKeyWords() {
        List<Integer> ids = publicationRepo.keyWords().stream()
                .flatMap(Arrays::stream)
                .map(el -> (Integer) el)
                .collect(Collectors.toList());
        return keyWordRepo.findByIdIn(ids);
    }

    @Override
    public List<Publication> findByThemeAndKeyWordAndAuthors(Theme theme, KeyWord keyWord, List<Author> authorsID) {
        List<Publication> plsByTheme = findByThemeAndKeyWord(theme, keyWord);
        List<Publication> result = new ArrayList<>();
        Set<Integer> ids = authorsID.stream().map(Author::getId).collect(Collectors.toSet());
        for (Publication publication : plsByTheme) {
            List<Author> authors = publication.getAuthors();
            for (Author author : authors) {
                if (ids.contains(author.getId())) {
                    result.add(publication);
                }
            }
        }
        return result;
    }

    @Override
    public List<Publication> findByThemeAndKeyWordsAndAuthors(Theme theme, List<KeyWord> keyWords, List<Author> authorsID) {
        return keyWords.stream()
                .flatMap(kw -> findByThemeAndKeyWordAndAuthors(theme, kw, authorsID).stream())
                .collect(Collectors.toList());
    }

}
