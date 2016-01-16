package com.github.butterbrother.ews.redirector;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Хранит, загружает и сохраняет настройки
 * приложения
 */
public class Settings {
    private Path settingsFile = Paths.get(System.getProperty("user.home"), ".ews_redirector.json");
    private JSONObject file;

    /**
     * Инициализация и чтение настроек
     */
    public Settings() {
        if (Files.notExists(settingsFile)) {
            file = new JSONObject();
        } else {
            StringBuilder settingsLoader = new StringBuilder();
            try (BufferedReader reader = Files.newBufferedReader(settingsFile, Charset.forName("UTF-8"))) {
                String buffer;
                while ((buffer = reader.readLine()) != null) {
                    settingsLoader.append(buffer).append('\n');
                }
                file = new JSONObject(settingsLoader.toString());
            } catch (JSONException | IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Unable to read settings", JOptionPane.ERROR_MESSAGE);
                file = new JSONObject();
            }
        }
    }

    /**
     * Сохранение настроек
     */
    public void saveSettings() {
        try (BufferedWriter writer = Files.newBufferedWriter(settingsFile, Charset.forName("UTF-8"))) {
            writer.write(file.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Unable to save settings", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int getInteger(String key) {
        return file.getInt(key);
    }

    public String getString(String key) {
        return file.getString(key);
    }

    public void setInteger(String key, int value) {
        file.put(key, value);
    }

    public void setString(String key, String value) {
        file.put(key, value);
    }

    public boolean getBoolean(String key) {
        return file.getBoolean(key);
    }

    public void setBoolean(String key, boolean value) {
        file.put(key, value);
    }
}
