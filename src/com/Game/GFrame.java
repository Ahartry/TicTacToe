package com.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GFrame extends JFrame{

    GPanel bottomPanel;
    
    public GFrame(int gameType) throws FontFormatException, IOException{

        System.out.println("\nStarting game type " + gameType + "\n");

        JPanel topPanel = new JPanel();
        JLabel topLabel = new JLabel("Player 1's turn");
        JButton functionButton = new JButton();
        functionButton.setFocusable(false);
        JButton helpButton = new JButton("Help");
        helpButton.setFocusable(false);

        bottomPanel = new GPanel(gameType, topLabel, functionButton);

        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("com/Res/font.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(30f);

        topLabel.setFont(font);
        topLabel.setHorizontalAlignment(JLabel.CENTER);

        topPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.weightx = 1;

        //stupid
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        if(gameType == 3){
            topPanel.add(Box.createRigidArea(new Dimension(120, 0)));
        }else if(gameType ==4){
            topPanel.add(Box.createRigidArea(new Dimension(140, 0)));
        }else{
            topPanel.add(Box.createRigidArea(new Dimension(80, 0)));
        }

        //gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 2;

        topPanel.setSize(new Dimension(1000, 50));
        topPanel.setBackground(Color.GRAY);
        topPanel.add(topLabel, gbc);

        bottomPanel.setSize(new Dimension(1000, 500));
        bottomPanel.requestFocus();

        gbc.weightx = 0;

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 3;
        topPanel.add(helpButton);

        gbc.gridx = 4;

        if(gameType == 3){
            topPanel.add(functionButton, gbc);
            functionButton.setText("Save");
        }else if(gameType == 4){
            topPanel.add(functionButton, gbc);
            functionButton.setText("Hide Lines");
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setTitle("Tic Tac Toe");
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel);
        setVisible(true);

        //help function
        helpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println("Clicked");
                try {
                    new HFrame(gameType);
                } catch (FontFormatException | IOException e1) {
                    e1.printStackTrace();
                }
                
            }

        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                bottomPanel.resizeVariables(e.getComponent().getWidth(), getContentPane().getHeight() - 50);
            }
            @Override
            public void componentMoved(ComponentEvent e) {
              
            }
        });
    }

    public GPanel getGPanel(){
        return bottomPanel;
    }

}
