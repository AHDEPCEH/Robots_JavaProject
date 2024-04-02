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

    /**
     * Получить состояние у объекта
     * @return возвращает собранное состояние
     */
    SubDictionary<String, String> getWindowState();

    /**
     * Установить состояние объекту
     * @param state - собранное состояние
     */
    void setWindowState(SubDictionary<String, String> state);
}
