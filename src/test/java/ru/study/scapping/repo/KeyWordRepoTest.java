package ru.study.scapping.repo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.study.scapping.model.domain.KeyWord;
import ru.study.scapping.model.domain.Theme;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class KeyWordRepoTest {

    @Autowired
    private KeyWordRepo keyWordRepo;

    @Autowired
    private ThemeRepo themeRepo;

    @Test
    public void whenSave() {
        Theme theme = new Theme("Theme", "Тема");
        themeRepo.save(theme);
        KeyWord keyWord = new KeyWord("name", "tran", theme);
        keyWordRepo.save(keyWord);
        Assert.assertEquals(keyWord, keyWordRepo.findById(keyWord.getId()).get());
    }

    @Test
    public void whenDelete() {
        Theme theme = new Theme("Theme", "Тема");
        themeRepo.save(theme);
        KeyWord keyWord = new KeyWord("name", "tran", theme);
        keyWordRepo.save(keyWord);
        keyWordRepo.deleteById(keyWord.getId());
        Assert.assertEquals(0, keyWordRepo.findAll().size());
    }

    @Test
    public void whenFinAll() {
        Theme theme1 = new Theme("Theme", "Тема");
        Theme theme2 = new Theme("Theme", "Тема");
        themeRepo.save(theme1);
        themeRepo.save(theme2);
        KeyWord keyWord1 = new KeyWord("name", "tran", theme1);
        KeyWord keyWord2 = new KeyWord("name", "tran", theme2);
        keyWordRepo.save(keyWord1);
        keyWordRepo.save(keyWord2);
        Assert.assertEquals(
                List.of(keyWord1, keyWord2),
                keyWordRepo.findAll()
        );
    }

    @Test
    public void whenUpdate() {
        Theme theme1 = new Theme("t1", "t1");
        Theme theme2 = new Theme("t2", "t2");
        themeRepo.save(theme1);
        themeRepo.save(theme2);
        KeyWord keyWord = new KeyWord("kw1", "kw2", theme1);
        keyWordRepo.save(keyWord);
        keyWord.setName("kw2");
        keyWord.setTranslation("kw2");
        keyWord.setTheme(theme2);
        Assert.assertEquals(keyWord, keyWordRepo.findById(keyWord.getId()).get());
    }

    @Test
    public void whenFindById() {
        Theme theme1 = new Theme("Theme", "Тема");
        Theme theme2 = new Theme("Theme", "Тема");
        Theme theme3 = new Theme("Theme", "Тема");
        themeRepo.save(theme1);
        themeRepo.save(theme2);
        themeRepo.save(theme3);
        KeyWord keyWord1 = new KeyWord("name", "tran", theme1);
        KeyWord keyWord2 = new KeyWord("name", "tran", theme1);
        KeyWord keyWord3 = new KeyWord("name", "tran", theme2);
        keyWordRepo.save(keyWord1);
        keyWordRepo.save(keyWord2);
        keyWordRepo.save(keyWord3);
        Assert.assertEquals(
                List.of(keyWord1,keyWord2),
                keyWordRepo.findAllByTheme(theme1)
        );
    }

}