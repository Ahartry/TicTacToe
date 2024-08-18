package com.Game;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LFrame extends JFrame{

    ArrayList<String> saveList;
    JPanel panel;

    GFrame frame;

    public LFrame(GFrame frame){

        this.frame = frame;
        
        //gets all the saves
        String path = System.getProperty("user.dir") + File.separator + "Saves";
        File file = new File(path);
        
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        saveList = new ArrayList<String>(Arrays.asList(directories));

        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        saveList = sortList(saveList);

        //adds the saves
        for(int i = 0; i < saveList.size(); i++){
            gbc.gridy = i;
            panel.add(new SaveMenu(saveList.get(i), this), gbc);
        }

        add(panel);

        gbc.gridy += 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
    
        panel.add(new JLabel(" "), gbc); 

        //actual frame stuff
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setTitle("Select save");
        setVisible(true);
        setResizable(false);
    }

    public void loadSave(File file) throws Exception{
        System.out.println(file.getPath());
        Scanner scan = new Scanner(file);

        System.out.println("Loading from " + file);

        if(file.getName().equals("move0")){
            frame.setupGame(3, false);
            frame.getGPanel().setOutputDir(file.getParentFile());
            MassiveBoard board = new MassiveBoard();
            board.setActive(true);
            frame.getGPanel().setMassiveBoard(board, false);
            scan.close();
            return;
        }

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
    
    
            //int i = 0;
            while(scan.hasNext()){
                //System.out.println(i);
                //i++;
                
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
    
            frame.setupGame(3, false);
            inputboard.calculateActive(xl, yl, xb, yb, xc, yc);
            frame.getGPanel().setMassiveBoard(inputboard, turn);
            frame.getGPanel().setOutputDir(file.getParentFile());
    
            scan.close();
        }else{
            System.out.println("Unsupported game type");
        }

        
    }
    
    public void refreshList(LFrame frame){
        saveList.clear();
        panel.removeAll();
        //gets all the saves
        String path = System.getProperty("user.dir") + File.separator + "Saves";
        File file = new File(path);
        
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        saveList = new ArrayList<String>(Arrays.asList(directories));

        //System.out.println(saveList);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;

        //adds the saves
        for(int i = 0; i < saveList.size(); i++){
            //System.out.println(i);
            gbc.gridy = i;
            panel.add(new SaveMenu(saveList.get(i), frame), gbc);
        }

        gbc.gridy += 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
    
        panel.add(new JLabel(" "), gbc); 

        panel.revalidate();
        panel.repaint();
    }

    public ArrayList<String> sortList(ArrayList<String> input){

        ArrayList<String> output = new ArrayList<String>();

        ArrayList<File> fileList = new ArrayList<File>();
        for(int i = 0; i < input.size(); i++){
            String path = System.getProperty("user.dir") + File.separator + "Saves" + File.separator + input.get(i);
            File file = new File(path);
            int fileCount=file.list().length;
            String savePath = path + File.separator + "move" + fileCount;
            File saveFile = new File(savePath);
            fileList.add(saveFile);
        }

        fileList.sort(Comparator.comparingLong(File::lastModified));

        for(int i = 0; i < input.size(); i++){
            output.add(fileList.get(i).getParentFile().getName().toString());
        }

        Collections.reverse(output);

        //System.out.println("Input: " + input + ", output: " + output);
        return output;
    }
}