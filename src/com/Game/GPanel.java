package com.Game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GPanel extends JPanel implements MouseWheelListener{

    Image xImage = new ImageIcon(getClass().getClassLoader().getResource("x.png")).getImage();
    Image oImage = new ImageIcon(getClass().getClassLoader().getResource("o.png")).getImage();
    Image Image1 = new ImageIcon(getClass().getClassLoader().getResource("1.png")).getImage();
    Image Image2 = new ImageIcon(getClass().getClassLoader().getResource("2.png")).getImage();
    Image Image3 = new ImageIcon(getClass().getClassLoader().getResource("3.png")).getImage();
    Image Image4 = new ImageIcon(getClass().getClassLoader().getResource("4.png")).getImage();
    Image Image5 = new ImageIcon(getClass().getClassLoader().getResource("5.png")).getImage();
    Image Image6 = new ImageIcon(getClass().getClassLoader().getResource("6.png")).getImage();
    Image Image7 = new ImageIcon(getClass().getClassLoader().getResource("7.png")).getImage();
    Image Image8 = new ImageIcon(getClass().getClassLoader().getResource("8.png")).getImage();
    Image Image9 = new ImageIcon(getClass().getClassLoader().getResource("9.png")).getImage();

    int game;
    int offsetx = 0;
    int offsety = 0;
    int width = 1000;
    int height = 540;
    double zoom = 0.75;
    int buffer = 50;
    boolean turn;
    int movementCounter = 0;
    int mouseLocx = 0;
    int mouseLocy = 0;
    int thickness;
    int boundingSize;
    int xbound;
    int ybound;
    int cellSize;
    Color red = new Color(200, 30, 0);
    Color blue = new Color(10, 75, 190);
    SimpleBoard simpleBoard;
    LargeBoard largeBoard;
    MassiveBoard massiveBoard;
    QuantumBoard quantumBoard;

    JLabel displayLabel;

    GButton replayButton;

    int count = 0;
    int turnCount = 0;
    boolean quantumMove = false;
    boolean quantumLineMode = true;
    int result = 0;

    int moveDrawLoc = -1;

    int recentLarge = 0;
    int recentSimple = 0;
    int recentCell = -1;

    int recentSquare1 = 0;
    int recentSquare2 = 0;

    boolean collapseMove = false;

    File outputDir;
    String outputPath;

    public GPanel(int gameType, JLabel displayLabel1, GButton button) throws FontFormatException, IOException{

        game = gameType;

        displayLabel = displayLabel1;

        replayButton = new GButton("Replay");

        //instantiates boards
        if(game == 1){
            simpleBoard = new SimpleBoard();
            zoom = 0.75;
            offsetx = width / 2;
            offsety = height / 2;
        }else if(game == 2){
            largeBoard = new LargeBoard();
            largeBoard.setActive(true);
        }else if(game == 3){
            massiveBoard = new MassiveBoard();
            massiveBoard.setActive(true);
        }else if(game == 4){
            quantumBoard = new QuantumBoard();
            zoom = 0.75;
            offsetx = width / 2;
            offsety = height / 2;
            turnCount = 1;
        }

        //replay button stuff
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(80f);
        setLayout(new GridBagLayout());
        replayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        replayButton.setFont(font);
        replayButton.setFocusable(false);
        replayButton.setPreferredSize(new Dimension(150, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(350, 50, 10, 50);

        displayLabel.setForeground(red);

        //all the clicking stuff
        addMouseListener(new MouseAdapter() { 
            public void mouseReleased(MouseEvent e) { 
                if(movementCounter == 0){

                    if(game == 1){
                        if((e.getX() > xbound && e.getX() < (xbound + boundingSize) && e.getY() > ybound && e.getY() < (ybound + boundingSize)) && simpleBoard.getState() == State.Blank){
                            int xboard = e.getX() - xbound;
                            int yboard = e.getY() - ybound;
    
                            int xcell = (int) Math.floor(3 * xboard / boundingSize);
                            int ycell = (int) Math.floor(3 * yboard / boundingSize);
    
                            if(simpleBoard.getBoardTile(xcell, ycell).getState() == State.Blank){

                                count++;
                                simpleBoard.setBoardTile(xcell, ycell, turn);

                                int result = simpleBoard.checkBoard(xcell, ycell);

                                turn = !turn;

                                if(turn){
                                    displayLabel.setText("Player 2's turn");
                                    displayLabel.setForeground(blue);

                                }else{
                                    displayLabel.setText("Player 1's turn");
                                    displayLabel.setForeground(red);
                                    
                                }

                                if(result == 1){
                                    simpleBoard.setState(State.Player1);
                                    displayLabel.setText("Player 1 wins");
                                    displayLabel.setForeground(red);

                                    //replay button stuff
                                    add(replayButton, gbc);
                                    
                                }else if(result == 2){
                                    simpleBoard.setState(State.Player2);
                                    displayLabel.setText("Player 2 wins");
                                    displayLabel.setForeground(blue);

                                    //replay button stuff
                                    add(replayButton, gbc);
                                    
                                }

                                if(count == 9 && result == 0){
                                    displayLabel.setText("Stalemate");
                                    displayLabel.setForeground(Color.BLACK);

                                    //replay button stuff
                                    add(replayButton, gbc);
                                    
                                }

                                repaint();
                            }

                            
                            //System.out.println("X: " + xcell + ", Y: " + ycell);
    
                        }
                    }else if(game == 2){
                        if((e.getX() > xbound && e.getX() < (xbound + (boundingSize * 3)) && e.getY() > ybound && e.getY() < (ybound + (boundingSize * 3))) && largeBoard.getState() == State.Blank){
                            int relativeX = e.getX() - xbound;
                            int relativeY = e.getY() - ybound;
    
                            int xboard = (int) Math.floor(relativeX / boundingSize);
                            int yboard = (int) Math.floor(relativeY / boundingSize);

                            //System.out.println("Board: " + xboard + ", " + yboard);

                            int xcell = (int) Math.floor((3 * (relativeX - (xboard * boundingSize))) / boundingSize);
                            int ycell = (int) Math.floor((3 * (relativeY - (yboard * boundingSize))) / boundingSize);
    
                            if(largeBoard.getBoardArray(xboard, yboard).getBoardTile(xcell, ycell).getState() == State.Blank && largeBoard.getBoardArray(xboard, yboard).getActive()){
                                largeBoard.getBoardArray(xboard, yboard).setBoardTile(xcell, ycell, turn);

                                int result = largeBoard.getBoardArray(xboard, yboard).checkBoard(xcell, ycell);

                                turn = !turn;

                                if(turn){
                                    displayLabel.setText("Player 2's turn");
                                    displayLabel.setForeground(blue);

                                }else{
                                    displayLabel.setText("Player 1's turn");
                                    displayLabel.setForeground(red);
                                    
                                }

                                if(result == 1){
                                    largeBoard.getBoardArray(xboard, yboard).setState(State.Player1);
                                    // displayLabel.setText("Player 1 wins");
                                    // displayLabel.setForeground(red);
                                }else if(result == 2){
                                    largeBoard.getBoardArray(xboard, yboard).setState(State.Player2);
                                    // displayLabel.setText("Player 2 wins");
                                    // displayLabel.setForeground(blue);
                                }

                                if(largeBoard.getBoardArray(xcell, ycell).getState() == State.Blank && largeBoard.getBoardArray(xcell, ycell).getMoveTally() != 9){
                                    for(int i = 0; i < 9; i++){
                                        int x = i % 3;
                                        int y = (int) Math.floor(i / 3);
                                        largeBoard.getBoardArray(x, y).setActive(false);
                                    }
                                    largeBoard.setActive(false);
                                    largeBoard.getBoardArray(xcell, ycell).setActive(true);
                                }else{
                                    for(int i = 0; i < 9; i++){
                                        int x = i % 3;
                                        int y = (int) Math.floor(i / 3);
                                        largeBoard.getBoardArray(x, y).setActive(true);
                                        if(largeBoard.getBoardArray(x, y).getState() != State.Blank){
                                            largeBoard.getBoardArray(x, y).setActive(false); 
                                        }
                                    }
                                    largeBoard.getBoardArray(xcell, ycell).setActive(false);
                                    largeBoard.setActive(true);
                                }

                                if(largeBoard.getMoveTally() == 9){
                                    displayLabel.setText("Stalemate");
                                    displayLabel.setForeground(Color.BLACK);
                                    largeBoard.setActive(false);

                                    //replay button stuff
                                    add(replayButton, gbc);
                                    
                                }

                                //checks the larger board
                                result = largeBoard.checkBoard(xboard, yboard);
                                if(result == 1){
                                    largeBoard.setState(State.Player1);
                                    displayLabel.setText("Player 1 wins");
                                    displayLabel.setForeground(red);
                                    largeBoard.setActive(false);

                                    //replay button stuff
                                    add(replayButton, gbc);
                                    
                                }else if(result == 2){
                                    largeBoard.setState(State.Player2);
                                    displayLabel.setText("Player 2 wins");
                                    displayLabel.setForeground(blue);
                                    largeBoard.setActive(false);

                                    //replay button stuff
                                    add(replayButton, gbc);
                                    
                                }

                                repaint();
                            }

                            
                            //System.out.println("X: " + xcell + ", Y: " + ycell);
    
                        }
                    }else if(game == 3){
                        if((e.getX() > xbound && e.getX() < (xbound + (boundingSize * 9)) && e.getY() > ybound && e.getY() < (ybound + (boundingSize * 9))) && massiveBoard.getState() == State.Blank){
                            int relativeX = e.getX() - xbound;
                            int relativeY = e.getY() - ybound;

                            int xlargeboard = (int) Math.floor(3 * relativeX / (boundingSize * 9));
                            int ylargeboard = (int) Math.floor(3 * relativeY / (boundingSize * 9));
    
                            int xboard = (int) Math.floor((3 * (relativeX - (xlargeboard * boundingSize * 3))) / (boundingSize * 3));
                            int yboard = (int) Math.floor((3 * (relativeY - (ylargeboard * boundingSize * 3))) / (boundingSize * 3));

                            int xcell = (int) Math.floor((3 * (relativeX - (xlargeboard * boundingSize * 3 + xboard * boundingSize))) / (boundingSize));
                            int ycell = (int) Math.floor((3 * (relativeY - (ylargeboard * boundingSize * 3 + yboard * boundingSize))) / (boundingSize));

                            //System.out.println("LargeBoard: " + xlargeboard + ", " + ylargeboard);
                            //System.out.println("Board: " + xboard + ", " + yboard);
                            //System.out.println("Cell: " + xcell + ", " + ycell);
    
                            if(massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xboard, yboard).getBoardTile(xcell, ycell).getState() == State.Blank && massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xboard, yboard).getActive()){
                                massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xboard, yboard).setBoardTile(xcell, ycell, turn);

                                //checks each scale of board
                                int resultsmall = massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xboard, yboard).checkBoard(xcell, ycell);
                                int resultlarge = massiveBoard.getBoardArray(xlargeboard, ylargeboard).checkBoard(xboard, yboard);
                                int resultmassive = massiveBoard.checkBoard(xlargeboard, ylargeboard);

                                //System.out.println("Massive result: " + resultmassive);

                                turn = !turn;

                                massiveBoard.setActive(false);

                                if(massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xcell, ycell).getState() == State.Blank && massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xcell, ycell).getMoveTally() != 9){
                                    massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xcell, ycell).setActive(true);
                                }else{
                                    massiveBoard.getBoardArray(xlargeboard, ylargeboard).setActive(true);
                                }

                                // //I think this is needed 
                                // //nope, it is not
                                // if(massiveBoard.getBoardArray(xlargeboard, ylargeboard).getState() == State.Blank && massiveBoard.getBoardArray(xlargeboard, ylargeboard).getMoveTally() != 9){
                                //     massiveBoard.getBoardArray(xlargeboard, ylargeboard).setActive(true);
                                // }else{
                                //     massiveBoard.setActive(true);
                                // }

                                if(turn){
                                    displayLabel.setText("Player 2's turn");
                                    displayLabel.setForeground(blue);

                                }else{
                                    displayLabel.setText("Player 1's turn");
                                    displayLabel.setForeground(red);
                                    
                                }
                                

                                if(resultsmall == 1){
                                    massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xboard, yboard).setState(State.Player1);
                                    massiveBoard.setActive(false);

                                    if(massiveBoard.getBoardArray(xboard, yboard).getState() == State.Blank){
                                        massiveBoard.getBoardArray(xboard, yboard).setActive(true);
                                    }else{
                                        massiveBoard.setActive(true);
                                    }
                                    
                                    
                                }else if(resultsmall == 2){
                                    massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xboard, yboard).setState(State.Player2);
                                    massiveBoard.setActive(false);

                                    if(massiveBoard.getBoardArray(xboard, yboard).getState() == State.Blank){
                                        massiveBoard.getBoardArray(xboard, yboard).setActive(true);
                                    }else{
                                        massiveBoard.setActive(true);
                                    }
                                }

                                if(resultlarge == 1){
                                    massiveBoard.getBoardArray(xlargeboard, ylargeboard).setState(State.Player1);
                                    massiveBoard.setActive(false);

                                    if(massiveBoard.getBoardArray(xboard, yboard).getState() == State.Blank){
                                        massiveBoard.getBoardArray(xboard, yboard).setActive(true);
                                    }else{
                                        massiveBoard.setActive(true);
                                    }
                                     
                                }else if(resultlarge == 2){
                                    massiveBoard.getBoardArray(xlargeboard, ylargeboard).setState(State.Player2);
                                    massiveBoard.setActive(false);

                                    if(massiveBoard.getBoardArray(xboard, yboard).getState() == State.Blank){
                                        massiveBoard.getBoardArray(xboard, yboard).setActive(true);
                                    }else{
                                        massiveBoard.setActive(true);
                                    }
                                }

                                if(massiveBoard.getMoveTally() == 9){
                                    displayLabel.setText("Stalemate");
                                    displayLabel.setForeground(Color.BLACK);
                                    massiveBoard.setActive(false);

                                    //replay button stuff
                                    add(replayButton, gbc);
                                    
                                }
                                
                                if(resultmassive == 1){
                                    displayLabel.setText("Player 1 wins");
                                    displayLabel.setForeground(red);
                                    massiveBoard.setActive(false);

                                    //replay button stuff
                                    add(replayButton, gbc);
                                    
                                }else if(resultmassive == 2){
                                    displayLabel.setText("Player 2 wins");
                                    displayLabel.setForeground(blue);
                                    massiveBoard.setActive(false);

                                    //replay button stuff
                                    add(replayButton, gbc);
                                    
                                }

                                repaint();

                                recentLarge = xlargeboard + (3 * ylargeboard);
                                recentSimple = xboard + (3 * yboard);
                                recentCell = xcell + (3 * ycell);

                                try {
                                    outputBoard(massiveBoard);
                                } catch (FileNotFoundException e1) {
                                    e1.printStackTrace();
                                }

                                //System.out.println(recentLarge + ", " + recentSimple + ", " + recentCell);
                            }

                            
                            //System.out.println("X: " + xcell + ", Y: " + ycell);
    
                        }
                    }else if(game == 4){
                        if((e.getX() > xbound && e.getX() < (xbound + boundingSize) && e.getY() > ybound && e.getY() < (ybound + boundingSize)) && quantumBoard.getState() == State.Blank && result == 0){
                            int xboard = e.getX() - xbound;
                            int yboard = e.getY() - ybound;
    
                            int xcell = (int) Math.floor(3 * xboard / boundingSize);
                            int ycell = (int) Math.floor(3 * yboard / boundingSize);
    
                            if(quantumBoard.getBoardTile(xcell, ycell).getState() == State.Blank && e.getButton() == MouseEvent.BUTTON1){

                                if(quantumMove && (xcell + (ycell * 3)) != recentCell){
                                    quantumBoard.getBoardTile(xcell, ycell).addMove(turnCount);

                                    quantumBoard.getBoardTile(recentCell % 3, (int) Math.floor(recentCell / 3)).addMove(turnCount);

                                    recentSquare1 = xcell + (ycell * 3);
                                    recentSquare2 = recentCell;
                                    //quantumBoard.printBoard();

                                    result = quantumBoard.checkLoops(turnCount);

                                    turnCount++;
                                    quantumMove = !quantumMove;

                                    //display stuff
                                    turn = !turn;
                                    recentCell = -1;

                                    if(turn){
                                        displayLabel.setText("Player 2's turn");
                                        displayLabel.setForeground(blue);
    
                                    }else{
                                        displayLabel.setText("Player 1's turn");
                                        displayLabel.setForeground(red);
                                        
                                    }

                                    //does stuff if a loop is found
                                    if(result == 0){
                                        //System.out.println("No loop");
                                    }else{
                                        System.out.println("Loop");
                                        if(turn){
                                            displayLabel.setText("Player 2 chooses collapse");
        
                                        }else{
                                            displayLabel.setText("Player 1 chooses collapse");
                                            
                                        }

                                        moveDrawLoc = recentSquare1;
                                    }

                                //handles second click
                                }else if((xcell + (ycell * 3)) != recentCell){
                                    recentCell = xcell + (ycell * 3);
                                    quantumMove = !quantumMove;
                                }

                                // count++;
                                // quantumBoard.setBoardTile(xcell, ycell, turn);

                                // if(count == 9 && result == 0){
                                //     displayLabel.setText("Stalemate");
                                //     displayLabel.setForeground(Color.BLACK);
                                // }

                                repaint();
                            }else if(e.getButton() == MouseEvent.BUTTON3){
                                quantumBoard.printTile(xcell, ycell);
                            }

                            
                            //System.out.println("X: " + xcell + ", Y: " + ycell);
    
                        }else if(result == 1){
                            int selection = -1;
                            if(((e.getX() > (xbound + ((recentSquare1 % 3) * cellSize))) && (e.getX() < (xbound + ((recentSquare1 % 3) * cellSize) + cellSize)) && (e.getY() > (ybound + ((int) (Math.floor(recentSquare1 / 3)) * cellSize))) && (e.getY() < (ybound + ((int) (Math.floor(recentSquare1 / 3)) * cellSize) + cellSize)))){
                                selection = recentSquare1;
                            }else if(((e.getX() > (xbound + ((recentSquare2 % 3) * cellSize))) && (e.getX() < (xbound + ((recentSquare2 % 3) * cellSize) + cellSize)) && (e.getY() > (ybound + ((int) (Math.floor(recentSquare2 / 3)) * cellSize))) && (e.getY() < (ybound + ((int) (Math.floor(recentSquare2 / 3)) * cellSize) + cellSize)))){
                                selection = recentSquare2;
                            }

                            if(selection != -1){
                                int otherLink = quantumBoard.getLink(turnCount - 1, selection);
                                quantumBoard.getBoardTile(otherLink % 3, (int) Math.floor(otherLink / 3)).getMovesList().remove(quantumBoard.getMoveLocationInArray(otherLink, turnCount - 1));
                                quantumBoard.collapseTile(selection, turnCount - 1);
                                result = 0;

                                //all the other stuff
                                if(turn){
                                    displayLabel.setText("Player 2's turn");
                                    displayLabel.setForeground(blue);
    
                                }else{
                                    displayLabel.setText("Player 1's turn");
                                    displayLabel.setForeground(red);
                                    
                                }
    
                                int blankCount = 0;
                                for(int i = 0; i < 9; i++){
                                    if(quantumBoard.getBoardTile(i % 3, (int) Math.floor(i /3)).getState().equals(State.Blank)){
                                        blankCount++;
                                    }
                                }
                                if(blankCount < 2){
                                    displayLabel.setText("Stalemate");
                                    displayLabel.setForeground(Color.BLACK);
    
                                    //replay button stuff
                                    add(replayButton, gbc);
                                    
                                }
    
                                int boardresult = quantumBoard.checkEntireBoard();
    
                                if(boardresult == 1){
                                    quantumBoard.setState(State.Player1);
                                    displayLabel.setText("Player 1 wins");
                                    displayLabel.setForeground(red);
    
                                    //replay button stuff
                                    add(replayButton, gbc);
                                    
                                }else if(boardresult == 2){
                                    quantumBoard.setState(State.Player2);
                                    displayLabel.setText("Player 2 wins");
                                    displayLabel.setForeground(blue);
    
                                    //replay button stuff
                                    add(replayButton, gbc);
                                    
                                }
                                repaint();
                            }
                            
                        }
                    }
                                      
                }
              
              movementCounter = 0;
            }
        }); 
        addMouseMotionListener(new MouseAdapter() { 
            public void mouseDragged(MouseEvent e){
                //if(e.getButton( == ))
                if(movementCounter == 0){
                    mouseLocx = e.getX();
                    mouseLocy = e.getY();
                }
    
                int deltax = e.getX() - mouseLocx;
                int deltay = e.getY() - mouseLocy;
    
                mouseLocx = e.getX();
                mouseLocy = e.getY();

                offsetx = offsetx + deltax;
                offsety = offsety + deltay;
    
                movementCounter++;

                //System.out.println("X offset: " + offsetx + ", Y offset: " + offsety);

                //System.out.println(movementCounter);
                //System.out.println(deltax);
                repaint();
            }
            public void mouseMoved(MouseEvent e){
                if(result == 1){
                    if(((e.getX() > (xbound + ((recentSquare1 % 3) * cellSize))) && (e.getX() < (xbound + ((recentSquare1 % 3) * cellSize) + cellSize)) && (e.getY() > (ybound + ((int) (Math.floor(recentSquare1 / 3)) * cellSize))) && (e.getY() < (ybound + ((int) (Math.floor(recentSquare1 / 3)) * cellSize) + cellSize)))){
                        moveDrawLoc = recentSquare1;
                    }else if(((e.getX() > (xbound + ((recentSquare2 % 3) * cellSize))) && (e.getX() < (xbound + ((recentSquare2 % 3) * cellSize) + cellSize)) && (e.getY() > (ybound + ((int) (Math.floor(recentSquare2 / 3)) * cellSize))) && (e.getY() < (ybound + ((int) (Math.floor(recentSquare2 / 3)) * cellSize) + cellSize)))){
                        moveDrawLoc = recentSquare2;
                    }else{
                        moveDrawLoc = -1;
                    }
                }else{
                    moveDrawLoc = -1;
                }
                repaint();
            }
        }); 
        
        // Add this panel as a MouseWheelListener
        this.addMouseWheelListener(this);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(gameType == 3){
                    try {
                        outputBoard(massiveBoard);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }else if(gameType == 4){
                    quantumLineMode = !quantumLineMode;
                    if(quantumLineMode){
                        button.setText("Hide Lines");
                    }else{
                        button.setText("Show Lines");
                    }
                    repaint();
                }
                
            }

        });

        replayButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                closeWindow();
                try {
                    if(gameType != 3){
                        new GFrame(gameType);
                    }else{
                        new SFrame();
                    }
                } catch (FontFormatException | IOException e1) {
                    e1.printStackTrace();
                }
                
            }

        });

    }

    @Override
    public void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        //drawSimpleBoard(g, simpleBoard, true, 1);

        if(game == 1){
            drawSimpleBoard(g, simpleBoard, true, 0, 0);
        }else if(game == 2){
            drawLargeBoard(g, largeBoard, true, 0);
        }else if(game == 3){
            drawMassiveBoard(g, massiveBoard);
        }else if(game == 4){
            drawQuantumBoard(g, quantumBoard);
        }


    }

    public void closeWindow(){
        setVisible(false);
        JFrame parent = (JFrame) this.getTopLevelAncestor();
        parent.dispose();
    }

    public void drawSimpleBoard(Graphics2D g, SimpleBoard board, boolean single, int boardIndex, int secondBoardIndex){
        //revalidate();
        if(board.getActive()){
            if(turn){
                g.setColor(blue);
            }else{
                g.setColor(red);
            }
        }else{
            g.setColor(Color.BLACK);
        }
        
        thickness = width / 100;
        boundingSize = Math.min(width, height);
        xbound = (width - boundingSize) / 2;
        ybound = (height - boundingSize) / 2;
        cellSize = (int) boundingSize / 3;

        if(zoom > 1){
            zoom = 1;
        }

        //applies zoom
        buffer = (int) (buffer * zoom);
        thickness = (int) (thickness * zoom);
        boundingSize = (int) (boundingSize * zoom);
        xbound = (int) (xbound * zoom);
        ybound = (int) (ybound * zoom);
        cellSize = (int) (cellSize * zoom);

        ybound = -1 * (boundingSize / 2);
        xbound = -1 * (boundingSize / 2);

        if(thickness < 1){
            thickness = 1;
        }

        buffer = (int) ((width / 40) * zoom);

        xbound = xbound + offsetx;
        ybound = ybound + offsety;

        if(single){
            g.fillRect(xbound + cellSize - (thickness / 2), ybound, thickness, boundingSize);
            g.fillRect(xbound + (2 * cellSize) - (thickness / 2), ybound, thickness, boundingSize);
    
            g.fillRect(xbound, ybound + cellSize - (thickness / 2), boundingSize, thickness);
            g.fillRect(xbound, ybound + (2 * cellSize) - (thickness / 2), boundingSize, thickness);

            //loops through the board
            double iconScale = 0.8;
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(simpleBoard.getBoardTile(i, j).getState() == State.Player1){
                        g.drawImage(xImage, (int) (xbound + (i * cellSize) + ((cellSize - (cellSize * iconScale)) / 2)), (int) (ybound + (j * cellSize) + ((cellSize - (cellSize * iconScale)) / 2)), (int) (cellSize * iconScale), (int) (cellSize * iconScale), null);
                    }else if(simpleBoard.getBoardTile(i, j).getState() == State.Player2){
                        g.drawImage(oImage, (int) (xbound + (i * cellSize) + ((cellSize - (cellSize * iconScale)) / 2)), (int) (ybound + (j * cellSize) + ((cellSize - (cellSize * iconScale)) / 2)), (int) (cellSize * iconScale), (int) (cellSize * iconScale), null);
                    }
                }
            }
        }else{
            int cellX = boardIndex % 3;
            int cellY = (int) Math.floor(boardIndex /3);

            int boardx = secondBoardIndex % 3;
            int boardy = (int) Math.floor(secondBoardIndex /3);

            g.fillRect(xbound + cellSize - (thickness / 2) + (boundingSize * cellX) + (boundingSize * 3 * boardx), ybound + (boundingSize * cellY) + buffer + (boundingSize * 3 * boardy), thickness, boundingSize - buffer - buffer);
            g.fillRect(xbound + (2 * cellSize) - (thickness / 2) + (boundingSize * cellX) + (boundingSize * 3 * boardx), ybound + (boundingSize * cellY) + buffer + (boundingSize * 3 * boardy), thickness, boundingSize - buffer - buffer);
    
            g.fillRect(xbound + (boundingSize * cellX) + buffer + (boundingSize * 3 * boardx), ybound + cellSize - (thickness / 2) + (boundingSize * cellY) + (boundingSize * 3 * boardy), boundingSize - buffer - buffer, thickness);
            g.fillRect(xbound + (boundingSize * cellX) + buffer + (boundingSize * 3 * boardx), ybound + (2 * cellSize) - (thickness / 2) + (boundingSize * cellY) + (boundingSize * 3 * boardy), boundingSize - buffer - buffer, thickness);

            //loops through the board
            double iconScale = 0.8;
            double iconOffset = (cellSize - (cellSize * iconScale)) / 2;
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(board.getBoardTile(i, j).getState() == State.Player1){
                        g.drawImage(xImage, (int) (xbound + (i * cellSize) + iconOffset) + (boundingSize * cellX) + (boundingSize * 3 * boardx), (int) (ybound + (j * cellSize) + iconOffset) + (boundingSize  * cellY) + (boundingSize * 3 * boardy), (int) (cellSize * iconScale), (int) (cellSize * iconScale), null);
                    }else if(board.getBoardTile(i, j).getState() == State.Player2){
                        g.drawImage(oImage, (int) (xbound + (i * cellSize) + iconOffset) + (boundingSize  * cellX) + (boundingSize * 3 * boardx), (int) (ybound + (j * cellSize) + iconOffset) + (boundingSize  * cellY) + (boundingSize * 3 * boardy), (int) (cellSize * iconScale), (int) (cellSize * iconScale), null);
                    }
                }
            }

            int x = boardIndex % 3;
            int y = (int) Math.floor(boardIndex / 3);

            iconOffset = (boundingSize - (boundingSize * iconScale)) / 2;
            if(board.getState() == State.Player1){
                g.drawImage(xImage, (int) (xbound + iconOffset) + (x * boundingSize) + (boundingSize * 3 * boardx), (int) (ybound + iconOffset) + (y * boundingSize) + (boundingSize * 3 * boardy), (int) (boundingSize * iconScale), (int) (boundingSize * iconScale), null);
            }else if (board.getState() == State.Player2){
                g.drawImage(oImage, (int) (xbound + iconOffset) + (x * boundingSize) + (boundingSize * 3 * boardx), (int) (ybound + iconOffset) + (y * boundingSize) + (boundingSize * 3 * boardy), (int) (boundingSize * iconScale), (int) (boundingSize * iconScale), null);
            }
        }

    }

    public void drawLargeBoard(Graphics2D g, LargeBoard board, boolean single, int boardIndex){
        if(board.getActive()){
            if(turn){
                g.setColor(blue);
            }else{
                g.setColor(red);
            }
        }else{
            g.setColor(Color.BLACK);
        }

        for(int i = 0; i < 9; i++){

            int x = i % 3;
            int y = (int) Math.floor(i / 3);
            drawSimpleBoard(g, board.getBoardArray(x, y), false, i, boardIndex);
        }

        //larger lines
        if(board.getActive()){
            if(turn){
                g.setColor(blue);
            }else{
                g.setColor(red);
            }
        }else{
            g.setColor(Color.BLACK);
        }
        
        thickness = width / 50;
        boundingSize = Math.min(width, height);
        xbound = (width - boundingSize) / 2;
        ybound = (height - boundingSize) / 2;
        cellSize = (int) boundingSize / 3;

        if(zoom > 1){
            zoom = 1;
        }

        //applies zoom
        thickness = (int) (thickness * zoom);
        boundingSize = (int) (boundingSize * zoom);
        xbound = (int) (xbound * zoom);
        ybound = (int) (ybound * zoom);
        cellSize = (int) (cellSize * zoom);

        ybound = -1 * (boundingSize / 2);
        xbound = -1 * (boundingSize / 2);

        if(thickness < 1){
            thickness = 1;
        }

        buffer = (int) ((width / 50) * zoom);

        xbound = xbound + offsetx;
        ybound = ybound + offsety;

        if(single){
            g.fillRect(xbound + boundingSize - (thickness / 2), ybound + buffer, thickness, boundingSize * 3 - buffer- buffer);
            g.fillRect(xbound + (2 * boundingSize) - (thickness / 2), ybound + buffer, thickness, boundingSize * 3 - buffer- buffer);
    
            g.fillRect(xbound + buffer, ybound + boundingSize - (thickness / 2), boundingSize * 3 - buffer- buffer, thickness);
            g.fillRect(xbound + buffer, ybound + (2 * boundingSize) - (thickness / 2), boundingSize * 3 - buffer- buffer, thickness);
        }else{
            int boardX = boardIndex % 3;
            int boardY = (int) Math.floor(boardIndex /3);

            g.fillRect(xbound + boundingSize - (thickness / 2) + (boundingSize * 3 * boardX), ybound + buffer + (boundingSize * 3 * boardY), thickness, boundingSize * 3 - buffer- buffer);
            g.fillRect(xbound + (2 * boundingSize) - (thickness / 2) + (boundingSize * 3 * boardX), ybound + buffer + (boundingSize * 3 * boardY), thickness, boundingSize * 3 - buffer- buffer);
    
            g.fillRect(xbound + buffer + (boundingSize * 3 * boardX), ybound + boundingSize - (thickness / 2) + (boundingSize * 3 * boardY), boundingSize * 3 - buffer- buffer, thickness);
            g.fillRect(xbound + buffer + (boundingSize * 3 * boardX), ybound + (2 * boundingSize) - (thickness / 2) + (boundingSize * 3 * boardY), boundingSize * 3 - buffer- buffer, thickness);

            int x = boardIndex % 3;
            int y = (int) Math.floor(boardIndex / 3);

            double iconScale = 0.8;
            double iconOffset = (boundingSize - (boundingSize * iconScale)) / 2;
            if(board.getState() == State.Player1){
                g.drawImage(xImage, (int) (xbound + iconOffset) + (x * boundingSize) + (boundingSize * 9 * boardX), (int) (ybound + iconOffset) + (y * boundingSize) + (boundingSize * 9 * boardY), (int) (boundingSize * iconScale), (int) (boundingSize * iconScale), null);
            }else if (board.getState() == State.Player2){
                g.drawImage(oImage, (int) (xbound + iconOffset) + (x * boundingSize) + (boundingSize * 9 * boardX), (int) (ybound + iconOffset) + (y * boundingSize) + (boundingSize * 9 * boardY), (int) (boundingSize * iconScale), (int) (boundingSize * iconScale), null);
            }
        }


    }
    
    public void drawMassiveBoard(Graphics2D g, MassiveBoard board){
        if(board.getActive()){
            if(turn){
                g.setColor(blue);
            }else{
                g.setColor(red);
            }
        }else{
            g.setColor(Color.BLACK);
        }

        //larger lines
        if(board.getActive()){
            if(turn){
                g.setColor(blue);
            }else{
                g.setColor(red);
            }
        }else{
            g.setColor(Color.BLACK);
        }
        
        thickness = width / 30;
        boundingSize = Math.min(width, height);
        xbound = (width - boundingSize) / 2;
        ybound = (height - boundingSize) / 2;
        cellSize = (int) boundingSize / 3;

        if(zoom > 1){
            zoom = 1;
        }

        //applies zoom
        thickness = (int) (thickness * zoom);
        boundingSize = (int) (boundingSize * zoom);
        xbound = (int) (xbound * zoom);
        ybound = (int) (ybound * zoom);
        cellSize = (int) (cellSize * zoom);

        ybound = -1 * (boundingSize / 2);
        xbound = -1 * (boundingSize / 2);

        if(thickness < 1){
            thickness = 1;
        }

        buffer = (int) ((width / 60) * zoom);

        xbound = xbound + offsetx;
        ybound = ybound + offsety;
        
        g.fillRect(xbound + (boundingSize * 3) - (thickness / 2), ybound + buffer, thickness, boundingSize * 9 - buffer- buffer);
        g.fillRect(xbound + (6 * boundingSize) - (thickness / 2), ybound + buffer, thickness, boundingSize * 9 - buffer- buffer);

        g.fillRect(xbound + buffer, ybound + (boundingSize * 3) - (thickness / 2), boundingSize * 9 - buffer- buffer, thickness);
        g.fillRect(xbound + buffer, ybound + (6 * boundingSize) - (thickness / 2), boundingSize * 9 - buffer- buffer, thickness);

        for(int i = 0; i < 9; i++){

            int x = i % 3;
            int y = (int) Math.floor(i / 3);

            drawLargeBoard(g, massiveBoard.getBoardArray(x, y), false, i);

            double iconScale = 0.9;
            double iconOffset = ((3 * boundingSize) - (3 * boundingSize * iconScale)) / 2;
            if(board.getBoardArray(x, y).getState() == State.Player1){
                g.drawImage(xImage, (int) (xbound + iconOffset) + (x * boundingSize * 3), (int) (ybound + iconOffset) + (y * boundingSize * 3), (int) (boundingSize * 3 * iconScale), (int) (boundingSize * 3 * iconScale), null);
            }else if (board.getBoardArray(x, y).getState() == State.Player2){
                g.drawImage(oImage, (int) (xbound + iconOffset) + (x * boundingSize * 3), (int) (ybound + iconOffset) + (y * boundingSize * 3), (int) (boundingSize * 3* iconScale), (int) (boundingSize * 3 * iconScale), null);
            }
        }
    }

    public void drawQuantumBoard(Graphics2D g, QuantumBoard board){
        g.setColor(Color.BLACK);
        
        thickness = width / 100;
        boundingSize = Math.min(width, height);
        xbound = (width - boundingSize) / 2;
        ybound = (height - boundingSize) / 2;
        cellSize = (int) boundingSize / 3;

        if(zoom > 1){
            zoom = 1;
        }

        //applies zoom
        buffer = (int) (buffer * zoom);
        thickness = (int) (thickness * zoom);
        boundingSize = (int) (boundingSize * zoom);
        xbound = (int) (xbound * zoom);
        ybound = (int) (ybound * zoom);
        cellSize = (int) (cellSize * zoom);

        ybound = -1 * (boundingSize / 2);
        xbound = -1 * (boundingSize / 2);

        if(thickness < 1){
            thickness = 1;
        }

        buffer = (int) ((width / 40) * zoom);

        xbound = xbound + offsetx;
        ybound = ybound + offsety;

        g.fillRect(xbound + cellSize - (thickness / 2), ybound, thickness, boundingSize);
        g.fillRect(xbound + (2 * cellSize) - (thickness / 2), ybound, thickness, boundingSize);

        g.fillRect(xbound, ybound + cellSize - (thickness / 2), boundingSize, thickness);
        g.fillRect(xbound, ybound + (2 * cellSize) - (thickness / 2), boundingSize, thickness);

        //loops through the board
        double iconScale = 0.8;
        double imageSize = iconScale * cellSize;
        double numSizeX = (imageSize * 3) / 16;
        double numSizeY = imageSize / 4;

        numSizeX *=1.5;
        numSizeY *=1.5;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                int xNetOffset = (int) (xbound + (i * cellSize) + ((cellSize - imageSize) / 2));
                int yNetOffset = (int) (ybound + (j * cellSize) + ((cellSize - imageSize) / 2));

                if(board.getBoardTile(i, j).getState() == State.Player1){
                    g.drawImage(xImage, xNetOffset, yNetOffset, (int) imageSize, (int) imageSize, null);
                    g.drawImage(getTurnImage(board.getBoardTile(i, j).getTurn()), (int) (xNetOffset + imageSize - numSizeX + (imageSize / 9)), (int) (yNetOffset + imageSize - numSizeY+ (imageSize / 9)), (int) numSizeX, (int) numSizeY, null);
                }else if(board.getBoardTile(i, j).getState() == State.Player2){
                    g.drawImage(oImage, xNetOffset, yNetOffset, (int) imageSize, (int) imageSize, null);
                    g.drawImage(getTurnImage(board.getBoardTile(i, j).getTurn()), (int) (xNetOffset + imageSize - numSizeX+ (imageSize / 9)), (int) (yNetOffset + imageSize - numSizeY + (imageSize / 9)), (int) numSizeX, (int) numSizeY, null);
                }
            }
        }

        double quantumCellSize = cellSize / 3;
        double quantumImageSize = quantumCellSize * iconScale;
        double quantumPadding = ((quantumCellSize - quantumImageSize) / 2);

        numSizeX = (quantumImageSize * 3) / 16;
        numSizeY = quantumImageSize / 4;

        numSizeX *=2;
        numSizeY *=2;

        //draws the quantum moves
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                for(int k = 0; k < quantumBoard.getBoardTile(i, j).getMovesList().size(); k++){
                    int xoffset = k % 3;
                    int yoffset = (int) Math.floor(k / 3);

                    int xNetOffset = (int) (xbound + (xoffset * quantumCellSize) + quantumPadding + (i * cellSize));
                    int yNetOffset = (int) (ybound + (yoffset * quantumCellSize) + quantumPadding + (j * cellSize));

                    int move = quantumBoard.getBoardTile(i, j).getMovesList().get(k);

                    if(move % 2 != 0){
                        g.drawImage(xImage, xNetOffset, yNetOffset, (int) quantumImageSize, (int) quantumImageSize, null);
                        g.drawImage(getTurnImage(move), (int) (xNetOffset + quantumImageSize - numSizeX + (quantumImageSize / 3)), (int) (yNetOffset + quantumImageSize - numSizeY + (quantumImageSize / 3)), (int) numSizeX, (int) numSizeY, null);
                    }else{
                        g.drawImage(oImage, xNetOffset, yNetOffset, (int) quantumImageSize, (int) quantumImageSize, null);
                        g.drawImage(getTurnImage(move), (int) (xNetOffset + quantumImageSize - numSizeX + (quantumImageSize / 3)), (int) (yNetOffset + quantumImageSize - numSizeY + (quantumImageSize / 3)), (int) numSizeX, (int) numSizeY, null);
                    }
                }
            }
        }

        //draws links
        if(quantumLineMode){
            for(int i = 1; i < turnCount; i++){
                if(i % 2 != 0){
                    g.setColor(red);
                }else{
                    g.setColor(blue);
                }
    
                //gets first one
                int first = quantumBoard.getBoardWithMove(i);
                int second = quantumBoard.getOtherBoardWithMove(i);
    
                int qoffset1 = quantumBoard.getMoveLocationInArray(first, i);
                int qoffset2 = quantumBoard.getMoveLocationInArray(second, i);
    
                //System.out.println(first + " " + second);
    
                int x1 = first % 3;
                int y1 = (int) Math.floor(first / 3);
                int x2 = second % 3;
                int y2 = (int) Math.floor(second / 3);
    
                int qx1 = (qoffset1 % 3) - 1;
                int qy1 = (int) Math.floor(qoffset1 / 3) - 1;
                int qx2 = (qoffset2 % 3) - 1;
                int qy2 = (int) Math.floor(qoffset2 / 3) - 1;
    
                g.drawLine(xbound + (x1 * cellSize) + (cellSize / 2) + (int) (qx1 * quantumCellSize) -1, ybound + (y1 * cellSize) + (cellSize / 2) + (int) (qy1 * quantumCellSize) -1, xbound + (x2 * cellSize) + (cellSize / 2) + (int) (qx2 * quantumCellSize) -1, ybound + (y2 * cellSize) + (cellSize / 2) + (int) (qy2 * quantumCellSize) -1);
            }
        }

        if(result == 1){
            if(moveDrawLoc != -1){
                drawMove(g, moveDrawLoc, turnCount - 1);
            }
        }

    }

    public void drawMove(Graphics2D g, int loc, int turn){

        int x = loc % 3;
        int y = (int) Math.floor(loc / 3);

        double iconScale = 0.8;
        double imageSize = iconScale * cellSize;

        if(turn % 2 != 0){
            g.drawImage(xImage, (int) (xbound + (x * cellSize) + ((cellSize - imageSize) / 2)), (int) (ybound + (y * cellSize) + ((cellSize - imageSize) / 2)), (int) imageSize, (int) imageSize, null);
        }else{
            g.drawImage(oImage, (int) (xbound + (x * cellSize) + ((cellSize - imageSize) / 2)), (int) (ybound + (y * cellSize) + ((cellSize - imageSize) / 2)), (int) imageSize, (int) imageSize, null);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        int realx = getRealX(e);
        int realy = getRealY(e);

        if (e.getWheelRotation() < 0) {
            zoom = zoom / 0.75;

        }else{
            zoom = zoom * 0.75;

        }

        int diffx = (getRealX(e) - realx);
        int diffy = (getRealY(e) - realy);

        if(zoom > 1){
            zoom = 1;
        }else{
            offsetx = offsetx + (int) (diffx * zoom);
            offsety = offsety + (int) (diffy * zoom);
        }

        repaint();

    }
    
    public void resizeVariables(int width1, int height1){
        width = width1;
        height = height1;
        
    }

    public int getRealX(MouseEvent e){
        return (int) ((e.getX() - offsetx) / zoom);
    }

    public int getRealY(MouseEvent e){
        //System.out.println(e.getY() / zoom);
        return (int) ((e.getY() - offsety) / zoom);
    }

    public void outputBoard(MassiveBoard board) throws FileNotFoundException{

        String output = "";
        output += game;
        //System.out.print("\n\n" + game);
        if(turn){
            //System.out.print("2");
            output += 2;
        }else{
            //System.out.print("1");
            output += 1;
        }
        //prints recent move
        //System.out.print(recentLarge);
        output += recentLarge;

        //System.out.print(recentSimple);
        output += recentSimple;

        //System.out.print(recentCell + " ");
        output += recentCell + " ";

        int moveTally = 0;

        for(int i = 0; i < 27; i++){
            for(int j = 0; j < 27; j++){
                int xlargeboard = (int) Math.floor(i / 9);
                int ylargeboard = (int) Math.floor(j / 9);

                int xboard = (int) Math.floor((i - (xlargeboard * 9)) / 3);
                int yboard = (int) Math.floor((j - (ylargeboard * 9)) / 3);

                int xcell = (int) Math.floor(i - (xlargeboard * 9 + xboard * 3));
                int ycell = (int) Math.floor(j - (ylargeboard * 9 + yboard * 3));

                int largeBoard = xlargeboard + (ylargeboard * 3);
                int Board = xboard + (yboard * 3);
                int cell = xcell + (ycell * 3);

                State state = board.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xboard, yboard).getBoardArray(xcell, ycell).getState();
                if(state == State.Player1){
                    //System.out.print("1");
                    output += 1;
                    moveTally++;
                }else if(state == State.Player2){
                    //System.out.print("2");
                    output += 2;
                    moveTally++;
                }

                if(state != State.Blank){
                    //System.out.print(largeBoard);
                    output += largeBoard;

                    //System.out.print(Board);
                    output += Board;

                    //System.out.print(cell + " ");
                    output += cell + " ";

                }

            }
        }

        String path = outputDir + File.separator + "move" + moveTally;
        File filepath = new File(path);
        
        FileWriter fw;
        try {
            fw = new FileWriter(filepath);
            fw.write(output.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

    public void setMassiveBoard(MassiveBoard board, boolean turn1){
        this.massiveBoard = board;
        turn = turn1;

        if(turn){
            displayLabel.setText("Player 2 goes");
            displayLabel.setForeground(blue);

        }else{
            displayLabel.setText("Player 1 goes");
            displayLabel.setForeground(red);
            
        }
    }

    public void setOutputDir(File file){
        outputDir = file;
        outputPath = outputDir.toString();
    }

    public Image getTurnImage(int turn){
        switch (turn){
            case 1: return Image1;
            case 2: return Image2;
            case 3: return Image3;
            case 4: return Image4;
            case 5: return Image5;
            case 6: return Image6;
            case 7: return Image7;
            case 8: return Image8;
            case 9: return Image9;
            default: return null;
        }
    }
}
