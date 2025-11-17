package ru.university.student.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.university.student.model.ProcessInfo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для класса ProcessService.
 */
class ProcessServiceTest {

    private List<ProcessInfo> testProcesses;

    // Этот метод будет выполняться ПЕРЕД каждым тестовым методом
    @BeforeEach
    void setUp() {
        // Создаем тестовый набор данных.
        testProcesses = List.of(
                new ProcessInfo(101, "chrome.exe", 15.2, 500),
                new ProcessInfo(102, "idea64.exe", 10.5, 2000),
                new ProcessInfo(103, "svchost.exe", 0.1, 50),
                new ProcessInfo(104, "java.exe", 80.0, 1500)
        );
        // ВАЖНО: Мы не можем легко тестировать ConfigLoader, поэтому
        // для тестов мы будем создавать сервис напрямую, передавая ему
        // все необходимые параметры, а не заставляя его читать файл.
        // Это делает тесты независимыми от внешних файлов.
    }

    // ВАЖНО: Мы не можем напрямую протестировать методы, которые зависят от ConfigLoader
    // без использования специальных библиотек (Mockito). Поэтому мы протестируем
    // логику немного по-другому, передавая параметры прямо в методы.
    // Для этого нам придется немного изменить ProcessService.

    @Test
    void findProcessIdByName_ShouldReturnCorrectPid_WhenProcessExists() {
        ProcessService processService = new ProcessService(testProcesses);
        // Предполагаем, что ищем "java"
        Optional<Integer> foundPid = processService.findProcessIdByName("java");

        assertTrue(foundPid.isPresent(), "PID должен был быть найден");
        assertEquals(104, foundPid.get(), "Найденный PID не соответствует ожидаемому");
    }

    @Test
    void findProcessIdByName_ShouldReturnEmpty_WhenProcessDoesNotExist() {
        ProcessService processService = new ProcessService(testProcesses);
        // Ищем заведомо несуществующий процесс
        Optional<Integer> foundPid = processService.findProcessIdByName("explorer.exe");

        assertFalse(foundPid.isPresent(), "PID не должен был быть найден");
    }

    @Test
    void sortProcesses_ShouldSortByMemoryCorrectly() {
        ProcessService processService = new ProcessService(testProcesses);
        List<ProcessInfo> sorted = processService.sortProcesses("MEMORY");

        assertEquals(4, sorted.size(), "Размер списка не должен меняться");
        assertEquals(102, sorted.get(0).getPid(), "Первым должен быть процесс с PID 102 (больше всего памяти)");
        assertEquals(104, sorted.get(1).getPid(), "Вторым должен быть процесс с PID 104");
        assertEquals(103, sorted.get(3).getPid(), "Последним должен быть процесс с PID 103 (меньше всего памяти)");
    }

    @Test
    void sortProcesses_ShouldSortByCpuCorrectly() {
        ProcessService processService = new ProcessService(testProcesses);
        List<ProcessInfo> sorted = processService.sortProcesses("CPU");

        assertEquals(104, sorted.get(0).getPid(), "Первым должен быть процесс с PID 104 (больше всего CPU)");
        assertEquals(101, sorted.get(1).getPid(), "Вторым должен быть процесс с PID 101");
    }

    @Test
    void filterProcessesByThresholds_ShouldReturnProcessesAboveLimits() {
        ProcessService processService = new ProcessService(testProcesses);
        // Пороги: 15% CPU, 1000 MB RAM
        List<ProcessInfo> filtered = processService.filterProcessesByThresholds(15.0, 1000);
        assertEquals(3, filtered.size(), "Должно быть найдено 3 процесса");
        // Проверяем, что в отфильтрованном списке есть нужные процессы
        assertTrue(filtered.stream().anyMatch(p -> p.getPid() == 101)); // CPU > 15
        assertTrue(filtered.stream().anyMatch(p -> p.getPid() == 102)); // MEM > 1000
        assertTrue(filtered.stream().anyMatch(p -> p.getPid() == 104)); // CPU > 15, MEM > 1000
        // Проверяем, что "чистого" процесса нет
        assertFalse(filtered.stream().anyMatch(p -> p.getPid() == 103));
    }
}