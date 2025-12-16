package com.hk.todo.app;

import com.hk.todo.model.Priority;
import com.hk.todo.model.Todo;
import com.hk.todo.service.TodoService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final DateTimeFormatter DUE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        var repo = new com.hk.todo.repository.FileTodoRepository(java.nio.file.Path.of("data", "todos.json"));
        TodoService service = new TodoService(repo);
        printStartupSummary(service);
        Scanner sc = new Scanner(System.in);
        var input = new com.hk.todo.util.InputHelper(sc);

        while (true) {
            printMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> addTodo(input, service);
                case "2" -> listTodos(service);
                case "3" -> markDone(sc, service);
                case "4" -> showOverdue(service);
                case "5" -> {
                    System.out.println("Bye");
                    return;
                }
                default -> System.out.println("Invalid choice. Please enter 1-5.");
            }
        }
    }

    private static void printStartupSummary(TodoService service) {
        LocalDateTime now = LocalDateTime.now();
        var sorted = service.sortedForNow(now);

        long total = sorted.size();
        long overdue = sorted.stream().filter(t -> t.isOverdue(now)).count();
        long dueSoon = sorted.stream().filter(t -> t.isDueSoon(now, 60)).count();

        System.out.println("\n=== Today Summary ===");
        System.out.println("Total todos: " + total);
        System.out.println("Overdue:     " + overdue);
        System.out.println("Due soon(60m): " + dueSoon);
        System.out.println("=====================\n");
    }


    private static void printMenu() {
        System.out.println("\n=== Priority Task Accountability ===");
        System.out.println("1) Add a todo");
        System.out.println("2) List todos (sorted)");
        System.out.println("3) Mark done (by id)");
        System.out.println("4) Show overdue (with reason)");
        System.out.println("5) Exit");
        System.out.print("Choose: ");
    }

    private static void addTodo(com.hk.todo.util.InputHelper input, TodoService service) {
        String title = input.readNonEmpty("Title: ");
        String reason = input.readNonEmpty("Reason (why you must do it): ");
        int minutes = input.readInt("Estimate minutes (number): ");

        Priority priority = input.readEnum("Priority (HIGH/MEDIUM/LOW): ", Priority.class);
        LocalDateTime dueAt = input.readDateTime("Due at (yyyy-MM-dd HH:mm): ", DUE_FMT);

        Todo todo = new Todo(title, reason, minutes, priority, dueAt);
        service.add(todo);

        System.out.println("Added id=" + todo.getId());
    }

    private static void listTodos(TodoService service) {
        LocalDateTime now = LocalDateTime.now();
        List<Todo> sorted = service.sortedForNow(now);

        if (sorted.isEmpty()) {
            System.out.println("No todos yet.");
            return;
        }

        System.out.println("\n--- Todos (sorted) ---");
        for (Todo t : sorted) {
            String status = t.isDone() ? "DONE" : (t.isOverdue(now) ? "OVERDUE" : "PENDING");
            System.out.printf(
                    "id=%s | %s | %s | due=%s | est=%dmin | %s%n",
                    t.getId(),
                    t.getPriority(),
                    status,
                    t.getDueAt().format(DUE_FMT),
                    t.getEstimateMinutes(),
                    t.getTitle()
            );
            System.out.println("   ↳ Reason: " + t.getReason());
        }
    }

    private static void markDone(Scanner sc, TodoService service) {
        System.out.print("Enter id to mark done: ");
        String id = sc.nextLine().trim();

        boolean ok = service.markDoneById(id);
        System.out.println(ok ? "Marked done ✅" : "Not found");
    }

    private static void showOverdue(TodoService service) {
        LocalDateTime now = LocalDateTime.now();
        List<Todo> sorted = service.sortedForNow(now);

        boolean any = false;
        System.out.println("\n--- Overdue Todos ---");
        for (Todo t : sorted) {
            if (t.isOverdue(now)) {
                any = true;
                System.out.printf(
                        "[%s] %s | due=%s | est=%dmin%nReason: %s%n%n",
                        t.getPriority(),
                        t.getTitle(),
                        t.getDueAt().format(DUE_FMT),
                        t.getEstimateMinutes(),
                        t.getReason()
                );
            }
        }
        if (!any) {
            System.out.println("None");
        }
    }
}
