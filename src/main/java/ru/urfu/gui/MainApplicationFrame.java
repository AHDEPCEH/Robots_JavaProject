package ru.urfu.gui;

import ru.urfu.log.Logger;
import ru.urfu.robot.RobotModel;
import ru.urfu.saveUtil.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Главное окно приложения
 */
public class MainApplicationFrame extends JFrame implements Savable
{
    private final JDesktopPane desktopPane = new JDesktopPane();

    /**
     * Создание главного окна приложения
     */
    public MainApplicationFrame() {
        setLocation(50, 50);
        setExtendedState(MAXIMIZED_BOTH);
        RobotModel model = new RobotModel();
        addWindow(new LogWindow());
        addWindow(new GameWindow(new GameVisualizer(model)));
        addWindow(new CoordinateWindow(model));
        List<Container> frames = new ArrayList<>
                (Arrays.asList(desktopPane.getAllFrames()));
        frames.add(this);
        StateManager.recoverAllStates(frames);
        setContentPane(desktopPane);
        initJMenuBar(new JMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing();
            }
        });
    }

    /**
     * Процедура закрытия окна
     */
    private void onWindowClosing(){
        Object[] options = { "Да", "Нет" };
        int n = JOptionPane.showOptionDialog(this, "Вы действительно желаете выйти?",
                "Подтверждение", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (n == JOptionPane.YES_OPTION){
            List<Container> frames = new ArrayList<>(Arrays.asList(desktopPane.getAllFrames()));
            frames.add(this);
            StateManager.saveAllStates(frames);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    /**
     * Инициализация меню приложения в виде строки
     * @param menuBar - меню
     */
    private void initJMenuBar(JMenuBar menuBar) {
        menuBar.add(createActionMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createTestMenu());
        setJMenuBar(menuBar);
    }

    /**
     * Метод для добавления и отображения внутренних окон
     * @param frame - Внутреннее окно
     */
    private void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * Создание пункта меню для взаимодействия с окном
     */
    private JMenu createActionMenu() {
        JMenu actionMenu = new JMenu("Действие");
        actionMenu.setMnemonic(KeyEvent.VK_A);

        {
            JMenuItem closeItem = new JMenuItem("Закрыть окно", KeyEvent.VK_X);
            closeItem.addActionListener((event) -> {
                WindowEvent windowClosing = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
                this.dispatchEvent(windowClosing);
            });
            actionMenu.add(closeItem);
        }
        return actionMenu;
    }

    /**
     * Создание пункта меню для смены отображения графического интерфейса
     */
    private JMenu createViewMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");
        
        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }
        return lookAndFeelMenu;
    }

    /**
     * Создание пункта меню, отвечающего за тестирование работы приложения
     */
    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem logMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            logMessageItem.addActionListener((event) -> Logger.debug("Привет от Logger"));
            testMenu.add(logMessageItem);
        }
        return testMenu;
    }

    /**
     * Метод для смены вида графического интерфейса
     */
    private void setLookAndFeel(String className) {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
            Logger.error(e.getMessage());
        }
    }

    @Override
    public String getPrefix() {
        return "main";
    }
}
