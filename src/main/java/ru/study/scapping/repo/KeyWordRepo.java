package ru.study.scapping.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.study.scapping.model.domain.KeyWord;
import ru.study.scapping.model.domain.Theme;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeyWordRepo extends JpaRepository<KeyWord, Integer> {

    List<KeyWord> findAllByTheme(Theme theme);

    Optional<KeyWord> findByName(String name);

}
