package com.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LFrame extends JFrame{

    ArrayList<String> saveList;
    JPanel panel;

    GFrame frame;
    Sound sound;
    Color c;

    public LFrame(GFrame frame, Sound sound, Color color){
        c = color;

        this.sound = sound;

        Font font = new Font(null);
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        this.frame = frame;
        
        //gets all the saves
        String path = System.getProperty("user.dir") + File.separator + "Saves" + File.separator;
        File file = new File(path);
        
        String[] files = file.list();

        saveList = new ArrayList<String>(Arrays.asList(files));

        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        saveList = sortList(saveList);

        //adds the saves
        for(int i = 0; i < saveList.size(); i++){
            gbc.gridy = i;
            panel.add(new SaveMenu(saveList.get(i), this, sound, color), gbc);
        }

        if(saveList.size() == 0){
            JLabel label = new JLabel("No saves stored");
            label.setFont(font);
            panel.add(label);
        }

        add(panel);

        gbc.gridy += 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
    
        panel.add(new JLabel(" "), gbc); 

        //actual frame stuff
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setTitle("Select save");
        setVisible(true);
        setResizable(false);
    }

    public void loadSave(File file) throws Exception{
        
        
    }
    
    public void refreshList(LFrame frame){
        Font font = new Font(null);
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        
        saveList.clear();
        panel.removeAll();
        //gets all the saves
        String path = System.getProperty("user.dir") + File.separator + "Saves";
        File file = new File(path);
        
        String[] files = file.list();

        saveList = new ArrayList<String>(Arrays.asList(files));

        saveList = sortList(saveList);

        //System.out.println(saveList);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;

        //adds the saves
        for(int i = 0; i < saveList.size(); i++){
            //System.out.println(i);
            gbc.gridy = i;
            panel.add(new SaveMenu(saveList.get(i), frame, sound, c), gbc);
        }

        if(saveList.size() == 0){
            JLabel label = new JLabel("No saves stored");
            label.setFont(font);
            panel.add(label);
        }


        gbc.gridy += 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
    
        panel.add(new JLabel(" "), gbc); 

        panel.revalidate();
        panel.repaint();
    }

    public ArrayList<String> sortList(ArrayList<String> input){

        ArrayList<String> output = new ArrayList<String>();

        ArrayList<File> fileList = new ArrayList<File>();
        for(int i = 0; i < input.size(); i++){
            String path = System.getProperty("user.dir") + File.separator + "Saves" + File.separator + input.get(i);
            File file = new File(path);
            fileList.add(file);
        }

        fileList.sort(Comparator.comparingLong(File::lastModified));

        for(int i = 0; i < input.size(); i++){
            output.add(fileList.get(i).getName().toString());
        }

        Collections.reverse(output);

        //System.out.println("Input: " + input + ", output: " + output);
        return output;
    }
}