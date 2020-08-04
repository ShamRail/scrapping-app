package ru.study.scapping.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.study.scapping.model.domain.Author;

import java.util.List;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Integer> {

    List<Author> findByNameIn(List<String> names);

}
