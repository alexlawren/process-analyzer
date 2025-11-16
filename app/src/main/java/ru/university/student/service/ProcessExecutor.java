package ru.university.student.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Отвечает за выполнение системных команд для получения списка процессов.
 * Адаптируется под операционную систему (Windows/Linux).
 * @author Ваше Имя
 */
public class ProcessExecutor {

    /**
     * Выполняет команду для получения списка процессов и возвращает ее вывод.
     * Автоматически определяет ОС и выбирает нужную команду.
     *
     * @return Строка, содержащая стандартный вывод (stdout) выполненной команды.
     * @throws IOException если возникает ошибка при выполнении команды.
     * @throws InterruptedException если поток, выполняющий команду, был прерван.
     */
    public String execute() throws IOException, InterruptedException {
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder processBuilder;

        if (os.contains("win")) {
            // Команда для Windows
            processBuilder = new ProcessBuilder("tasklist");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            // Команда для Linux/Unix-подобных систем
            processBuilder = new ProcessBuilder("ps", "aux");
        } else {
            throw new UnsupportedOperationException("Данная ОС не поддерживается: " + os);
        }

        Process process = processBuilder.start();

        // Считываем стандартный вывод (stdout) процесса
        String output = readStream(new InputStreamReader(process.getInputStream()));

        // Считываем вывод ошибок (stderr) процесса
        String error = readStream(new InputStreamReader(process.getErrorStream()));

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            // Если команда завершилась с ошибкой, выбрасываем исключение с подробностями
            throw new IOException("Команда завершилась с кодом ошибки " + exitCode + ". Ошибка: " + error);
        }

        return output;
    }

    /**
     * Вспомогательный метод для чтения данных из потока в строку.
     * @param reader Источник данных (например, вывод процесса).
     * @return Содержимое потока в виде строки.
     * @throws IOException если возникает ошибка чтения.
     */
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