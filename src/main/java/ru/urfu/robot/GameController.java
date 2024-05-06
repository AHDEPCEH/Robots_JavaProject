package ru.urfu.robot;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс для управления моделью робота
 */
public class GameController {
    private final RobotModel model;
    public GameController(RobotModel model) {
        this.model = model;
        Timer timer = new Timer("events generator", true);
        timer.schedule(new TimerTask()
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

}
