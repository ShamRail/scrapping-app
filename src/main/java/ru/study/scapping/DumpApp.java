package ru.study.scapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.study.scapping.model.dto.PublicationDTO;
import ru.study.scapping.model.dto.WebPageDTO;
import ru.study.scapping.service.*;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringBootApplication
public class DumpApp implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) {
        dumpArticles();
        dumpPublications();
    }

    private void dumpArticles() {
        WebPageService service = (WebPageService) applicationContext.getBean("webPageServiceImpl");
        DumpService<WebPageDTO> dumpService = applicationContext.getBean(GDumpService.class);
        File file = Path.of(GDumpService.ROOT_PATH).toFile();
        List<WebPageDTO> result = Arrays.stream(Objects.requireNonNull(file.listFiles()))
                .filter(f -> !f.getName().equals("gs"))
                .flatMap(f -> dumpService.fromFile(f).stream())
                .collect(Collectors.toList());
        service.saveAll(result);
        System.out.println("SAVED");
    }

    private void dumpPublications() {
        PublicationService service = (PublicationService) applicationContext.getBean("publicationServiceImpl");
        DumpService<PublicationDTO> dumpService = applicationContext.getBean(GSDumpService.class);
        File root = new File(GSDumpService.ROOT_PATH);
        List<PublicationDTO> result = Arrays.stream(Objects.requireNonNull(root.listFiles()))
                .flatMap(f -> dumpService.fromFile(f).stream())
                .collect(Collectors.toList());
        System.out.println("Found: " + result.size());
        service.saveAll(result);
        System.out.println("SAVED");
    }

    public static void main(String[] args) {
        SpringApplication.run(DumpApp.class, args);
    }

}
