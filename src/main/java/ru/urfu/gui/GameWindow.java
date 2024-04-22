package ru.urfu.gui;

import ru.urfu.robot.Visualizer;
import ru.urfu.saveUtil.Savable;
import javax.swing.*;
import java.awt.*;

/**
 * Внутренне окно - Игровое поле, по которому перемещается робот
 */
public class GameWindow extends JInternalFrame implements Savable
{
    public GameWindow(Visualizer visualizer) {
        super("Игровое поле", true, true, true, true);
        setLocation(400, 50);
        setSize(500, 500);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

    @Override
    public String getPrefix() {
        return "model";
    }
}
