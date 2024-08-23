package com.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GFrame extends JFrame{

    GPanel bottomPanel;
    JPanel topPanel;
    JPanel startPanel;
    JLabel topLabel;
    GButton functionButton;
    GButton helpButton;
    GButton backButton;
    Font font;
    
    public GFrame() throws Exception{

        try {
            Image image = ImageIO.read(getClass().getClassLoader().getResource("icon.png"));
            setIconImage(image);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupWindow();

        //setupGame(gameType, bot);

    }

    public void setupGame(int gameType, boolean bot) throws FontFormatException, IOException{
        
        getContentPane().removeAll();
        revalidate();

        setResizable(true);
        setPreferredSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        pack();
        setTitle("Tic Tac Toe");

        centerFrame();

        System.out.println("\nStarting game type " + gameType + "\n");

        topPanel = new JPanel();
        topLabel = new JLabel("Player 1's turn");
        functionButton = new GButton("");
        functionButton.setFocusable(false);
        helpButton = new GButton("Help");
        helpButton.setFocusable(false);
        backButton = new GButton("");
        backButton.setFocusable(false);

        topPanel.setBackground(Color.GRAY);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        //this is used to check panel balancing

        // leftPanel.setBackground(Color.RED);
        // centerPanel.setBackground(Color.BLUE);
        // rightPanel.setBackground(Color.GREEN);

        leftPanel.setBackground(Color.GRAY);
        centerPanel.setBackground(Color.GRAY);
        rightPanel.setBackground(Color.GRAY);

        leftPanel.setPreferredSize(new Dimension(280, leftPanel.getHeight()));
        leftPanel.setMinimumSize(new Dimension(150, leftPanel.getHeight()));
        rightPanel.setPreferredSize(new Dimension(280, rightPanel.getHeight()));
        rightPanel.setMinimumSize(new Dimension(150, rightPanel.getHeight()));

        try {
            Image image = ImageIO.read(getClass().getClassLoader().getResource("back4.png"));
            backButton.setImage(image);
            //backButton.setImageSize(128, 86);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //help button sizing
        Dimension ddd = backButton.getPreferredSize();
        ddd.width = 70;
        ddd.height = 40;
        backButton.setPreferredSize(ddd); 

        leftPanel.add(backButton);
        centerPanel.add(topLabel);

        //help button sizing
        Dimension dd = helpButton.getPreferredSize();
        dd.width = 90;
        helpButton.setPreferredSize(dd); 

        if(gameType == 4 || gameType == 5){
            rightPanel.add(functionButton);
            functionButton.setText("Hide Lines");
            Dimension d = functionButton.getPreferredSize();
            d.width = 160;
            functionButton.setPreferredSize(d); 
        }

        rightPanel.add(helpButton);

        bottomPanel = new GPanel(gameType, topLabel, functionButton, bot, this);

        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(30f);

        topLabel.setFont(font);
        topLabel.setHorizontalAlignment(JLabel.CENTER);
        topPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        topPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        topPanel.add(centerPanel, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        topPanel.add(rightPanel, gbc);

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

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println("Clicked");
                try {
                    setupWindow();

                    //I do NOT trust this
                    centerFrame();
                    Thread.sleep(5);
                    centerFrame();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            centerFrame();
                        }
                    });

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                
            }

        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                bottomPanel.resizeVariables(getContentPane().getWidth(), getContentPane().getHeight()/* */ - 50);
            }
            @Override
            public void componentMoved(ComponentEvent e) {
              
            }
        });

        centerFrame();
    }

    public void setupWindow() throws Exception {
        getContentPane().removeAll();
        revalidate();

        setLocationRelativeTo(null);
        setVisible(false);
        setResizable(true);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setTitle("Select your game");
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);

        centerFrame();

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
        GButton ultButton = new GButton("Ultimate");
        GButton tranButton = new GButton("Omega");
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
        regButton.setSubtext(" The classic 3x3 board ");
        ultButton.setSubtext(" A larger 9x9 board, made of regular boards");
        tranButton.setSubtext("​ A huge, 27x27 board, composed of ultimate boards​");
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
                askLoad(3, font);
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
        repaint();

        centerFrame();
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
                    if(gameType == 2 || gameType == 4 || gameType == 5){
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

    public void askLoad(int gameType, Font font){
        startPanel.removeAll();
        startPanel.repaint();
        startPanel.setLayout(new GridBagLayout());

        GButton newButton = new GButton("New Game");
        GButton loadButton = new GButton("Load Game");

        newButton.setFont(font);
        loadButton.setFont(font);

        newButton.setPreferredSize(new Dimension(200, 40));
        loadButton.setPreferredSize(new Dimension(200, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;

        startPanel.add(newButton, gbc);

        gbc.gridx = 1;

        startPanel.add(loadButton, gbc);

        startPanel.repaint();
        startPanel.revalidate();
        startPanel.setVisible(true);

        newButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 

                new SFrame(GFrame.this);
            } 
            
        });
        loadButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 

                new LFrame(GFrame.this);

            } 
            
        });
    }

    public GPanel getGPanel(){
        return bottomPanel;
    }

    public void setDepth(int x){
        bottomPanel.setDepth(x);
    }

    private void centerFrame() {

        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();

        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;
        setLocation(dx, dy);

        //to make sure it gets down when it can
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setLocation(dx, dy);
            }
        });
        setLocation(dx, dy);
        
    }

}
