package ru.study.scapping.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class PublicationRequestDTO {

    private Integer themeID;

    private List<Integer> kwIDs;

    private List<Integer> athIDs;

}
