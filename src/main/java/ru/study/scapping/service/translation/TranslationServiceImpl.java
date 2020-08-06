package ru.study.scapping.service.translation;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class TranslationServiceImpl implements TranslationService {

    public static final String PATH = "./data/g-keywords.txt";

    private final Map<String, String> enRu = new HashMap<>();

    private final Map<String, String> ruEn = new HashMap<>();

    @PostConstruct
    private void init() {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(PATH)))) {
            br.lines().filter(l -> !l.isEmpty()).forEach(
                    line -> {
                        String[] parts = line.split("\\|");
                        String en = parts[0].trim();
                        String ru = parts[1].trim();
                        enRu.put(en, ru);
                        ruEn.put(ru, en);
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String translation(String word) {
        return enRu.getOrDefault(word, ruEn.getOrDefault(word, ""));
    }
}
