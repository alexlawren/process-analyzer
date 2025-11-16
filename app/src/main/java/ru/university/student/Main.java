package ru.university.student;

import ru.university.student.model.ProcessInfo;
import ru.university.student.service.ProcessExecutor;
import ru.university.student.service.ProcessParser;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProcessExecutor executor = new ProcessExecutor();
        ProcessParser parser = ProcessParser.create(); // Используем нашу фабрику!

        try {
            System.out.println("1. Получение списка процессов...");
            String processListOutput = executor.execute();

            System.out.println("2. Парсинг вывода...");
            List<ProcessInfo> processes = parser.parse(processListOutput);

            System.out.println("3. Вывод первых 10 распарсенных процессов:");
            processes.stream()
                    .limit(10) // Ограничим вывод, чтобы не заспамить консоль
                    .forEach(System.out::println); // Используем наш метод toString()

            System.out.println("\nВсего найдено процессов: " + processes.size());

        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка выполнения: " + e.getMessage());
        } catch (UnsupportedOperationException e) {
            System.err.println(e.getMessage());
        }
    }
}