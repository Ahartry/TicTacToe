package com.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GFrame extends JFrame{

    GPanel bottomPanel;
    JPanel topPanel;
    JPanel startPanel;
    JLabel topLabel;
    GButton functionButton;
    GButton helpButton;
    Font font;
    
    public GFrame() throws Exception{

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupWindow();

        //setupGame(gameType, bot);

    }

    public void setupGame(int gameType, boolean bot) throws FontFormatException, IOException{
        remove(startPanel);

        setResizable(true);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setTitle("Tic Tac Toe");

        System.out.println("\nStarting game type " + gameType + "\n");

        topPanel = new JPanel();
        topLabel = new JLabel("Player 1's turn");
        functionButton = new GButton("");
        functionButton.setFocusable(false);
        helpButton = new GButton("Help");
        helpButton.setFocusable(false);

        bottomPanel = new GPanel(gameType, topLabel, functionButton, bot, this);

        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(30f);

        topLabel.setFont(font);
        topLabel.setHorizontalAlignment(JLabel.CENTER);

        topPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.weightx = 1;

        //stupid
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        if(gameType == 4 || gameType == 5){
            topPanel.add(Box.createRigidArea(new Dimension(250, 0)));
        }else{
            topPanel.add(Box.createRigidArea(new Dimension(90, 0)));
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

        //help button sizing
        Dimension dd = helpButton.getPreferredSize();
        dd.width = 90;
        helpButton.setPreferredSize(dd); 
        if(gameType == 3){
            // topPanel.add(functionButton, gbc);
            // functionButton.setText("Save");
            // Dimension d = functionButton.getPreferredSize();
            // d.width = 90;
            // functionButton.setPreferredSize(d); 
        }else if(gameType == 4 || gameType == 5){
            topPanel.add(functionButton, gbc);
            functionButton.setText("Hide Lines");
            Dimension d = functionButton.getPreferredSize();
            d.width = 160;
            functionButton.setPreferredSize(d); 
        }

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

    public void setupWindow() throws Exception {

        setSize(400, 250);
        setLocationRelativeTo(null);
        setTitle("Tic Tac Toe Game");
        setResizable(false);

        startPanel = new JPanel();
        startPanel.setLayout(new GridBagLayout());
        startPanel.setFocusable(true);
        startPanel.requestFocus();

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
                    //new GFrame(1, false);
                    setupGame(1, false);
                } catch (FontFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            } 
        });

        ultButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts massive game
                askIfUserWantsBot(2, font);
            } 
        });

        

        tranButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts ultimate game
                new SFrame(GFrame.this);
            } 
        });

        quanButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts quantum game
                askIfUserWantsBot(4, font);
            } 
        });

        quan3DButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts quantum game
                askIfUserWantsBot(5, font);
            } 
        });

        loadButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 

                new LFrame(GFrame.this);

            } 
            
        });

        startPanel.repaint();
        startPanel.revalidate();
        startPanel.setVisible(true);

        add(startPanel);
        setVisible(true);
    }

    public void askIfUserWantsBot(int gameType, Font font){
        startPanel.removeAll();
        startPanel.repaint();
        startPanel.setLayout(new GridBagLayout());

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

        startPanel.add(singleplayerButton, gbc);

        gbc.gridx = 1;

        startPanel.add(multiplayerButton, gbc);

        startPanel.repaint();
        startPanel.revalidate();
        startPanel.setVisible(true);

        singleplayerButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 

                try {
                    //new GFrame(gameType, true);
                    if(gameType == 2 || gameType == 4){
                        askDifficulty(gameType, font);
                    }else{
                        setupGame(gameType, true);
                        //setupWindow();
                    }
                    
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            } 
            
        });
        multiplayerButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 

                try {
                    setupGame(gameType, false);
                    //setupWindow();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            } 
            
        });
    }

    public void askDifficulty(int gameType, Font font){
        startPanel.removeAll();
        startPanel.repaint();

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
        startPanel.setLayout(new GridBagLayout());

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
        startPanel.add(titleLabel, gbc2);

        gbc2.gridy = 1;
        startPanel.add(buttonPanel, gbc2);

        startPanel.repaint();
        startPanel.revalidate();
        startPanel.setVisible(true);

        oneButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    setupGame(gameType, true);
                    bottomPanel.setDepth(1);
                    //setupWindow();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        twoButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    setupGame(gameType, true);
                    bottomPanel.setDepth(2);
                    //setupWindow();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        threeButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    setupGame(gameType, true);
                    bottomPanel.setDepth(3);
                    //setupWindow();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        fourButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    setupGame(gameType, true);
                    bottomPanel.setDepth(4);
                    //setupWindow();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        fiveButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    setupGame(gameType, true);
                    bottomPanel.setDepth(5);
                    //setupWindow();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        sixButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    setupGame(gameType, true);
                    bottomPanel.setDepth(6);
                    //setupWindow();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        sevenButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    setupGame(gameType, true);
                    bottomPanel.setDepth(7);
                    //setupWindow();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        eightButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    setupGame(gameType, true);
                    bottomPanel.setDepth(8);
                    //setupWindow();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public GPanel getGPanel(){
        return bottomPanel;
    }

    public void setDepth(int x){
        bottomPanel.setDepth(x);
    }

}
