package ru.urfu.saveUtil;

import java.util.ArrayList;
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
    public static void saveAllStates (List<Savable> windows, FileManager fileManager) {
        List<SubDictionary<String, String>> states = new ArrayList<>();
        for (Savable window : windows) {
            states.add(window.getWindowState());
        }
        fileManager.writeState(states);
    }

    /**
     * Метод собирающий восстанавливающий состояние для окон
     * @param windows - окна
     * @param fileManager - объект для работы с файлом сохранения
     */
    public static void recoverAllStates (List<Savable> windows, FileManager fileManager){
        try {
            Map<String, SubDictionary<String, String>> states = fileManager.readState();
            for (Savable window : windows){
                window.setWindowState(states.get(window.getPrefix()));
            }
        } catch (Exception e) {
            for (Savable window : windows){
                window.setWindowState(null);
            }
        }
    }
}
