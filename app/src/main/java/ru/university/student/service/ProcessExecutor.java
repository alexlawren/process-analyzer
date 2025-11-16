package ru.university.student.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset; // Можно и так

public class ProcessExecutor {

    public String execute() throws IOException, InterruptedException {
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder processBuilder;

        if (os.contains("win")) {
            processBuilder = new ProcessBuilder("tasklist");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            processBuilder = new ProcessBuilder("ps", "aux");
        } else {
            throw new UnsupportedOperationException("Данная ОС не поддерживается: " + os);
        }

        Process process = processBuilder.start();

        // Определяем кодировку в зависимости от ОС
        String charsetName = os.contains("win") ? "CP866" : "UTF-8";

        // Считываем стандартный вывод (stdout) процесса с явным указанием кодировки
        String output = readStream(new InputStreamReader(process.getInputStream(), charsetName));

        // Считываем вывод ошибок (stderr) процесса с явным указанием кодировки
        String error = readStream(new InputStreamReader(process.getErrorStream(), charsetName));

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("Команда завершилась с кодом ошибки " + exitCode + ". Ошибка: " + error);
        }

        return output;
    }

    private String readStream(InputStreamReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append(System.lineSeparator());
            }
        }
        return builder.toString();
    }
}