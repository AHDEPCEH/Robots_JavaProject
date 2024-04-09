package ru.urfu.saveUtil;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс с утилитарными методами для управления состояниями группы окон
 */
public class StateManager {

    private StateManager(){}

    /**
     * Метод для сохранения состояния у окон
     * @param windows - окна
     * @param fileManager - объект для работы с файлом сохранения
     */
    public static void saveAllStates (List<Container> windows, FileManager fileManager) {
        List<SubDictionary<String, String>> states = new ArrayList<>();
        for (Container window : windows) {
            states.add(buildState(window));
        }
        fileManager.writeState(states);
    }

    /**
     * Метод собирающий восстанавливающий состояние для окон
     * @param windows - окна
     * @param fileManager - объект для работы с файлом сохранения
     */
    public static void recoverAllStates (List<Container> windows, FileManager fileManager){
        Map<String, SubDictionary<String, String>> states = fileManager.readState();
        for (Container window : windows){
            setState(window, states.get(((Savable) window).getPrefix()));
        }
    }

    /**
     * Собирает все состояния окна в словарь
     * @param window - окно
     * @return Словарь состояний окна
     */
    private static SubDictionary<String, String> buildState(Container window) {
        SubDictionary<String, String> state = new SubDictionary<>(new HashMap<>(), ((Savable) window).getPrefix());
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
     * @throws PropertyVetoException Ошибки восстановления свёрнутого окна
     */
    private static void setState(Container window, SubDictionary<String, String> state){
        try {
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
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }
}
