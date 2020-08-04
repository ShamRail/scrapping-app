package ru.study.scapping.repo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.study.scapping.model.domain.Theme;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ThemeRepoTest {

    @Autowired
    private ThemeRepo themeRepo;

    @Test
    public void whenSave() {
        Theme theme = new Theme("cyber physical systems", "кибер-физические системы");
        themeRepo.save(theme);
        Theme out = themeRepo.findById(theme.getId()).get();
        Assert.assertEquals(theme, out);
    }

    @Test
    public void whenFindById() {
        Theme theme1 = new Theme("cyber physical systems", "кибер-физические системы");
        Theme theme2 = new Theme("digital economy", "цифровая экономика");
        themeRepo.save(theme1);
        themeRepo.save(theme2);
        Theme out = themeRepo.findById(theme2.getId()).get();
        Assert.assertEquals(theme2, out);
    }

    @Test
    public void whenFindAll() {
        Theme theme1 = new Theme("cyber physical systems", "кибер-физические системы");
        Theme theme2 = new Theme("digital economy", "цифровая экономика");
        themeRepo.save(theme1);
        themeRepo.save(theme2);
        Assert.assertEquals(List.of(theme1, theme2), themeRepo.findAll());
    }

    @Test
    public void whenUpdate() {
        Theme theme = new Theme("cyber physical systems", "кибер-физические системы");
        themeRepo.save(theme);
        theme.setName("new name");
        themeRepo.save(theme);
        Theme out = themeRepo.findById(theme.getId()).get();
        Assert.assertEquals("new name", out.getName());
        Assert.assertEquals(theme, out);
    }

    @Test
    public void whenDelete() {
        Theme theme = new Theme("cyber physical systems", "кибер-физические системы");
        themeRepo.save(theme);
        themeRepo.deleteById(theme.getId());
        Optional<Theme> out = themeRepo.findById(theme.getId());
        Assert.assertEquals(Optional.empty(), out);
    }

    @Test
    public void whenFindByName() {
        Theme theme = new Theme("t", "t");
        themeRepo.save(theme);
        Assert.assertEquals(theme, themeRepo.findByName("t").get());
    }

}