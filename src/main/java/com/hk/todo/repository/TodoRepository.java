package com.hk.todo.repository;

import com.hk.todo.model.Todo;

import java.util.List;

public interface TodoRepository {
    List<Todo> loadAll();
    void saveAll(List<Todo> todos);
}
