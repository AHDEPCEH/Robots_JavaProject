package ru.urfu.gui;

import ru.urfu.saveUtil.Savable;
import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Внутренне окно - Игровое поле, по которому перемещается робот
 */
public class GameWindow extends JInternalFrame implements Savable, Localizable {
    private JPanel panel;
    public GameWindow(GameVisualizer gameVisualizer) {
        super("Игровое поле", true, true, true, true);
        setLocation(400, 50);
        setSize(500, 500);
        panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

    public void addPanel(JPanel visualizer) {
        panel.add(visualizer, BorderLayout.CENTER);
    }

    @Override
    public String getPrefix() {
        return "model";
    }

    @Override
    public String getObjectName() {
        return "model";
    }

    @Override
    public void onUpdateContent(ResourceBundle resourceBundle) {
        setTitle(resourceBundle.getString("title"));
    }
}
