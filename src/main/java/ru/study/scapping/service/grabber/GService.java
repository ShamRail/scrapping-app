package ru.study.scapping.service.grabber;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.study.scapping.model.dto.PublicationDTO;
import ru.study.scapping.model.dto.WebPageDTO;
import ru.study.scapping.service.ScrappingService;
import ru.study.scapping.utils.JSONUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GService implements ScrappingService<WebPageDTO> {

    private static final String ENG = "lang_en";

    private static final String RU = "lang_ru";

    private final static HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

    public static final int NEXT_PAGE = 11;

    private String httpRequest(String url) {
        String json = "";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .build();
            HttpResponse<String> response = HTTP_CLIENT.send(
                    request, HttpResponse.BodyHandlers.ofString());
            json = response.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public List<WebPageDTO> parse(String keyWord) {
        List<WebPageDTO> result = new LinkedList<>();
        result.addAll(parseEN(keyWord));
        result.addAll(parseRU(keyWord));
        return result;
    }

    private List<WebPageDTO> parseRU(String keyWord) {
        return parse(keyWord, RU);
    }

    private List<WebPageDTO> parseEN(String keyWord) {
        return parse(keyWord, ENG);
    }

    private List<WebPageDTO> parse(String keyWord, String language) {
        List<WebPageDTO> result = new LinkedList<>();
        try {

            String[] parts = keyWord.split("\\|");
            String enKey = parts[0].trim();
            String ruKey = parts[1].trim();

            File dir = Path.of("./json-out", enKey).toFile();
            if (!dir.exists()) {
                dir.mkdir();
            }

            String key = ENG.equals(language) ? enKey : ruKey;
            String lang = ENG.equals(language) ? "en" : "ru";

            String searchPart1 = httpRequest(QueryBuilder.query(key, key, language));
            String searchPart2 = httpRequest(QueryBuilder.query(key, key, language, NEXT_PAGE));

            if (!checkContentSize(searchPart1) || !checkContentSize(searchPart2)) {
                searchPart1 = httpRequest(QueryBuilder.query(key, language));
                searchPart2 = httpRequest(QueryBuilder.query(key, language, NEXT_PAGE));
            }

            String target1 = "./json-out/" + enKey + "/" + key + "-" + lang + "-1" + ".json";
            String target2 = "./json-out/" + enKey + "/" + key + "-" + lang + "-2" + ".json";

            Files.write(Path.of(target1), searchPart1.getBytes());
            Files.write(Path.of(target2), searchPart2.getBytes());

            List<WebPageDTO> part1 = parseFile(target1, lang);
            List<WebPageDTO> part2 = parseFile(target2, lang);

            result.addAll(part1);
            result.addAll(part2);

        } catch (IOException e) {
            System.out.println("Error with parsing and saving "+ keyWord);
        }
        return result;
    }

    private List<WebPageDTO> parseFile(String path, String lang) {
        List<WebPageDTO> data = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = Path.of(path).toFile();
            JsonNode root = mapper.readTree(file);
            JsonNode itemsNode = root.get("items");
            if (itemsNode != null) {
                Iterator<JsonNode> iterator = itemsNode.elements();
                while (iterator.hasNext()) {
                    WebPageDTO currentDTO = new WebPageDTO();
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

    private boolean checkContentSize(String content) {
        boolean result = true;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(content);
            JsonNode itemsNode = root.get("items");
            if (itemsNode == null || itemsNode.size() < 10) {
                result = false;
            }
        } catch (IOException e) {
            result = false;
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        GService gss = new GService();
        List<String> analyzed = Files.readAllLines(Path.of("./log/g-log.txt"));
        List<String> keyWords = Files.readAllLines(Path.of("./data/g-keywords.txt"));
        keyWords.removeAll(analyzed);
        for (int count = 0; count < keyWords.size(); count++) {
            long start = System.currentTimeMillis();
            String key = keyWords.get(count);
            System.out.println("Parse key word: " + key);
            List<WebPageDTO> result = gss.parse(key);

            if (!result.isEmpty()) {
                Files.write(Path.of("./log/g-log.txt"), (System.lineSeparator() + key).getBytes(), StandardOpenOption.APPEND);
            }

            long end = System.currentTimeMillis();
            double time = (end - start) / 1000.0;
            System.out.println();
            System.out.printf("%d. Time: %.2f%n", count + 1, (time / 60.0));
        }
    }

    private static final class QueryBuilder {

        private static final String URL = "https://www.googleapis.com/customsearch/v1";

        private static final String API_KEY = "AIzaSyDXYlsWbeLjSskkUf_UG9wP-KkXulNnUGc";

        private static final String CX = "007797256824700561655:3aloqhwpuzu";

        private static final String REQUEST_TEMPLATE = String.format("%s?key=%s&cx=%s", URL, API_KEY, CX);

        private static String query(String q, String lr) {
            return String.format(
                    "%s&q=%s&lr=%s",
                    REQUEST_TEMPLATE,
                    URLEncoder.encode(q, StandardCharsets.UTF_8),
                    lr
            );
        }

        private static String query(String q, String lr, int start) {
            return String.format(
                    "%s&q=%s&lr=%s&start=%d",
                    REQUEST_TEMPLATE,
                    URLEncoder.encode(q, StandardCharsets.UTF_8),
                    lr,
                    start
            );
        }

        private static String query(String q, String exactTerms, String lr) {
            return String.format(
                    "%s&q=%s&exactTerms=%s&lr=%s",
                    REQUEST_TEMPLATE,
                    URLEncoder.encode(q, StandardCharsets.UTF_8),
                    URLEncoder.encode(exactTerms, StandardCharsets.UTF_8),
                    lr
            );
        }

        private static String query(String q, String exactTerms, String lr, int start) {
            return String.format(
                    "%s&q=%s&exactTerms=%s&lr=%s&start=%d",
                    REQUEST_TEMPLATE,
                    URLEncoder.encode(q, StandardCharsets.UTF_8),
                    URLEncoder.encode(exactTerms, StandardCharsets.UTF_8),
                    lr,
                    start
            );
        }


    }

}
