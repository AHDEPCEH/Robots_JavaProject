package ru.urfu.gui;

import ru.urfu.log.LogChangeListener;
import ru.urfu.log.LogEntry;
import ru.urfu.log.LogWindowSource;
import ru.urfu.log.Logger;
import ru.urfu.saveUtil.Savable;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;


/**
 * Внутреннее окно для отображения событий(логов)
 */
public class LogWindow extends JInternalFrame implements LogChangeListener, Savable
{
    private final LogWindowSource logSource;
    private final TextArea logContent;

    /**
     * Конструктор класса
     */
    public LogWindow()
    {
        super("Протокол работы", true, true, true, true);
        setSize(300, 600);
        setLocation(50, 50);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                logSource.unregisterListener(LogWindow.this);
                dispose();
            }
        });
        logSource = Logger.getDefaultLogSource();
        logSource.registerListener(this);
        logContent = new TextArea("");
        logContent.setSize(200, 500);
        Logger.debug("Протокол работает");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        updateLogContent();
    }

    /**
     * Добавление новых записей в очередь для отображения на окне
     */
    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        logContent.setText(content.toString());
        logContent.invalidate();
    }

    /**
     * Обновление данных в окне
     */
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public String getPrefix() {
        return "log";
    }
}
