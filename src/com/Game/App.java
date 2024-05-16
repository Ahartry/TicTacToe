package com.Game;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;

public class App {

    public static void main(String[] args) throws Exception {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        //gbc.fill = GridBagConstraints.BOTH;
        
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
        
        startFrame.add(startPanel);

        System.out.println(new File(".").getCanonicalPath());
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("src/com/Res/font.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);

        JButton regButton = new JButton("Regular");
        JButton ultButton = new JButton("Massive");
        JButton tranButton = new JButton("Ultimate");
        JButton quanButton = new JButton("Quantum");
        JButton loadButton = new JButton("Load");
        
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

        startFrame.setVisible(true);

        regButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts regular game
                try {
                    new GFrame(1);
                } catch (FontFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            } 
        });

        ultButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts regular game
                try {
                    new GFrame(2);
                } catch (FontFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            } 
        });

        tranButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts regular game
                try {
                    new GFrame(3);
                } catch (FontFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            } 
        });

        quanButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts regular game
                try {
                    new GFrame(4);
                } catch (FontFormatException | IOException e1) {
                    e1.printStackTrace();
                }
            } 
        });

        loadButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 

                String path = System.getProperty("user.dir") + File.separator + "Saves";
                File filepath = new File(path);

                //starts regular game
                //old JFileChooser argument: FileSystemView.getFileSystemView().getHomeDirectory()
                JFileChooser chooser = new JFileChooser(filepath);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
                chooser.setFileFilter(filter);
                chooser.showOpenDialog(null);

                File file = chooser.getSelectedFile();

                try {
                    loadSave(file);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } 
            
        });
    }

    public static void loadSave(File file) throws Exception{
        System.out.println(file.getPath());
        Scanner scan = new Scanner(file);

        String firstBlock = scan.next();

        int gameType = Integer.parseInt(firstBlock.substring(0, 1));

        if(gameType == 3){
            boolean turn;
            //opposite based on last move
            if(firstBlock.substring(1, 2).equals("2")){
                turn = false;
            }else{
                turn = true;
            }

            turn = !turn;
    
            int recentLarge = Integer.parseInt(firstBlock.substring(2, 3));
            int recentBoard = Integer.parseInt(firstBlock.substring(3, 4));
            int recentCell = Integer.parseInt(firstBlock.substring(4, 5));
    
            int xl = recentLarge % 3;
            int yl = (int) Math.floor(recentLarge / 3);
            int xb = recentBoard % 3;
            int yb = (int) Math.floor(recentBoard / 3);
            int xc = recentCell % 3;
            int yc = (int) Math.floor(recentCell / 3);
    
            System.out.println("Loading gametype: " + gameType + ", Board Location: " + recentLarge + " " + recentBoard + " " + recentCell);
            
            MassiveBoard inputboard = new MassiveBoard();
    
            String input;
    
    
            int i = 0;
            while(scan.hasNext()){
                System.out.println(i);
                i++;
                
                input = scan.next();
    
                State state;
                
                if(input.substring(0, 1).equals("2")){
                    state = State.Player2;
                }else{
                    state = State.Player1;
                }
    
                int large = Integer.parseInt(input.substring(1, 2));
                int board = Integer.parseInt(input.substring(2, 3));
                int cell = Integer.parseInt(input.substring(3, 4));
    
                xl = large % 3;
                yl = (int) Math.floor(large / 3);
                xb = board % 3;
                yb = (int) Math.floor(board / 3);
                xc = cell % 3;
                yc = (int) Math.floor(cell / 3);
    
                inputboard.getBoardArray(xl, yl).getBoardArray(xb, yb).getBoardArray(xc, yc).setState(state);
                
                int resultsmall = inputboard.getBoardArray(xl, yl).getBoardArray(xb, yb).checkBoard(xc, yc);

                //this doesn't seem to be working
                int resultlarge = inputboard.getBoardArray(xl, yl).checkBoard(xb, yb);

                //this is irrelevant
                //int resultmassive = inputboard.checkBoard(xl, yl);
    
                if(resultsmall == 1){
                    inputboard.getBoardArray(xl, yl).getBoardArray(xb, yb).setState(State.Player1);
                }else if(resultsmall == 2){
                    inputboard.getBoardArray(xl, yl).getBoardArray(xb, yb).setState(State.Player2);
                }
    
                if(resultlarge == 1){
                    inputboard.getBoardArray(xl, yl).setState(State.Player1);
                }else if(resultlarge == 2){
                    inputboard.getBoardArray(xl, yl).setState(State.Player2);
                }
    
                //System.out.println("xl: " + xl + ", yl: " + yl + ", xb: " + xb + ", yb: " + yb + ", xc: " + xc + ", yc: " + yc + " (input: " + large + " " + board + " " + cell + ")");
            }

            //additional checks for larger victories
            for(int k = 0; k < 3; k++){
                for(int j = 0; j < 3; j++){
                    int result = inputboard.getBoardArray(k, j).checkEntireBoard();

                    if(result == 1){
                        inputboard.getBoardArray(k, j).setState(State.Player1);
                    }else if(result == 2){
                        inputboard.getBoardArray(k, j).setState(State.Player2);
                    }

                }
            }

            xl = recentLarge % 3;
            yl = (int) Math.floor(recentLarge / 3);
            xb = recentBoard % 3;
            yb = (int) Math.floor(recentBoard / 3);
            xc = recentCell % 3;
            yc = (int) Math.floor(recentCell / 3);
    
            GFrame frame = new GFrame(gameType);
            inputboard.calculateActive(xl, yl, xb, yb, xc, yc);
            frame.getGPanel().setMassiveBoard(inputboard, turn);
    
            scan.close();
        }else{
            System.out.println("Unsupported game type");
        }

        
    }
}
