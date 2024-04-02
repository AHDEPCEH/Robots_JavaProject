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
    public GameWindow()
    {
        super("Игровое поле", true, true, true, true);
        GameVisualizer m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

    @Override
    public String getPrefix() {
        String prefix = "model";
        return prefix;
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
