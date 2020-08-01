package ru.study.scapping.service;

import ru.study.scapping.model.dto.ScrappingModel;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ScrappingService<T extends ScrappingModel> {

    List<T> parse(String keyWord);

    default Map<String, List<T>> parse(List<String> keyWords) {
        return keyWords.stream()
                .collect(Collectors.toMap(
                        Function.identity(), this::parse,
                        (left, right) -> {
                            left.addAll(right);
                            return left;
                        }
                ));
    }

}
