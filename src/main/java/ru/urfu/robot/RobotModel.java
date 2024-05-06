package ru.urfu.robot;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Модель поведения робота
 */
public class RobotModel {
    private volatile double robotPositionX = 100;
    private volatile double robotPositionY = 100;
    private volatile double robotDirection = 0;
    private double angleToTarget;

    private volatile int targetPositionX = 150;
    private volatile int targetPositionY = 100;

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
        targetPositionX = p.x;
        targetPositionY = p.y;
        supporter.firePropertyChange("newTarget", null, null);
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
        double distance = distance(targetPositionX, targetPositionY,
                robotPositionX, robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        angleToTarget = angleTo(robotPositionX, robotPositionY, targetPositionX, targetPositionY);
        double angularVelocity;
        if (asNormalizedRadians(angleToTarget - robotDirection) < Math.PI) {
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
        double newX = robotPositionX + velocity / angularVelocity *
                (Math.sin(robotDirection  + angularVelocity * duration) -
                        Math.sin(robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = robotPositionX + velocity * duration * Math.cos(robotDirection);
        }
        double newY = robotPositionY - velocity / angularVelocity *
                (Math.cos(robotDirection  + angularVelocity * duration) -
                        Math.cos(robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = robotPositionY + velocity * duration * Math.sin(robotDirection);
        }
        Double newRobotDirection = asNormalizedRadians(robotDirection + angularVelocity * duration); 
        robotPositionX = newX;
        robotPositionY = newY;
        robotDirection = newRobotDirection;
        supporter.firePropertyChange("newRobot", null, null);
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
        return robotPositionX;
    }

    public double getRobotPositionY(){
        return robotPositionY;
    }

    public int getTargetPositionX() { return targetPositionX; }

    public int getTargetPositionY() { return targetPositionY; }

    public double getRobotDirection() { return robotDirection; }

    public double getAngleToTarget(){
        return angleToTarget;
    }
}