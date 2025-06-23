package com.example;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

public class BotAccountsPanel extends JPanel{
    private DefaultTableModel tableModel;
    private JTable botAccountsTable;
    private int selectedRowIndex = -1;

    public BotAccountsPanel(){
        setLayout(new BorderLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Available Accounts");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        setBorder(titledBorder);

        String[] columnNames = {"Select", "Account name", "Bot Status", "Time Running", "Script Running"};
        tableModel = new DefaultTableModel(new Object[][]{}, columnNames){
            @Override
            public boolean isCellEditable(int row, int column){
                return column == 0;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex){
                if (columnIndex == 0){
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        botAccountsTable = new JTable(tableModel);
        //Account selection listener
        botAccountsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()){
                int currentSelectedRow = botAccountsTable.getSelectedRow();
                handleRowSelection(currentSelectedRow);
            }
        });
        botAccountsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row == selectedRowIndex){
                    c.setBackground(new Color(173, 216, 230));
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }
                return c;
            }
        });
        JScrollPane scrollPane = new JScrollPane(botAccountsTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    //Helper
    private void handleRowSelection(int newSelectedRow){
        if (newSelectedRow != -1 && newSelectedRow != selectedRowIndex){
            if (selectedRowIndex != -1 && selectedRowIndex < tableModel.getRowCount()){
                tableModel.setValueAt(false, selectedRowIndex, 0);
            }
            tableModel.setValueAt(true, newSelectedRow, 0);
            selectedRowIndex = newSelectedRow;
            botAccountsTable.repaint();
        } else if (newSelectedRow == -1 && selectedRowIndex != -1){
            if ( selectedRowIndex < tableModel.getRowCount()){
                tableModel.setValueAt(false, selectedRowIndex, 0);
            }
            selectedRowIndex = -1;
            botAccountsTable.repaint();
        }
    }
    //Used to add new rows to table
    public void addBotAccountRow(String botName, String botStatus, String TimeRunning, String ScriptRunning){
        tableModel.addRow(new Object[]{false, botName, botStatus, TimeRunning, ScriptRunning});
    }
    //Updates cell in table
    public void updateTableCell(int rowIndex, int columnIndex, Object newValue){
        if (rowIndex >= 0 && rowIndex < tableModel.getRowCount()
            && columnIndex >= 0 && columnIndex < tableModel.getColumnCount()){
            tableModel.setValueAt(newValue, rowIndex, columnIndex);
        }
    }



}
