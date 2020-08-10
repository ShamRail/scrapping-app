package ru.study.scapping.service;

import ru.study.scapping.model.domain.KeyWord;

import java.util.List;

public interface KeyWordService {

    List<KeyWord> findAll();

    List<KeyWord> findAllByIds(List<Integer> ids);

}
