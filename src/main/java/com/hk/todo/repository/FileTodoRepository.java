package com.hk.todo.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hk.todo.model.Todo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTodoRepository implements TodoRepository {

    private final Path filePath;
    private final ObjectMapper mapper;

    public FileTodoRepository(Path filePath) {
        this.filePath = filePath;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<Todo> loadAll() {
        try {
            if (!Files.exists(filePath)) return new ArrayList<>();
            byte[] bytes = Files.readAllBytes(filePath);
            if (bytes.length == 0) return new ArrayList<>();
            Todo[] items = mapper.readValue(bytes, Todo[].class);
            return new ArrayList<>(Arrays.asList(items));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load todos from " + filePath, e);
        }
    }

    @Override
    public void saveAll(List<Todo> todos) {
        try {
            if (filePath.getParent() != null) Files.createDirectories(filePath.getParent());
            mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), todos);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save todos to " + filePath, e);
        }
    }
}
