package ru.study.scapping.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.study.scapping.model.domain.KeyWord;
import ru.study.scapping.model.domain.Theme;
import ru.study.scapping.model.domain.WebPage;

import java.util.List;

@Repository
public interface WebPageRepo extends JpaRepository<WebPage, Integer> {

    List<WebPage> findByTheme(Theme theme);

    List<WebPage> findByThemeAndKeyWord(Theme theme, KeyWord keyWord);

}
