package com.hk.todo.model;

import com.hk.todo.model.Priority;
import com.hk.todo.model.Todo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void overdue_when_not_done_and_due_in_past() {
        LocalDateTime now = LocalDateTime.now();
        Todo t = new Todo("Pay rent", "Avoid late fee", 10, Priority.HIGH, now.minusMinutes(5));

        assertTrue(t.isOverdue(now));
    }

    @Test
    void not_overdue_when_done() {
        LocalDateTime now = LocalDateTime.now();
        Todo t = new Todo("Submit report", "Needed for manager review", 30, Priority.HIGH, now.minusMinutes(5));
        t.markDone();

        assertFalse(t.isOverdue(now));
    }

    @Test
    void due_soon_when_within_threshold() {
        LocalDateTime now = LocalDateTime.now();
        Todo t = new Todo("Call mom", "Stay connected", 5, Priority.MEDIUM, now.plusMinutes(10));

        assertTrue(t.isDueSoon(now, 15));
        assertFalse(t.isDueSoon(now, 5));
    }
}
