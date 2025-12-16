package com.hk.todo.service;

import com.hk.todo.model.Priority;
import com.hk.todo.model.Todo;
import com.hk.todo.service.TodoService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceTest {

    @Test
    void sortedForNow_puts_not_done_high_priority_earliest_due_first() {
        LocalDateTime now = LocalDateTime.now();

        var repo = new com.hk.todo.repository.InMemoryTodoRepository();
        TodoService service = new TodoService(repo);
        Todo lowLater = new Todo("Low later", "not urgent", 10, Priority.LOW, now.plusHours(5));
        Todo highSoon = new Todo("High soon", "important", 10, Priority.HIGH, now.plusMinutes(30));
        Todo highLater = new Todo("High later", "important", 10, Priority.HIGH, now.plusHours(2));

        service.add(lowLater);
        service.add(highLater);
        service.add(highSoon);

        List<Todo> sorted = service.sortedForNow(now);

        assertEquals("High soon", sorted.get(0).getTitle());
        assertEquals("High later", sorted.get(1).getTitle());
        assertEquals("Low later", sorted.get(2).getTitle());
    }
}
