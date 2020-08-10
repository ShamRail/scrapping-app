package ru.study.scapping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.study.scapping.model.domain.KeyWord;
import ru.study.scapping.repo.KeyWordRepo;

import java.util.List;

@Service
public class KeyWordServiceImpl implements KeyWordService {

    @Autowired
    private KeyWordRepo keyWordRepo;

    @Override
    public List<KeyWord> findAll() {
        return keyWordRepo.findAll();
    }

    @Override
    public List<KeyWord> findAllByIds(List<Integer> ids) {
        return keyWordRepo.findByIdIn(ids);
    }
}
