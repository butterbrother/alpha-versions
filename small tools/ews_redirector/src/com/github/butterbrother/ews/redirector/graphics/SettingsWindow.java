package com.github.butterbrother.ews.redirector.graphics;

import com.github.butterbrother.ews.redirector.Settings;
import org.json.JSONException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Окно с параметрами.
 * Пока что здесь только параметры подключения.
 */
public class SettingsWindow {
    public static final String KEY_WINDOW_HEIGHT = "Settings window height";
    public static final String KEY_WINDOW_WIDTH = "Settings window width";
    public static final String KEY_WINDOW_X = "Settings window X position";
    public static final String KEY_WINDOW_Y = "Settings window Y position";
    public static final String DOMAIN = "Domain name";
    public static final String EMAIL = "e-mail";
    public static final String LOGIN = "User login";
    public static final String PASSWORD = "User password";
    public static final String AUTO_DISCOVER_URL = "Auto discover EWS service URL";
    public static final String AUTO_DISCOVER_ENABLED = "Enable auto discover URL";
    private JPanel switchPanel;
    private JTabbedPane SwitchView;
    private JPanel ConnectionSettings;
    private JLabel LoginLabel;
    private JTextField LoginInput;
    private JLabel PasswordLabel;
    private JPasswordField PasswordInput;
    private JLabel DomainLabel;
    private JTextField DomainInput;
    private JLabel EMailLabel;
    private JTextField EMailInput;
    private JPanel CSInputPanel;
    private JCheckBox AutoDiscover;
    private JLabel URLLabel;
    private JTextField URLInput;
    private JButton ApplyButton;
    private JButton StartStopButton;
    private JButton ExitButton;
    // Родительское окно
    private JFrame win;
    // Настройки
    private Settings settings;
    // Статус активности
    private boolean active;

    public SettingsWindow(Settings settings) {
        this.settings = settings;
        $$$setupUI$$$();

        win = new JFrame("EWS mail redirection");
        win.setContentPane(switchPanel);
        win.pack();
        active = false;

        // Добавляем меню для работы с текстом в поля ввода
        new TextPopup(DomainInput);
        new TextPopup(EMailInput);
        new TextPopup(LoginInput);
        new TextPopup(PasswordInput);
        new TextPopup(URLInput);
        // При закрытии окна - скрываемся
        win.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveWindowPos();
                win.setVisible(false);
            }
        });
        // Активация автоопределения EWS URL
        AutoDiscover.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                URLInput.setEnabled(!AutoDiscover.isSelected());
            }
        });
        // Применение настроек
        ApplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveConnectionSettings();
                // TODO: добавить переподключение к серверу с новыми настройками
            }
        });
        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveWindowPos();
                System.exit(0);
            }
        });
    }

    /**
     * Сохраняет позицию окна в файл конфигурации
     */
    public void saveWindowPos() {
        settings.setInteger(KEY_WINDOW_HEIGHT, win.getHeight());
        settings.setInteger(KEY_WINDOW_WIDTH, win.getWidth());
        settings.setInteger(KEY_WINDOW_X, win.getX());
        settings.setInteger(KEY_WINDOW_Y, win.getY());
        settings.saveSettings();
    }

    /**
     * Сохранение настроек соединения
     */
    public void saveConnectionSettings() {
        settings.setString(DOMAIN, DomainInput.getText());
        settings.setString(EMAIL, EMailInput.getText());
        settings.setString(LOGIN, LoginInput.getText());
        settings.setString(PASSWORD, new String(PasswordInput.getPassword()));
        settings.setBoolean(AUTO_DISCOVER_ENABLED, AutoDiscover.isSelected());
        settings.setString(AUTO_DISCOVER_URL, URLInput.getText());
        settings.saveSettings();
    }

    /**
     * Отображаем окно. При каждом отображении восстанавливаем статус.
     */
    public void showSettingsWin() {
        win.setVisible(true);
        try {
            win.setLocation(settings.getInteger(KEY_WINDOW_X), settings.getInteger(KEY_WINDOW_Y));
            win.setSize(settings.getInteger(KEY_WINDOW_WIDTH), settings.getInteger(KEY_WINDOW_HEIGHT));
            DomainInput.setText(settings.getString(DOMAIN));
            EMailInput.setText(settings.getString(EMAIL));
            LoginInput.setText(settings.getString(LOGIN));
            PasswordInput.setText(settings.getString(PASSWORD));
            AutoDiscover.setSelected(settings.getBoolean(AUTO_DISCOVER_ENABLED));
            URLInput.setText(settings.getString(AUTO_DISCOVER_URL));
            URLInput.setEnabled(!AutoDiscover.isSelected());
        } catch (JSONException ignore) {
            ignore.printStackTrace();
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        switchPanel = new JPanel();
        switchPanel.setLayout(new BorderLayout(0, 0));
        SwitchView = new JTabbedPane();
        switchPanel.add(SwitchView, BorderLayout.CENTER);
        ConnectionSettings = new JPanel();
        ConnectionSettings.setLayout(new GridBagLayout());
        SwitchView.addTab("Connection settings", ConnectionSettings);
        CSInputPanel = new JPanel();
        CSInputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        ConnectionSettings.add(CSInputPanel, gbc);
        EMailLabel = new JLabel();
        EMailLabel.setHorizontalAlignment(4);
        EMailLabel.setHorizontalTextPosition(10);
        EMailLabel.setText("e-mail:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        CSInputPanel.add(EMailLabel, gbc);
        EMailInput = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        CSInputPanel.add(EMailInput, gbc);
        DomainLabel = new JLabel();
        DomainLabel.setHorizontalAlignment(4);
        DomainLabel.setHorizontalTextPosition(10);
        DomainLabel.setText("Domain:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        CSInputPanel.add(DomainLabel, gbc);
        DomainInput = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        CSInputPanel.add(DomainInput, gbc);
        LoginLabel = new JLabel();
        LoginLabel.setText("Login:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        CSInputPanel.add(LoginLabel, gbc);
        LoginInput = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        CSInputPanel.add(LoginInput, gbc);
        PasswordLabel = new JLabel();
        PasswordLabel.setText("Password:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        CSInputPanel.add(PasswordLabel, gbc);
        PasswordInput = new JPasswordField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        CSInputPanel.add(PasswordInput, gbc);
        AutoDiscover = new JCheckBox();
        AutoDiscover.setSelected(true);
        AutoDiscover.setText("Auto discover EWS URL");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        CSInputPanel.add(AutoDiscover, gbc);
        URLLabel = new JLabel();
        URLLabel.setText("EWS URL:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        CSInputPanel.add(URLLabel, gbc);
        URLInput = new JTextField();
        URLInput.setEnabled(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        CSInputPanel.add(URLInput, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ConnectionSettings.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        ConnectionSettings.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        ConnectionSettings.add(spacer3, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        ConnectionSettings.add(panel1, gbc);
        ApplyButton = new JButton();
        ApplyButton.setText("Apply");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(ApplyButton, gbc);
        StartStopButton = new JButton();
        StartStopButton.setText("Start");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(StartStopButton, gbc);
        ExitButton = new JButton();
        ExitButton.setText("Exit");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(ExitButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return switchPanel;
    }
}
