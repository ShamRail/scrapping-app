package ru.study.scapping.service.grabber;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import ru.study.scapping.model.dto.PublicationDTO;
import ru.study.scapping.service.ScrappingService;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class GoogleScholarService implements ScrappingService<PublicationDTO> {

    public static final int KEY_PAIR_SEPARATOR = 15;

    public static final int SCHOLAR_PREFIX_END = 26;

    private static final Pattern DOUBLE_LINK = Pattern.compile("http.+http.+");

    private static final String SCRIPT_PATH = Path.of("./", "scripts", "scholar.py")
            .toAbsolutePath()
            .normalize()
            .toString();


    public GoogleScholarService() {
        prepareDirs();
    }

    private void prepareDirs() {
        System.out.println("Creating out dir ...");
        File outDir = new File("./script-out");
        System.out.println("Result: " + outDir.mkdir());
    }

    private String parseCmd(String keyWord) {
        String path = "";
        try {
            File outFile = createFile(keyWord);
            String cmdArguments = String.format("-c 10 --all \"%s\"", keyWord);
            String scriptCommand = String.format("py %s %s", SCRIPT_PATH, cmdArguments);
            System.out.println("Script command: " + scriptCommand);
            System.out.println("Executing script ...");

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(scriptCommand);
            process.waitFor(3, TimeUnit.SECONDS);
            System.out.println("Script executed");

            System.out.println("Writing result ...");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("Windows-1251")))) {
                List<String> rows = br.lines().collect(Collectors.toList());
                if (!rows.isEmpty()) {
                    Path target = outFile.toPath();
                    path = target.normalize().toAbsolutePath().toString();
                    Files.write(target, rows);
                }
            }
            System.out.println("Result wrote at " + path);

            Thread.sleep(60 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    private File createFile(String keyWord) throws IOException {
        System.out.println("Creating out file ...");
        File outFile = new File(String.format("./script-out/%s.txt", keyWord));
        System.out.println("Result: " + outFile.createNewFile());
        return outFile;
    }

    private List<PublicationDTO> parseFile(String path) {
        List<PublicationDTO> list = new ArrayList<>();
        List<String> versionsLinks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {
            PublicationDTO current = new PublicationDTO();
            boolean hasContent = true;
            while (hasContent) {
                String row;
                Map<String, String> data = new HashMap<>();
                while ((row = br.readLine()) != null && !row.isEmpty()) {
                    String[] pair = parsePair(row);
                    data.put(pair[0], pair[1]);
                }
                hasContent = !data.isEmpty();
                if (hasContent) {
                    current.setTitle(data.get("Title"));
                    String link = data.get("URL");
                    if (DOUBLE_LINK.matcher(link).matches()) {
                        link = link.substring(SCHOLAR_PREFIX_END);
                    }
                    current.setLink(link);
                    current.setYear(Integer.parseInt(data.get("Year")));
                    versionsLinks.add(data.get("Versions list"));
                    current.setSnippet(data.get("Excerpt"));
                    list.add(current);
                    current = new PublicationDTO();
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getCause());
        }
//        if (!versionsLinks.isEmpty()) {
//            IntStream.range(0, list.size()).forEach(i -> {
//                parseDetail(list.get(i), versionsLinks.get(i));
//            });
//        }
        return list;
    }

    private String[] parsePair(String row) {
        String key = row.substring(0, KEY_PAIR_SEPARATOR).trim();
        String value = row.substring(KEY_PAIR_SEPARATOR);
        return new String[] {key, value};
    }

    private void parseDetail(PublicationDTO current, String link) {
        try {
            if (link != null) {
                System.out.println("Parse link: " + link);
                int seconds = 1000 * (60 + new Random().nextInt(60));
                System.out.printf("Wait: %.2f", (seconds / 60.0));
                Thread.sleep(seconds);
                Document document = Jsoup.connect(link).get();
                String text = document.getElementsByClass("gs_a").text();

                int last = lastIndexOf(text, '-', 1);
                int prev = lastIndexOf(text, '-', 2);

                if (last != -1 && prev != -1) {
                    String authours = text.substring(0, prev);
                    String journal = text.substring(prev + 1, last);
                    current.setAuthors(parseAuthors(authours));
                    current.setJournal(parseJournal(journal));
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private List<String> parseAuthors(String text) {
        return Arrays.stream(
                text.trim()
                        .replace("…", "")
                        .split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private String parseJournal(String text) {
        String journal = text.trim();
        int separator = journal.indexOf(',');
        if (separator != -1) {
            journal = journal.substring(0, separator);
        }
        return journal;
    }

    @Override
    public List<PublicationDTO> parse(String keyWord) {
        String path = String.format("./script-out/%s.txt", keyWord);
        return parseFile(path);
    }

    private static int lastIndexOf(String string, char c, int count) {
        int index = -1;
        int number = 1;
        for (int i = string.length() - 1; i >= 0; i--) {
            if (string.charAt(i) == c) {
                if (count == number) {
                    index = i;
                    break;
                }
                number++;
            }
        }
        return index;
    }

    public static void main(String[] args) throws Exception {
        //parseKeyWords();
        saveToJson();
    }

    private static void saveToJson() throws IOException {
        GoogleScholarService gss = new GoogleScholarService();
        List<String> analyzed = Files.readAllLines(Path.of("./log/log-to-save.txt"));
        List<String> keyWords = Files.readAllLines(Path.of("./data/en-keywords.txt"));
        keyWords.removeAll(analyzed);
        ObjectMapper objectMapper = new ObjectMapper();
        for (String key : keyWords) {
            long start = System.currentTimeMillis();
            System.out.println("Parse key word: " + key);
            String path = String.format("./script-out/%s.txt", key);
            List<PublicationDTO> result = gss.parseFile(path);
            Files.write(Path.of("./log/log-to-save.txt"), (System.lineSeparator() + key).getBytes(), StandardOpenOption.APPEND);
            Files.write(Path.of(String.format("./json-out/gs/%s.json", key)),
                    objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result).getBytes()
            );
            long end = System.currentTimeMillis();
            double time = (end - start) / 1000.0;
            System.out.println();
            System.out.printf("Time: %.2f", (time / 60.0));
        }
    }

    private static void parseKeyWords() throws IOException {
        GoogleScholarService gss = new GoogleScholarService();
        List<String> analyzed = Files.readAllLines(Path.of("./log/log.txt"));
        List<String> keyWords = Files.readAllLines(Path.of("./data/en-keywords.txt"));
        keyWords.removeAll(analyzed);
        List<String> saved = keyWords.stream()
                .filter(s -> !gss.parseCmd(s).equals(""))
                .collect(Collectors.toList());
        Files.write(Path.of("./log/log.txt"), saved, StandardOpenOption.APPEND);
    }


}
