package com.hk.todo.service;

import com.hk.todo.model.Priority;
import com.hk.todo.model.Todo;
import com.hk.todo.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TodoService {

    private final TodoRepository repo;
    private final List<Todo> todos;

    public TodoService(TodoRepository repo) {
        this.repo = repo;
        this.todos = new ArrayList<>(repo.loadAll());
    }

    public void add(Todo todo) {
        todos.add(todo);
        repo.saveAll(todos);
    }

    public List<Todo> all() {
        return new ArrayList<>(todos);
    }

    public boolean markDoneById(String id) {
        for (Todo t : todos) {
            if (t.getId().equals(id)) {
                t.markDone();
                repo.saveAll(todos);
                return true;
            }
        }
        return false;
    }

    public List<Todo> sortedForNow(LocalDateTime now) {
        List<Todo> copy = new ArrayList<>(todos);

        copy.sort(
                Comparator
                        .comparing(Todo::isDone) // false first
                        .thenComparing((Todo t) -> priorityRank(t.getPriority()))
                        .thenComparing((Todo t) -> t.getDueAt() == null ? LocalDateTime.MAX : t.getDueAt())
                        .thenComparing((Todo t) -> t.isOverdue(now) ? 0 : 1)
        );

        return copy;
    }

    private int priorityRank(Priority p) {
        return switch (p) {
            case HIGH -> 0;
            case MEDIUM -> 1;
            case LOW -> 2;
        };
    }
}
