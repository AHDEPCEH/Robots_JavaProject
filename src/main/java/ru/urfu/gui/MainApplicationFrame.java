package ru.urfu.gui;

import ru.urfu.log.Logger;
import ru.urfu.robot.RobotModel;
import ru.urfu.saveUtil.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

/**
 * Главное окно приложения
 */
public class MainApplicationFrame extends JFrame implements Savable, Localizable
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private String title = "Подтверждение";
    private String message = "Вы действительно желаете выйти?";
    private String button1 = "Да";
    private String button2 = "Нет";

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
        List<Container> frames = new ArrayList<>(Arrays.asList(desktopPane.getAllFrames()));
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
        Object[] options = { button1, button2 };
        int n = JOptionPane.showOptionDialog(this, message,
                title, JOptionPane.YES_NO_CANCEL_OPTION,
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
        menuBar.add(createLanguageMenu());
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
     * Создание пункта меню для смены языка
     */
    private JMenu createLanguageMenu() {
        JMenu languageMenu = new JMenu("Язык");
        languageMenu.setName("language");
        languageMenu.setMnemonic(KeyEvent.VK_L);

        {
            JMenuItem closeItem = new JMenuItem("Русский", KeyEvent.VK_R);
            closeItem.addActionListener((event) -> {
                changeLocale(new Locale("ru"));
            });
            languageMenu.add(closeItem);
        }

        {
            JMenuItem closeItem = new JMenuItem("Pyckuy", KeyEvent.VK_P);
            closeItem.addActionListener((event) -> {
                changeLocale(new Locale("py"));
            });
            languageMenu.add(closeItem);
        }

        return languageMenu;
    }

    /**
     * Создание пункта меню для взаимодействия с окном
     */
    private JMenu createActionMenu() {
        JMenu actionMenu = new JMenu("Действие");
        actionMenu.setName("action");
        actionMenu.setMnemonic(KeyEvent.VK_A);

        {
            JMenuItem closeItem = new JMenuItem("Закрыть окно", KeyEvent.VK_X);
            closeItem.setName("close");
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
        lookAndFeelMenu.setName("look");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");
        
        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.setName("systemLook");
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.setName("crossplatform");
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
        testMenu.setName("test");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem logMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            logMessageItem.setName("log");
            logMessageItem.addActionListener((event) -> Logger.debug("hello"));
            testMenu.add(logMessageItem);
        }
        return testMenu;
    }

    /**
     * Меняет локализацию приложения
     * @param locale - язык приложения
     */
    private void changeLocale(Locale locale) {
        for (Container frame : desktopPane.getAllFrames()) {
            if (frame instanceof Localizable localizable) {
                ResourceBundle bundle = ResourceBundle.getBundle(localizable.getObjectName(), locale);
                localizable.onUpdateContent(bundle);
            }
        }
        onUpdateContent(ResourceBundle.getBundle(getObjectName(), locale));
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
            Logger.error("lookError");
        }
    }

    @Override
    public String getPrefix() {
        return "main";
    }

    @Override
    public String getObjectName() {
        return "main";
    }

    @Override
    public void onUpdateContent(ResourceBundle resourceBundle) {
        title = resourceBundle.getString("title");
        message = resourceBundle.getString("message");
        button1 = resourceBundle.getString("button1");
        button2 = resourceBundle.getString("button2");
        for (int i = 0; i < getJMenuBar().getMenuCount(); i++) {
            JMenu menu = getJMenuBar().getMenu(i);
            if (menu.getName() != null) {
                menu.setText(resourceBundle.getString(menu.getName()));
                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem menuItem = menu.getItem(j);
                    if (menuItem.getName() != null) {
                        menuItem.setText(resourceBundle.getString(menuItem.getName()));
                    }
                }
            }
        }
    }
}
