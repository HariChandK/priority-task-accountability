package com.hk.todo.util;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class InputHelper {

    private final Scanner sc;

    public InputHelper(Scanner sc) {
        this.sc = sc;
    }

    public String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Please enter a value.");
        }
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public <E extends Enum<E>> E readEnum(String prompt, Class<E> enumType) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim().toUpperCase();
            try {
                return Enum.valueOf(enumType, s);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid value. Allowed: " + String.join(", ", allowedEnumValues(enumType)));
            }
        }
    }

    public LocalDateTime readDateTime(String prompt, DateTimeFormatter fmt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return LocalDateTime.parse(s, fmt);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date/time. Format must be: " + fmt);
            }
        }
    }

    private <E extends Enum<E>> String[] allowedEnumValues(Class<E> enumType) {
        E[] constants = enumType.getEnumConstants();
        String[] values = new String[constants.length];
        for (int i = 0; i < constants.length; i++) values[i] = constants[i].name();
        return values;
    }

}
