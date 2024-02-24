package ru.urfu.listener;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class WindowListener implements java.awt.event.WindowListener {
    public void windowActivated(WindowEvent event) {

    }

    public void windowClosed(WindowEvent event) {

    }

    public void windowClosing(WindowEvent event) {
        Object[] options = { "Да", "Нет" };
        int n = JOptionPane
                .showOptionDialog(event.getWindow(), "Закрыть окно?",
                        "Подтверждение", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options,
                        options[0]);
        if (n == 0) {
            event.getWindow().setVisible(false);
            event.getWindow().dispose();
        }
    }

    public void windowDeactivated(WindowEvent event) {

    }

    public void windowDeiconified(WindowEvent event) {

    }

    public void windowIconified(WindowEvent event) {

    }

    public void windowOpened(WindowEvent event) {

    }
}
