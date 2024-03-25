package ru.urfu.gui;

import ru.urfu.saveUtil.FileManager;
import ru.urfu.saveUtil.Savable;
import ru.urfu.saveUtil.SubDictionary;

import java.awt.BorderLayout;
import java.util.HashMap;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 * Внутренне окно - Игровое поле, по которому перемещается робот
 */
public class GameWindow extends JInternalFrame implements Savable
{
    private final String prefix = "model";
    private final FileManager fileManager;

    public GameWindow(FileManager fileManager)
    {
        super("Игровое поле", true, true, true, true);
        this.fileManager = fileManager;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        recoverState();
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                saveState();
                dispose();
            }
        });
        GameVisualizer m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
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
        } catch (Exception e) {
            setLocation(400, 50);
            setSize(500, 500);
        }
    }
}
