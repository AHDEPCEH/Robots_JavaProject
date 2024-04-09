package ru.urfu.robot;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс для управления моделью робота
 */
public class Controller{
    private final RobotModel model;
    public Controller(RobotModel model) {
        this.model = model;
        Timer m_timer = initTimer();
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                model.onModelUpdateEvent();
            }
        }, 0, 10);
    }

    /**
     * Метод для установки новых координат роботу
     * @param point - точка клика
     */
    public void setClickCoordinates (Point point) {
        model.setTargetPosition(point);
    }

    /**
     * Инициализация таймера
     * @return Таймер
     */
    private static Timer initTimer()
    {
        return new Timer("events generator", true);
    }

}
