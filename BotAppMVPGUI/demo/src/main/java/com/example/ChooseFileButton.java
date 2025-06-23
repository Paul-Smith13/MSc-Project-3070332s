package com.example;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.File;


public class ChooseFileButton extends JButton{
    private JFrame parentFrame;
    public ChooseFileButton(JFrame parentFrame){
        super("Add botscript");
        addChooseFileFunctionality();

    }
    private void addChooseFileFunctionality(){
        this.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int result = fileChooser.showOpenDialog(parentFrame);
            if (result == JFileChooser.APPROVE_OPTION){
                File chosenFile = fileChooser.getSelectedFile();
                JOptionPane.showMessageDialog(parentFrame, "Selected file: " + chosenFile.getAbsolutePath());
            } else if (result == JFileChooser.CANCEL_OPTION){
                JOptionPane.showMessageDialog(parentFrame, "File selection cancelled.");
                System.out.println("File selection cancelled.");
            }

        });
    }

}
