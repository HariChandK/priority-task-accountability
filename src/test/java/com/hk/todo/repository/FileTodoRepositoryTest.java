package com.hk.todo.repository;

import com.hk.todo.model.Priority;
import com.hk.todo.model.Todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileTodoRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void saveAll_then_loadAll_roundTrip() {
        Path file = tempDir.resolve("todos.json");
        FileTodoRepository repo = new FileTodoRepository(file);

        LocalDateTime now = LocalDateTime.now();
        Todo t1 = new Todo("Pay rent", "Avoid late fee", 10, Priority.HIGH, now.plusHours(2));
        Todo t2 = new Todo("Call mom", "Stay connected", 5, Priority.MEDIUM, now.plusDays(1));

        repo.saveAll(List.of(t1, t2));

        List<Todo> loaded = repo.loadAll();
        assertEquals(2, loaded.size());

        assertEquals(t1.getId(), loaded.get(0).getId());
        assertEquals("Pay rent", loaded.get(0).getTitle());
        assertEquals(Priority.HIGH, loaded.get(0).getPriority());

        assertEquals(t2.getId(), loaded.get(1).getId());
        assertEquals("Call mom", loaded.get(1).getTitle());
        assertEquals(Priority.MEDIUM, loaded.get(1).getPriority());
    }
}
