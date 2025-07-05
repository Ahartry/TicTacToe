package com.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class SaveMenu extends JPanel{
    
    public SaveMenu(String saveName, LFrame frame, Sound sound, Color color){

        //make font
        Font font = new Font(null);
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        JLabel label = new JLabel(saveName);
        label.setFont(font);

        GButton loadButton = new GButton("Load");
        loadButton.setFont(font);
        loadButton.setSound(sound);
        loadButton.setColor(color);

        GButton deleteButton = new GButton("Delete");
        deleteButton.setFont(font);
        deleteButton.setSound(sound);
        deleteButton.setColor(color);

        //set sizes
        Dimension d = label.getPreferredSize();
        d.width = 150;
        label.setPreferredSize(d);
        
        add(label);
        add(loadButton);
        add(deleteButton);

        //gets move number
        String path = System.getProperty("user.dir") + File.separator + "Saves" + File.separator + saveName;
        File saveFile = new File(path);


        //button functionality
        loadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                try {
                    frame.dispose();
                    frame.loadSave(saveFile);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        });

        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveFile.delete();
                frame.refreshList(frame);
            }

        });

    }
}
