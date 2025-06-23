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

public class BotScriptPanel extends JPanel{
    private DefaultTableModel tableModel;
    private JTable botscriptTable;
    private int selectedRowIndex = -1; //So none are selected by default

    public BotScriptPanel(){
        setLayout(new BorderLayout());

        TitledBorder titledBorder = BorderFactory.createTitledBorder("Available Botscripts");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        setBorder(titledBorder);

        String[] columnNames = {"Select", "Botscript name", "Location", "Doing", "Bots running"};
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
        botscriptTable = new JTable(tableModel);

        //List selection listener
        botscriptTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()){
                int currentSelectedRow = botscriptTable.getSelectedRow();
                handleRowSelection(currentSelectedRow);
            }
        });

        botscriptTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        JScrollPane scrollPane = new JScrollPane(botscriptTable);
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
            botscriptTable.repaint();
        } else if (newSelectedRow == -1 && selectedRowIndex != -1){
            if ( selectedRowIndex < tableModel.getRowCount()){
                tableModel.setValueAt(false, selectedRowIndex, 0);
            }
            selectedRowIndex = -1;
            botscriptTable.repaint();
        }
    }


    //Used to add new rows to table
    public void addBotscriptRow(String name, String location, String status, String runningCount){
        tableModel.addRow(new Object[]{false, name, location, status, runningCount});
    }
    //Updates cell in table
    public void updateTableCell(int rowIndex, int columnIndex, Object newValue){
        if (rowIndex >= 0 && rowIndex < tableModel.getRowCount()
            && columnIndex >= 0 && columnIndex < tableModel.getColumnCount()){
            tableModel.setValueAt(newValue, rowIndex, columnIndex);
        }
    }

}
