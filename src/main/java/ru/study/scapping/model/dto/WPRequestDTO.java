package ru.study.scapping.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WPRequestDTO {

    private Integer themeId;

    private List<Integer> keyWordsIDs = new LinkedList<>();

}
