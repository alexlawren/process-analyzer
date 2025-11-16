package ru.university.student.service;

import ru.university.student.model.ProcessInfo;
import ru.university.student.util.ConfigLoader;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Содержит основную бизнес-логику для обработки списка процессов:
 * сортировка, фильтрация и поиск.
 * @author Ваше Имя
 */
public class ProcessService {

    private final List<ProcessInfo> processes;

    /**
     * Конструктор сервиса. Принимает на вход список процессов для обработки.
     * @param processes список объектов ProcessInfo.
     */
    public ProcessService(List<ProcessInfo> processes) {
        this.processes = processes;
    }

    /**
     * Задание 1: Сортирует процессы по CPU или памяти в соответствии
     * с параметром 'sort.by' в app.properties.
     * @return новый отсортированный список процессов.
     */
    public List<ProcessInfo> sortProcesses() {
        String sortBy = ConfigLoader.getProperty("sort.by");

        Comparator<ProcessInfo> comparator;

        if ("CPU".equalsIgnoreCase(sortBy)) {
            // Сортировка по убыванию CPU
            comparator = Comparator.comparing(ProcessInfo::getCpuUsage).reversed();
        } else if ("MEMORY".equalsIgnoreCase(sortBy)) {
            // Сортировка по убыванию памяти
            comparator = Comparator.comparing(ProcessInfo::getMemoryUsageMb).reversed();
        } else {
            // Если в конфиге что-то не то, просто возвращаем как есть
            return processes;
        }

        return processes.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * Задание 2: Ищет PID процесса по имени, указанному
     * в 'search.process.name' в app.properties.
     * @return Optional, содержащий PID, если процесс найден, иначе пустой.
     */
    public Optional<Integer> findProcessIdByName() {
        String processName = ConfigLoader.getProperty("search.process.name");

        return processes.stream()
                // Ищем первый процесс, имя которого (без учета регистра) содержит искомую строку
                .filter(p -> p.getName().toLowerCase().contains(processName.toLowerCase()))
                .map(ProcessInfo::getPid)
                .findFirst();
    }

    /**
     * Задание 3: Фильтрует процессы, потребляющие больше ресурсов,
     * чем указано в порогах 'threshold.cpu.percent' и 'threshold.memory.mb'.
     * @return список процессов, превышающих хотя бы один из порогов.
     */
    public List<ProcessInfo> filterProcessesByThresholds() {
        double cpuThreshold = Double.parseDouble(ConfigLoader.getProperty("threshold.cpu.percent"));
        long memThreshold = Long.parseLong(ConfigLoader.getProperty("threshold.memory.mb"));

        return processes.stream()
                .filter(p -> p.getCpuUsage() > cpuThreshold || p.getMemoryUsageMb() > memThreshold)
                .collect(Collectors.toList());
    }
}