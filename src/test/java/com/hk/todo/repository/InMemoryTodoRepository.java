package com.hk.todo.repository;

import com.hk.todo.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTodoRepository implements TodoRepository {

    private List<Todo> data = new ArrayList<>();

    @Override
    public List<Todo> loadAll() {
        return new ArrayList<>(data);
    }

    @Override
    public void saveAll(List<Todo> todos) {
        data = new ArrayList<>(todos);
    }
}
