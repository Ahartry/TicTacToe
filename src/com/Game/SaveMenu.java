package com.Game;

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
    
    public SaveMenu(String saveName, LFrame frame, int game, Sound sound){

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

        GButton deleteButton = new GButton("Delete");
        deleteButton.setFont(font);
        deleteButton.setSound(sound);

        //set sizes
        Dimension d = label.getPreferredSize();
        d.width = 150;
        label.setPreferredSize(d);
        
        add(label);
        add(loadButton);
        add(deleteButton);

        //gets move number
        String path = System.getProperty("user.dir") + File.separator + "Saves" + File.separator + Integer.toString(game) + File.separator + saveName;
        File file = new File(path);
        int fileCount=file.list().length;
        String savePath = path + File.separator + "move" + (fileCount - 1);
        File saveFile = new File(savePath);


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
                File fileD = new File(path + File.separator);
                System.out.println("Deleting " + fileD);
                String[]entries = fileD.list();
                for(String s: entries){
                    File currentFile = new File(fileD.getPath(),s);
                    currentFile.delete();
                }
                fileD.delete();
                frame.refreshList(frame);
            }

        });

    }
}
