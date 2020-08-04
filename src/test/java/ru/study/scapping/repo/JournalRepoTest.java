package ru.study.scapping.repo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.study.scapping.model.domain.Journal;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JournalRepoTest {

    @Autowired
    private JournalRepo journalRepo;

    @Test
    public void whenSave() {
        Journal journal =  new Journal("The Times");
        journalRepo.save(journal);
        Assert.assertEquals(journalRepo.findAll().get(0), journal);
    }

    @Test
    public void whenFindById() {
        Journal journal = new Journal("The Times");
        journalRepo.save(journal);
        Assert.assertEquals(journalRepo.findById(journal.getId()).get(), journal);
    }

    @Test
    public void whenUpdate() {
        Journal journal = new Journal("The Times");
        journalRepo.save(journal);
        journal.setName("IEEE");
        journalRepo.save(journal);
        Assert.assertEquals(journalRepo.findById(journal.getId()).get(), journal);
    }

    @Test
    public void whenDelete() {
        Journal journal = new Journal("The Times");
        journalRepo.save(journal);
        journalRepo.deleteById(journal.getId());
        Assert.assertEquals(0, journalRepo.findAll().size());
    }

    @Test
    public void whenFindAll() {
        Journal journal1 = new Journal("The Times");
        Journal journal2 = new Journal("IEEE");
        journalRepo.save(journal1);
        journalRepo.save(journal2);
        Assert.assertEquals(List.of(journal1, journal2), journalRepo.findAll());
    }

    @Test
    public void whenFindByName() {
        Journal journal1 = new Journal("The Times");
        Journal journal2 = new Journal("IEEE");
        journalRepo.save(journal1);
        journalRepo.save(journal2);
        Assert.assertEquals(journal1, journalRepo.findByName("The Times").get());
    }

}