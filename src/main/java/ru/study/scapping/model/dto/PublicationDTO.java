package ru.study.scapping.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode(of = {"title", "link", "journal"})
public class PublicationDTO implements ScrappingModel {

    private String title;

    private String link = "#";

    private String snippet;

    private Integer year;

    private String journal = "Unknown";

    private String theme = "#";

    private String themeTranslation = "#";

    private String keyWord = "#";

    private String keyWordTranslation = "#";

    private List<String> authors = new LinkedList<>();

    @Override
    public String toString() {
        return String.format(
                        "%10s: %s%n" +
                        "%10s: %s%n" +
                        "%10s: %s%n" +
                        "%10s: %s%n" +
                        "%10s: %s%n" +
                                "%10s: %s%n" +
                        "%10s: %s%n",
                "Title", title,
                "Link", link,
                "Snippet", snippet,
                "Year", year,
                "Journal", journal,
                "Authors", authors,
                "Theme", theme
        );
    }
}
