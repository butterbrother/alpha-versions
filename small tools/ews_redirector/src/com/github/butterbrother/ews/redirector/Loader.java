package com.github.butterbrother.ews.redirector;

import com.github.butterbrother.ews.redirector.graphics.SettingsWindow;
import com.github.butterbrother.ews.redirector.graphics.TrayLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * Запуск приложения начинается здесь.
 * Осуществляет загрузку иконки в трее, загружает настройки приложения.
 */
public class Loader {

    public static void main(String args[]) {
        // Пытаемся задействовать системный внешний вид. Linux - GTK+, Windows - нативный вид
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } else if (System.getProperty("os.name").toLowerCase().contains("lin")) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } else {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
        } catch (Exception ignore) {
        }

        try {
            TrayLoader trayLoader = new TrayLoader();
            Settings config = new Settings();
            trayLoader.addIcon();
            SettingsWindow win = new SettingsWindow(config);

            trayLoader.setConfigListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    win.showSettingsWin();
                }
            });
            trayLoader.setIconDoubleClickListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() >= 2)
                        win.showSettingsWin();
                }
            });
            trayLoader.setCloseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    win.saveWindowPos();
                    System.exit(0);
                }
            });
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
