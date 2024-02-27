package ru.urfu.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import ru.urfu.log.Logger;

/**
 * Главное окно приложения
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();

    /**
     * Создание главного окна приложения
     */
    public MainApplicationFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(50, 50, screenSize.width  - 100, screenSize.height - 100);
        setContentPane(desktopPane);
        addWindow(createLogWindow());
        addWindow(createGameWindow());
        initJMenuBar(new JMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             */
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = { "Да", "Нет" };
                int n = JOptionPane.showOptionDialog(e.getWindow(), "Вы действительно желаете выйти?",
                        "Подтверждение", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (n == JOptionPane.YES_OPTION) {
                    MainApplicationFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
            }
        });
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
     * Создание внутреннего окна(Игровое поле)
     */
    protected JInternalFrame createGameWindow() {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        return gameWindow;
    }

    /**
     * Создание внутреннего окна с логами
     */
    protected JInternalFrame createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    /**
     * Метод для добавления и отображения внутренних окон
     * @param frame - Внутреннее окно
     */
    protected void addWindow(JInternalFrame frame)
    {
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
            JMenuItem addWindowItem = new JMenuItem("Закрыть окно", KeyEvent.VK_X);
            addWindowItem.addActionListener((event) -> {
                WindowEvent windowClosing = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
                this.dispatchEvent(windowClosing);
            });
            actionMenu.add(addWindowItem);
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
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }
        return testMenu;
    }

    /**
     * Метод для смены вида графического интерфейса
     * @param className
     */
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }

}
