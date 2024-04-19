package ru.urfu.saveUtil;

/**
 * Даёт возможность объекту быть сохранённым в файловом представлении
 */
public interface Savable {
    /**
     * Получение префикса у объекта
     * @return - префикс
     */
    String getPrefix();
}
