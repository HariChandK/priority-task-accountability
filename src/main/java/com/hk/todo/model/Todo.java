package com.hk.todo.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Todo {

    private String id;
    private String title;
    private String reason;
    private int estimateMinutes;
    private Priority priority;
    private LocalDateTime dueAt;
    private boolean done;
    private LocalDateTime createdAt;

    public Todo() {
        // for Jackson
    }

    public Todo(String title, String reason, int estimateMinutes, Priority priority, LocalDateTime dueAt) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.reason = reason;
        this.estimateMinutes = estimateMinutes;
        this.priority = priority;
        this.dueAt = dueAt;
        this.done = false;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isOverdue(LocalDateTime now) {
        return !done && dueAt != null && now.isAfter(dueAt);
    }

    public boolean isDueSoon(LocalDateTime now, int minutes) {
        if (done || dueAt == null) return false;
        LocalDateTime soon = now.plusMinutes(minutes);
        return (now.isBefore(dueAt) || now.isEqual(dueAt)) && (soon.isAfter(dueAt) || soon.isEqual(dueAt));
    }

    public void markDone() {
        this.done = true;
    }

    // Getters (keep it simple for now)
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getReason() { return reason; }
    public int getEstimateMinutes() { return estimateMinutes; }
    public Priority getPriority() { return priority; }
    public LocalDateTime getDueAt() { return dueAt; }
    public boolean isDone() { return done; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    //setters

    public void setId(String id) { this.id = id; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setTitle(String title) { this.title = title; }
    public void setReason(String reason) { this.reason = reason; }
    public void setEstimateMinutes(int estimateMinutes) { this.estimateMinutes = estimateMinutes; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setDueAt(LocalDateTime dueAt) { this.dueAt = dueAt; }
    public void setDone(boolean done) { this.done = done; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Todo todo)) return false;
        return Objects.equals(id, todo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
