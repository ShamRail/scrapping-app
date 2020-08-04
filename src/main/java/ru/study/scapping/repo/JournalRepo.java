package ru.study.scapping.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.study.scapping.model.domain.Journal;

import java.util.Optional;

@Repository
public interface JournalRepo extends JpaRepository<Journal, Integer> {

    Optional<Journal> findByName(String name);

}
