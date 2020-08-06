package ru.study.scapping.service;

import java.io.File;
import java.util.List;

public interface DumpService<T> {

    List<T> fromFile(File file);

}
