package ru.study.scapping.repo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.study.scapping.model.domain.KeyWord;
import ru.study.scapping.model.domain.Theme;
import ru.study.scapping.model.domain.WebPage;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WebPageRepoTest {

    @Autowired
    private WebPageRepo webPageRepo;

    @Autowired
    private ThemeRepo themeRepo;

    @Autowired
    private KeyWordRepo keyWordRepo;

    @Test
    public void whenSave() {
        WebPage webPage = new WebPage();
        webPage.setLink("link");
        webPageRepo.save(webPage);
        Assert.assertEquals(webPage, webPageRepo.findAll().get(0));
    }

    @Test
    public void whenDelete() {
        WebPage webPage = new WebPage();
        webPage.setLink("link");
        webPageRepo.save(webPage);
        webPageRepo.delete(webPage);
        Assert.assertEquals(0, webPageRepo.findAll().size());
    }

    @Test
    public void whenUpdate() {
        WebPage webPage = new WebPage();
        webPage.setLink("link");
        webPageRepo.save(webPage);
        webPage.setLink("link2");
        webPageRepo.save(webPage);
        Assert.assertEquals(webPage, webPageRepo.findAll().get(0));
    }

    @Test
    public void whenFindById() {
        WebPage webPage = new WebPage();
        webPage.setLink("link");
        webPageRepo.save(webPage);
        Assert.assertEquals(webPage, webPageRepo.findById(webPage.getId()).get());
    }

    @Test
    public void whenFindAll() {
        WebPage webPage = new WebPage();
        webPage.setLink("link");
        WebPage webPage2 = new WebPage();
        webPage.setLink("link2");
        webPageRepo.save(webPage);
        webPageRepo.save(webPage2);
        Assert.assertEquals(List.of(webPage, webPage2), webPageRepo.findAll());
    }

    @Test
    public void whenFindAllByTheme() {
        Theme theme = new Theme("t", "t");
        themeRepo.save(theme);
        KeyWord keyWord = new KeyWord("a", "a", theme);
        keyWordRepo.save(keyWord);
        WebPage webPage = new WebPage();
        webPage.setLink("link");
        webPage.setTheme(theme);
        webPage.setKeyWord(keyWord);
        WebPage webPage2 = new WebPage();
        webPage2.setLink("link2");
        webPage2.setTheme(theme);
        webPage2.setKeyWord(keyWord);
        webPageRepo.save(webPage);
        webPageRepo.save(webPage2);
        List<WebPage> pages = webPageRepo.findByTheme(theme);
        Assert.assertEquals(List.of(webPage, webPage2), pages);
        Assert.assertEquals(keyWord, pages.get(0).getKeyWord());
        Assert.assertEquals(keyWord, pages.get(1).getKeyWord());
    }

    @Test
    public void whenFindAllByThemeAndKeyWord() {
        Theme theme = new Theme("t", "t");
        themeRepo.save(theme);
        KeyWord keyWord = new KeyWord("a", "a", theme);
        keyWordRepo.save(keyWord);
        WebPage webPage = new WebPage();
        webPage.setLink("link");
        webPage.setTheme(theme);
        webPage.setKeyWord(keyWord);
        WebPage webPage2 = new WebPage();
        webPage2.setLink("link2");
        webPage2.setTheme(theme);
        webPage2.setKeyWord(keyWord);
        webPageRepo.save(webPage);
        webPageRepo.save(webPage2);
        List<WebPage> pages = webPageRepo.findByThemeAndKeyWord(theme, keyWord);
        Assert.assertEquals(List.of(webPage, webPage2), pages);
        Assert.assertEquals(keyWord, pages.get(0).getKeyWord());
        Assert.assertEquals(keyWord, pages.get(1).getKeyWord());
    }


}