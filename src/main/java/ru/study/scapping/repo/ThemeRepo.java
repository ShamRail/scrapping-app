package ru.study.scapping.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.study.scapping.model.domain.Theme;

import java.util.Optional;

@Repository
public interface ThemeRepo extends JpaRepository<Theme, Integer> {

    Optional<Theme> findByName(String name);

}
