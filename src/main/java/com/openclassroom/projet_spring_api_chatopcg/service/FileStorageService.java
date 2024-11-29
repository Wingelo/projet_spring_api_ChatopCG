package com.openclassroom.projet_spring_api_chatopcg.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;


@Service
public class FileStorageService implements FileStorageServiceImpl {

    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!", e);
        }
    }

    @Override
    public String save(MultipartFile file) {
        try {

            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            String newFileName = UUID.randomUUID() + "_" + originalFilename;
            Path destinationFile = this.root.resolve(newFileName);
            Files.copy(file.getInputStream(), destinationFile);
            return newFileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage(), e);
        }
    }

    @Override
    public Resource loadAsResource(String fileName) throws MalformedURLException {
        Path file = load(fileName);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read the file: " + fileName);
        }
    }

    @Override
    public Path load(String filename) {
        return root.resolve(filename);
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    @Override
    public void deleteAll() {
        // You can implement a method to delete all files if needed.
    }
}
