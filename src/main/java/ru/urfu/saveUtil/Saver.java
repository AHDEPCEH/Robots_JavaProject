package ru.urfu.saveUtil;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Класс с утилитарными методами для работы с состояниями окна
 */
public class Saver {

    private Saver(){};

    /**
     * Собирает все состояния окна в словарь
     * @param window - окно
     * @return Словарь состояний окна
     */
    public static SubDictionary<String, String> buildState(Savable window) {
        SubDictionary<String, String> state = new SubDictionary<>(new HashMap<>(), window.getPrefix());
        if (window instanceof Container) {
            state.put("height", Integer.toString(((Container) window).getHeight()));
            state.put("width", Integer.toString(((Container) window).getWidth()));
            state.put("positionX", Integer.toString(((Container) window).getX()));
            state.put("positionY", Integer.toString(((Container) window).getY()));

            if (window instanceof JFrame) {
                state.put("state", Integer.toString(((JFrame) window).getExtendedState()));
            }
            if (window instanceof JInternalFrame) {
                state.put("icon", Boolean.toString(((JInternalFrame) window).isIcon()));
            }
        }
        return state;
    }

    /**
     * Метод устанавливающий окну все его параметры
     * @param window - окно
     * @param state - словарь состояния
     * @throws Exception Ошибки перевода значений из словаря
     */
    public static void setState(Container window, SubDictionary<String, String> state) throws Exception {
        window.setLocation(
                Integer.parseInt(state.get("positionX")),
                Integer.parseInt(state.get("positionY")));

        window.setSize(
                Integer.parseInt(state.get("width")),
                Integer.parseInt(state.get("height")));

        if (window instanceof JFrame) {
            ((JFrame) window).setExtendedState(Integer.parseInt(state.get("state")));
        }
        if (window instanceof JInternalFrame) {
            ((JInternalFrame) window).setIcon(Boolean.parseBoolean(state.get("icon")));
        }
    }
}
