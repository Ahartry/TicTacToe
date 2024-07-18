package com.Game;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;

public class App {

    public static void main(String[] args) throws Exception {
        
        JFrame startFrame = new JFrame();
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(400, 300);
        startFrame.setLocationRelativeTo(null);
        startFrame.setTitle("Tic Tac Toe Game");
        startFrame.setResizable(false);

        JPanel startPanel = new JPanel();
        startPanel.setLayout(new GridBagLayout());
        startPanel.setFocusable(true);
        startPanel.requestFocus();

        setupPanel(startPanel);
        
        startFrame.add(startPanel);

        startFrame.setVisible(true);
    }

    public static void setupPanel(JPanel startPanel) throws Exception {
        startPanel.removeAll();
        startPanel.repaint();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        //gbc.fill = GridBagConstraints.BOTH;

        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);

        GButton regButton = new GButton("Regular");
        GButton ultButton = new GButton("Massive");
        GButton tranButton = new GButton("Ultimate");
        GButton quanButton = new GButton("Quantum");
        GButton loadButton = new GButton("Load");
        
        regButton.setFont(font);
        ultButton.setFont(font);
        tranButton.setFont(font);
        quanButton.setFont(font);
        loadButton.setFont(font);

        regButton.setFocusable(false);
        ultButton.setFocusable(false);
        tranButton.setFocusable(false);
        quanButton.setFocusable(false);
        loadButton.setFocusable(false);

        regButton.setPreferredSize(new Dimension(150, 40));
        ultButton.setPreferredSize(new Dimension(150, 40));
        tranButton.setPreferredSize(new Dimension(150, 40));
        quanButton.setPreferredSize(new Dimension(150, 40));

        //adds the items to the panel
        // gbc.gridx = 1;
        // gbc.gridy = 5;

        Insets i = new Insets(30, 0, 0, 0);

        gbc.insets = i;

        gbc.gridx = 1;
        gbc.gridy = 6;

        startPanel.add(loadButton, gbc);

        //this is a silly solution, but it definitely works
        i = new Insets(0, 0, 0, 0);
        gbc.insets = i;

        // gbc.gridx = 0;
        // gbc.gridy = 0;

        //gbc.gridx = 0;
        gbc.gridy = 1;

        startPanel.add(regButton, gbc);

        // gbc.gridx = 2;

        gbc.gridy = 2;

        startPanel.add(ultButton, gbc);

        // gbc.gridx = 0;
        // gbc.gridy = 2;

        gbc.gridy = 3;

        startPanel.add(tranButton, gbc);

        // gbc.gridx = 2;

        gbc.gridy = 4;

        startPanel.add(quanButton, gbc);

        regButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts regular game
                try {
                    new GFrame(1, false);
                } catch (FontFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            } 
        });

        ultButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts massive game
                askIfUserWantsBot(2, startPanel, font);
            } 
        });

        tranButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts ultimate game
                new SFrame();
            } 
        });

        quanButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts quantum game
                try {
                    new GFrame(4, false);
                } catch (FontFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            } 
        });

        loadButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 

                new LFrame();

            } 
            
        });

        startPanel.repaint();
        startPanel.revalidate();
        startPanel.setVisible(true);
    }

    public static void askIfUserWantsBot(int gameType, JPanel panel, Font font){
        panel.removeAll();
        panel.repaint();
        panel.setLayout(new GridBagLayout());

        GButton singleplayerButton = new GButton("Singleplayer");
        GButton multiplayerButton = new GButton("Multiplayer");

        singleplayerButton.setFont(font);
        multiplayerButton.setFont(font);

        singleplayerButton.setPreferredSize(new Dimension(200, 40));
        multiplayerButton.setPreferredSize(new Dimension(200, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;

        panel.add(singleplayerButton, gbc);

        gbc.gridx = 1;

        panel.add(multiplayerButton, gbc);

        panel.repaint();
        panel.revalidate();
        panel.setVisible(true);

        singleplayerButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 

                try {
                    new GFrame(gameType, true);
                    setupPanel(panel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            } 
            
        });
        multiplayerButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 

                try {
                    new GFrame(gameType, false);
                    setupPanel(panel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            } 
            
        });
    }

}
