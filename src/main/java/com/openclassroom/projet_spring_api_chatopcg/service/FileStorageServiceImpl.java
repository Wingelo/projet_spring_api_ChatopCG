package com.openclassroom.projet_spring_api_chatopcg.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageServiceImpl {

    void init();

    String save(MultipartFile file);

    Resource loadAsResource(String fileName) throws MalformedURLException;

    void deleteAll();

    Stream<Path> loadAll();

    Path load(String filename);
}
