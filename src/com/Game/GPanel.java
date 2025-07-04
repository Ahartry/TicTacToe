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
    Board board;

    JLabel displayLabel;
    JPanel replayPanel;

    GButton replayButton;
    GFrame frameobject;

    mAI mAI;

    Sound sound;

    int count = 0;
    int turn = 1;
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

    int[] coords;

    boolean collapseMove = false;

    boolean bot;

    double theta = Math.PI / 4;
    int imagex = 0;
    int imagey = 0;

    int zoomMax = -8;

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

        coords = new int[2];

        //instantiates boards and sets default zooms
        if(game == 1){
            board = new Board(6, false);
            zoom = 0.75;
        }else if(game == 2){
            board = new Board(4, false);
            zoom = Math.pow(0.75, 4);
        }else if(game == 3){
            board = new Board(6, false);
            zoom = Math.pow(0.75, 8);
        }else if(game == 4){
            board = new Board(2, true);
            zoom = 0.75;
        }else if(game == 5){
            board = new Board(3, true);
            zoom = Math.pow(0.75, 5);
        }

        repaint();

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

        displayLabel.setForeground(red);

        //all the clicking stuff
        addMouseListener(new MouseAdapter() { 
            public void mouseReleased(MouseEvent e) { 
                if(movementCounter == 0){
                    handleClick(e);
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

                //stuff for keeping it close to view (disables panning past board)
                if(offsetx > width){
                    offsetx = width;
                    return;
                }
                if(offsetx + boardSize < 0){
                    offsetx = -1 * (int) boardSize;
                    return;
                }
                if(offsety > height){
                    offsety = height;
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

                //repaint();
            }
        }); 
        
        // Add this panel as a MouseWheelListener
        this.addMouseWheelListener(this);

        // button.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent e) {
        //         if(gameType == 3){
        //             try {
        //                 outputBoard(massiveBoard);
        //             } catch (FileNotFoundException e1) {
        //                 e1.printStackTrace();
        //             }
        //         }else if(gameType == 4 || gameType == 5){
        //             quantumLineMode = !quantumLineMode;
        //             if(quantumLineMode){
        //                 button.setText("Hide Lines");
        //             }else{
        //                 button.setText("Show Lines");
        //             }
        //             repaint();
        //         }
                
        //     }

        // });

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

    public void handleClick(MouseEvent e){
        //board tile that has been clicked
        int loc = coordToLoc(e.getX(), e.getY());

        //makes sure that click is in a valid location
        if(loc == -1){
            return;
        }
        //checks at every level if the tile is already taken
        for(int i = 0; i < board.getBoardArrays().size(); i++){
            if(board.getBoardArrays().get(i)[loc / (int) (Math.pow(9, i))] != 0){
                return;
            }
        }
        if(!board.isActive(0, loc)){
            return;
        }

        //actually moves once it passes the checks
        board.move(loc, turn);
        turn++;

        //updates UI
        if(turn % 2 == 0){
            displayLabel.setText("Player 2's turn");
            displayLabel.setForeground(blue);

        }else{
            displayLabel.setText("Player 1's turn");
            displayLabel.setForeground(red);
        }

        repaint();
    }

    //current plan is to split the board into 28x28 tiles, borders are 1 wide, individual squares are 8
    public void drawBoard(Graphics2D g){
        //draws all the board grids
        drawGrid(g, 0, 0);
        //iterates through all the scale levels
        for(int i = 2; i < board.getScale(); i += 2){ // 2 4 6
            //iterates through each board on the scale level
            int tileCount = (int) Math.pow(9, i/2);
            for(int j = 0; j < tileCount; j++){
                drawGrid(g, i / 2, j);
            }
        }

        //iterates over all tiles
        for(int i = 0; i < board.getBoardArrays().size() - 1; i++){

            int scale = board.getBoardArrays().size() - i - 1;

            //skips drawing if it is too small
            if(((Math.min(width, height) * zoom) * Math.pow(8.0/28.0, scale - 1)) / 28.0 < 1){
                continue;
            }

            for(int j = 0; j < board.getBoardArrays().get(i).length; j++){
                int t = board.getBoardArrays().get(i)[j];

                //t=0 means blank tile
                if(t == 0){
                    continue;
                }

                //dimension of tile
                int size = (int) ((Math.min(width, height) * zoom) * Math.pow(8.0/28.0, scale) * 26.0/28.0);

                //loads drawing coordinates into coords[]
                locToCoord(scale, j);

                if(t % 2 == 0){
                    g.drawImage(oImage, offsetx + coords[0], offsety + coords[1], size, size, null);
                }else{
                    g.drawImage(xImage, offsetx + coords[0], offsety + coords[1], size, size, null);
                }
            }
        }
    }

    //IMPORTANT: Takes in board.getScale() / 2
    public void drawGrid(Graphics2D g, int scale, int location){
        //gets board size, multiplied by scale coeffecient. Each board is 8/28 of the larger one
        boundingSize = (int) ((Math.min(width, height) * zoom) * Math.pow(8.0/28.0, scale));
        double d_cellSize = (double) boundingSize / 28.0;
        cellSize = (int) d_cellSize;

        //useful lengths
        //one third, two thirds, length of line
        int b1_3rd = (int) (d_cellSize * 9);
        int b2_3rd = (int) (d_cellSize * 18);
        int length = (int) (d_cellSize * 26);

        locToCoord(scale, location);

        //position on board
        int offx = coords[0];
        int offy = coords[1];

        //scale counting from other direction
        int bscale = board.getScale() / 2 - scale;

        //decies what color to draw
        if(board.isActive(bscale, location) && board.getBoardArrays().get(bscale)[location] == 0){
            if(turn % 2 == 0){
                g.setColor(blue);
            }else{
                g.setColor(red);
            }
        }else{
            g.setColor(Color.BLACK);
        }

        //draws all 4 rectangles that make up a board
        g.fillRect(offsetx + b1_3rd + offx - cellSize, offsety + offy, cellSize, length);
        g.fillRect(offsetx + b2_3rd + offx - cellSize, offsety + offy, cellSize, length);
        g.fillRect(offsetx + offx, offsety + b1_3rd + offy - cellSize, length, cellSize);
        g.fillRect(offsetx + offx, offsety + b2_3rd + offy - cellSize, length, cellSize);
    }

    //IMPORTANT: Takes in board.getScale() / 2
    public void locToCoord(int scale, int location){

        coords[0] = 0;
        coords[1] = 0;

        //i is n in my formula
        for(int i = 1; i < scale + 1; i++){
            double board = ((Math.min(width, height) * zoom) * Math.pow(8/28.0, i));

            double d_cell = board / 8;

            int loc = (location / (int) Math.pow(9, scale-i)) % 9;
            // System.out.println("K: " + location + ", n: " + scale + ", L: " + loc);
            int locx = loc % 3;
            int locy = loc / 3;

            //d_cell by itself is starting offset, then positional offset is added
            coords[0] = coords[0] + (int) d_cell + (int) (locx * d_cell * 9);
            coords[1] = coords[1] + (int) d_cell + (int) (locy * d_cell * 9);
        }

        //gets sizes to add final offset
        boundingSize = (int) ((Math.min(width, height) * zoom) * Math.pow(8.0/28.0, scale));
        double d_cellSize = (double) boundingSize / 28.0;
        cellSize = (int) d_cellSize;

        //adds final offset
        coords[0] = coords[0] + cellSize;
        coords[1] = coords[1] + cellSize;
        
    }

    public int coordToLoc(int x, int y){
        int loc = -1;

        //norm is space relative to board (instead of window)
        int normx = x - offsetx;
        int normy = y - offsety;

        int scale = board.getScale();

        //offset and width of possible tiles in memory. Gets refined each loop by checking which nonant the click is in
        int rangeStart = 0;
        int range = board.getTileCount();

        //0 1 2 (if scale = 6)
        for(int i = 0; i < scale / 2; i++){
            boundingSize = (int) ((Math.min(width, height) * zoom) * Math.pow(8.0/28.0, i));
            //trims off the ends
            normx -= boundingSize / 28;
            normy -= boundingSize / 28;

            //size of tile for dividing coordinates
            int tile = (int) (boundingSize * (9.0/28.0));
            int tx = normx / tile;
            int ty = normy / tile;

            //the tile from 0-8
            int t = tx + 3 * ty;

            //narrows the range of possible tiles (until it is just one tile)
            range /= 9;
            rangeStart += t * range;

            //renormalizes to smaller board
            normx -= tx * tile;
            normy -= ty * tile;

        }

        locToCoord(scale / 2, rangeStart);

        //reset norm for checking if click is within tile
        normx = x - offsetx;
        normy = y - offsety;
                
        int tileSize = (int) ((Math.min(width, height) * zoom) * Math.pow(8.0/28.0, (scale / 2)));

        //checks if click is within the found tile
        if(normx > coords[0] && normx < coords[0] + tileSize && normy > coords[1] && normy < coords[1] + tileSize){
            loc = rangeStart;
        }

        return loc;
    }

    @Override
    public void paintComponent(Graphics g1) {
        frameobject.revalidate();
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;

        //TODO find where this should actually go
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBoard(g);
        
        frameobject.revalidate();
        Toolkit.getDefaultToolkit().sync();
    }

    public void closeWindow(){
        setVisible(false);
        JFrame parent = (JFrame) this.getTopLevelAncestor();
        parent.dispose();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        int circleDivisions = 48;
        int divisor = circleDivisions / 2;

        int realx = getRealX(e);
        int realy = getRealY(e);

        if(e.getWheelRotation() < 0){
            zoom = zoom / 0.75;
        }else{
            zoom = zoom * 0.75;
        }

        int diffx = (getRealX(e) - realx);
        int diffy = (getRealY(e) - realy);

        if(zoom > Math.pow(0.75, zoomMax)){
            zoom = Math.pow(0.75, zoomMax);
        }else if(zoom < Math.pow(0.75, 4)){
            zoom = Math.pow(0.75, 4);
        }else{
            offsetx = offsetx + (int) (diffx * zoom);
            offsety = offsety + (int) (diffy * zoom);
        }

        repaint();

    }
    
    public void resizeVariables(int width1, int height1, boolean firstTime){
        width = width1;
        height = height1;

        //do anything needed with comparing to old size

        int pmidx = pwidth / 2;
        int pmidy = pheight / 2;

        double xoffsetc = offsetx - pmidx;
        double yoffsetc = offsety - pmidy;

        //the first time code runs when window is created, after that it is for when user resized
        if(firstTime){
            //makes default offset
            boundingSize = (int) (Math.min(width, height) * zoom);
            offsetx = (width - boundingSize) / 2;
            //System.out.println(boundingSize + ", " + offsetx + ", " + width);
            offsety = (height - boundingSize) / 2;
        }else{
            offsetx = (int) ((width / 2) + xoffsetc);
            if(game != 5){
                offsety = (int) ((height / 2) + yoffsetc);
            }
        }

        //sets the new previous size
        pwidth = width;
        pheight = height;

    }

    public int getRealX(MouseEvent e){
        return (int) ((e.getX() - offsetx) / zoom);
    }

    public int getRealY(MouseEvent e){
        return (int) ((e.getY() - offsety) / zoom);
    }

    public void setOutputDir(File file){
        outputDir = file;
        outputPath = outputDir.toString();
    }

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
