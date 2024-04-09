package ru.urfu.robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Класс для отрисовки игры
 */
public class Visualizer extends JPanel implements PropertyChangeListener {
    private double robotPositionX = 100;
    private double robotPositionY = 100;
    private int targetPositionX = 150;
    private int targetPositionY = 100;
    private double robotDirection = 0;
    public Visualizer(Controller controller) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.setClickCoordinates(e.getPoint());
            }
        });
        setDoubleBuffered(true);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawRobot(g2d, round(robotPositionX), round(robotPositionY), robotDirection);
        drawTarget(g2d, round(targetPositionX), round(targetPositionY));
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        AffineTransform t = AffineTransform.getRotateInstance(direction, x, y);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, x, y, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, x  + 10, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x  + 10, y, 5, 5);
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    private static int round(double value) {
        return (int)(value + 0.5);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RobotModel model = (RobotModel) evt.getSource();
        robotPositionX = model.getRobotPositionX();
        robotPositionY = model.getRobotPositionY();
        robotDirection = model.getRobotDirection();
        targetPositionX = model.getTargetPositionX();
        targetPositionY = model.getTargetPositionY();
        EventQueue.invokeLater(this::repaint);
    }
}
