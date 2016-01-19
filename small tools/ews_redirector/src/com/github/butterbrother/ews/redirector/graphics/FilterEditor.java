package com.github.butterbrother.ews.redirector.graphics;

import com.github.butterbrother.ews.redirector.filter.EditableMailFilter;
import com.github.butterbrother.ews.redirector.filter.FilterRule;
import com.github.butterbrother.ews.redirector.filter.MailFilter;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Управление правилом фильтрации
 */
public class FilterEditor {
    private JPanel FilterEditorForm;
    private JLabel FilterNameLabel;
    private JTextField FilterNameInput;
    private JLabel RuleOperatorLabel;
    private JComboBox RuleOperatorList;
    private JPanel UpLevel;
    private JPanel RuleEditLevel;
    private JComboBox RuleType;
    private JComboBox RuleOperator;
    private JTextField RuleValue;
    private JPanel RuleTablesLevel;
    private JPanel RulesTablePanel;
    private JScrollPane RulesTableScroll;
    private JPanel RulesControl;
    private JButton AddRuleButton;
    private JButton RemoveRuleButton;
    private JButton DropRulesButton;
    private JButton SaveFilterButton;
    private JPanel FilterControlLevel;
    private JButton CancelFilterButton;
    private JTable RulesTable;
    private DefaultTableModel RulesTableModel;

    private EditableMailFilter current;

    /**
     * Редактирование существующего фильтра
     * @param filter    Редактируемый фильтр
     */
    protected FilterEditor(EditableMailFilter filter) {
        current = filter;
        $$$setupUI$$$();

        JFrame frame = new JFrame("Filter editor");
        frame.setContentPane(this.FilterEditorForm);
        frame.pack();
        frame.setVisible(true);

        FilterNameInput.setText(current.getName());
    }

    /**
     * Создание нового фильтра
     */
    protected FilterEditor() {
        this(new EditableMailFilter());
    }

    /*
    TODO: здесь должен вызов создания и редактирования фильтра
    public static EditableMailFilter createNewFilter() {

    }

    public static EditableMailFilter editFilter(EditableMailFilter) {

    }
    */

    // Для тестового запуска и пробы
    public static void main(String[] args) {
        new FilterEditor();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        FilterEditorForm = new JPanel();
        FilterEditorForm.setLayout(new BorderLayout(0, 0));
        UpLevel = new JPanel();
        UpLevel.setLayout(new GridBagLayout());
        FilterEditorForm.add(UpLevel, BorderLayout.NORTH);
        FilterNameLabel = new JLabel();
        FilterNameLabel.setHorizontalAlignment(11);
        FilterNameLabel.setHorizontalTextPosition(10);
        FilterNameLabel.setText("Filter name:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        UpLevel.add(FilterNameLabel, gbc);
        FilterNameInput = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 100;
        UpLevel.add(FilterNameInput, gbc);
        RuleOperatorLabel = new JLabel();
        RuleOperatorLabel.setText("Logical operator:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        UpLevel.add(RuleOperatorLabel, gbc);
        RuleOperatorList = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 100;
        UpLevel.add(RuleOperatorList, gbc);
        FilterControlLevel = new JPanel();
        FilterControlLevel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        UpLevel.add(FilterControlLevel, gbc);
        SaveFilterButton = new JButton();
        SaveFilterButton.setText("Save");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        FilterControlLevel.add(SaveFilterButton, gbc);
        CancelFilterButton = new JButton();
        CancelFilterButton.setText("Cancel");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        FilterControlLevel.add(CancelFilterButton, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        FilterControlLevel.add(spacer1, gbc);
        RuleEditLevel = new JPanel();
        RuleEditLevel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        UpLevel.add(RuleEditLevel, gbc);
        RuleEditLevel.setBorder(BorderFactory.createTitledBorder("Rule:"));
        RuleType = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RuleEditLevel.add(RuleType, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RuleEditLevel.add(spacer2, gbc);
        RuleOperator = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RuleEditLevel.add(RuleOperator, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RuleEditLevel.add(spacer3, gbc);
        RuleValue = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RuleEditLevel.add(RuleValue, gbc);
        RuleTablesLevel = new JPanel();
        RuleTablesLevel.setLayout(new GridBagLayout());
        FilterEditorForm.add(RuleTablesLevel, BorderLayout.CENTER);
        RulesTablePanel = new JPanel();
        RulesTablePanel.setLayout(new CardLayout(0, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 400;
        gbc.ipady = 200;
        RuleTablesLevel.add(RulesTablePanel, gbc);
        RulesTableScroll = new JScrollPane();
        RulesTableScroll.setVerticalScrollBarPolicy(22);
        RulesTablePanel.add(RulesTableScroll, "Card1");
        RulesControl = new JPanel();
        RulesControl.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        RuleTablesLevel.add(RulesControl, gbc);
        AddRuleButton = new JButton();
        AddRuleButton.setText("+");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RulesControl.add(AddRuleButton, gbc);
        RemoveRuleButton = new JButton();
        RemoveRuleButton.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RulesControl.add(RemoveRuleButton, gbc);
        DropRulesButton = new JButton();
        DropRulesButton.setText("X");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        RulesControl.add(DropRulesButton, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        RulesControl.add(spacer4, gbc);
        FilterNameLabel.setLabelFor(FilterNameInput);
        RuleOperatorLabel.setLabelFor(RuleOperatorList);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return FilterEditorForm;
    }

    private void createUIComponents() {
        RuleOperatorList = new JComboBox(MailFilter.Operators);
        RuleType = new JComboBox(FilterRule.RuleTypes);
        RuleOperator = new JComboBox(FilterRule.RuleOperators);

        RulesTableModel = new DefaultTableModel();
        RulesTableModel.setColumnCount(3);
        RulesTable = new JTable(RulesTableModel, new DefaultTableColumnModel(), new DefaultListSelectionModel());
    }
}
