package ru.urfu.gui;

import java.util.ResourceBundle;

/**
 * Объекты поддерживающие локализацию
 */
public interface Localizable {
    /**
     * Возвращает имя класса, который поддерживает локализацию
     * @return Строка - имя класса
     */
    String getObjectName();

    /**
     * Событие обновления языка
     * @param resourceBundle - объект локали
     */
    void onUpdateContent(ResourceBundle resourceBundle);
}
