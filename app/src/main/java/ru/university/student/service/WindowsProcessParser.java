package ru.university.student.service;

import ru.university.student.model.ProcessInfo;

import java.util.ArrayList;
import java.util.List;

public class WindowsProcessParser implements ProcessParser {

    @Override
    public List<ProcessInfo> parse(String output) {
        List<ProcessInfo> processes = new ArrayList<>();
        String[] lines = output.split(System.lineSeparator());

        // Пропускаем заголовки
        for (int i = 3; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty() || line.startsWith("=")) {
                continue;
            }

            try {
                // Находим позицию, где заканчивается имя процесса.
                // Имя может содержать пробелы, а PID - это первое число после него.
                // Мы ищем первое число, которое окружено пробелами с обеих сторон.
                int pidStartIndex = -1;
                for (int j = 25; j < line.length(); j++) { // PID обычно начинается после 25-го символа
                    if (Character.isDigit(line.charAt(j)) && Character.isWhitespace(line.charAt(j - 1))) {
                        pidStartIndex = j;
                        break;
                    }
                }

                if (pidStartIndex == -1) continue;

                // Выделяем имя процесса
                String imageName = line.substring(0, pidStartIndex).trim();

                // Оставшуюся часть строки делим по пробелам, чтобы легко извлечь PID и память
                String remainingLine = line.substring(pidStartIndex);
                String[] parts = remainingLine.trim().split("\\s+");

                if (parts.length < 4) continue; // Ожидаем как минимум PID, SessionName, Session#, Memory

                int pid = Integer.parseInt(parts[0]);

                // Память - это предпоследняя колонка
                String memString = parts[parts.length - 2].replaceAll("[^\\d]", "");
                long memoryKb = Long.parseLong(memString);
                long memoryMb = memoryKb / 1024;

                processes.add(new ProcessInfo(pid, imageName, 0.0, memoryMb));

            } catch (Exception e) {
                // Игнорируем строки, которые не удалось разобрать
                // System.err.println("Не удалось распарсить строку: " + line);
            }
        }
        return processes;
    }
}