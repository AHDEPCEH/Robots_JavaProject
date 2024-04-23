package ru.urfu.gui;

import ru.urfu.log.LogChangeListener;
import ru.urfu.log.LogEntry;
import ru.urfu.log.LogWindowSource;
import ru.urfu.log.Logger;
import ru.urfu.saveUtil.Savable;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Внутреннее окно для отображения событий(логов)
 */
public class LogWindow extends JInternalFrame implements LogChangeListener, Savable
{
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;

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
                m_logSource.unregisterListener(LogWindow.this);
                dispose();
            }
        });
        m_logSource = Logger.getDefaultLogSource();
        m_logSource.registerListener(this);
        Logger.debug("Протокол работает");
        JPanel panel = new JPanel(new GridLayout(2, 1));
        m_logContent = new TextArea("");
        JTextField smallField = getTextField();
        panel.add(m_logContent);
        panel.add(smallField);
        getContentPane().add(panel);
        updateLogContent();
    }

    private JTextField getTextField() {
        JTextField smallField = new JTextField(15);
        smallField.setToolTipText("Короткое поле");
        smallField.setBounds(0, 510, 50, 20);
        smallField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String[] message = smallField.getText().split(" ");
                    if (message.length == 2) {
                        int indexFrom = Integer.parseInt(message[0]);
                        int indexTo = Integer.parseInt(message[1]);
                        showLogSegment(indexFrom, indexTo);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LogWindow.this,
                            "Введите через пробел 2 числа чтобы посмотреть записи за указанный период");
                }
            }
        });
        return smallField;
    }

    /**
     * Отображение записей в окне
     */
    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    private void showLogSegment(int indexFrom, int indexTo) {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.range(indexFrom, indexTo - indexFrom)) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
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
