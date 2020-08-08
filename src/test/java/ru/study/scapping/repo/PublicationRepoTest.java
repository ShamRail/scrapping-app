package ru.study.scapping.repo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.study.scapping.model.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PublicationRepoTest {

    @Autowired
    private PublicationRepo publicationRepo;

    @Autowired
    private JournalRepo journalRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @Autowired
    private KeyWordRepo keyWordRepo;

    @Autowired
    private ThemeRepo themeRepo;

    @Test
    public void whenSave() {
        Publication publication = new Publication();
        publication.setTitle("title");
        publication.setLink("link");
        publicationRepo.save(publication);
        Assert.assertEquals(publication, publicationRepo.findAll().get(0));
    }

    @Test
    public void whenFindById() {
        Publication publication = new Publication();
        publication.setTitle("title");
        publication.setLink("link");
        publicationRepo.save(publication);
        Assert.assertEquals(publication, publicationRepo.findById(publication.getId()).get());
    }

    @Test
    public void whenDelete() {
        Publication publication = new Publication();
        publication.setTitle("title");
        publication.setLink("link");
        publicationRepo.save(publication);
        publicationRepo.delete(publication);
        Assert.assertEquals(0, publicationRepo.findAll().size());
    }

    @Test
    public void whenUpdate() {
        Publication publication = new Publication();
        publication.setTitle("title");
        publication.setLink("link");
        publicationRepo.save(publication);
        publication.setTitle("title2");
        publication.setLink("link2");
        publicationRepo.save(publication);
        Assert.assertEquals(publication, publicationRepo.findAll().get(0));
    }

    @Test
    public void whenSaveAndGetWithJournalAndAuthours() {
        Theme theme = new Theme("t", "t");
        themeRepo.save(theme);
        KeyWord keyWord = new KeyWord("kw", "kw", theme);
        keyWordRepo.save(keyWord);
        Author author = new Author("a1");
        Author author2 = new Author("a2");
        authorRepo.save(author);
        authorRepo.save(author2);
        Journal journal = new Journal("j");
        journalRepo.save(journal);
        Publication publication = new Publication();
        publication.setTitle("title");
        publication.setLink("link");
        publication.setKeyWord(keyWord);
        publication.setJournal(journal);
        publication.getAuthors().add(author);
        publication.getAuthors().add(author2);
        publication.setTheme(theme);
        publicationRepo.save(publication);
        List<Publication> result = publicationRepo.findWithAllByTheme(theme);
        Assert.assertEquals(publication, result.get(0));
        Assert.assertEquals(journal, result.get(0).getJournal());
        Assert.assertEquals(List.of(author, author2), result.get(0).getAuthors());
        Assert.assertEquals(keyWord, result.get(0).getKeyWord());
    }

    @Test
    public void whenSaveAndGetWithJournalAndAuthoursByKeyWord() {
        Theme theme = new Theme("t", "t");
        themeRepo.save(theme);
        KeyWord keyWord = new KeyWord("kw", "kw", theme);
        keyWordRepo.save(keyWord);
        Author author = new Author("a1");
        Author author2 = new Author("a2");
        authorRepo.save(author);
        authorRepo.save(author2);
        Journal journal = new Journal("j");
        journalRepo.save(journal);
        Publication publication = new Publication();
        publication.setTitle("title");
        publication.setLink("link");
        publication.setKeyWord(keyWord);
        publication.setJournal(journal);
        publication.getAuthors().add(author);
        publication.getAuthors().add(author2);
        publication.setTheme(theme);
        publicationRepo.save(publication);
        List<Publication> result = publicationRepo.findWithAllByThemeAndKeyWord(theme, keyWord);
        Assert.assertEquals(publication, result.get(0));
        Assert.assertEquals(journal, result.get(0).getJournal());
        Assert.assertEquals(List.of(author, author2), result.get(0).getAuthors());
        Assert.assertEquals(keyWord, result.get(0).getKeyWord());
    }

    @Test
    public void whenSaveWithAlreadyAndNotExistAuthor() {
        Theme theme = new Theme("a", "a");
        themeRepo.save(theme);
        Journal journal = new Journal("j");
        journalRepo.save(journal);
        KeyWord keyWord = new KeyWord("kw", "kw");
        keyWordRepo.save(keyWord);
        Author author = new Author("a");
        authorRepo.save(author);
        Publication publication = new Publication();
        Author author1 = new Author("b");
        publication.setTheme(theme);
        publication.setJournal(journal);
        publication.setKeyWord(keyWord);
        publication.getAuthors().add(author);
        publication.getAuthors().add(author1);
        publicationRepo.save(publication);
        Assert.assertEquals(2, authorRepo.findAll().size());
        List<Publication> result = publicationRepo.findWithAllByTheme(theme);
        Assert.assertEquals(List.of(author, author1), result.get(0).getAuthors());
    }

    @Test
    public void test() {
        KeyWord kw1 = new KeyWord("kw1", "kw1");
        KeyWord kw2 = new KeyWord("kw2", "kw2");
        keyWordRepo.save(kw1);
        keyWordRepo.save(kw2);
        Publication publication1 = new Publication();
        publication1.setKeyWord(kw1);
        Publication publication2 = new Publication();
        publication2.setKeyWord(kw2);
        publicationRepo.save(publication1);
        publicationRepo.save(publication2);
        List<Integer> ids = publicationRepo.keyWords().stream()
                .flatMap(Arrays::stream)
                .map(i -> (Integer) i)
                .collect(Collectors.toList());
        Assert.assertEquals(List.of(kw1.getId(), kw2.getId()), ids);
    }
}