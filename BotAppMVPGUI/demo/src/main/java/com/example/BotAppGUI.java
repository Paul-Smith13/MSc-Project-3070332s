package com.example;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities; //<--Thread-safe UI updates

import java.awt.BorderLayout;


public class BotAppGUI {
    private JFrame frame;
    private ChooseFileButton chooseFileButton;
    private BotScriptPanel botScriptsPanel;
    private BotAccountsPanel botAccountsPanel;
    private RunBotscriptButton runBotscriptButton;

    //Default Constructor
    public BotAppGUI(){

        frame = new JFrame("MVP Bot Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); //Width by height
        frame.setLocationRelativeTo(null); //null makes it go in screen center
        
        JPanel contentPane = new JPanel(new BorderLayout());
        frame.setContentPane(contentPane);

        JPanel topPanel = new JPanel();

        chooseFileButton = new ChooseFileButton(frame);
        topPanel.add(chooseFileButton);

        runBotscriptButton = new RunBotscriptButton(frame);
        topPanel.add(runBotscriptButton);

        contentPane.add(topPanel, BorderLayout.NORTH);
        
        botScriptsPanel = new BotScriptPanel();
        contentPane.add(botScriptsPanel, BorderLayout.CENTER);

        botAccountsPanel = new BotAccountsPanel();
        contentPane.add(botAccountsPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        SwingUtilities.invokeLater(() ->{
            botScriptsPanel.addBotscriptRow("AutoClicker.py", "Draynor", "High alching", "5");
            botScriptsPanel.addBotscriptRow("Fishing", "Catherby", "Lobsters", "3");
        
            botAccountsPanel.addBotAccountRow("Unsinkable", "Running", "60mins", "Auto-alcher");
            botAccountsPanel.addBotAccountRow("BotsALot", "Running", "1hr", "Fishing");
            botAccountsPanel.addBotAccountRow("Sleepy", "Inactive", "0mins", "N/A");

        });

    }

    public static void main(String[] args){
        //Creates a new unnamed class which implements Runnable interface
        //It is an Anonymouse Inner class
        SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                new BotAppGUI();
            }
        });
    }
}
