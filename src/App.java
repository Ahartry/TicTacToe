import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;
import java.awt.Font;
import java.awt.GridBagConstraints;

public class App {

    public static void main(String[] args) throws Exception {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        
        JFrame startFrame = new JFrame();
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(400, 300);
        startFrame.setLocationRelativeTo(null);
        startFrame.setTitle("Tic Tac Toe Game");

        JPanel startPanel = new JPanel();
        startPanel.setLayout(new GridBagLayout());
        startPanel.setFocusable(true);
        startPanel.requestFocus();
        
        startFrame.add(startPanel);

        JButton regButton = new JButton("Regular");
        JButton ultButton = new JButton("Massive");
        JButton tranButton = new JButton("Ultimate");
        JButton quanButton = new JButton("Quantum");
        JButton loadButton = new JButton("Load");
        
        regButton.setFont(new Font("Sans Serif", Font.BOLD, 18));
        ultButton.setFont(new Font("Sans Serif", Font.BOLD, 18));
        tranButton.setFont(new Font("Sans Serif", Font.BOLD, 18));
        quanButton.setFont(new Font("Sans Serif", Font.BOLD, 18));
        loadButton.setFont(new Font("Sans Serif", Font.BOLD, 18));

        //adds the items to the panel
        gbc.gridx = 1;
        gbc.gridy = 5;

        startPanel.add(loadButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;

        startPanel.add(regButton, gbc);

        gbc.gridx = 2;

        startPanel.add(ultButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;

        startPanel.add(tranButton, gbc);

        gbc.gridx = 2;

        startPanel.add(quanButton, gbc);

        startFrame.setVisible(true);

        regButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts regular game
                new GFrame(1);
            } 
        });

        ultButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts regular game
                new GFrame(2);
            } 
        });

        tranButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts regular game
                new GFrame(3);
            } 
        });

        quanButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts regular game
                new GFrame(4);
            } 
        });

        loadButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                //starts regular game
                JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
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
    
                inputboard.getBoardArray(large % 3, (int) Math.floor(large / 3)).getBoardArray(board % 3, (int) Math.floor(board / 3)).getBoardArray(cell % 3, (int) Math.floor(cell / 3)).setState(state);
                
                int resultsmall = inputboard.getBoardArray(xl, yl).getBoardArray(xb, yb).checkBoard(xc, yc);
                int resultlarge = inputboard.getBoardArray(xl, yl).checkBoard(xb, yb);
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
    
            GFrame frame = new GFrame(gameType);
            inputboard.calculateActive(xl, yl, xb, yb, xc, yc);
            frame.getGPanel().setMassiveBoard(inputboard, turn, recentLarge, recentBoard, recentCell);
    
            scan.close();
        }else{
            System.out.println("Unsupported game type");
        }

        
    }
}
