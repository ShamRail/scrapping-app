package ru.study.scapping.repo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.study.scapping.model.domain.Author;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AuthorRepoTest {

    @Autowired
    private AuthorRepo authorRepo;

    @Test
    public void whenSave() {
        Author author = new Author("a");
        authorRepo.save(author);
        Assert.assertEquals(author, authorRepo.findAll().get(0));
    }

    @Test
    public void whenDelete() {
        Author author = new Author("a");
        authorRepo.save(author);
        authorRepo.delete(author);
        Assert.assertEquals(0, authorRepo.findAll().size());
    }

    @Test
    public void whenUpdate() {
        Author author = new Author("a");
        authorRepo.save(author);
        author.setName("b");
        authorRepo.save(author);
        Assert.assertEquals(author, authorRepo.findAll().get(0));
    }

    @Test
    public void whenFindAll() {
        Author author1 = new Author("a1");
        Author author2 = new Author("a2");
        authorRepo.save(author1);
        authorRepo.save(author2);
        Assert.assertEquals(List.of(author1, author2), authorRepo.findAll());
    }

    @Test
    public void whenFindById() {
        Author author = new Author("a");
        authorRepo.save(author);
        Assert.assertEquals(author, authorRepo.findById(author.getId()).get());
    }

    @Test
    public void whenFindByName() {
        Author author1 = new Author("a");
        Author author2 = new Author("b");
        authorRepo.save(author1);
        authorRepo.save(author2);
        Assert.assertEquals(List.of(author1, author2), authorRepo.findByNameIn(List.of("a", "b", "c")));
    }

}