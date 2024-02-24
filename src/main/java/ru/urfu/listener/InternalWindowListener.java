package ru.urfu.listener;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class InternalWindowListener implements InternalFrameListener {
    /**
     * Invoked when a internal frame has been opened.
     *
     * @param event an {@code InternalFrameEvent} with information about the
     *          {@code JInteralFrame} that originated the event
     * @see JInternalFrame#show
     */
    @Override
    public void internalFrameOpened(InternalFrameEvent event) {

    }

    /**
     * Invoked when an internal frame is in the process of being closed.
     * The close operation can be overridden at this point.
     *
     * @param event an {@code InternalFrameEvent} with information about the
     *          {@code JInteralFrame} that originated the event
     * @see JInternalFrame#setDefaultCloseOperation
     */
    @Override
    public void internalFrameClosing(InternalFrameEvent event) {
        Object[] options = { "Да", "Нет" };
        int n = JOptionPane
                .showOptionDialog(event.getInternalFrame(), "Закрыть окно?",
                        "Подтверждение", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options,
                        options[0]);
        if (n == 0) {
            event.getInternalFrame().setVisible(false);
            event.getInternalFrame().dispose();
        }
    }

    /**
     * Invoked when an internal frame has been closed.
     *
     * @param event an {@code InternalFrameEvent} with information about the
     *          {@code JInteralFrame} that originated the event
     * @see JInternalFrame#setClosed
     */
    @Override
    public void internalFrameClosed(InternalFrameEvent event) {

    }

    /**
     * Invoked when an internal frame is iconified.
     *
     * @param event an {@code InternalFrameEvent} with information about the
     *          {@code JInteralFrame} that originated the event
     * @see JInternalFrame#setIcon
     */
    @Override
    public void internalFrameIconified(InternalFrameEvent event) {

    }

    /**
     * Invoked when an internal frame is de-iconified.
     *
     * @param event an {@code InternalFrameEvent} with information about the
     *          {@code JInteralFrame} that originated the event
     * @see JInternalFrame#setIcon
     */
    @Override
    public void internalFrameDeiconified(InternalFrameEvent event) {

    }

    /**
     * Invoked when an internal frame is activated.
     *
     * @param event an {@code InternalFrameEvent} with information about the
     *          {@code JInteralFrame} that originated the event
     * @see JInternalFrame#setSelected
     */
    @Override
    public void internalFrameActivated(InternalFrameEvent event) {

    }

    /**
     * Invoked when an internal frame is de-activated.
     *
     * @param event an {@code InternalFrameEvent} with information about the
     *          {@code JInteralFrame} that originated the event
     * @see JInternalFrame#setSelected
     */
    @Override
    public void internalFrameDeactivated(InternalFrameEvent event) {

    }
}
