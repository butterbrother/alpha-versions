package com.github.butterbrother.csvfinder.Core;

import javax.swing.*;

/**
 * Осуществляет локализацию компонентов интерфейса диалоговых окон.
 * Действует для компонентов JFileChooser, OptionPane и ProgressMonitor
 */
public class UILocalize {

    /**
     * Русская локализация
     */
    public static void ru_RU() {
        UIManager.put("FileChooser.openDialogTitleText", "Открыть");
        UIManager.put("FileChooser.saveDialogTitleText", "Сохранить");
        UIManager.put("FileChooser.lookInLabelText", "Каталог:");
        UIManager.put("FileChooser.saveInLabelText", "Каталог:");
        UIManager.put("FileChooser.upFolderToolTipText", "На уровень вверх");
        UIManager.put("FileChooser.homeFolderToolTipText", "Домой");
        UIManager.put("FileChooser.newFolderToolTipText", "Создать каталог");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Отображать элементы списком");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Отображать элементы таблицей");
        UIManager.put("FileChooser.fileNameHeaderText", "Имя:");
        UIManager.put("FileChooser.fileSizeHeaderText", "Размер:");
        UIManager.put("FileChooser.fileTypeHeaderText", "Тип:");
        UIManager.put("FileChooser.fileDateHeaderText", "Изменён:");
        UIManager.put("FileChooser.fileAttrHeaderText", "Атрибуты:");
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип:");
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.openButtonToolTipText", "Открыть выбранный файл");
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить в выбранный файл");
        UIManager.put("FileChooser.directoryOpenButtonText", "Выбрать");
        UIManager.put("FileChooser.directoryOpenButtonToolTipText", "Выбрать данный каталог");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Отменить выбор");
        UIManager.put("FileChooser.updateButtonText", "Обновить");
        UIManager.put("FileChooser.updateButtonToolTipText", "Обновить");
        UIManager.put("FileChooser.helpButtonText", "Помощь");
        UIManager.put("FileChooser.helpButtonToolTipText", "Показать справку");
        UIManager.put("FileChooser.newFolderErrorText", "Возникла ошибка при создании каталога");
        UIManager.put("FileChooser.acceptAllFileFilterText", "Все файлы");

        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");

        UIManager.put("ProgressMonitor.progressText", "Пожалуйста, подождите...");
    }
}
