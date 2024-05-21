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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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
        Logger.debug("Протокол работает");
        JPanel panel = new JPanel(new GridLayout(2, 1));
        logContent = new TextArea("");
        JTextField smallField = getTextField();
        panel.add(logContent);
        panel.add(smallField);
        getContentPane().add(panel);
        updateLogContent();
    }

    /**
     * Создаёт поле ввода для получения определённых записей
     * @return поле ввода
     */
    private JTextField getTextField() {
        JTextField smallField = new JTextField(15);
        smallField.setToolTipText("Индексы сегмента");
        smallField.setBounds(0, 510, 50, 20);
        smallField.addActionListener(e -> {
            try {
                String[] message = smallField.getText().split(" ");
                if (message.length == 2) {
                    int indexFrom = Integer.parseInt(message[0]);
                    int indexTo = Integer.parseInt(message[1]);
                    showLogSegment(indexFrom, indexTo);
                }
            } catch (Exception ex) {
                Logger.error("Введены некорректные данные");
                JOptionPane.showMessageDialog(LogWindow.this,
                        "Введите через пробел 2 числа чтобы посмотреть записи за указанный период");
            }
        });
        return smallField;
    }

    /**
     * Отображение записей в окне
     */
    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        logContent.setText(content.toString());
        logContent.invalidate();
    }

    private void showLogSegment(int indexFrom, int indexTo) {
        StringBuilder content = new StringBuilder();
        Iterable<LogEntry> logs = logSource.range(indexFrom, indexTo - indexFrom + 1);
        for (LogEntry entry : logs) {
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
