package ru.urfu.saveUtil;

import ru.urfu.log.Logger;

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

    private static final StateFile stateFile = new StateFile();

    private StateManager(){}

    /**
     * Метод для сохранения состояния у окон
     * @param windows - окна
     */
    public static void saveAllStates (List<Container> windows) {
        List<SubDictionary<String, String>> states = new ArrayList<>();
        for (Container window : windows) {
            if (window instanceof Savable) {
                states.add(buildState(window));
            }
        }
        stateFile.writeState(states);
    }

    /**
     * Метод собирающий восстанавливающий состояние для окон
     * @param windows - окна
     */
    public static void recoverAllStates (List<Container> windows){
        Map<String, SubDictionary<String, String>> states = stateFile.readState();
        for (Container window : windows){
            if (window instanceof Savable savableWindow) {
                setState(window, states.get(savableWindow.getPrefix()));
            }
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

        if (window instanceof JFrame frame) {
            state.put("state", Integer.toString(frame.getExtendedState()));
        }
        if (window instanceof JInternalFrame frame) {
            state.put("icon", Boolean.toString(frame.isIcon()));
        }
        return state;
    }

    /**
     * Метод устанавливающий окну все его параметры
     * @param window - окно
     * @param state - словарь состояния
     */
    private static void setState(Container window, SubDictionary<String, String> state){
        try {
            window.setLocation(
                    Integer.parseInt(state.get("positionX")),
                    Integer.parseInt(state.get("positionY")));

            window.setSize(
                    Integer.parseInt(state.get("width")),
                    Integer.parseInt(state.get("height")));

            if (window instanceof JFrame frame) {
                frame.setExtendedState(Integer.parseInt(state.get("state")));
            }
            if (window instanceof JInternalFrame frame) {
                frame.setIcon(Boolean.parseBoolean(state.get("icon")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("stateError");
        }
    }
}