package ru.urfu.saveUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для записи и чтения состояний окон из файла
 */
public class FileManager {

    private final String fileName = System.getProperty("user.home")+"/save";
    /**
     * Метод для записи состояния объекта в файл
     * @param state - состояние объекта
     */
    public void writeState(SubDictionary<String, String> state){
        Map<String, String> lastState = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    lastState.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String key : state.keySet()) {
            lastState.put(key, state.get(key.split("\\.")[1]));
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String key : lastState.keySet()) {
                writer.write(key + "=" + lastState.get(key));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для чтения состояния объекта из файла
     * @param prefix - идентификатор объекта
     * @return словарь с состоянием
     * @throws Exception ошибки доступа к файлу
     */
    public SubDictionary readState(String prefix) throws Exception{
        SubDictionary<String, String> subDictionary = new SubDictionary<>(new HashMap<>(), prefix);
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(prefix)) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    subDictionary.put(parts[0].split("\\.")[1], parts[1]);
                }
            }
        }
        return subDictionary;
    }
}
