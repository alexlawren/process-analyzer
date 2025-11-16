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
            System.out.println("Получение списка системных процессов...");
            ProcessExecutor executor = new ProcessExecutor();
            ProcessParser parser = ProcessParser.create();
            List<ProcessInfo> processes = parser.parse(executor.execute());
            System.out.println("Найдено и обработано процессов: " + processes.size());

            // Этап 2: Создание сервиса с полученными данными
            ProcessService service = new ProcessService(processes);

            // Этап 3: Выполнение заданий и вывод результатов
            // --- Задание 1: Сортировка ---
            String sortBy = ConfigLoader.getProperty("sort.by");
            System.out.println("\n--- Задание 1: Сортировка (согласно sort.by=" + sortBy + ") ---");
            List<ProcessInfo> sortedProcesses = service.sortProcesses(sortBy);
            printTopN(sortedProcesses, 5); // Выводим топ-5

            // --- Задание 2: Поиск PID ---
            String processToSearch = ConfigLoader.getProperty("search.process.name");
            System.out.println("\n--- Задание 2: Поиск PID (для search.process.name=" + processToSearch + ") ---");
            Optional<Integer> foundPid = service.findProcessIdByName(processToSearch);
            foundPid.ifPresentOrElse(
                    pid -> System.out.println("Найден процесс с PID: " + pid),
                    () -> System.out.println("Процесс '" + processToSearch + "' не найден.")
            );

            // --- Задание 3: Фильтрация ---
            double cpuThreshold = Double.parseDouble(ConfigLoader.getProperty("threshold.cpu.percent"));
            long memThreshold = Long.parseLong(ConfigLoader.getProperty("threshold.memory.mb"));
            System.out.println("\n--- Задание 3: Фильтрация по порогам ---");
            System.out.println("Поиск процессов, потребляющих >" + cpuThreshold + "% CPU или >" + memThreshold + "MB RAM:");
            List<ProcessInfo> filteredProcesses = service.filterProcessesByThresholds(cpuThreshold, memThreshold);
            if (filteredProcesses.isEmpty()) {
                System.out.println("Процессы, превышающие пороги, не найдены.");
            } else {
                filteredProcesses.forEach(System.out::println);
            }

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
            System.out.println("Топ " + Math.min(n, processes.size()) + " процессов:");
            processes.stream().limit(n).forEach(System.out::println);
        }
    }
}