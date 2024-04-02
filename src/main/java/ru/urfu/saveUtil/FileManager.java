package ru.urfu.saveUtil;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для записи и чтения состояний окон из файла
 */
public class FileManager {

    private final String fileName = System.getProperty("user.home")+"/save";
    /**
     * Метод для записи состояния объекта в файл
     * @param states - состояние всех сохраняемых окон
     */
    public void writeState(List<SubDictionary<String, String>> states){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (SubDictionary<String, String> state : states) {
                for (String key : state.keySet()) {
                    writer.write(key + "=" + state.get(key.split("\\.")[1]));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для чтения состояний объекта из файла
     * @return словарь с состоянием всех сохранённых окон
     * @throws Exception ошибки доступа к файлу
     */
    public Map<String, SubDictionary<String, String>> readState() throws Exception{
        Map<String, SubDictionary<String, String>> states = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("=");
            String[] parameters = parts[0].split("\\.");
            if (!states.containsKey(parameters[0])) {
                states.put(parameters[0], new SubDictionary<String, String>(new HashMap<>(), parameters[0]));
            }
            states.get(parameters[0]).put(parameters[1], parts[1]);
        }
        return states;
    }
}
