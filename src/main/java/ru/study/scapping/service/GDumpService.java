package ru.study.scapping.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.study.scapping.model.dto.WebPageDTO;
import ru.study.scapping.service.translation.TranslationService;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GDumpService implements DumpService<WebPageDTO> {

    @Autowired
    private TranslationService translationService;

    public static final String ROOT_PATH = "./json-out";

    @Override
    public List<WebPageDTO> fromFile(File file) {
        List<WebPageDTO> webPageDTOS;
        if (file.isDirectory()) {
            webPageDTOS = Arrays.stream(Objects.requireNonNull(file.listFiles()))
                    .filter(f -> !f.isDirectory())
                    .flatMap(f -> parseFile(f).stream())
                    .collect(Collectors.toList());
        } else {
            webPageDTOS = parseFile(file);
        }
        return webPageDTOS;
    }

    private List<WebPageDTO> parseFile(File file) {
        List<WebPageDTO> data = new ArrayList<>();
        String name = file.getName();
        System.out.println("Analyze: " + file.getAbsolutePath());
        int index = lastIndexOf(name, '-', 2);
        String lang = name.substring(index + 1, index + 3);
        String keyword = name.substring(0, index);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(file);
            JsonNode itemsNode = root.get("items");
            if (itemsNode != null) {
                Iterator<JsonNode> iterator = itemsNode.elements();
                while (iterator.hasNext()) {
                    WebPageDTO currentDTO = new WebPageDTO();
                    if ("ru".equals(lang)) {
                        currentDTO.setKeyWord(translationService.translation(keyword));
                        currentDTO.setKeyWordTranslation(keyword);
                    } else {
                        currentDTO.setKeyWord(keyword);
                        currentDTO.setKeyWordTranslation(translationService.translation(keyword));
                    }
                    currentDTO.setLanguage(lang);
                    JsonNode current = iterator.next();
                    currentDTO.setTitle(current.get("title").asText());
                    String link = current.get("link").asText();
                    currentDTO.setLink(URLDecoder.decode(link, StandardCharsets.UTF_8));
                    currentDTO.setSnippet(current.get("snippet").asText());
                    currentDTO.setSite(current.get("displayLink").asText());
                    data.add(currentDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private static int lastIndexOf(String line, char c, int count) {
        int index = -1;
        int number = 1;
        for (int i = line.length() - 1; i >= 0; i--) {
            if (line.charAt(i) == c) {
                if (number == count) {
                    index = i;
                    break;
                }
                number++;
            }
        }
        return index;
    }

}
