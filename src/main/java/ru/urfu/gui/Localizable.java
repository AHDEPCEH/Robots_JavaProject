package ru.urfu.gui;

import java.util.ResourceBundle;

public interface Localizable {
    /**
     * Возвращает имя класса
     * @return
     */
    String getObjectName();

    /**
     * Событие обновления языка
     * @param resourceBundle
     */
    void onUpdateContent(ResourceBundle resourceBundle);
}
