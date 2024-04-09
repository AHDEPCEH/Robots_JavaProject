package ru.urfu.gui;

import ru.urfu.saveUtil.Savable;
import ru.urfu.saveUtil.Saver;
import ru.urfu.saveUtil.SubDictionary;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Внутреннее окно с информацией о роботе
 */
public class CoordinateWindow extends JInternalFrame implements Savable, PropertyChangeListener {

    private TextArea context;
    public CoordinateWindow(GameVisualizer m_visualizer) {
        super("Координаты робота", true, true, true, true);
        context = new TextArea("");
        context.setSize(100, 100);
        JPanel panel = new JPanel(new BorderLayout());
        m_visualizer.setPropertyChangeListener(this);
        panel.add(context, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

    @Override
    public String getPrefix() {
        return "coordWin";
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
            setLocation(400, 600);
            setSize(100, 100);
            e.printStackTrace();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        GameVisualizer source = (GameVisualizer) evt.getSource();
        context.setText("X: " + source.getM_robotPositionX() + "\n"
                + "Y: " + source.getM_robotPositionY() + "\n"
                + "Angle: " + source.getAngleToTarget());
    }
}
