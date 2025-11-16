package ru.university.student.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Утилитный класс для загрузки и предоставления доступа к конфигурационным
 * параметрам из файла app.properties.
 * @author Ваше Имя
 */
public class ConfigLoader {

    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "/app.properties"; // Слеш в начале важен!

    // Статический блок инициализации.
    // Выполняется один раз при первой загрузке класса в память.
    static {
        try (InputStream input = ConfigLoader.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                // Если файл не найден, выбрасываем исключение.
                // Это лучше, чем приложение будет работать с неверной конфигурацией.
                throw new IOException("Не найден файл конфигурации: " + CONFIG_FILE);
            }
            // Загружаем properties из потока
            properties.load(input);
        } catch (IOException e) {
            // В случае ошибки ввода/вывода, "заворачиваем" ее в RuntimeException.
            // Это остановит выполнение программы, что правильно в данной ситуации.
            throw new RuntimeException("Ошибка при загрузке файла конфигурации", e);
        }
    }

    /**
     * Получает значение свойства по его ключу.
     * @param key ключ свойства (например, "sort.by").
     * @return значение свойства в виде строки.
     */
    public static String getProperty(String key) {
        String property = properties.getProperty(key);
        if (property == null) {
            throw new IllegalArgumentException("Свойство с ключом '" + key + "' не найдено в " + CONFIG_FILE);
        }
        return property;
    }

    // Приватный конструктор, чтобы нельзя было создать экземпляр этого класса.
    private ConfigLoader() {}
}