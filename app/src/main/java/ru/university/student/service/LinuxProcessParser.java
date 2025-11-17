package ru.university.student.service;

import ru.university.student.model.ProcessInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LinuxProcessParser implements ProcessParser {

    private static final int PID_INDEX = 1;
    private static final int CPU_INDEX = 2;
    private static final int MEM_RSS_INDEX = 5;
    private static final int COMMAND_INDEX = 10;

    @Override
    public List<ProcessInfo> parse(String output) {
        List<ProcessInfo> processes = new ArrayList<>();
        String[] lines = output.split(System.lineSeparator());

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.trim().split("\\s+");

            if (parts.length < 11) {
                continue;
            }

            try {
                int pid = Integer.parseInt(parts[PID_INDEX]);
                double cpu = Double.parseDouble(parts[CPU_INDEX]);
                long memoryKb = Long.parseLong(parts[MEM_RSS_INDEX]);
                long memoryMb = memoryKb / 1024;
                String command = String.join(" ", Arrays.copyOfRange(parts, COMMAND_INDEX, parts.length));
                processes.add(new ProcessInfo(pid, command, cpu, memoryMb));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Не удалось распарсить строку: " + line);
            }
        }
        return processes;
    }
}