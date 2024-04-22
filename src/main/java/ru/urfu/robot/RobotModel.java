package ru.urfu.robot;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Модель поведения робота
 */
public class RobotModel {
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;
    private double angleToTarget;

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    private final PropertyChangeSupport supporter;

    public RobotModel(){
        supporter = new PropertyChangeSupport(this);
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public void setTargetPosition(Point p)
    {
        supporter.firePropertyChange("property", (double) m_targetPositionX, p.x);
        supporter.firePropertyChange("property", (double) m_targetPositionY, p.y);
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    private double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        double newAngle = asNormalizedRadians(Math.atan2(diffY, diffX));
        if (supporter != null) {
            supporter.firePropertyChange("angle", (int) angleToTarget, (int) newAngle);
        }
        return newAngle;
    }

    public void onModelUpdateEvent() {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity;
        if (asNormalizedRadians(angleToTarget - m_robotDirection) < Math.PI) {
            angularVelocity = maxAngularVelocity;
        } else {
            angularVelocity = -maxAngularVelocity;
        }

        moveRobot(maxVelocity, angularVelocity, 10);
    }

    /**
     * Добавление слушателей для подписки на изменение модели робота
     * @param listener - слушатель
     */
    public void setPropertyChangeListener(PropertyChangeListener listener) {
        supporter.addPropertyChangeListener(listener);
    }

    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        return Math.min(value, max);
    }
    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection  + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection  + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        if (supporter != null) {
            supporter.firePropertyChange("property", (int) m_robotPositionX, newX);
            supporter.firePropertyChange("property", (int) m_robotPositionY, newY);
            supporter.firePropertyChange("property", m_robotDirection, newDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        m_robotDirection = newDirection;
    }

    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    public double getRobotPositionX(){
        return m_robotPositionX;
    }

    public double getRobotPositionY(){
        return m_robotPositionY;
    }

    public int getTargetPositionX() { return m_targetPositionX; }

    public int getTargetPositionY() { return m_targetPositionY; }

    public double getRobotDirection() { return m_robotDirection; }

    public double getAngleToTarget(){
        return angleToTarget;
    }
}