package ru.urfu.gui;

import ru.urfu.saveUtil.FileManager;
import ru.urfu.saveUtil.Savable;
import ru.urfu.saveUtil.Saver;

import javax.swing.*;
import java.awt.*;

/**
 * Внутренне окно - Игровое поле, по которому перемещается робот
 */
public class GameWindow extends JInternalFrame implements Savable
{
    private final String prefix = "model";
    private final FileManager fileManager;

    public GameWindow(FileManager fileManager)
    {
        super("Игровое поле", true, true, true, true);
        this.fileManager = fileManager;
        recoverState();
        GameVisualizer m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

    @Override
    public void saveState() {
        fileManager.writeState(Saver.buildState(this, prefix));
    }

    @Override
    public void recoverState() {
        try {
            Saver.setState(this, fileManager.readState(prefix));
        } catch (Exception e) {
            setLocation(400, 50);
            setSize(500, 500);
        }
    }
}
