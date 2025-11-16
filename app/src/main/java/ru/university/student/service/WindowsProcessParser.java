package ru.university.student.service;

import ru.university.student.model.ProcessInfo;
import java.util.ArrayList;
import java.util.List;

// Ключевое слово "implements" говорит, что этот класс выполняет контракт интерфейса ProcessParser
public class WindowsProcessParser implements ProcessParser {

    private static final int IMAGE_NAME_INDEX = 0;
    private static final int PID_INDEX = 1;
    private static final int MEM_USAGE_INDEX = 4;

    @Override // Аннотация @Override подтверждает, что мы реализуем метод из интерфейса
    public List<ProcessInfo> parse(String output) {
        List<ProcessInfo> processes = new ArrayList<>();
        String[] lines = output.split(System.lineSeparator());

        for (String line : lines) {
            if (line.isEmpty() || !Character.isLetterOrDigit(line.charAt(0)) || line.startsWith("=")) {
                continue;
            }

            String[] parts = line.trim().split("\\s{2,}");

            if (parts.length < 5) {
                continue;
            }

            try {
                String imageName = parts[IMAGE_NAME_INDEX];
                int pid = Integer.parseInt(parts[PID_INDEX]);
                String memString = parts[MEM_USAGE_INDEX].replace(",", "").replace("K", "").trim();
                long memoryKb = Long.parseLong(memString);
                long memoryMb = memoryKb / 1024;
                processes.add(new ProcessInfo(pid, imageName, 0.0, memoryMb));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Не удалось распарсить строку: " + line);
            }
        }
        return processes;
    }
}