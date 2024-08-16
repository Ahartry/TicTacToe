package com.Game;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
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
        startFrame.setSize(400, 250);
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
        GButton quan3DButton = new GButton("Quantum 3D");
        GButton loadButton = new GButton("Load");
        
        regButton.setFont(font);
        ultButton.setFont(font);
        tranButton.setFont(font);
        quanButton.setFont(font);
        quan3DButton.setFont(font);
        loadButton.setFont(font);

        regButton.setFocusable(false);
        ultButton.setFocusable(false);
        tranButton.setFocusable(false);
        quanButton.setFocusable(false);
        quan3DButton.setFocusable(false);
        loadButton.setFocusable(false);

        regButton.setPreferredSize(new Dimension(180, 40));
        ultButton.setPreferredSize(new Dimension(180, 40));
        tranButton.setPreferredSize(new Dimension(180, 40));
        quanButton.setPreferredSize(new Dimension(180, 40));
        quan3DButton.setPreferredSize(new Dimension(180, 40));

        //regButton.setToolTipText("The classic 3x3 board");
        regButton.setSubtext(" The classic 3x3 board");
        ultButton.setSubtext("A larger 9x9 board, made of smaller boards");
        tranButton.setSubtext("A huge, 27x27 board, composed of massive boards");
        quanButton.setSubtext(" There's no explaining this one");
        quan3DButton.setSubtext(" A 3D variant of quantum tic tac toe");

        //adds the items to the panel
        // gbc.gridx = 1;
        // gbc.gridy = 5;

        // Insets i = new Insets(50, 0, 0, 0);

        // gbc.insets = i;

        // gbc.gridx = 1;
        // gbc.gridy = 6;

        // startPanel.add(loadButton, gbc);

        //this is a silly solution, but it definitely works
        // i = new Insets(0, 0, 0, 0);
        // gbc.insets = i;

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

        gbc.gridy = 5;

        startPanel.add(quan3DButton, gbc);

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
                askIfUserWantsBot(4, startPanel, font);
            } 
        });

        quan3DButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts quantum game
                askIfUserWantsBot(5, startPanel, font);
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
                    //new GFrame(gameType, true);
                    if(gameType == 2 || gameType == 4){
                        askDifficulty(gameType, panel, font);
                    }else{
                        new GFrame(gameType, true);
                        setupPanel(panel);
                    }
                    
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

    public static void askDifficulty(int gameType, JPanel panel, Font font){
        panel.removeAll();
        panel.repaint();

        JLabel titleLabel = new JLabel("Select your difficulty level");

        GButton oneButton = new GButton("1");
        GButton twoButton = new GButton("2");
        GButton threeButton = new GButton("3");
        GButton fourButton = new GButton("4");
        GButton fiveButton = new GButton("5");
        GButton sixButton = new GButton("6");
        GButton sevenButton = new GButton("7");
        GButton eightButton = new GButton("8");

        titleLabel.setFont(font);
        
        oneButton.setFont(font);
        twoButton.setFont(font);
        threeButton.setFont(font);
        fourButton.setFont(font);
        fiveButton.setFont(font);
        sixButton.setFont(font);
        sevenButton.setFont(font);
        eightButton.setFont(font);
        
        oneButton.setPreferredSize(new Dimension(100, 40));
        twoButton.setPreferredSize(new Dimension(100, 40));
        threeButton.setPreferredSize(new Dimension(100, 40));
        fourButton.setPreferredSize(new Dimension(100, 40));
        fiveButton.setPreferredSize(new Dimension(100, 40));
        sixButton.setPreferredSize(new Dimension(100, 40));
        sevenButton.setPreferredSize(new Dimension(100, 40));
        eightButton.setPreferredSize(new Dimension(100, 40));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;

        buttonPanel.add(oneButton, gbc);

        gbc.gridx = 1;
        buttonPanel.add(twoButton, gbc);
        
        gbc.gridx = 2;
        buttonPanel.add(threeButton, gbc);
        
        gbc.gridx = 3;
        buttonPanel.add(fourButton, gbc);
        
        gbc.gridy = 1;
        gbc.gridx = 0;
        buttonPanel.add(fiveButton, gbc);
        
        gbc.gridx = 1;
        buttonPanel.add(sixButton, gbc);
        
        gbc.gridx = 2;
        buttonPanel.add(sevenButton, gbc);
        
        gbc.gridx = 3;
        buttonPanel.add(eightButton, gbc);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.anchor = GridBagConstraints.CENTER;
        gbc2.weightx = 1;
        gbc2.weighty = 1;

        gbc2.gridx = 0;
        gbc2.gridy = 0;
        panel.add(titleLabel, gbc2);

        gbc2.gridy = 1;
        panel.add(buttonPanel, gbc2);

        panel.repaint();
        panel.revalidate();
        panel.setVisible(true);

        oneButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    GFrame frame = new GFrame(gameType, true);
                    frame.setDepth(1);
                    setupPanel(panel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        twoButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    GFrame frame = new GFrame(gameType, true);
                    frame.setDepth(2);
                    setupPanel(panel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        threeButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    GFrame frame = new GFrame(gameType, true);
                    frame.setDepth(3);
                    setupPanel(panel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        fourButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    GFrame frame = new GFrame(gameType, true);
                    frame.setDepth(4);
                    setupPanel(panel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        fiveButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    GFrame frame = new GFrame(gameType, true);
                    frame.setDepth(5);
                    setupPanel(panel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        sixButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    GFrame frame = new GFrame(gameType, true);
                    frame.setDepth(6);
                    setupPanel(panel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        sevenButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    GFrame frame = new GFrame(gameType, true);
                    frame.setDepth(7);
                    setupPanel(panel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        eightButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    GFrame frame = new GFrame(gameType, true);
                    frame.setDepth(8);
                    setupPanel(panel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
