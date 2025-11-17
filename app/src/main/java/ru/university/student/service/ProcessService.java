package ru.university.student.service;

import ru.university.student.model.ProcessInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Содержит основную бизнес-логику для обработки списка процессов:
 * сортировка, фильтрация и поиск.
 *
 * @author Ваше Имя
 */
public class ProcessService {

    private final List<ProcessInfo> processes;

    /**
     * Конструктор сервиса. Принимает на вход список процессов для обработки.
     *
     * @param processes список объектов ProcessInfo.
     */
    public ProcessService(List<ProcessInfo> processes) {
        this.processes = processes;
    }

    /**
     * Задание 1: Сортирует процессы по CPU или памяти в соответствии
     * с параметром 'sort.by' в app.properties.
     *
     * @return новый отсортированный список процессов.
     */
    public List<ProcessInfo> sortProcesses(String sortBy) {
        Comparator<ProcessInfo> comparator;
        if ("CPU".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(ProcessInfo::getCpuUsage).reversed();
        } else if ("MEMORY".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(ProcessInfo::getMemoryUsageMb).reversed();
        } else {
            return processes;
        }
        return processes.stream().sorted(comparator).collect(Collectors.toList());
    }


    /**
     * Задание 2: Ищет PID процесса по имени, указанному
     * в 'search.process.name' в app.properties.
     *
     * @return Optional, содержащий PID, если процесс найден, иначе пустой.
     */
    public Optional<Integer> findProcessIdByName(String processName) {
        return processes.stream()
                .filter(p -> p.getName().toLowerCase().contains(processName.toLowerCase()))
                .map(ProcessInfo::getPid)
                .findFirst();
    }

    /**
     * Задание 3: Фильтрует процессы, потребляющие больше ресурсов,
     * чем указано в порогах 'threshold.cpu.percent' и 'threshold.memory.mb'.
     *
     * @return список процессов, превышающих хотя бы один из порогов.
     */
    public List<ProcessInfo> filterProcessesByThresholds(double cpuThreshold, long memThreshold) {
        return processes.stream()
                .filter(p -> p.getCpuUsage() > cpuThreshold || p.getMemoryUsageMb() > memThreshold)
                .collect(Collectors.toList());
    }

    // В ProcessService.java
    public List<ProcessInfo> getProcesses() {
        return processes;
    }
}