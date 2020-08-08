package ru.study.scapping.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.study.scapping.model.dto.PublicationDTO;
import ru.study.scapping.service.translation.TranslationService;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class GSDumpService implements DumpService<PublicationDTO> {

    @Autowired
    private TranslationService translationService;

    public static final String ROOT_PATH = "./json-out/gs";

    @Override
    public List<PublicationDTO> fromFile(File file) {
        List<PublicationDTO> list = new LinkedList<>();
        try {
            String keyWord = file.getName().replace(".json", "");
            System.out.println("Analyze: " + keyWord);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(file);
            Iterator<JsonNode> current = root.elements();
            while (current.hasNext()) {
                JsonNode currDTO = current.next();
                PublicationDTO publicationDTO = new PublicationDTO();
                publicationDTO.setTitle(currDTO.get("title").asText());
                publicationDTO.setLink(currDTO.get("link").asText());
                publicationDTO.setSnippet(currDTO.get("snippet").asText());
                publicationDTO.setYear(currDTO.get("year").asInt());
                publicationDTO.setJournal(currDTO.get("journal").asText());
                publicationDTO.setKeyWord(keyWord);
                publicationDTO.setKeyWordTranslation(translationService.translation(keyWord));
                JsonNode authorsNode = currDTO.get("authors");
                Iterator<JsonNode> authorsIt = authorsNode.elements();
                while (authorsIt.hasNext()) {
                    publicationDTO.getAuthors().add(authorsIt.next().asText());
                }
                list.add(publicationDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Processing finished. Found: " + list.size());
        return list;
    }
}
