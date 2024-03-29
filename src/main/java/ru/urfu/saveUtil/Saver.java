package ru.urfu.saveUtil;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Класс с утилитарными методами для работы с состояниями
 */
public class Saver {

    private Saver(){};

    /**
     * Собирает все состояния окна в словарь
     * @param window - окно
     * @param prefix - идентификатор окна
     * @return Словарь состояний окна
     */
    public static SubDictionary<String, String> buildState(Container window, String prefix) {
        SubDictionary<String, String> state = new SubDictionary<>(new HashMap<>(), prefix);
        state.put("height", Integer.toString(window.getHeight()));
        state.put("width", Integer.toString(window.getWidth()));
        state.put("positionX", Integer.toString(window.getX()));
        state.put("positionY", Integer.toString(window.getY()));

        if (window instanceof JFrame) {
            state.put("state", Integer.toString(((JFrame) window).getExtendedState()));
        }
        if (window instanceof JInternalFrame) {
            state.put("icon", Boolean.toString(((JInternalFrame) window).isIcon()));
        }
        return state;
    }

    /**
     * Метод устанавливающий окну все его параметры
     * @param window - окно
     * @param state - словарь состояния
     * @throws Exception Ошибки перевода значений из словаря
     */
    public static void setState(Container window, SubDictionary<String, String> state) throws Exception{
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
