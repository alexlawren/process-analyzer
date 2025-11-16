package ru.university.student;

import ru.university.student.model.ProcessInfo;
import ru.university.student.service.ProcessExecutor;
import ru.university.student.service.ProcessParser;
import ru.university.student.service.ProcessService;
import ru.university.student.util.ConfigLoader;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        try {
            // Этап 1: Получение и парсинг данных
            ProcessExecutor executor = new ProcessExecutor();
            ProcessParser parser = ProcessParser.create();
            List<ProcessInfo> processes = parser.parse(executor.execute());

            // Этап 2: Создание сервиса с полученными данными
            ProcessService service = new ProcessService(processes);

            // Этап 3: Выполнение заданий и вывод результатов
            System.out.println("--- Задание 1: Сортировка (согласно sort.by=" + ConfigLoader.getProperty("sort.by") + ") ---");
            List<ProcessInfo> sortedProcesses = service.sortProcesses();
            printTopN(sortedProcesses, 5);

            System.out.println("\n--- Задание 2: Поиск PID (для search.process.name=" + ConfigLoader.getProperty("search.process.name") + ") ---");
            Optional<Integer> foundPid = service.findProcessIdByName();
            foundPid.ifPresentOrElse(
                    pid -> System.out.println("Найден процесс с PID: " + pid),
                    () -> System.out.println("Процесс не найден.")
            );

            System.out.println("\n--- Задание 3: Фильтрация по порогам ---");
            List<ProcessInfo> filteredProcesses = service.filterProcessesByThresholds();
            System.out.println("Найдены процессы, потребляющие >" + ConfigLoader.getProperty("threshold.cpu.percent") + "% CPU или >" + ConfigLoader.getProperty("threshold.memory.mb") + "MB RAM:");
            filteredProcesses.forEach(System.out::println);

        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка выполнения системной команды: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Вспомогательный метод для красивого вывода N первых элементов списка.
     */
    private static void printTopN(List<ProcessInfo> processes, int n) {
        if (processes.isEmpty()) {
            System.out.println("Список процессов пуст.");
        } else {
            processes.stream().limit(n).forEach(System.out::println);
        }
    }
}