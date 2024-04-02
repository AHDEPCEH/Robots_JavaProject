package ru.urfu.gui;

import ru.urfu.log.Logger;
import ru.urfu.saveUtil.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Главное окно приложения
 */
public class MainApplicationFrame extends JFrame implements Savable
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final FileManager fileManager = new FileManager();

    private final List<Savable> savableFrames = new ArrayList<>();

    /**
     * Создание главного окна приложения
     */
    public MainApplicationFrame() {
        addWindow(new LogWindow());
        addWindow(new GameWindow());
        for (JInternalFrame window : desktopPane.getAllFrames()) {
            if (window instanceof Savable){
                savableFrames.add((Savable) window);
            }
        }
        savableFrames.add(this);
        StateManager.recoverAllStates(savableFrames, fileManager);
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
            StateManager.saveAllStates(savableFrames, fileManager);
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
            logMessageItem.addActionListener((event) -> Logger.debug("Новая строка"));
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
            // just ignore
        }
    }

    @Override
    public String getPrefix() {
        return "main";
    }

    @Override
    public SubDictionary<String, String> getWindowState() {
        return Saver.buildState(this);
    }

    @Override
    public void setWindowState(SubDictionary<String, String> state) {
        try {
            Saver.setState(this, state);
        } catch (Exception e){
            setLocation(50, 50);
            setExtendedState(MAXIMIZED_BOTH);
            e.printStackTrace();
        }
    }
}
