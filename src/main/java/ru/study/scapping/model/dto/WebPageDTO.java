package ru.study.scapping.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class WebPageDTO implements ScrappingModel {

    private String title;

    private String site;

    private String link;

    private String snippet;

    private String language;

    private String theme = "#";

    private String themeTranslation = "#";

    private String keyWord;

    private String keyWordTranslation = "#";

    @Override
    public String toString() {
        return String.format(
                        "%10s: %s%n" +
                        "%10s: %s%n" +
                        "%10s: %s%n" +
                                "%10s: %s%n" +
                                "%10s: %s%n" +
                                "%10s: %s%n",
                "Title", title,
                "Site", site,
                "Link", link,
                "Snippet", snippet,
                "Language", language,
                "Key word", keyWord
        );
    }
}
