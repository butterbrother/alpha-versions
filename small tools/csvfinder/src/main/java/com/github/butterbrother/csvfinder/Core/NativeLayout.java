package com.github.butterbrother.csvfinder.Core;

import javax.swing.*;

/**
 * Пытается задействовать нативный системный внешний вид.
 * Linux - GTK+, Windows - нативный вид.
 * Остальное - по-умолчанию
 */
public class NativeLayout {
    public static void apply() {
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
    }
}
