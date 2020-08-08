package ru.study.scapping.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.study.scapping.model.domain.KeyWord;
import ru.study.scapping.model.domain.Publication;
import ru.study.scapping.model.domain.Theme;

import java.util.List;

@Repository
public interface PublicationRepo extends JpaRepository<Publication, Integer> {

    @Query("from Publication p " +
            "join fetch p.journal " +
            "join fetch p.authors " +
            "join p.keyWord " +
            "join fetch p.theme " +
            "where p.theme = :theme")
    List<Publication> findWithAllByTheme(@Param("theme") Theme theme);

    @Query("from Publication p " +
            "join fetch p.journal " +
            "join fetch p.authors " +
            "join p.keyWord " +
            "join fetch p.theme " +
            "where p.theme = :theme and p.keyWord = :kw")
    List<Publication> findWithAllByThemeAndKeyWord(@Param("theme") Theme theme, @Param("kw") KeyWord keyWord);

    @Query(value = "select distinct p.key_word_id from publication p", nativeQuery = true)
    List<Object[]> keyWords();

}
