package com.github.butterbrother.csvfinder;

import com.github.butterbrother.csvfinder.Core.NativeLayout;
import com.github.butterbrother.csvfinder.Core.PathWithAttr;
import com.github.butterbrother.csvfinder.Core.TextPopup;
import com.github.butterbrother.csvfinder.Core.UILocalize;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Основное и единственное GUI приложения.
 * Поиск в огромных csv-файлах.
 * Результат отображается в таблице.
 */
public class TableForm {
    // Числовое представление этих кодировок
    // Порядок должен совпадать с позицией в массиве
    private static final int ENCODING_WINDOWS = 0;
    private static final int ENCODING_UTF8 = 1;
    private static final int ENCODING_DOS = 2;
    private static final int ENCODING_MAC = 3;
    private JPanel TablePanel;
    private JScrollPane ScrollBackground;
    private JTable ResultPanel;
    private JPanel SearchBackground;
    private JLabel SearchLabel;
    private JTextField SearchField;
    private JButton DoSearch;
    private JPanel OptionsPanel;
    private JLabel FileNameLabel;
    private JLabel FileNameDisplay;
    private JButton DoBrowseFile;
    private JPanel FileOptionsPanel;
    private JLabel EncodingLabel;
    private JComboBox Encoding;
    private JLabel SeparatorLabel;
    private JTextField Separator;
    private JCheckBox HeaderInFirst;
    private JProgressBar ProgressBar;
    private JLabel StatusBar;
    private JCheckBox FastSearch;
    private JPanel ControlPanel;

    // Путь к текущему выбранному файлу
    private PathWithAttr selectedCsvPath = null;

    private CsvSearcher csvSearcher = null;
    // Доступные кодировки
    private String Encodings[] = {
            "Кириллица Windows",
            "UTF-8",
            "Кириллица DOS",
            "Кириллица MAC"
    };

    /**
     * Основная форма приложения
     */
    private TableForm() {
        $$$setupUI$$$();

        JFrame thisWindow = new JFrame("CSV Finder");
        thisWindow.setContentPane(TablePanel);
        thisWindow.pack();
        thisWindow.setSize(630, 460);
        thisWindow.setVisible(true);
        FastSearch.setVisible(false);   // Пока не работает

        new TextPopup(SearchField);

        // Закрытие окна
        thisWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // TODO: сюда добавить сохранение настроек: положение окна, размер
                System.exit(0);
            }
        });

        // Нажание на кнопку "Обзор", открытие csv-файла
        DoBrowseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser CSVFileChooser = new JFileChooser(System.getProperty("user.dir"));
                CSVFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                CSVFileChooser.setDialogTitle("Выберите файл");
                CSVFileChooser.showDialog(null, "Выбрать");

                File selectedFile = CSVFileChooser.getSelectedFile();

                if (selectedFile != null) {
                    Path tmpPath = selectedFile.toPath();
                    if (Files.exists(tmpPath) && Files.isRegularFile(tmpPath)) {
                        selectedCsvPath = new PathWithAttr(tmpPath);

                        FileNameDisplay.setText(selectedCsvPath.toString());
                    }
                }
            }
        });

        // Нажатие на кнопку поиска
        DoSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchInCSV();
            }
        });
        SearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    searchInCSV();
            }
        });
    }

    /**
     * Точка входа в наше приложение.
     *
     * @param args cmdline
     */
    public static void main(String args[]) {
        // Применяем нативный внешний вид
        NativeLayout.apply();

        // Локализуем диалоги
        UILocalize.ru_RU();

        new TableForm();
    }

    /**
     * Запуск поиска в CSV-файле
     */
    private void searchInCSV() {
        lockControls();

        if (csvSearcher == null || csvSearcher.isDone()) {
            csvSearcher = new CsvSearcher(this);
        } else if (csvSearcher != null || !csvSearcher.isDone()) {
            csvSearcher.stop();
        }
    }

    /**
     * Блокирует элементы управления на время поиска.
     * Меняет кнопку старта поиска на "стоп".
     */
    private void lockControls() {
        StatusBar.setText("Подготовка...");
        // Блокируем управление
        SearchField.setEnabled(false);
        DoSearch.setEnabled(false);
        DoBrowseFile.setEnabled(false);
        Encoding.setEnabled(false);
        Separator.setEnabled(false);
        HeaderInFirst.setEnabled(false);

        ResultPanel.setEnabled(false);
        if (ResultPanel.isEditing())
            ResultPanel.getCellEditor().stopCellEditing();

        // Меняем заговок кнопки поиска
        DoSearch.setText("Стоп");
        // И активируем обратно
        DoSearch.setEnabled(true);
    }

    /**
     * Выполняет разблокировку управления, после поиска.
     * Вызывается на стороне поискового потока.
     */
    void unlockControls() {
        StatusBar.setText("Готово");
        DoSearch.setText("Поиск");

        SearchField.setEnabled(true);
        DoBrowseFile.setEnabled(true);
        Encoding.setEnabled(true);
        Separator.setEnabled(true);
        HeaderInFirst.setEnabled(true);

        replaceTableEditor(ResultPanel);
        ResultPanel.setEnabled(true);

        ProgressBar.setValue(0);
        ProgressBar.setStringPainted(false);
    }

    /**
     * Меняет редактор ячеек на текстовые поля с контекстным меню
     * и редактированием по одиночному щелчку.
     *
     * @param table таблица
     */
    private void replaceTableEditor(JTable table) {
        // Создаём редактор ячеек - текстовое поле с popup и одиночным кликом
        // для начала редактирования
        JTextField fieldWithPopup = new JTextField();
        new TextPopup(fieldWithPopup);
        DefaultCellEditor singleClick = new DefaultCellEditor(fieldWithPopup);
        singleClick.setClickCountToStart(1);

        // Назначаем редактор для каждой колонки
        for (int i = 0; i < table.getColumnModel().getColumnCount(); ++i) {
            TableColumn ResultTableColumn = table.getColumnModel().getColumn(i);
            ResultTableColumn.setCellEditor(singleClick);
        }

        // Меняем выделяющий клик на двойной
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Возвращает выбранное имя файла
     *
     * @return путь к выбранному файлу
     */
    PathWithAttr getSelectedCsvPath() {
        return selectedCsvPath;
    }

    /**
     * Возвращает выбранную пользователем кодировку
     *
     * @return кодировка
     */
    Charset getSelectedCharset() {
        switch (Encoding.getSelectedIndex()) {
            case ENCODING_WINDOWS:
                return Charset.forName("WINDOWS-1251");
            case ENCODING_UTF8:
                return Charset.forName("UTF-8");
            case ENCODING_DOS:
                return Charset.forName("CP866");
            case ENCODING_MAC:
                return Charset.forName("MacCyrillic");
        }

        return Charset.defaultCharset();
    }

    /**
     * Возвращает выбор - содержит ли файл имена колонок
     * в первой строке.
     *
     * @return флаг
     */
    boolean csvHaveHeaders() {
        return HeaderInFirst.isSelected();
    }

    /**
     * Возвращает разделитель колонок.
     *
     * @return разделитель
     */
    String getDelimiter() {
        return Separator.getText().isEmpty() ? ";" : Separator.getText();
    }

    /**
     * Возвращает строку поиска.
     *
     * @return строка поиска
     */
    String getSearchQuery() {
        return SearchField.getText().toLowerCase();
    }

    /**
     * Возвращает таблицу с результатами поиска.
     *
     * @return таблица
     */
    JTable getResultPanel() {
        return ResultPanel;
    }

    /**
     * Возвращает прогрессбар
     *
     * @return прогрессбар
     */
    JProgressBar getProgressBar() {
        return ProgressBar;
    }

    /**
     * Возвращает статусбар
     *
     * @return статусбар
     */
    JLabel getStatusBar() {
        return StatusBar;
    }

    /**
     * Возвращает выбор - включен ли fast search
     *
     * @return статус быстрого поиска
     */
    boolean isFastSearch() {
        return FastSearch.isSelected();
    }

    /**
     * Ручная генерация компонентов, вне редактора GUI
     */
    private void createUIComponents() {
        ResultPanel = new JTable(new DefaultTableModel());
        Encoding = new JComboBox<>(Encodings);

        // Немного увеличиваем высоту ячеек
        int rowHeight = (int) (ResultPanel.getRowHeight() * 1.2);
        ResultPanel.setRowHeight(rowHeight);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        TablePanel = new JPanel();
        TablePanel.setLayout(new BorderLayout(0, 0));
        ScrollBackground = new JScrollPane();
        TablePanel.add(ScrollBackground, BorderLayout.CENTER);
        ScrollBackground.setViewportView(ResultPanel);
        SearchBackground = new JPanel();
        SearchBackground.setLayout(new GridBagLayout());
        TablePanel.add(SearchBackground, BorderLayout.NORTH);
        SearchLabel = new JLabel();
        SearchLabel.setText("Найти:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        SearchBackground.add(SearchLabel, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SearchBackground.add(spacer1, gbc);
        SearchField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SearchBackground.add(SearchField, gbc);
        DoSearch = new JButton();
        DoSearch.setText("Поиск");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SearchBackground.add(DoSearch, gbc);
        OptionsPanel = new JPanel();
        OptionsPanel.setLayout(new GridBagLayout());
        TablePanel.add(OptionsPanel, BorderLayout.SOUTH);
        FileOptionsPanel = new JPanel();
        FileOptionsPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        OptionsPanel.add(FileOptionsPanel, gbc);
        ProgressBar = new JProgressBar();
        ProgressBar.setOrientation(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        FileOptionsPanel.add(ProgressBar, gbc);
        StatusBar = new JLabel();
        StatusBar.setText("Готов.");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        FileOptionsPanel.add(StatusBar, gbc);
        ControlPanel = new JPanel();
        ControlPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        OptionsPanel.add(ControlPanel, gbc);
        FileNameLabel = new JLabel();
        FileNameLabel.setText("Имя файла:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        ControlPanel.add(FileNameLabel, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ControlPanel.add(spacer2, gbc);
        FileNameDisplay = new JLabel();
        FileNameDisplay.setText("[Не выбрано]");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        ControlPanel.add(FileNameDisplay, gbc);
        EncodingLabel = new JLabel();
        EncodingLabel.setText("Кодировка:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        ControlPanel.add(EncodingLabel, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ControlPanel.add(spacer3, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ControlPanel.add(Encoding, gbc);
        SeparatorLabel = new JLabel();
        SeparatorLabel.setText("Разделитель:");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        ControlPanel.add(SeparatorLabel, gbc);
        Separator = new JTextField();
        Separator.setText(";");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        ControlPanel.add(Separator, gbc);
        DoBrowseFile = new JButton();
        DoBrowseFile.setText("Обзор");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ControlPanel.add(DoBrowseFile, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.BOTH;
        ControlPanel.add(panel1, gbc);
        HeaderInFirst = new JCheckBox();
        HeaderInFirst.setSelected(true);
        HeaderInFirst.setText("Заголовки в первой строке");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(HeaderInFirst, gbc);
        FastSearch = new JCheckBox();
        FastSearch.setText("Ускоренный поиск (регистр игнорируется)");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(FastSearch, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ControlPanel.add(spacer4, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return TablePanel;
    }
}
