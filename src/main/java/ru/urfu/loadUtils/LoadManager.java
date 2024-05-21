package ru.urfu.loadUtils;

import ru.urfu.gui.GameWindow;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LoadManager extends ClassLoader{
    private String path = ".";
    private GameWindow gameWindow;

    public LoadManager(GameWindow gameWindow){
        this.gameWindow = gameWindow;
    }

    /**
     * Загрузчик классов из jar файла
     * @param extensionName - имя загружаемого jar файла
     */
    public void loadExtension(String extensionName) {
        try {
            Class<?> modelClass = findClass("Project2-1.0-SNAPSHOT.jar", "robot.SecondRobotModel");
            Class<?> controllerClass = findClass(extensionName, "ru.urfu.SecondGameController");
            Class<?> visualizerClass = findClass(extensionName, "ru.urfu.SecondGameVisualizer");
            Object robotModel = modelClass.getConstructor().newInstance();
            Object controller = visualizerClass.getConstructor(modelClass).newInstance(robotModel);
            Object visualizer = visualizerClass.getConstructor(modelClass).newInstance(robotModel);
            if (visualizer instanceof JPanel) {
                gameWindow.addPanel((JPanel) visualizer);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public Class<?> findClass(String jarName, String name) {
        byte[] bytecode = getClassData(jarName, name);
        return defineClass(name, bytecode, 0, bytecode.length);
    }

    /**
     * Побайтовое чтение файла из jar файла
     * @param jarName - имя файла
     * @param name - имя класса
     * @return массив из байтов
     */
    private byte[] getClassData(String jarName, String name) {
        try {
            name = name.replace('.', '/');
            name = name + ".class";

            byte[] buffer = new byte[5242880];
            JarFile jarFile = new JarFile(path + File.separator + jarName);
            Enumeration<?> enu = jarFile.entries();
            while (enu.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enu.nextElement();
                if (jarEntry.getName().equals(name)) {
                    InputStream inputStream = jarFile.getInputStream(jarEntry);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, read);
                    }
                    return byteArrayOutputStream.toByteArray();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
}
