package ru.university.student.service;

import ru.university.student.model.ProcessInfo;

import java.util.List;

/**
 * Интерфейс для парсеров, преобразующих текстовый вывод
 * системных команд в список объектов ProcessInfo.
 */
public interface ProcessParser {

    /**
     * Парсит строку с выводом команды и возвращает список информации о процессах.
     *
     * @param output текстовый вывод команды (например, tasklist или ps aux).
     * @return список объектов ProcessInfo.
     */
    List<ProcessInfo> parse(String output);

    /**
     * Фабричный метод для создания парсера, соответствующего текущей ОС.
     *
     * @return экземпляр парсера для Windows или Linux.
     */
    static ProcessParser create() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return new WindowsProcessParser();
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return new LinuxProcessParser();
        } else {
            throw new UnsupportedOperationException("Парсер для ОС '" + os + "' не поддерживается.");
        }
    }
}