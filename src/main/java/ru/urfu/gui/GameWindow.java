package ru.urfu.gui;

import ru.urfu.saveUtil.Savable;
import ru.urfu.saveUtil.Saver;
import ru.urfu.saveUtil.SubDictionary;
import javax.swing.*;
import java.awt.*;

/**
 * Внутренне окно - Игровое поле, по которому перемещается робот
 */
public class GameWindow extends JInternalFrame implements Savable
{
    public GameWindow(GameVisualizer m_visualizer) {
        super("Игровое поле", true, true, true, true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

    @Override
    public String getPrefix() {
        return "model";
    }

    @Override
    public SubDictionary<String, String> getWindowState() {
        return Saver.buildState(this);
    }

    @Override
    public void setWindowState(SubDictionary<String, String> state) {
        try {
            Saver.setState(this, state);
        } catch (Exception e) {
            setLocation(400, 50);
            setSize(500, 500);
            e.printStackTrace();
        }
    }
}
