package ru.study.scapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.study.scapping.model.dto.WebPageDTO;
import ru.study.scapping.service.DumpService;
import ru.study.scapping.service.GDumpService;
import ru.study.scapping.service.WebPageService;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//@SpringBootApplication
public class DumpApp implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) {
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

    public static void main(String[] args) {
        SpringApplication.run(DumpApp.class, args);
    }

}
