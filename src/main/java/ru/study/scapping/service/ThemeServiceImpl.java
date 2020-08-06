package ru.study.scapping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.study.scapping.model.domain.Theme;
import ru.study.scapping.repo.ThemeRepo;

import java.util.List;

@Service
public class ThemeServiceImpl implements ThemeService {

    @Autowired
    private ThemeRepo themeRepo;

    @Override
    public Theme findFirst() {
        List<Theme> all = themeRepo.findAll();
        return all.isEmpty() ? new Theme() : all.get(0);
    }
}
