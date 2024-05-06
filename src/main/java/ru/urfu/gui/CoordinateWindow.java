package ru.urfu.gui;

import ru.urfu.robot.RobotModel;
import ru.urfu.saveUtil.Savable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Внутреннее окно с информацией о роботе
 */
public class CoordinateWindow extends JInternalFrame implements Savable, PropertyChangeListener {

    private final TextArea context;
    public CoordinateWindow(RobotModel model) {
        super("Координаты робота", true, true, true, true);
        setSize(300, 300);
        setLocation(300, 300);
        context = new TextArea("");
        context.setSize(100, 100);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(context, BorderLayout.CENTER);
        getContentPane().add(panel);
        model.setPropertyChangeListener(this);
    }

    @Override
    public String getPrefix() {
        return "coordWin";
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof RobotModel model) {
            context.setText("X: " + model.getRobotPositionX() + "\n" +
            "Y: " + model.getRobotPositionY() + "\n" +
            "Angle: " + model.getAngleToTarget() + "\n" +
            "Direction: " + model.getRobotDirection());
        }
    }
}
