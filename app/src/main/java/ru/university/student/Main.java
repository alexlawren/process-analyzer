package ru.university.student;

import ru.university.student.util.ConfigLoader; // Не забудьте импорт

public class Main {
    public static void main(String[] args) {
        // Тестовый вывод для проверки
        System.out.println("Режим сортировки из файла: " + ConfigLoader.getProperty("sort.by"));
        System.out.println("Имя процесса для поиска: " + ConfigLoader.getProperty("search.process.name"));
    }
}