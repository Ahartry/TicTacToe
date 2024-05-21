package com.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SFrame extends JFrame{

    boolean clicked = false;
    
    public SFrame(){

        Font font = new Font(null);
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        //set name based on other folders
        String path = System.getProperty("user.dir") + File.separator + "Saves";
        File file = new File(path);
        
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        ArrayList<String> saveList = new ArrayList<String>(Arrays.asList(directories));
        int autoTally = 0;
        for(int i = 0; i < saveList.size(); i++){
            //bootleg solution
            if(saveList.get(i).startsWith("Game ") && Character.isDigit(saveList.get(i).charAt(saveList.get(i).length()-1))){
                autoTally++;
            }
        }
        //sets the new folder name
        autoTally++;

        JTextField nameField = new JTextField(10);
        nameField.setText("Game " + autoTally);
        nameField.setForeground(new Color(150, 150, 150));

        GButton okButton = new GButton("Start");
        okButton.setFont(font);

        nameField.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(!clicked){
                    clicked = true;
                    nameField.setText("");
                    repaint();
                    revalidate();
                    nameField.setForeground(Color.BLACK);
                }           
            }
        });

        Dimension d = okButton.getPreferredSize();
        d.width = 120;
        okButton.setPreferredSize(d); 

        nameField.setFont(font);

        JLabel label = new JLabel("Enter game name for autosaves");
        label.setFont(font);

        gbc.gridy = 0;
        add(label, gbc);

        gbc.gridy = 1;
        add(nameField, gbc);

        gbc.gridy = 2;
        add(Box.createRigidArea(new Dimension(0, 30)), gbc);

        gbc.gridy = 3;
        add(okButton, gbc);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 200);
        setLocationRelativeTo(null);
        setTitle("Set Game Name");
        setVisible(true);

        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String output = file.toString() + File.separator + nameField.getText();
                File outputFile = new File(output);
                File move0 = new File(output + File.separator + "move0");

                try {
                    dispose();
                    new File(output).mkdirs();
                    move0.createNewFile();
                    GFrame frame = new GFrame(3);
                    frame.getGPanel().setOutputDir(outputFile);
                } catch (FontFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            }

        });
    }
}
