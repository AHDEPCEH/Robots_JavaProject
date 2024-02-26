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
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final JMenuBar menuBar = new JMenuBar();

    /**
     * Конструктор окна с его описанием
     */
    public MainApplicationFrame() {
        setBounds(50, 50,screenSize.width  - 100,screenSize.height - 100);
        setContentPane(desktopPane);
        createLogWindow();
        createGameWindow();
        addWindowMenu();
        addViewMenu();
        addTestMenu();
        setJMenuBar(menuBar);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            /**
             * Слушатель событий
             * Переопределённый метод закрытия окна
             */
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = { "Да", "Нет" };
                int n = JOptionPane
                        .showOptionDialog(e.getWindow(), "Вы действительно желаете выйти?",
                                "Подтверждение", JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options,
                                options[0]);
                if (n == JOptionPane.YES_OPTION) {
                    MainApplicationFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
                }
            });
    }

    /**
     * Создание внутреннего окна(Игровое поле)
     */
    protected void createGameWindow() {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);
    }

    /**
     * Создание внутреннего окна с логами
     */
    protected void createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        addWindow(logWindow);
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
     * Создание и добавление пункта меню - окно
     */
    private void addWindowMenu() {
        JMenu windowMenu = new JMenu("Окно");
        windowMenu.setMnemonic(KeyEvent.VK_X);

        {
            JMenuItem addWindowItem = new JMenuItem("Закрыть", KeyEvent.VK_X);
            addWindowItem.addActionListener((event) -> {
                WindowEvent windowClosing = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
                this.dispatchEvent(windowClosing);
            });
            windowMenu.add(addWindowItem);
        }

        menuBar.add(windowMenu);
    }

    /**
     * Создание и добавление пункта меню - Режим отображения
     */
    private void addViewMenu() {
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

        menuBar.add(lookAndFeelMenu);
    }

    /**
     * Создание и добавление пункта меню - Тесты(для вывода записи в окно логов)
     */
    private void addTestMenu() {
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

        menuBar.add(testMenu);
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
