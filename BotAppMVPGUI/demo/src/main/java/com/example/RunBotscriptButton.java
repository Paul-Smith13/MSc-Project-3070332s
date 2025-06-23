package com.example;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RunBotscriptButton extends JButton {
    private JFrame parentFrame;
    
    //Constructor
    public RunBotscriptButton(JFrame parentFrame){
        super("Run selected botscripts");
        this.parentFrame = parentFrame;
        addRunScriptFunctionality();
    }
    //This will run the scripts it is told
    private void addRunScriptFunctionality(){
        this.addActionListener(e ->{
            String pythonExecutablePath = "C:\\Users\\pauls\\AppData\\Local\\Programs\\Python\\Python311\\python.exe";
            String pythonScriptPath = "C:\\Users\\pauls\\OneDrive\\Desktop\\University of Glasgow\\Semester 3\\MScProject\\BotAppMVPGUI\\demo\\src\\main\\java\\com\\example\\autoClickerTest.py";
            File scriptFile = new File(pythonScriptPath);
            if (!scriptFile.exists()){
                JOptionPane.showMessageDialog(parentFrame, 
                    "ERROR: python script wasn't found at " + pythonScriptPath,
                    "Script not found.",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            new Thread(() -> {
                try {
                    List<String> command = new ArrayList<>();
                    command.add(pythonExecutablePath);
                    command.add(pythonScriptPath);
                    ProcessBuilder processBuilder = new ProcessBuilder(command);
                    //Sets the working directory to the script's directory.
                    processBuilder.directory(new File(scriptFile.getParent()));
                    processBuilder.environment().put("PYTHONUNBUFFERED", "1");
                    processBuilder.redirectErrorStream(true);
                    Process process = processBuilder.start();

                    StringBuilder sbOutput = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while((line = reader.readLine()) != null){
                        System.out.println(line);
                        sbOutput.append(line).append("\n");
                    }
                    reader.close();
                    int exitCode = process.waitFor();
                    System.out.println("Botscript finished with exit code: " + exitCode);
                    if (exitCode == 0 || exitCode == 15){
                        //exitCodes: 0 means successful exit, 15 is specific Windows, relates to script being terminated by SIGTERM signal
                        //15 happens in os.kill() in the python script 
                        JOptionPane.showMessageDialog(parentFrame, 
                            "Botscript " + scriptFile.getName() + " finished.",
                            "Script finished.",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                            JOptionPane.showMessageDialog(parentFrame, 
                            "Botscript " + scriptFile.getName() + " failed with exit code: " + exitCode + "\nOutput:\n" + sbOutput.toString(),
                            "Script error.",
                            JOptionPane.ERROR_MESSAGE);
                    }

                } catch (IOException | InterruptedException ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(parentFrame,
                        "ERROR RUNNING SCRIPT: " + ex.getMessage(),
                        "EXECUTION ERROR",
                        JOptionPane.ERROR_MESSAGE);
                }
            }).start();
        });
    }


}
