package ru.university.student;

import ru.university.student.service.ProcessExecutor; // Импортируем

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ProcessExecutor executor = new ProcessExecutor();
        try {
            System.out.println("Получаем список процессов...");
            String processListOutput = executor.execute();
            System.out.println(processListOutput);
        } catch (IOException | InterruptedException e) {
            System.err.println("Не удалось получить список процессов: " + e.getMessage());
            e.printStackTrace();
        }
    }
}