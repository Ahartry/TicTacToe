package com.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
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
    Sound sound;
    Sound music;
    Color color = new Color(0,150,50,255);
    ArrayList<GButton> buttonList = new ArrayList<>();
    Image xImage = new ImageIcon(getClass().getClassLoader().getResource("x.png")).getImage();
    Image xImage2 = new ImageIcon(getClass().getClassLoader().getResource("x2.png")).getImage();
    Image oImage = new ImageIcon(getClass().getClassLoader().getResource("o.png")).getImage();
    Image oImage2 = new ImageIcon(getClass().getClassLoader().getResource("o2.png")).getImage();
    boolean fullscreen = false;
    boolean gaming = false;
    
    public GFrame() throws Exception{

        try {
            Image image = ImageIO.read(getClass().getClassLoader().getResource("icon.png"));
            setIconImage(image);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        sound = new Sound();
        music = new Sound();
        configLoad();
        music.play("song.wav", true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupWindow();
        setVisible(true);

    }

    public void setupGame(int gameType, boolean bot)throws Exception{
        if(!SwingUtilities.isEventDispatchThread()){
            SwingUtilities.invokeLater(()->{
                try{
                    setupGame(gameType,bot);
                }catch(Exception e){
                    e.printStackTrace();
                }
            });
            return;
        }
    
        gaming=true;
        for(int i = 0; i < buttonList.size(); i++){
            if(buttonList.get(i).hasTooltip()){
                buttonList.get(i).getToolTip().kill();
            }
        }
        buttonList.clear();
        //setVisible(false);
        setResizable(true);
        getContentPane().removeAll();
    
        System.out.println("\nStarting game type "+gameType+"\n");
    
        topPanel=new JPanel();
        topLabel=new JLabel("Player 1's turn");
        functionButton=new GButton("");
        functionButton.setFocusable(false);
        helpButton=new GButton("Help");
        helpButton.setFocusable(false);
        backButton=new GButton("");
        backButton.setFocusable(false);
    
        functionButton.setSound(sound);
        helpButton.setSound(sound);
        backButton.setSound(sound);
    
        functionButton.setColor(color);
        helpButton.setColor(color);
        backButton.setColor(color);
    
        buttonList.add(functionButton);
        buttonList.add(helpButton);
        buttonList.add(backButton);
    
        topPanel.setBackground(Color.GRAY);
    
        JPanel leftPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel centerPanel=new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel rightPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
    
        leftPanel.setBackground(Color.GRAY);
        centerPanel.setBackground(Color.GRAY);
        rightPanel.setBackground(Color.GRAY);
    
        leftPanel.setPreferredSize(new Dimension(280,leftPanel.getHeight()));
        leftPanel.setMinimumSize(new Dimension(150,leftPanel.getHeight()));
        rightPanel.setPreferredSize(new Dimension(280,rightPanel.getHeight()));
        rightPanel.setMinimumSize(new Dimension(150,rightPanel.getHeight()));
    
        try{
            Image image=ImageIO.read(getClass().getClassLoader().getResource("back4.png"));
            backButton.setImage(image);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    
        Dimension dd=backButton.getPreferredSize();
        dd.width=70;
        dd.height=40;
        backButton.setPreferredSize(dd);
    
        leftPanel.add(backButton);
        centerPanel.add(topLabel);
    
        dd=helpButton.getPreferredSize();
        dd.width=90;
        helpButton.setPreferredSize(dd);
    
        rightPanel.add(helpButton);
    
        bottomPanel = new GPanel(gameType,topLabel,functionButton,bot,this,sound);
    
        InputStream stream=ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        font=Font.createFont(Font.TRUETYPE_FONT,stream).deriveFont(30f);
    
        topLabel.setFont(font);
        topLabel.setHorizontalAlignment(JLabel.CENTER);
        topPanel.setLayout(new GridBagLayout());
    
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=0;
        gbc.fill=GridBagConstraints.BOTH;
        topPanel.add(leftPanel,gbc);
    
        gbc.gridx=1;
        gbc.weightx=0.6;
        topPanel.add(centerPanel,gbc);
    
        gbc.gridx=2;
        gbc.weightx=0;
        topPanel.add(rightPanel,gbc);

        dd = topPanel.getPreferredSize();
        dd.height = 50;
        topPanel.setPreferredSize(dd);
    
        add(topPanel,BorderLayout.NORTH);
        add(bottomPanel);
    
        bottomPanel.setColor(color);
    
        helpButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                try{
                    new HFrame(gameType,sound,color);
                }catch(FontFormatException|IOException e1){
                    e1.printStackTrace();
                }
            }
        });
    
        backButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                try{
                    setupWindow();
                }catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        });
    
        addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e){
                bottomPanel.resizeVariables(getContentPane().getWidth(), getContentPane().getHeight() - 50, false);
            }
            @Override
            public void componentMoved(ComponentEvent e){
                
            }
        });
    
        setTitle("Tic Tac Toe");
        fancyResize(1000, 600);
        bottomPanel.resizeVariables(getContentPane().getWidth(), getContentPane().getHeight() - 50, true);
        bottomPanel.repaint();
    }

    @SuppressWarnings("unused")
    public void setupWindow()throws Exception{
        if(!SwingUtilities.isEventDispatchThread()){
            SwingUtilities.invokeLater(()->{
                try{
                    setupWindow(); // Ensure this does not cause recursion
                }catch(Exception e){
                    e.printStackTrace();
                }
            });
            return;
        }
    
        setResizable(false);
        gaming = false;
        for(int i = 0; i < buttonList.size(); i++){
            if(buttonList.get(i).hasTooltip()){
                buttonList.get(i).getToolTip().kill();
            }
        }
        buttonList.clear();
    
        //setVisible(false);
        getContentPane().removeAll();
    
        startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());
        startPanel.setFocusable(true);
        startPanel.requestFocus();
    
        JPanel buttonPanel=new JPanel(new GridBagLayout());
        JPanel topPanel=new JPanel(new GridBagLayout());
    
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.weightx=1;
        gbc.weighty=1;
    
        InputStream stream=ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        font=Font.createFont(Font.TRUETYPE_FONT,stream).deriveFont(20f);
        InputStream stream2=ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        Font font2=Font.createFont(Font.TRUETYPE_FONT,stream2).deriveFont(25f);
    
        GButton newButton=new GButton("New Game");
        GButton loadButton=new GButton("Load Save");
        GButton optionButton=new GButton("Options");
        GButton exitButton=new GButton("Exit");
    
        JLabel titleLabel1=new JLabel("Ultimate Tic Tac Toe Collection");
        titleLabel1.setFont(font2);
    
        newButton.setFont(font);
        loadButton.setFont(font);
        optionButton.setFont(font);
        exitButton.setFont(font);
    
        newButton.setFocusable(false);
        loadButton.setFocusable(false);
        optionButton.setFocusable(false);
        exitButton.setFocusable(false);
    
        newButton.setPreferredSize(new Dimension(180,40));
        loadButton.setPreferredSize(new Dimension(180,40));
        optionButton.setPreferredSize(new Dimension(180,40));
        exitButton.setPreferredSize(new Dimension(180,40));
    
        newButton.setSound(sound);
        loadButton.setSound(sound);
        optionButton.setSound(sound);
        exitButton.setSound(sound);
    
        newButton.setColor(color);
        loadButton.setColor(color);
        optionButton.setColor(color);
        exitButton.setColor(color);
    
        buttonList.add(newButton);
        buttonList.add(loadButton);
        buttonList.add(optionButton);
        buttonList.add(exitButton);
    
        gbc.gridx=0;
        gbc.gridy=2;
        buttonPanel.add(newButton,gbc);
    
        gbc.gridy=3;
        buttonPanel.add(loadButton,gbc);
    
        gbc.gridy=4;
        buttonPanel.add(optionButton,gbc);
    
        gbc.gridy=5;
        buttonPanel.add(exitButton,gbc);
    
        gbc.gridy=1;
        gbc.gridx=0;
        startPanel.add(buttonPanel,BorderLayout.CENTER);
    
        gbc.gridy=0;
        topPanel.setMinimumSize(new Dimension(400,100));
        startPanel.add(topPanel,BorderLayout.SOUTH);
    
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.gridy=0;
        buttonPanel.add(titleLabel1,gbc);
        gbc.gridy=1;
        buttonPanel.add(Box.createVerticalStrut(50),gbc);
        gbc.gridy=6;
        buttonPanel.add(Box.createVerticalStrut(20),gbc);
    
        newButton.addActionListener(e->{
            try{
                setupGame(1,false);
                System.out.println("Chandelier wanted a line here");
                repaint();
            }catch(Exception e1){
                e1.printStackTrace();
            }
        });
    
        loadButton.addActionListener(e->{
            new LFrame(GFrame.this, sound, color);
        });

        optionButton.addActionListener(e->{
            new CFrame(GFrame.this,sound,music,font,color);
        });

        exitButton.addActionListener(e->{
            System.exit(0);
        });
    
        add(startPanel);
        setTitle("Choose your game");
    
        fancyResize(420,330);
    }

    public void askIfUserWantsBot(int gameType, Font font){
        buttonList.clear();
        startPanel.removeAll();
        startPanel.repaint();
        startPanel.setLayout(new GridBagLayout());

        GButton singleplayerButton = new GButton("Singleplayer");
        GButton multiplayerButton = new GButton("Multiplayer");

        singleplayerButton.setFont(font);
        multiplayerButton.setFont(font);

        singleplayerButton.setSound(sound);
        multiplayerButton.setSound(sound);

        singleplayerButton.setColor(color);
        multiplayerButton.setColor(color);

        buttonList.add(singleplayerButton);
        buttonList.add(multiplayerButton);

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
                    if(gameType == 2 || gameType == 3 || gameType == 4 || gameType == 5){
                        askDifficulty(gameType, font);
                    }else{
                        //setupGame(gameType, true);
                        setupGame(gameType, true);
                        repaint();
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
                    if(gameType == 3){
                        new SFrame(GFrame.this, false, 4, gameType, sound, color);
                    }else{
                        //setupGame(gameType, false);
                        setupGame(gameType, false);
                        repaint();
                    }

                    //setupWindow();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            } 
            
        });
    }

    public void askDifficulty(int gameType, Font font){
        buttonList.clear();
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

        buttonList.add(oneButton);
        buttonList.add(twoButton);
        buttonList.add(threeButton);
        buttonList.add(fourButton);
        buttonList.add(fiveButton);
        buttonList.add(sixButton);
        buttonList.add(sevenButton);
        buttonList.add(eightButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        startPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;

        for(int i = 0; i < buttonList.size(); i++){
            buttonList.get(i).setFont(font);
            buttonList.get(i).setSound(sound);
            buttonList.get(i).setColor(color);
            buttonList.get(i).setPreferredSize(new Dimension(100, 40));

            gbc.gridx = i % 4;
            gbc.gridy = i / 4;
            buttonPanel.add(buttonList.get(i), gbc);

            int difficulty = i + 1;

            buttonList.get(i).addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    botStart(gameType, difficulty);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        }

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

    }

    public GPanel getGPanel(){
        return bottomPanel;
    }

    public void setDepth(int x){
        bottomPanel.setDepth(x);
    }

    private void fancyResize(int x, int y){
        // Ensure this method runs on the Event Dispatch Thread (EDT)
        if(!SwingUtilities.isEventDispatchThread()){
            SwingUtilities.invokeLater(() -> fancyResize(x, y));
            return;
        }
    
        final int left = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration()).left;
        final int right = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration()).right;
        final int top = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration()).top;
        final int bottom = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration()).bottom;
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int width = screenSize.width - left - right;
        final int height = screenSize.height - top - bottom;

        setMinimumSize(new Dimension(x, y));
    
        if(fullscreen && gaming){
            setBounds(0, 0, width, height);
        }else{
            int xPos = (width - x) / 2;
            int yPos = (height - y) / 2;
            setBounds(xPos, yPos, x, y);
        }
    
        //setVisible(true);
        validate();
        repaint();

        if(gaming){
            getGPanel().repaint();
        }
    }

    public void botStart(int gameType, int depth) throws Exception{
        if(gameType != 3){
            setupGame(gameType, true);
            //setupGame(gameType, true);
            bottomPanel.setDepth(depth);
        }else{
            new SFrame(GFrame.this, true, depth, gameType, sound, color);
        }

    }

    public void setTopText(String s){
        topLabel.setText(s);
    }

    public void setColor(Color c){
        color = c;
        for(int i = 0; i < buttonList.size(); i++){
            buttonList.get(i).setColor(c);
        }
        if(gaming){
            bottomPanel.setColor(c);
        }
        repaint();
    }

    public void setFullscreen(boolean x){
        fullscreen =x;
    }

    public boolean getFullscreen(){
        return fullscreen;
    }

    public void configLoad() throws Exception{
        //sound, music, fullscreen, color
        String configDefault = "75 75 0 1";

        Scanner scanner;

        String jarDir = getJarContainingFolder(getClass());

        // Create a File object for the current directory
        File directory = new File(jarDir);

        // Get a list of files in the directory
        File[] files = directory.listFiles();

        Path path = Path.of(jarDir + File.separator + "config.txt");

        boolean configFound = false;
        // Display the list of files
        if (files != null) {
            for (File file : files) {
                if(file.getName().equals("config.txt")){
                    configFound = true;
                }
            }
        }
        if(!configFound){
            try {
                // Create the file and write the content to it
                Files.write(path, configDefault.getBytes(), StandardOpenOption.CREATE);
    
                System.out.println("Config file recreated successfully.");
            } catch (IOException e) {
                // Handle file creation error
                System.err.println("Error creating the file: " + e.getMessage());
            }
        }

        File config = path.toFile();

        scanner = new Scanner(config);

        int soundV = Integer.parseInt(scanner.next());
        int musicV = Integer.parseInt(scanner.next());
        int fullsc = Integer.parseInt(scanner.next());
        int colorC = Integer.parseInt(scanner.next());

        if(soundV == 0){
            sound.presetVolume(-80f);
        }else{
            sound.presetVolume((soundV / 2) - 44);
        }

        if(musicV == 0){
            music.presetVolume(-80f);
        }else{
            music.presetVolume((musicV / 2) - 44);
        }

        if(fullsc == 1){
            fullscreen = true;
        }

        switch (colorC){
            case 1: color = new Color(0, 150, 50);break;
            case 2: color = new Color(0, 150, 200);break;
            case 3: color = new Color(0, 50, 200);break;
            case 4: color = new Color(50, 0, 150);break;
            case 5: color = new Color(255, 200, 00);break;
            case 6: color = new Color(255, 100, 0);break;
            case 7: color = new Color(200, 0, 0);break;
            case 8: color = new Color(0, 0, 0);break;
        }

        scanner.close();
    }

    public Image getxImage(){
        return xImage;
    }

    public Image getoImage(){
        return oImage;
    }

    public Image getxImage2(){
        return xImage2;
    }

    public Image getoImage2(){
        return oImage2;
    }

    public void setImages(Image x1, Image o1, Image x2, Image o2){
        xImage = x1;
        xImage2 = x2;
        oImage = o1;
        oImage2 = o2;
    }

    public void setImageX(Image x1){
        xImage = x1;
        xImage2 = x1;
    }

    public void setImageO(Image o1){
        oImage = o1;
        oImage2 = o1;
    }

    @SuppressWarnings("rawtypes")
    public String getJarContainingFolder(Class aclass) throws Exception {
        CodeSource codeSource = aclass.getProtectionDomain().getCodeSource();
      
        File jarFile;
      
        if (codeSource.getLocation() != null) {
          jarFile = new File(codeSource.getLocation().toURI());
        }
        else {
          String path = aclass.getResource(aclass.getSimpleName() + ".class").getPath();
          String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
          jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
          jarFile = new File(jarFilePath);
        }
        return jarFile.getParentFile().getAbsolutePath();
      }
}
