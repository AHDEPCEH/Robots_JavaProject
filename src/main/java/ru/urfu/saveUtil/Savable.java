package ru.urfu.saveUtil;

/**
 * Даёт возможность объекту быть сохранённым в файловом представлении
 */
public interface Savable {
    /**
     * Сохранение параметров окна и его состояния
     */
    void saveState();

    /**
     * Восстановление параметров окна и его состояния
     */
    void recoverState();
}
