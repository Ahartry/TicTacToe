package com.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GPanel extends JPanel implements MouseWheelListener{

    Image xImage;
    Image xImage2;
    Image oImage;
    Image oImage2;

    int game;
    int offsetx = 0;
    int offsety = 0;
    int width = 1000;
    int height = 550;
    int pwidth = 1000;
    int pheight = 550;
    double zoom = 1;
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
    Color buttonColor;
    SimpleBoard simpleBoard;
    LargeBoard largeBoard;
    MassiveBoard massiveBoard;
    QuantumBoard quantumBoard;
    QuantumBoard3D quantumBoard3D;

    JLabel displayLabel;

    JPanel replayPanel;

    GButton replayButton;
    GFrame frameobject;

    //qAI qAI;
    //qAI3D qAI3D;
    //oAI oAI;
    mAI mAI;

    Sound sound;

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

    int theSquare = 0;

    int depth = 6;

    boolean collapseMove = false;

    boolean bot;

    double theta = Math.PI / 4;
    int imagex = 0;
    int imagey = 0;

    File outputDir;
    String outputPath;

    public GPanel(int gameType, JLabel displayLabel1, GButton button, boolean bot, GFrame frame, Sound sound) throws FontFormatException, IOException{

        xImage = frame.getxImage();
        oImage = frame.getoImage();
        xImage2 = frame.getxImage2();
        oImage2 = frame.getoImage2();

        game = gameType;

        displayLabel = displayLabel1;

        replayButton = new GButton("Replay");
        replayButton.setSound(sound);
        
        this.bot = bot;
        this.sound = sound;

        frameobject = frame;

        //instantiates boards and sets default zooms
        if(game == 1){
            simpleBoard = new SimpleBoard();
            zoom = 0.75;
            offsetx = width / 2;
            offsety = height / 2;
        }else if(game == 2){
            largeBoard = new LargeBoard();
            largeBoard.setActive(true);
            zoom = Math.pow(0.75, 4);
            offsetx = 325;
            offsety = 100;
        }else if(game == 3){
            massiveBoard = new MassiveBoard();
            massiveBoard.setActive(true);
            zoom = Math.pow(0.75, 8);
            offsetx = 285;
            offsety = 50;
        }else if(game == 4){
            quantumBoard = new QuantumBoard();
            zoom = 0.75;
            offsetx = width / 2;
            offsety = height / 2;
            turnCount = 1;
        }else if(game == 5){
            quantumBoard3D = new QuantumBoard3D();
            zoom = Math.pow(0.75, 5);
            offsetx = 505;
            offsety = 95;
            turnCount = 1;
        }

        repaint();

        // qAI = new qAI();
        // qAI3D = new qAI3D();
        //oAI = new oAI();
        //mAI = new mAI(gameType, depth);

        //replay button stuff
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(80f);
        setLayout(new BorderLayout());
        replayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        replayButton.setFont(font);
        replayButton.setFocusable(false);
        replayButton.setPreferredSize(new Dimension(120, 50));

        replayPanel = new JPanel();
        replayPanel.add(replayButton);
        replayPanel.setOpaque(false);

        // GridBagConstraints gbc = new GridBagConstraints();
        // gbc.insets = new Insets(400, 50, 10, 50);

        displayLabel.setForeground(red);

        //all the clicking stuff
        addMouseListener(new MouseAdapter() { 
            public void mouseReleased(MouseEvent e) { 
                if(movementCounter == 0){

                    if(game == 1 && simpleBoard.getState() == State.Blank){
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
                                    replay();
                                    
                                }else if(result == 2){
                                    simpleBoard.setState(State.Player2);
                                    displayLabel.setText("Player 2 wins");
                                    displayLabel.setForeground(blue);

                                    //replay button stuff
                                    replay();
                                    
                                }

                                if(count == 9 && result == 0){
                                    displayLabel.setText("Stalemate");
                                    displayLabel.setForeground(Color.BLACK);

                                    //replay button stuff
                                    replay();
                                    
                                }

                                repaint();
                            }

                            
                            //System.out.println("X: " + xcell + ", Y: " + ycell);
    
                        }
                    }else if(game == 2 && largeBoard.getState() == State.Blank){
                        //System.out.println("\nclick1");
                        if((e.getX() > xbound && e.getX() < (xbound + (boundingSize * 3)) && e.getY() > ybound && e.getY() < (ybound + (boundingSize * 3))) && largeBoard.getState() == State.Blank){
                            //System.out.println("click2");
                            int relativeX = e.getX() - xbound;
                            int relativeY = e.getY() - ybound;
    
                            int xboard = (int) Math.floor(relativeX / boundingSize);
                            int yboard = (int) Math.floor(relativeY / boundingSize);

                            //System.out.println("Board: " + xboard + ", " + yboard);

                            int xcell = (int) Math.floor((3 * (relativeX - (xboard * boundingSize))) / boundingSize);
                            int ycell = (int) Math.floor((3 * (relativeY - (yboard * boundingSize))) / boundingSize);
    
                            if(largeBoard.getBoardArray(xboard, yboard).getBoardTile(xcell, ycell).getState() == State.Blank && largeBoard.getBoardArray(xboard, yboard).getActive()){
                                largeBoard.getBoardArray(xboard, yboard).setBoardTile(xcell, ycell, turn);
                                largeBoard.add(xboard, yboard, xcell, ycell, turn);

                                int end = largeMoveAftermath(xboard, yboard, xcell, ycell);

                                if(bot && end == 0){
                                    displayLabel.setText("Player 2 is thinking");
                                    displayLabel.setForeground(blue);
                                    repaint();

                                    //from chatgpt to make the ui update
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Perform the long-running task
                                            // int move = AI.checkLargeBoard(largeBoard, (((xboard + (yboard * 3)) * 10) + (xcell + (ycell * 3))));
                                            // largeBoard.move(move, turn);
                                            Move choice = mAI.check(largeBoard, depth, false);
                                            int move = choice.loc;
                                            //System.out.println("Moving to " + move);
                                            int board = (int) Math.floor(move / 10);
                                            int xboard2 = board % 3;
                                            int yboard2 = (int) Math.floor(board / 3);
                                            int cell = move - (board * 10);
                                            int xcell2 = cell % 3;
                                            int ycell2 = (int) Math.floor(cell / 3);
                                            largeMoveAftermath(xboard2, yboard2, xcell2, ycell2);
                                        }
                                    });
                                }

                                //System.out.println("Eval: " + AI.evalBoard(largeBoard));
                                repaint();
                            }else{
                                //System.out.println("Illegal click at board " + xboard + ", " + yboard + " and cell " + xcell + ", " + ycell);
                            }

                            
                            //System.out.println("X: " + xcell + ", Y: " + ycell);
    
                        }else if((e.getX() > xbound && e.getX() < (xbound + (boundingSize * 3)) && e.getY() > ybound && e.getY() < (ybound + (boundingSize * 3))) && largeBoard.getState() != State.Blank){
                            //System.out.println("Error here");
                        }
                    }else if(game == 3 && massiveBoard.getState() == State.Blank){
                        if((e.getX() > xbound && e.getX() < (xbound + (boundingSize * 9)) && e.getY() > ybound && e.getY() < (ybound + (boundingSize * 9))) && massiveBoard.getState() == State.Blank){
                            int relativeX = e.getX() - xbound;
                            int relativeY = e.getY() - ybound;

                            int xlargeboard = (int) Math.floor(3 * relativeX / (boundingSize * 9));
                            int ylargeboard = (int) Math.floor(3 * relativeY / (boundingSize * 9));
    
                            int xboard = (int) Math.floor((3 * (relativeX - (xlargeboard * boundingSize * 3))) / (boundingSize * 3));
                            int yboard = (int) Math.floor((3 * (relativeY - (ylargeboard * boundingSize * 3))) / (boundingSize * 3));

                            int xcell = (int) Math.floor((3 * (relativeX - (xlargeboard * boundingSize * 3 + xboard * boundingSize))) / (boundingSize));
                            int ycell = (int) Math.floor((3 * (relativeY - (ylargeboard * boundingSize * 3 + yboard * boundingSize))) / (boundingSize));
    
                            if(massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xboard, yboard).getBoardTile(xcell, ycell).getState() == State.Blank && massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xboard, yboard).getActive()){
                                massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xboard, yboard).setBoardTile(xcell, ycell, turn);

                                int end = massiveMoveAftermath(xlargeboard, ylargeboard, xboard, yboard, xcell, ycell);

                                if(bot && end == 0){
                                    displayLabel.setText("Player 2 is thinking");
                                    displayLabel.setForeground(blue);
                                    repaint();

                                    //from chatgpt to make the ui update
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            //maybe temporary, create new one
                                            Move move = mAI.check(massiveBoard, depth, false);
                                            int t3 = move.loc / 100;
                                            int t2 = (move.loc - (t3 * 100)) / 10;
                                            int t1 = (move.loc - (t3 * 100) - (t2 * 10));
                                            massiveMoveAftermath(t3 % 3, t3 / 3, t2 % 3, t2 / 3, t1 % 3,t1 / 3);
                                        }
                                    });
                                }
                            }
    
                        }
                    }else if(game == 4 && quantumBoard.getState() == State.Blank){
                        if(quantumBoard.getState() == State.Blank && (e.getX() > xbound && e.getX() < (xbound + boundingSize) && e.getY() > ybound && e.getY() < (ybound + boundingSize)) && quantumBoard.getState() == State.Blank && result == 0){
                            int xboard = e.getX() - xbound;
                            int yboard = e.getY() - ybound;
    
                            int xcell = (int) Math.floor(3 * xboard / boundingSize);
                            int ycell = (int) Math.floor(3 * yboard / boundingSize);

                            theSquare = xcell + (ycell * 3);
    
                            if(quantumBoard.getState() == State.Blank && quantumBoard.getBoardStatelist()[theSquare] == State.Blank && e.getButton() == MouseEvent.BUTTON1){

                                if(quantumMove && (xcell + (ycell * 3)) != recentCell){
                                    Move move = new Move(xcell + (ycell * 3), recentCell);
                                    move.setTurn(turnCount);
                                    // quantumBoard.getBoardTile(xcell, ycell).addMove(turnCount);

                                    // quantumBoard.getBoardTile(recentCell % 3, recentCell / 3).addMove(turnCount);

                                    //don't really need this here I think
                                    //quantumBoard.incrementMoveCount();

                                    quantumBoard.move(move);

                                    recentSquare1 = xcell + (ycell * 3);
                                    recentSquare2 = recentCell;
                                    //quantumBoard.printBoard();

                                    result = quantumBoard.checkLoops(move);

                                    turnCount++;
                                    quantumMove = !quantumMove;

                                    //display stuff
                                    turn = !turn;
                                    recentCell = -1;

                                    if(turn){
                                        displayLabel.setText("Player 2's turn");
                                        displayLabel.setForeground(blue);
                                        if(bot){
                                            displayLabel.setText("Player 2 is thinking");
                                            displayLabel.setForeground(blue);
                                            repaint();
                                            SwingUtilities.invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(result != 0){
                                                        mAI.chooseCollapse(quantumBoard, recentSquare1, recentSquare2);
                                                        result = 0;
                                                        if(turn){
                                                            displayLabel.setText("Player 2's turn");
                                                            displayLabel.setForeground(blue);
                            
                                                        }else{
                                                            displayLabel.setText("Player 1's turn");
                                                            displayLabel.setForeground(red);
                                                            
                                                        }
                            
                                                        int boardresult = quantumBoard.checkEntireBoard();
                            
                                                        result = 0;

                                                        int blankCount = 0;
                                                        for(int i = 0; i < 9; i++){
                                                            if(quantumBoard.getBoardStatelist()[i].equals(State.Blank)){
                                                                blankCount++;
                                                            }
                                                        }
                                                        if(blankCount < 2){
                                                            displayLabel.setText("Stalemate");
                                                            displayLabel.setForeground(Color.BLACK);
                                                            //quantumBoard.clear();

                                                            //so that it can't be played on again
                                                            quantumBoard.setState(State.Player1);
                            
                                                            result = 1;
                                                            //replay button stuff
                                                            replay();
                                                        }

                                                        if(boardresult == 1){
                                                            quantumBoard.setState(State.Player1);
                                                            displayLabel.setText("Player 1 wins");
                                                            displayLabel.setForeground(red);
                                                            //quantumBoard.clear();

                                                            result = 1;
                                                            //replay button stuff
                                                            replay();
                                                            quantumBoard.setState(State.Player1);
                                                            
                                                        }else if(boardresult == 2){
                                                            quantumBoard.setState(State.Player2);
                                                            displayLabel.setText("Player 2 wins");
                                                            displayLabel.setForeground(blue);
                                                            //quantumBoard.clear();
                            
                                                            result = 1;
                                                            //replay button stuff
                                                            replay();
                                                            quantumBoard.setState(State.Player2);
                                                            
                                                        }
                                                        repaint();
                                                    }
                                                    //these are actually using result completely differently. First is if there is a loop, second is game end
                                                    if(result == 1){
                                                        return;
                                                    }
                                                    Move move;
                                                    move = mAI.check(quantumBoard, 1, true);

                                                    recentSquare1 = move.loc;
                                                    recentSquare2 = move.loc2;
                                                    result = quantumBoard.checkLoops(move);
                                                    turnCount++;
                                                    turn = !turn;
                                                    if(quantumBoard.getState() == State.Blank){
                                                        displayLabel.setText("Player 1's turn");
                                                        displayLabel.setForeground(red);
                                                        int blankCount = 0;
                                                        for(int i = 0; i < 9; i++){
                                                            if(quantumBoard.getBoardStatelist()[i].equals(State.Blank)){
                                                                blankCount++;
                                                            }
                                                        }
                                                        if(blankCount < 2){
                                                            displayLabel.setText("Stalemate");
                                                            displayLabel.setForeground(Color.BLACK);
                                                            //quantumBoard.clear();
                            
                                                            //replay button stuff
                                                            replay();
                                                        }
                                                    }else{
                                                        //quantumBoard.clear();
                                                    }
                                                    //does stuff if a loop is found
                                                    if(result == 0){
                                                        //System.out.println("No loop");
                                                    }else{
                                                        //System.out.println("Loop");
                                                        if(turn){
                                                            displayLabel.setText("Player 2 chooses collapse");
                        
                                                        }else{
                                                            displayLabel.setText("Player 1 chooses collapse");
                                                            
                                                        }

                                                        moveDrawLoc = recentSquare1;
                                                    }
                                                    
                                                    repaint();
                                                }
                                            });

                                        }

                                    }else{
                                        displayLabel.setText("Player 1's turn");
                                        displayLabel.setForeground(red);
                                        
                                    }

                                    //does stuff if a loop is found
                                    if(result == 0){
                                        //System.out.println("No loop");
                                    }else{
                                        //System.out.println("Loop");
                                        if(turn){
                                            displayLabel.setText("Player 2 chooses collapse");
        
                                        }else{
                                            displayLabel.setText("Player 1 chooses collapse");
                                            
                                        }

                                        moveDrawLoc = recentSquare1;
                                    }

                                //thing to unselect
                                }else if(quantumMove && (xcell + (ycell * 3)) == recentCell){
                                    quantumMove = false;
                                    recentCell = -1;

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
                                //quantumBoard.printTile(xcell, ycell);
                            }

                            
                            //System.out.println("X: " + xcell + ", Y: " + ycell);
    
                        }else if(result == 1 && quantumBoard.getState() == State.Blank){
                            //choosing the collpase
                            int selection = -1;
                            if(((e.getX() > (xbound + ((recentSquare1 % 3) * cellSize))) && (e.getX() < (xbound + ((recentSquare1 % 3) * cellSize) + cellSize)) && (e.getY() > (ybound + ((int) (Math.floor(recentSquare1 / 3)) * cellSize))) && (e.getY() < (ybound + ((int) (Math.floor(recentSquare1 / 3)) * cellSize) + cellSize)))){
                                selection = recentSquare1;
                            }else if(((e.getX() > (xbound + ((recentSquare2 % 3) * cellSize))) && (e.getX() < (xbound + ((recentSquare2 % 3) * cellSize) + cellSize)) && (e.getY() > (ybound + ((int) (Math.floor(recentSquare2 / 3)) * cellSize))) && (e.getY() < (ybound + ((int) (Math.floor(recentSquare2 / 3)) * cellSize) + cellSize)))){
                                selection = recentSquare2;
                            }

                            if(selection != -1){
                                //int otherLink = quantumBoard.getLink(turnCount - 1, selection);
                                //quantumBoard.getBoardTile(otherLink % 3, (int) Math.floor(otherLink / 3)).getMovesList().remove(quantumBoard.getMoveLocationInArray(otherLink, turnCount - 1));
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
                                    if(quantumBoard.getBoardStatelist()[i].equals(State.Blank)){
                                        blankCount++;
                                    }
                                }
                                if(blankCount < 2){
                                    displayLabel.setText("Stalemate");
                                    displayLabel.setForeground(Color.BLACK);
                                    moveDrawLoc = -1;

                                    //so that it can't be played on again
                                    quantumBoard.setState(State.Player1);
    
                                    //replay button stuff
                                    replay();
                                    
                                }
    
                                int boardresult = quantumBoard.checkEntireBoard();
    
                                if(boardresult == 1){
                                    quantumBoard.setState(State.Player1);
                                    displayLabel.setText("Player 1 wins");
                                    displayLabel.setForeground(red);
                                    moveDrawLoc = -1;
    
                                    //replay button stuff
                                    replay();
                                    
                                }else if(boardresult == 2){
                                    quantumBoard.setState(State.Player2);
                                    displayLabel.setText("Player 2 wins");
                                    displayLabel.setForeground(blue);
                                    moveDrawLoc = -1;
    
                                    //replay button stuff
                                    replay();
                                    
                                }
                                repaint();
                            }
                            
                        }
                    }else if(game == 5 && quantumBoard3D.getState() == State.Blank){
                        AffineTransform transform = new AffineTransform(Math.cos(theta), (-1 * Math.sin(theta)) / 2, Math.sin(theta), Math.cos(theta) / 2, offsetx, offsety);
                        try {
                            transform.invert();
                        } catch (NoninvertibleTransformException e1) {
                            System.out.println("Non invertible transform");
                        }

                        int boardZ = 0;

                        Point2D.Double click1 = new Point2D.Double(e.getX(), e.getY());
                        if(e.getY() < height / 3){
                            boardZ = 2;
                            click1.setLocation(e.getX() - offsetx, e.getY() - offsety);
                        }else if(e.getY() > height / 3 && e.getY() < (2 * (height / 3))){
                            boardZ = 1;
                            click1.setLocation(e.getX() - offsetx, e.getY() - offsety - (height / 3));
                        }else{
                            click1.setLocation(e.getX() - offsetx, e.getY() - offsety - (2 * height / 3));
                        }

                        Point2D.Double click2 = new Point2D.Double();
                        transform.deltaTransform(click1, click2);

                        if(quantumBoard3D.getState() == State.Blank && (click2.getX() > xbound && click2.getX() < (xbound + boundingSize) && click2.getY() > ybound && click2.getY() < (ybound + boundingSize)) && quantumBoard3D.getState() == State.Blank && result == 0){
                            int xboard = (int) click2.getX() - xbound;
                            int yboard = (int) click2.getY() - ybound;
    
                            int xcell = (int) Math.floor(3 * xboard / boundingSize);
                            int ycell = (int) Math.floor(3 * yboard / boundingSize);

                            theSquare = xcell + (ycell * 3) + (boardZ * 9);

                            //System.out.println("Square " + xcell + ", " + ycell + " on board" + boardZ);
    
                            if(quantumBoard3D.getState() == State.Blank && quantumBoard3D.getBoardStatelist()[xcell + (ycell * 3) + (boardZ * 9)] == State.Blank && e.getButton() == MouseEvent.BUTTON1){

                                if(quantumMove && (xcell + (ycell * 3) + (boardZ * 9)) != recentCell){
                                    //quantumBoard3D.getBoardTile(xcell, ycell, boardZ).addMove(turnCount);

                                    //quantumBoard3D.getBoardTile(recentCell % 3, (recentCell - ((recentCell / 9) * 9)) / 3, recentCell / 9).addMove(turnCount);

                                    //don't really need this here I think
                                    //quantumBoard3D.incrementMoveCount();

                                    Move move = new Move(recentCell, (xcell + (ycell * 3) + (boardZ * 9)));
                                    //System.out.println(move.loc + " " + move.loc2);
                                    move.setTurn(turnCount);

                                    quantumBoard3D.move(move);

                                    //quantumBoard3D.print();

                                    recentSquare1 = xcell + (ycell * 3) + (boardZ * 9);
                                    recentSquare2 = recentCell;
                                    //quantumBoard3D.printBoard();

                                    result = quantumBoard3D.checkLoops(move);

                                    turnCount++;
                                    quantumMove = !quantumMove;

                                    //display stuff
                                    turn = !turn;
                                    recentCell = -1;

                                    if(turn){
                                        displayLabel.setText("Player 2's turn");
                                        displayLabel.setForeground(blue);
                                        if(bot){
                                            displayLabel.setText("Player 2 is thinking");
                                            displayLabel.setForeground(blue);
                                            repaint();
                                            SwingUtilities.invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //if the previous move was a loop
                                                    if(result != 0){
                                                        mAI.chooseCollapse(quantumBoard3D, recentSquare1, recentSquare2);
                                                        result = 0;
                                                        if(turn){
                                                            displayLabel.setText("Player 2's turn");
                                                            displayLabel.setForeground(blue);
                            
                                                        }else{
                                                            displayLabel.setText("Player 1's turn");
                                                            displayLabel.setForeground(red);
                                                            
                                                        }
                            
                                                        int boardresult = quantumBoard3D.checkEntireBoard();
                            
                                                        result = 0;

                                                        int blankCount = 0;
                                                        for(int i = 0; i < 27; i++){
                                                            if(quantumBoard3D.getBoardStatelist()[i].equals(State.Blank)){
                                                                blankCount++;
                                                            }
                                                        }
                                                        if(blankCount < 2){
                                                            displayLabel.setText("Stalemate");
                                                            displayLabel.setForeground(Color.BLACK);
                                                            //quantumBoard3D.clear();

                                                            //so that it can't be played on again
                                                            quantumBoard3D.setState(State.Player1);
                            
                                                            result = 1;
                                                            //replay button stuff
                                                            replay();
                                                        }

                                                        if(boardresult == 1){
                                                            quantumBoard3D.setState(State.Player1);
                                                            displayLabel.setText("Player 1 wins");
                                                            displayLabel.setForeground(red);
                                                            //quantumBoard3D.clear();

                                                            result = 1;
                                                            //replay button stuff
                                                            replay();
                                                            quantumBoard3D.setState(State.Player1);
                                                            
                                                        }else if(boardresult == 2){
                                                            quantumBoard3D.setState(State.Player2);
                                                            displayLabel.setText("Player 2 wins");
                                                            displayLabel.setForeground(blue);
                                                            //quantumBoard3D.clear();
                            
                                                            result = 1;
                                                            //replay button stuff
                                                            replay();
                                                            quantumBoard3D.setState(State.Player2);
                                                            
                                                        }
                                                        repaint();
                                                    }
                                                    //this uses the same variable, but is actually for the game ending
                                                    if(result == 1){
                                                        return;
                                                    }
                                                    Move move;
                                                    move = mAI.check(quantumBoard3D, 1, true);

                                                    recentSquare1 = move.loc;
                                                    recentSquare2 = move.loc2;
                                                    result = quantumBoard3D.checkLoops(move);
                                                    turnCount++;
                                                    turn = !turn;
                                                    if(quantumBoard3D.getState() == State.Blank){
                                                        displayLabel.setText("Player 1's turn");
                                                        displayLabel.setForeground(red);
                                                        int blankCount = 0;
                                                        for(int i = 0; i < 27; i++){
                                                            if(quantumBoard3D.getBoardStatelist()[i] == State.Blank){
                                                                blankCount++;
                                                            }
                        
                                                        }
                                                        if(blankCount < 2){
                                                            displayLabel.setText("Stalemate");
                                                            displayLabel.setForeground(Color.BLACK);
                                                            //quantumBoard3D.clear();
                            
                                                            //replay button stuff
                                                            replay();
                                                        }
                                                    }else{
                                                        //quantumBoard3D.clear();
                                                    }
                                                    //does stuff if a loop is found
                                                    if(result == 0){
                                                        //System.out.println("No loop");
                                                    }else{
                                                        //System.out.println("Loop");
                                                        if(turn){
                                                            displayLabel.setText("Player 2 chooses collapse");
                        
                                                        }else{
                                                            displayLabel.setText("Player 1 chooses collapse");
                                                            
                                                        }

                                                        moveDrawLoc = recentSquare1;
                                                    }
                                                    
                                                    repaint();
                                                }
                                            });

                                        }

                                    }else{
                                        displayLabel.setText("Player 1's turn");
                                        displayLabel.setForeground(red);
                                        
                                    }

                                    //does stuff if a loop is found
                                    if(result == 0){
                                        //System.out.println("No loop");
                                    }else{
                                        //System.out.println("Loop");
                                        if(turn){
                                            displayLabel.setText("Player 2 chooses collapse");
        
                                        }else{
                                            displayLabel.setText("Player 1 chooses collapse");
                                            
                                        }

                                        moveDrawLoc = recentSquare1;
                                    }

                                //thing to unselect
                                }else if(quantumMove && (xcell + (ycell * 3) + (boardZ * 9)) == recentCell){
                                    quantumMove = false;
                                    recentCell = -1;

                                //handles second click
                                }else if((xcell + (ycell * 3) + (boardZ * 9)) != recentCell){
                                    recentCell = xcell + (ycell * 3) + (boardZ * 9);
                                    quantumMove = !quantumMove;
                                }

                                repaint();
                            }else if(e.getButton() == MouseEvent.BUTTON3){
                                //quantumBoard3D.printTile(xcell, ycell, boardZ);
                            }

                            
                            //System.out.println("X: " + xcell + ", Y: " + ycell);
    
                        }else if(result == 1 && quantumBoard3D.getState() == State.Blank){
                            //choosing the collpase
                            int selection = -1;
                            int adjustedSquare1X = xbound + ((recentSquare1 % 3) * cellSize);
                            int adjustedSquare1Y = ybound + ((recentSquare1 - ((recentSquare1 / 9) * 9)) / 3) * cellSize;
                            int adjustedSquare2X = xbound + ((recentSquare2 % 3) * cellSize);
                            int adjustedSquare2Y = ybound + ((recentSquare2 - ((recentSquare2 / 9) * 9)) / 3) * cellSize;

                            if(click2.getX() > adjustedSquare1X && click2.getX() < adjustedSquare1X + cellSize && click2.getY() > adjustedSquare1Y && click2.getY() < adjustedSquare1Y + cellSize){
                                selection = recentSquare1;
                            }else if(click2.getX() > adjustedSquare2X && click2.getX() < adjustedSquare2X + cellSize && click2.getY() > adjustedSquare2Y && click2.getY() < adjustedSquare2Y + cellSize){
                                selection = recentSquare2;
                            }

                            if(selection != -1){
                                //int otherLink = quantumBoard3D.getLink(turnCount - 1, selection);
                                //quantumBoard3D.getBoardTile(otherLink % 3, (int) Math.floor(otherLink / 3)).getMovesList().remove(quantumBoard3D.getMoveLocationInArray(otherLink, turnCount - 1));
                                quantumBoard3D.collapseTile(selection, turnCount - 1);
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
                                for(int i = 0; i < 27; i++){
                                    if(quantumBoard3D.getBoardStatelist()[i] == State.Blank){
                                        blankCount++;
                                    }

                                }
                                if(blankCount < 2){
                                    displayLabel.setText("Stalemate");
                                    displayLabel.setForeground(Color.BLACK);
                                    moveDrawLoc = -1;

                                    //so that it can't be played on again
                                    quantumBoard3D.setState(State.Player1);
    
                                    //replay button stuff
                                    replay();
                                    
                                }
    
                                int boardresult = quantumBoard3D.checkEntireBoard();
    
                                if(boardresult == 1){
                                    quantumBoard3D.setState(State.Player1);
                                    displayLabel.setText("Player 1 wins");
                                    displayLabel.setForeground(red);
                                    moveDrawLoc = -1;
    
                                    //replay button stuff
                                    replay();
                                    
                                }else if(boardresult == 2){
                                    quantumBoard3D.setState(State.Player2);
                                    displayLabel.setText("Player 2 wins");
                                    displayLabel.setForeground(blue);
                                    moveDrawLoc = -1;
    
                                    //replay button stuff
                                    replay();
                                    
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
                if(movementCounter == 0){
                    mouseLocx = e.getX();
                    mouseLocy = e.getY();
                }

                if(game == 5){
                    return;
                }
    
                int deltax = (e.getX() - mouseLocx);
                int deltay = (e.getY() - mouseLocy);
    
                mouseLocx = e.getX();
                mouseLocy = e.getY();

                double boardSize = Math.min(width, height) * zoom;

                if(game == 2){
                    boardSize *= 3;
                }else if(game == 3){
                    boardSize *= 9;
                }

                //stuff for keeping it close to view
                if(offsetx > width + (boardSize / 2)){
                    offsetx = width + (int) (boardSize / 2);
                    return;
                }
                if(offsetx + boardSize < 0){
                    offsetx = -1 * (int) boardSize;
                    return;
                }
                if(offsety > height + (boardSize / 2)){
                    offsety = height + (int) (boardSize / 2);
                    return;
                }
                if(offsety + boardSize < 0){
                    offsety = -1 * (int) boardSize;
                    return;
                }

                offsetx = offsetx + deltax;
                offsety = offsety + deltay;
    
                movementCounter++;

                repaint();
            }
            public void mouseMoved(MouseEvent e){
                if(result == 1){
                    if(((e.getX() > (xbound + ((recentSquare1 % 3) * cellSize))) && (e.getX() < (xbound + ((recentSquare1 % 3) * cellSize) + cellSize)) && (e.getY() > (ybound + ((int) (Math.floor(recentSquare1 / 3)) * cellSize))) && (e.getY() < (ybound + (Math.floor(recentSquare1 / 3) * cellSize) + cellSize)))){
                        moveDrawLoc = recentSquare1;
                    }else if(((e.getX() > (xbound + ((recentSquare2 % 3) * cellSize))) && (e.getX() < (xbound + ((recentSquare2 % 3) * cellSize) + cellSize)) && (e.getY() > (ybound + ((int) (Math.floor(recentSquare2 / 3)) * cellSize))) && (e.getY() < (ybound + ((recentSquare2 / 3) * cellSize) + cellSize)))){
                        moveDrawLoc = recentSquare2;
                    }else{
                        moveDrawLoc = -1;
                    }
                }else{
                    moveDrawLoc = -1;
                }

                //makes it skip this unless game 5
                if(game != 5){
                    repaint();
                    return;
                }

                if(result == 1){
                    calculateHover3D(e.getX(), e.getY());
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
                }else if(gameType == 4 || gameType == 5){
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

                //closeWindow();
                try {
                    if(gameType != 3){
                        frame.setupGame(gameType, bot);

                        //handles difficulty
                        if(gameType != 1){
                            frame.setDepth(depth);
                        
                        }
                    }else{
                        new SFrame(frame, bot, depth, 3, sound, buttonColor);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                
            }

        });

    }

    public int largeMoveAftermath(int xboard, int yboard, int xcell, int ycell){
        int result = largeBoard.getBoardArray(xboard, yboard).checkBoard(xcell, ycell);

        int end = 0;

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
            largeBoard.clearLocationList(xboard, yboard);
            // displayLabel.setText("Player 1 wins");
            // displayLabel.setForeground(red);
        }else if(result == 2){
            largeBoard.getBoardArray(xboard, yboard).setState(State.Player2);
            largeBoard.clearLocationList(xboard, yboard);
            // displayLabel.setText("Player 2 wins");
            // displayLabel.setForeground(blue);
        }

        //calculates new active tiles
        if(largeBoard.getBoardArray(xcell, ycell).getState() == State.Blank && largeBoard.getBoardArray(xcell, ycell).getMoveTally() != 9){
            largeBoard.setActive(false);
            largeBoard.getBoardArray(xcell, ycell).setActive(true);
        }else{
            largeBoard.setActive(true);
        }

        if(largeBoard.getMoveTally() == 9){
            displayLabel.setText("Stalemate");
            displayLabel.setForeground(Color.BLACK);
            largeBoard.setActive(false);

            end = 1;

            //replay button stuff
            replay();
            
        }

        //checks the larger board
        result = largeBoard.checkBoard(xboard, yboard);
        if(result == 1){
            largeBoard.setState(State.Player1);
            displayLabel.setText("Player 1 wins");
            displayLabel.setForeground(red);
            largeBoard.setActive(false);

            end = 1;

            //replay button stuff
            replay();
            
        }else if(result == 2){
            largeBoard.setState(State.Player2);
            displayLabel.setText("Player 2 wins");
            displayLabel.setForeground(blue);
            largeBoard.setActive(false);

            end = 1;

            //replay button stuff
            replay();
            

        }
        repaint();

        return end;
    }

    public int massiveMoveAftermath(int xlargeboard, int ylargeboard, int xboard, int yboard, int xcell, int ycell){
        //checks each scale of board
        int resultsmall = massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xboard, yboard).checkBoard(xcell, ycell);

        turn = !turn;

        massiveBoard.setActive(false);

        if(massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xcell, ycell).getState() == State.Blank && massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xcell, ycell).getMoveTally() != 9){
            massiveBoard.getBoardArray(xlargeboard, ylargeboard).getBoardArray(xcell, ycell).setActive(true);
        }else{
            massiveBoard.getBoardArray(xlargeboard, ylargeboard).setActive(true);
        }

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

        int resultlarge = massiveBoard.getBoardArray(xlargeboard, ylargeboard).checkBoard(xboard, yboard);

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

        int resultmassive = massiveBoard.checkBoard(xlargeboard, ylargeboard);

        if(massiveBoard.getMoveTally() == 9){
            displayLabel.setText("Stalemate");
            displayLabel.setForeground(Color.BLACK);
            massiveBoard.setActive(false);

            //replay button stuff
            replay();
            
        }
        
        if(resultmassive == 1){
            displayLabel.setText("Player 1 wins");
            displayLabel.setForeground(red);
            massiveBoard.setActive(false);

            //replay button stuff
            replay();
            
        }else if(resultmassive == 2){
            displayLabel.setText("Player 2 wins");
            displayLabel.setForeground(blue);
            massiveBoard.setActive(false);

            //replay button stuff
            replay();
            
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

        return resultmassive;
    }

    @Override
    public void paintComponent(Graphics g1) {
        frameobject.revalidate();
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(game == 1){
            drawSimpleBoard(g, simpleBoard, true, 0, 0);
        }else if(game == 2){
            drawLargeBoard(g, largeBoard, true, 0);
        }else if(game == 3){
            drawMassiveBoard(g, massiveBoard);
        }else if(game == 4){
            drawQuantumBoard(g, quantumBoard);
            if(quantumMove){
                drawMouseLine(g, quantumBoard);
            }
        }else if(game == 5){
            drawQuantumBoard3D(g, quantumBoard3D);
            if(quantumMove){
                drawMouseLine3D(g, quantumBoard3D);
            }
        }
        
        frameobject.revalidate();
        Toolkit.getDefaultToolkit().sync();
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
        
        boundingSize = Math.min(width, height);
        thickness = boundingSize / 50;
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

        // double xscale = (double) width / (double) pwidth;
        // double yscale = (double) height / (double) pheight;
        
        boundingSize = Math.min(width, height);
        thickness = boundingSize / 25;
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

        // xbound = xbound + (int) (offsetx * xscale);
        // ybound = ybound + (int) (offsety * yscale);

        xbound = xbound + offsetx;
        ybound = ybound + offsety;

        if(single){
            g.fillRect(xbound + boundingSize - (thickness / 2), ybound + buffer, thickness, boundingSize * 3 - buffer- buffer);
            g.fillRect(xbound + (2 * boundingSize) - (thickness / 2), ybound + buffer, thickness, boundingSize * 3 - buffer- buffer);
    
            g.fillRect(xbound + buffer, ybound + boundingSize - (thickness / 2), boundingSize * 3 - buffer- buffer, thickness);
            g.fillRect(xbound + buffer, ybound + (2 * boundingSize) - (thickness / 2), boundingSize * 3 - buffer- buffer, thickness);
        }else{
            int boardX = boardIndex % 3;
            int boardY = boardIndex /3;

            g.fillRect(xbound + boundingSize - (thickness / 2) + (boundingSize * 3 * boardX), ybound + buffer + (boundingSize * 3 * boardY), thickness, boundingSize * 3 - buffer- buffer);
            g.fillRect(xbound + (2 * boundingSize) - (thickness / 2) + (boundingSize * 3 * boardX), ybound + buffer + (boundingSize * 3 * boardY), thickness, boundingSize * 3 - buffer- buffer);
    
            g.fillRect(xbound + buffer + (boundingSize * 3 * boardX), ybound + boundingSize - (thickness / 2) + (boundingSize * 3 * boardY), boundingSize * 3 - buffer- buffer, thickness);
            g.fillRect(xbound + buffer + (boundingSize * 3 * boardX), ybound + (2 * boundingSize) - (thickness / 2) + (boundingSize * 3 * boardY), boundingSize * 3 - buffer- buffer, thickness);

            //for some reason, this causes visual bug (don't want to figure out why, this just works as a fix)
            // int x = boardIndex % 3;
            // int y = boardIndex / 3;

            // double iconScale = 0.8;
            // double iconOffset = (boundingSize - (boundingSize * iconScale)) / 2;
            // // if(board.getState() == State.Player1){
            // //     g.drawImage(xImage, (int) (xbound + iconOffset) + (x * boundingSize) + (boundingSize * 9 * boardX), (int) (ybound + iconOffset) + (y * boundingSize) + (boundingSize * 9 * boardY), (int) (boundingSize * iconScale), (int) (boundingSize * iconScale), null);
            // // }else if (board.getState() == State.Player2){
            // //     g.drawImage(oImage, (int) (xbound + iconOffset) + (x * boundingSize) + (boundingSize * 9 * boardX), (int) (ybound + iconOffset) + (y * boundingSize) + (boundingSize * 9 * boardY), (int) (boundingSize * iconScale), (int) (boundingSize * iconScale), null);
            // // }
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
        
        // double xscale = (double) width / (double) pwidth;
        // double yscale = (double) height / (double) pheight;
       
        boundingSize = Math.min(width, height);
        thickness = boundingSize / 15;
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
        
        // xbound = xbound + (int) (offsetx * xscale);
        // ybound = ybound + (int) (offsety * yscale);

        xbound = xbound + offsetx;
        ybound = ybound + offsety;
        
        g.fillRect(xbound + (boundingSize * 3) - (thickness / 2), ybound + buffer, thickness, boundingSize * 9 - buffer- buffer);
        g.fillRect(xbound + (6 * boundingSize) - (thickness / 2), ybound + buffer, thickness, boundingSize * 9 - buffer- buffer);

        g.fillRect(xbound + buffer, ybound + (boundingSize * 3) - (thickness / 2), boundingSize * 9 - buffer- buffer, thickness);
        g.fillRect(xbound + buffer, ybound + (6 * boundingSize) - (thickness / 2), boundingSize * 9 - buffer- buffer, thickness);

        for(int i = 0; i < 9; i++){

            int x = i % 3;
            int y = i / 3;

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
        
        // double xscale = (double) width / (double) pwidth;
        // double yscale = (double) height / (double) pheight;
       
        boundingSize = Math.min(width, height);
        thickness = boundingSize / 50;
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
        
        // xbound = xbound + (int) (offsetx * xscale);
        // ybound = ybound + (int) (offsety * yscale);

        xbound = xbound + offsetx;
        ybound = ybound + offsety;

        g.fillRect(xbound + cellSize - (thickness / 2), ybound, thickness, boundingSize);
        g.fillRect(xbound + (2 * cellSize) - (thickness / 2), ybound, thickness, boundingSize);

        g.fillRect(xbound, ybound + cellSize - (thickness / 2), boundingSize, thickness);
        g.fillRect(xbound, ybound + (2 * cellSize) - (thickness / 2), boundingSize, thickness);

        //loops through the board
        double iconScale = 0.8;
        double imageSize = iconScale * cellSize;
        // double numSizeX = (imageSize * 3) / 16;
        // double numSizeY = imageSize / 4;

        // numSizeX *=1.5;
        // numSizeY *=1.5;

        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        Font font = null;
        float size = 50f;
        size *= zoom;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        g.setFont(font);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                int xNetOffset = (int) (xbound + (i * cellSize) + ((cellSize - imageSize) / 2));
                int yNetOffset = (int) (ybound + (j * cellSize) + ((cellSize - imageSize) / 2));

                if(board.getBoardStatelist()[i + (3 * j)] == State.Player1){
                    g.drawImage(xImage, xNetOffset, yNetOffset, (int) imageSize, (int) imageSize, null);
                    g.drawString(Integer.toString(board.getBoardTurnlist()[i + (3 * j)]), xNetOffset + (int) (cellSize / 1.4), yNetOffset + (int) (cellSize * 0.85));
                }else if(board.getBoardStatelist()[i + (3 * j)] == State.Player2){
                    g.drawImage(oImage, xNetOffset, yNetOffset, (int) imageSize, (int) imageSize, null);
                    g.drawString(Integer.toString(board.getBoardTurnlist()[i + (3 * j)]), xNetOffset + (int) (cellSize / 1.4), yNetOffset + (int) (cellSize * 0.85));
                }
            }
        }

        double quantumCellSize = cellSize / 3;
        double quantumImageSize = quantumCellSize * iconScale;
        double quantumPadding = ((quantumCellSize - quantumImageSize) / 2);

        stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        font = null;
        size = 25f;
        size *= zoom;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        g.setFont(font);

        //draws the quantum moves
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(board.getBoardStatelist()[i + (3 * j)] != State.Blank){
                    continue;
                }
                for(int k = 0; k < board.getQuantumCacheList()[i + (3 * j)].size(); k++){
                    int xoffset = k % 3;
                    int yoffset = k / 3;

                    int xNetOffset = (int) (xbound + (xoffset * quantumCellSize) + quantumPadding + (i * cellSize));
                    int yNetOffset = (int) (ybound + (yoffset * quantumCellSize) + quantumPadding + (j * cellSize));

                    int turn = board.getQuantumCacheList()[i + (3 * j)].get(k).turn;
                    
                    //these need to stay in
                    @SuppressWarnings("unused")
                    int offsetx = 5;
                    @SuppressWarnings("unused")
                    int offsety = 15;

                    offsetx *= zoom;
                    offsety *= zoom;

                    if(turn % 2 != 0){
                        g.drawImage(xImage, xNetOffset, yNetOffset, (int) quantumImageSize, (int) quantumImageSize, null);
                        //g.drawString(Integer.toString(move), xNetOffset + (int) (quantumCellSize / 1.25) - offsetx, yNetOffset + (int) (quantumCellSize * 1.25) - offsety);
                        //g.drawImage(getTurnImage(move), (int) (xNetOffset + quantumImageSize - numSizeX + (quantumImageSize / imageDivisor)), (int) (yNetOffset + quantumImageSize - numSizeY + (quantumImageSize / imageDivisor)), (int) numSizeX, (int) numSizeY, null);
                    }else{
                        g.drawImage(oImage, xNetOffset, yNetOffset, (int) quantumImageSize, (int) quantumImageSize, null);
                        //g.drawString(Integer.toString(move), xNetOffset + (int) (quantumCellSize / 1.25) - offsetx, yNetOffset + (int) (quantumCellSize * 1.25) - offsety);
                        //g.drawImage(getTurnImage(move), (int) (xNetOffset + quantumImageSize - numSizeX + (quantumImageSize / imageDivisor)), (int) (yNetOffset + quantumImageSize - numSizeY + (quantumImageSize / imageDivisor)), (int) numSizeX, (int) numSizeY, null);
                    }

                    String number = Integer.toString(turn);

                    g.setColor(Color.WHITE);
                    for(int m = -1; m < 2; m++){
                        for(int l = -1; l < 2; l++){
                            g.drawString(number, xNetOffset + (int) (quantumImageSize / 1.25) + m, yNetOffset + (int) (quantumImageSize * 1.25) + l);
                        }
                    }

                    //the actual text
                    g.setColor(Color.BLACK);
                    g.drawString(number, xNetOffset + (int) (quantumImageSize / 1.25), yNetOffset + (int) (quantumImageSize * 1.25));

                    if(turn % 2 != 0){
                    g.setColor(red);
                    }else{
                        g.setColor(blue);
                    }
        
                    //gets first one
                    int first = board.getQuantumCacheList()[i + (3 * j)].get(k).loc;
                    int second = board.getQuantumCacheList()[i + (3 * j)].get(k).loc2;

                    int qoffset1 = k;
                    int qoffset2 = k;

                    if(first == i + (3 * j)){
                        qoffset2 = board.getQuantumCacheList()[second].indexOf(board.getQuantumCacheList()[i + (3 * j)].get(k));
                    }else{
                        qoffset1 = board.getQuantumCacheList()[first].indexOf(board.getQuantumCacheList()[i + (3 * j)].get(k));
                    }
        
                    //System.out.println(first + " " + second);
        
                    int x1 = first % 3;
                    int y1 = first / 3;
                    int x2 = second % 3;
                    int y2 = second / 3;
        
                    int qx1 = (qoffset1 % 3) - 1;
                    int qy1 = (int) Math.floor(qoffset1 / 3) - 1;
                    int qx2 = (qoffset2 % 3) - 1;
                    int qy2 = (int) Math.floor(qoffset2 / 3) - 1;
        
                    g.drawLine(xbound + (x1 * cellSize) + (cellSize / 2) + (int) (qx1 * quantumCellSize) -1, ybound + (y1 * cellSize) + (cellSize / 2) + (int) (qy1 * quantumCellSize) -1, xbound + (x2 * cellSize) + (cellSize / 2) + (int) (qx2 * quantumCellSize) -1, ybound + (y2 * cellSize) + (cellSize / 2) + (int) (qy2 * quantumCellSize) -1);
                }
            }
        }

        // //draws links
        // if(quantumLineMode){
        //     for(int i = 1; i < turnCount; i++){
        //         if(i % 2 != 0){
        //             g.setColor(red);
        //         }else{
        //             g.setColor(blue);
        //         }

        //         Move move = board.getQuantumCacheList()[i];
    
        //         //gets first one
        //         int first = board.getBoardWithMove(i);
        //         int second = board.getOtherBoardWithMove(i);
    
        //         int qoffset1 = board.getMoveLocationInArray(first, i);
        //         int qoffset2 = board.getMoveLocationInArray(second, i);
    
        //         //System.out.println(first + " " + second);
    
        //         int x1 = first % 3;
        //         int y1 = first / 3;
        //         int x2 = second % 3;
        //         int y2 = second / 3;
    
        //         int qx1 = (qoffset1 % 3) - 1;
        //         int qy1 = (int) Math.floor(qoffset1 / 3) - 1;
        //         int qx2 = (qoffset2 % 3) - 1;
        //         int qy2 = (int) Math.floor(qoffset2 / 3) - 1;
    
        //         g.drawLine(xbound + (x1 * cellSize) + (cellSize / 2) + (int) (qx1 * quantumCellSize) -1, ybound + (y1 * cellSize) + (cellSize / 2) + (int) (qy1 * quantumCellSize) -1, xbound + (x2 * cellSize) + (cellSize / 2) + (int) (qx2 * quantumCellSize) -1, ybound + (y2 * cellSize) + (cellSize / 2) + (int) (qy2 * quantumCellSize) -1);
        //     }
        // }

        if(result == 1){
            if(moveDrawLoc != -1 && quantumBoard.getState() == State.Blank){
                drawMove(g, moveDrawLoc, turnCount - 1);
            }
        }

    }

    public void drawQuantumBoard3DSlice(BufferedImage image, QuantumBoard3D board, int slice){
        Graphics2D g = image.createGraphics();

        AffineTransform transform = new AffineTransform(Math.cos(theta), (-1 * Math.sin(theta)) / 2, Math.sin(theta), Math.cos(theta) / 2, offsetx, offsety);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.transform(transform);

        g.setColor(Color.BLACK);

        int widthbackup = width;
        int heightbackup = height;

        width = image.getWidth();
        height = image.getHeight();

        boundingSize = 1024;
        thickness = boundingSize / 40;
        xbound = 512;
        ybound = 512;
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

        //draws the lines
        g.fillRect(xbound + cellSize - (thickness / 2), ybound, thickness, boundingSize);
        g.fillRect(xbound + (2 * cellSize) - (thickness / 2), ybound, thickness, boundingSize);

        g.fillRect(xbound, ybound + cellSize - (thickness / 2), boundingSize, thickness);
        g.fillRect(xbound, ybound + (2 * cellSize) - (thickness / 2), boundingSize, thickness);

        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(30f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        g.setFont(font);

        //loops through the board
        double iconScale = 0.75;
        double imageSize = iconScale * cellSize;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                int xNetOffset = (int) (xbound + (i * cellSize) + ((cellSize - imageSize) / 2));
                int yNetOffset = (int) (ybound + (j * cellSize) + ((cellSize - imageSize) / 2));

                double xOffsetMulti = 0.55;
                double yOffsetMulti = 1;

                if(board.getBoardStatelist()[i + (j * 3) + (slice * 9)] == State.Player1){
                    g.drawImage(xImage, xNetOffset, yNetOffset, (int) imageSize, (int) imageSize, null);  
                }else if(board.getBoardStatelist()[i + (j * 3) + (slice * 9)] == State.Player2){
                    g.drawImage(oImage, xNetOffset, yNetOffset, (int) imageSize, (int) imageSize, null);
                }

                //draws the number
                if(board.getBoardStatelist()[i + (j * 3) + (slice * 9)] != State.Blank){
                    String number = Integer.toString(board.getBoardTurnlist()[i + (j * 3) + (slice * 9)]);
                    if(number.length() == 1){
                        number = "  " + number;
                    }

                    g.setColor(Color.WHITE);
                    for(int k = -1; k < 2; k++){
                        for(int l = -1; l < 2; l++){
                            g.drawString(number, xNetOffset + (int) (imageSize * xOffsetMulti) + k, yNetOffset + (int) (imageSize * yOffsetMulti) + l);
                        }
                    }

                    //the actual text
                    g.setColor(Color.BLACK);
                    g.drawString(number, xNetOffset + (int) (imageSize * xOffsetMulti), yNetOffset + (int) (imageSize * yOffsetMulti));
                }
            }
        }

        if(moveDrawLoc / 9 == slice && moveDrawLoc > -1){
            int xcell = moveDrawLoc % 3;
            int ycell = ((moveDrawLoc - (slice * 9)) / 3);
            int xNetOffset = (int) (xbound + (xcell * cellSize) + ((cellSize - imageSize) / 2));
            int yNetOffset = (int) (ybound + (ycell * cellSize) + ((cellSize - imageSize) / 2));

            if(!turn){
                g.drawImage(oImage, xNetOffset, yNetOffset, (int) imageSize, (int) imageSize, null);
            }else{
                g.drawImage(xImage, xNetOffset, yNetOffset, (int) imageSize, (int) imageSize, null);
            }
        }

        int quantumCellSize = cellSize / 3;
        int quantumImageSize = (int) (quantumCellSize * iconScale);
        int quantumPadding = ((quantumCellSize - quantumImageSize) / 2);

        stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        g.setFont(font);

        //draws the quantum moves
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(!board.getBoardStatelist()[i + (j * 3) + (slice * 9)].equals(State.Blank)){
                    continue;
                }
                for(int k = 0; k < board.getQuantumCacheList()[i + (j * 3) + (slice * 9)].size(); k++){
                    int xoffset = k % 3;
                    int yoffset = k / 3;

                    int xNetOffset = (xbound + (xoffset * quantumCellSize) + quantumPadding + (i * cellSize));
                    int yNetOffset = (ybound + (yoffset * quantumCellSize) + quantumPadding + (j * cellSize));

                    int move = board.getQuantumCacheList()[i + (j * 3) + (slice * 9)].get(k).turn;

                    //adjusts how close the numbers are to the mark, larger is closer
                    //int imageDivisor = 4;

                    if(move % 2 != 0){
                        g.drawImage(xImage2, xNetOffset, yNetOffset, quantumImageSize, quantumImageSize, null);
                        //g.drawString(Integer.toString(move), xNetOffset + (int) (quantumImageSize / 1.25), yNetOffset + (int) (quantumImageSize * 1.25));
                        //g.drawImage(getTurnImage(move), (int) (xNetOffset + quantumImageSize - numSizeX + (quantumImageSize / imageDivisor)), (int) (yNetOffset + quantumImageSize - numSizeY + (quantumImageSize / imageDivisor)), (int) numSizeX, (int) numSizeY, null);
                    }else{
                        g.drawImage(oImage2, xNetOffset, yNetOffset, quantumImageSize, quantumImageSize, null);
                        //g.drawString(Integer.toString(move), xNetOffset + (int) (quantumImageSize / 1.25), yNetOffset + (int) (quantumImageSize * 1.25));
                        //g.drawImage(getTurnImage(move), (int) (xNetOffset + quantumImageSize - numSizeX + (quantumImageSize / imageDivisor)), (int) (yNetOffset + quantumImageSize - numSizeY + (quantumImageSize / imageDivisor)), (int) numSizeX, (int) numSizeY, null);
                    }

                    //draws the number
                    String number = Integer.toString(move);
                    // if(number.length() == 1){
                    //     number = "  " + number;
                    // }

                    g.setColor(Color.WHITE);
                    for(int m = -1; m < 2; m++){
                        for(int l = -1; l < 2; l++){
                            g.drawString(number, xNetOffset + (int) (quantumImageSize / 1.25) + m, yNetOffset + (int) (quantumImageSize * 1.25) + l);
                        }
                    }

                    //the actual text
                    g.setColor(Color.BLACK);
                    g.drawString(number, xNetOffset + (int) (quantumImageSize / 1.25), yNetOffset + (int) (quantumImageSize * 1.25));
                }
            }
        }

        //needs to be replaced for quantum 3d
        if(result == 1 && game == 4){
            if(moveDrawLoc != -1){
                drawMove(g, moveDrawLoc, turnCount - 1);
            }
        }

        width = widthbackup;
        height = heightbackup;

    }

    public void drawQuantumBoard3D(Graphics2D g, QuantumBoard3D board){

        int boardoffset = height / 3;

        int IMAGE_SCALE = 4;

        BufferedImage image0 = new BufferedImage(1024 * IMAGE_SCALE, 1024 * IMAGE_SCALE, BufferedImage.TYPE_INT_ARGB);
        BufferedImage image1 = new BufferedImage(1024 * IMAGE_SCALE, 1024 * IMAGE_SCALE, BufferedImage.TYPE_INT_ARGB);
        BufferedImage image2 = new BufferedImage(1024 * IMAGE_SCALE, 1024 * IMAGE_SCALE, BufferedImage.TYPE_INT_ARGB);

        drawQuantumBoard3DSlice(image0, board, 0);
        drawQuantumBoard3DSlice(image1, board, 1);
        drawQuantumBoard3DSlice(image2, board, 2);

        imagex = image1.getWidth();
        imagey = image1.getHeight();

        int quantumCellSize = cellSize / 3;

        //for the links
        AffineTransform transform = new AffineTransform(Math.cos(theta), (-1 * Math.sin(theta)) / 2, Math.sin(theta), Math.cos(theta) / 2, offsetx, offsety);

        //draws links
        if(quantumLineMode){
            //i is the board tile that it is looking at, j is move position on that tile
            for(int i = 0; i < 27; i++){
                if(!board.getBoardStatelist()[i].equals(State.Blank)){
                    continue;
                }
                for(int j = 0; j < board.getQuantumCacheList()[i].size(); j++){
                    if(board.getQuantumCacheList()[i].get(j).turn % 2 != 0){
                        g.setColor(red);
                    }else{
                        g.setColor(blue);
                    }
        
                    //gets first one
                    int first = board.getQuantumCacheList()[i].get(j).loc;
                    int second = board.getQuantumCacheList()[i].get(j).loc2;
        
                    int qoffset1 = 0;
                    int qoffset2 = 0;
                    if(first == i){
                        qoffset1 = j;
                        qoffset2 = board.getQuantumCacheList()[second].indexOf(board.getQuantumCacheList()[i].get(j));
                        //System.out.println("First is " + j + ", second is " + qoffset2 + " for move " + board.getQuantumCacheList()[i].get(j).getTurn());
                    }else{
                        qoffset1 = board.getQuantumCacheList()[first].indexOf(board.getQuantumCacheList()[i].get(j));
                        qoffset2 = j;
                        //System.out.println("first is " + qoffset2 + ", second is " + j + " for move " + board.getQuantumCacheList()[i].get(j).getTurn());
                    }

        
                    //System.out.println(first + " " + second);
    
                    int z1 = first / 9;
                    int x1 = first % 3;
                    int y1 = ((first - (z1 * 9)) / 3);
    
                    int z2 = second / 9;
                    int x2 = second % 3;
                    int y2 = ((second - (z2 * 9)) / 3);
        
                    int qx1 = (qoffset1 % 3) - 1;
                    int qy1 = (qoffset1 / 3) - 1;
                    int qx2 = (qoffset2 % 3) - 1;
                    int qy2 = (qoffset2 / 3) - 1;
    
                    int xdraw1 = xbound + (x1 * cellSize) + (cellSize / 2) + (qx1 * quantumCellSize) -1;
                    int ydraw1 = ybound + (y1 * cellSize) + (cellSize / 2) + (qy1 * quantumCellSize) -1;
                    int xdraw2 = xbound + (x2 * cellSize) + (cellSize / 2) + (qx2 * quantumCellSize) -1;
                    int ydraw2 = ybound + (y2 * cellSize) + (cellSize / 2) + (qy2 * quantumCellSize) -1;
    
                    Point2D.Double one = new Point2D.Double(xdraw1, ydraw1);
                    Point2D.Double two = new Point2D.Double(xdraw2, ydraw2);
                    Point2D.Double oneT = new Point2D.Double();
                    Point2D.Double twoT = new Point2D.Double();
    
                    transform.transform(one, oneT);
                    transform.transform(two, twoT);
        
                    //the 2 is there because I flipped board order (I think)
                    g.drawLine((int) oneT.getX(), (int) oneT.getY() + ((2 - z1) * boardoffset), (int) twoT.getX(), (int) twoT.getY() + ((2 - z2) * boardoffset));
                }
            }
        }
    
        //put this after, so that the move lines don't cover the numbers
        g.drawImage(image2, 0, 0, null);
        g.drawImage(image1, 0, boardoffset, null);
        g.drawImage(image0, 0, boardoffset * 2, null);
    }

    public void drawMouseLine(Graphics2D g, QuantumBoard board){

        if(!turn){
            g.setColor(red);
        }else{
            g.setColor(blue);
        }
        
        int x1 = theSquare % 3;
        int y1 = (int) Math.floor(theSquare / 3);

        // int cell = board.getBoardTile(x1, y1).getMovesList().size();
        int cell = board.getQuantumCacheList()[theSquare].size();
        int xcell = cell % 3;
        int ycell = (int) Math.floor(cell / 3);

        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int x2 = (int) (b.getX() - this.getLocationOnScreen().getLocation().getX());
        int y2 = (int) (b.getY() - this.getLocationOnScreen().getLocation().getY());

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
        int qcellSize = (int) cellSize / 3;

        ybound = -1 * (boundingSize / 2);
        xbound = -1 * (boundingSize / 2);

        buffer = (int) ((width / 40) * zoom);

        xbound = xbound + offsetx;
        ybound = ybound + offsety;

        int xNetOffset = (int) (xbound + (x1 * cellSize) + (xcell * qcellSize) + (qcellSize / 2));
        int yNetOffset = (int) (ybound + (y1 * cellSize) + (ycell * qcellSize) + (qcellSize / 2));

        g.drawLine(xNetOffset, yNetOffset, x2, y2);

    }

    public void drawMouseLine3D(Graphics2D g, QuantumBoard3D board){
        AffineTransform transform = new AffineTransform(Math.cos(theta), (-1 * Math.sin(theta)) / 2, Math.sin(theta), Math.cos(theta) / 2, offsetx, offsety);
        int boardoffset = height / 3;
        int quantumCellSize = cellSize / 3;

        if(!turn){
            g.setColor(red);
        }else{
            g.setColor(blue);
        }

        int first = theSquare;

        int z1 = first / 9;
        int x1 = first % 3;
        int y1 = ((first - (z1 * 9)) / 3);

        int qoffset1 = board.getQuantumCacheList()[first].size();

        int qx1 = (qoffset1 % 3) - 1;
        int qy1 = (qoffset1 / 3) - 1;

        int xdraw1 = xbound + (x1 * cellSize) + (cellSize / 2) + (qx1 * quantumCellSize) -1;
        int ydraw1 = ybound + (y1 * cellSize) + (cellSize / 2) + (qy1 * quantumCellSize) -1;

        Point2D.Double one = new Point2D.Double(xdraw1, ydraw1);
        Point2D.Double oneT = new Point2D.Double();

        Point mouse = MouseInfo.getPointerInfo().getLocation();
        mouse = getMousePosition();

        transform.transform(one, oneT);

        //the 2 is there because I flipped board order (I think)
        g.drawLine((int) oneT.getX(), (int) oneT.getY() + ((2 - z1) * boardoffset), (int) mouse.getX(), (int) mouse.getY());
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

    public void calculateHover3D(double x, double y){
        moveDrawLoc = -1;

        if(result == 0 || quantumBoard3D.getState() != State.Blank){
            return;
        }
        
        AffineTransform transform = new AffineTransform(Math.cos(theta), (-1 * Math.sin(theta)) / 2, Math.sin(theta), Math.cos(theta) / 2, offsetx, offsety);
        try {
            transform.invert();
        } catch (NoninvertibleTransformException e1) {
            System.out.println("Non invertible transform");
        }

        int boardZ = 0;

        Point2D.Double click1 = new Point2D.Double(x, y);
        if(y < height / 3){
            boardZ = 2;
            click1.setLocation(x - offsetx, y - offsety);
            //System.out.println("Top board, " + click1.getX() + ", " + click1.getY());
        }else if(y > height / 3 && y < (2 * (height / 3))){
            boardZ = 1;
            click1.setLocation(x - offsetx, y - offsety - (height / 3));
            //System.out.println("Middle board, " + click1.getX() + ", " + click1.getY());
        }else{
            click1.setLocation(x - offsetx, y - offsety - (2 * height / 3));
            //System.out.println("Bottom board, " + click1.getX() + ", " + click1.getY());
        }

        Point2D.Double click2 = new Point2D.Double();
        transform.deltaTransform(click1, click2);

        if((click2.getX() > xbound && click2.getX() < (xbound + boundingSize) && click2.getY() > ybound && click2.getY() < (ybound + boundingSize)) && quantumBoard3D.getState() == State.Blank){
            int xboard = (int) click2.getX() - xbound;
            int yboard = (int) click2.getY() - ybound;

            int xcell = (int) Math.floor(3 * xboard / boundingSize);
            int ycell = (int) Math.floor(3 * yboard / boundingSize);

            //System.out.println("Hovering over " + xcell + ", " + ycell + ", " + boardZ);

            if(xcell + (ycell * 3) + (boardZ * 9) == recentSquare1){
                moveDrawLoc = recentSquare1;
            }else if(xcell + (ycell * 3) + (boardZ * 9) == recentSquare2){
                moveDrawLoc = recentSquare2;
            }
        }
        
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        int circleDivisions = 48;
        int divisor = circleDivisions / 2;

        int realx = getRealX(e);
        int realy = getRealY(e);

        if (e.getWheelRotation() < 0) {
            if(game != 5){
                zoom = zoom / 0.75;
            }else{
                theta += Math.PI / divisor;
                calculateHover3D(getMousePosition().getX(), getMousePosition().getY());
                repaint();
                //zoom = zoom / 0.75;
            }  


        }else{
            if(game != 5){
                zoom = zoom * 0.75;
            }else{
                theta -= Math.PI / divisor;
                calculateHover3D(getMousePosition().getX(), getMousePosition().getY());
                repaint();
                //zoom = zoom * 0.75;
            }  
        }

        int diffx = (getRealX(e) - realx);
        int diffy = (getRealY(e) - realy);

        if(zoom > 1){
            zoom = 1;
        }else if(zoom < Math.pow(0.75, 8)){
            zoom = Math.pow(0.75, 8);
        }else{
            offsetx = offsetx + (int) (diffx * zoom);
            offsety = offsety + (int) (diffy * zoom);
        }

        repaint();

    }
    
    public void resizeVariables(int width1, int height1){
        width = width1;
        height = height1;

        //do anything needed with comparing to old size

        int pmidx = pwidth / 2;
        int pmidy = pheight / 2;

        double xoffsetc = offsetx - pmidx;
        double yoffsetc = offsety - pmidy;

        offsetx = (int) ((width / 2) + xoffsetc);
        if(game != 5){
            offsety = (int) ((height / 2) + yoffsetc);
        }

        //sets the new previous size
        pwidth = width;
        pheight = height;
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
        output += recentCell;

        if(bot){
            output += 1;
        }else{
            output += 0;
        }
        output += depth;

        output += " ";

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

        int end = board.checkEntireBoard();

        if(end == 1){
            displayLabel.setForeground(red);
            displayLabel.setText("Player 1 wins");
            replay();
            board.setActive(false);
        }else if(end == 2){
            displayLabel.setForeground(blue);
            displayLabel.setText("Player 2 wins");
            replay();
            board.setActive(false);
        }

        //handles the first bot move
        if(bot && end == 0 && turn){
            displayLabel.setText("Player 2 is thinking");
            displayLabel.setForeground(blue);
            repaint();

            //from chatgpt to make the ui update
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    //maybe temporary, create new one
                    Move move = new mAI(3, depth).check(massiveBoard, depth, false);
                    int large = move.loc / 100;
                    int boardnum = (move.loc - (large * 100)) / 10;
                    int cell = (move.loc - (large * 100) - (boardnum * 10));
                    massiveMoveAftermath(large % 3, large / 3, boardnum % 3, boardnum / 3, cell % 3, cell / 3);
                }
            });
        }
    
    }

    public void setOutputDir(File file){
        outputDir = file;
        outputPath = outputDir.toString();
    }

    // public Image getTurnImage(int turn){
    //     switch (turn){
    //         case 1: return Image1;
    //         case 2: return Image2;
    //         case 3: return Image3;
    //         case 4: return Image4;
    //         case 5: return Image5;
    //         case 6: return Image6;
    //         case 7: return Image7;
    //         case 8: return Image8;
    //         case 9: return Image9;
    //         default: return null;
    //     }
    // }

    public void setDepth(int x){
        depth = x;
        mAI = new mAI(game, x);
    }

    public void replay(){
        add(replayPanel, BorderLayout.SOUTH);
    }

    public void setColor(Color c){
        buttonColor = c;
        replayButton.setColor(c);
    }

    public void setImages(Image x1, Image o1, Image x2, Image o2){
        xImage = x1;
        xImage2 = x2;
        oImage = o1;
        oImage2 = o2;
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
}
