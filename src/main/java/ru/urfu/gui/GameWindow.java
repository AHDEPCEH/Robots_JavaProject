package ru.urfu.gui;

import ru.urfu.saveUtil.Savable;
import javax.swing.*;
import java.awt.*;

/**
 * Внутренне окно - Игровое поле, по которому перемещается робот
 */
public class GameWindow extends JInternalFrame implements Savable
{
    public GameWindow(GameVisualizer gameVisualizer) {
        super("Игровое поле", true, true, true, true);
        setLocation(400, 50);
        setSize(500, 500);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

    @Override
    public String getPrefix() {
        return "model";
    }
}
