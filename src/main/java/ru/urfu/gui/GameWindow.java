package ru.urfu.gui;

import ru.urfu.saveUtil.FileManager;
import ru.urfu.saveUtil.Savable;
import ru.urfu.saveUtil.Saver;
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
    private final Saver saver = new Saver();

    public GameWindow(FileManager fileManager)
    {
        super("Игровое поле", true, true, true, true);
        this.fileManager = fileManager;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        recoverState();
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
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
        fileManager.writeState(saver.buildState(this, prefix));
    }

    @Override
    public void recoverState() {
        try {
            saver.setState(this, fileManager.readState(prefix));
        } catch (Exception e) {
            setLocation(400, 50);
            setSize(500, 500);
        }
    }
}
