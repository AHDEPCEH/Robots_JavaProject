package ru.urfu.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.util.HashMap;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import ru.urfu.log.LogChangeListener;
import ru.urfu.log.LogEntry;
import ru.urfu.log.LogWindowSource;
import ru.urfu.saveUtil.FileManager;
import ru.urfu.saveUtil.Savable;
import ru.urfu.saveUtil.SubDictionary;


/**
 * Внутреннее окно для отображения событий(логов)
 */
public class LogWindow extends JInternalFrame implements LogChangeListener, Savable
{
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;
    private final String prefix = "log";
    private final FileManager fileManager;

    /**
     * Конструктор класса
     * @param logSource - источник
     */
    public LogWindow(LogWindowSource logSource, FileManager fileManager)
    {
        super("Протокол работы", true, true, true, true);
        this.fileManager = fileManager;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        recoverState();
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                m_logSource.unregisterListener(LogWindow.this);
                saveState();
                dispose();
            }
        });
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        updateLogContent();
    }

    /**
     * Добавление новых записей в очередь для отображения на окне
     */
    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
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
    public void saveState() {
        SubDictionary<String, String> state = new SubDictionary<>(new HashMap<>(), prefix);
        state.put("height", Integer.toString(getHeight()));
        state.put("width", Integer.toString(getWidth()));
        state.put("positionX", Integer.toString(getX()));
        state.put("positionY", Integer.toString(getY()));
        state.put("icon", Boolean.toString(isIcon));
        fileManager.writeState(state);
    }

    @Override
    public void recoverState() {
        try {
            SubDictionary<String, String> state = fileManager.readState(prefix);
            setLocation(Integer.parseInt(state.get("positionX")), Integer.parseInt(state.get("positionY")));
            setSize(Integer.parseInt(state.get("width")), Integer.parseInt(state.get("height")));
            setIcon(Boolean.parseBoolean(state.get("icon")));
        } catch (Exception e){
            setSize(300, 600);
            setLocation(50, 50);
        }
    }
}
