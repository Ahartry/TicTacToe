package com.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HFrame extends JFrame{
    
    public HFrame(int gameType, Sound sound, Color c) throws FontFormatException, IOException{

        int width = 500;
        int height = 350;

        //I need more explanation space
        if(gameType == 4){
            height *= 1.4;
        }
        if(gameType == 1){
            height /= 2;
        }
        if(gameType == 3){
            height *= 1.1;
        }
        if(gameType == 5){
            height *= 0.8;
        }

        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        Font fontTest = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);
        if(GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(fontTest)){
            System.out.println("Font Sucessfully Registered");
        }else{
            System.out.println("Font Failed to Register");
        }

        JPanel panel = new JPanel();
        JLabel infoLabel = new JLabel();
        infoLabel.setFont(fontTest);
        infoLabel.setPreferredSize(new java.awt.Dimension(width, height - 80));
        infoLabel.setVerticalAlignment(SwingConstants.TOP);

        GButton okButton = new GButton("Ok");
        okButton.setFont(fontTest);
        okButton.setVerticalAlignment(SwingConstants.BOTTOM);
        okButton.setHorizontalAlignment(SwingConstants.CENTER);
        okButton.setSound(sound);
        okButton.setColor(c);

        add(panel);

        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(infoLabel, BorderLayout.CENTER);

        panel.add(infoLabel);
        panel.add(okButton);

        //Explanation strings:
        String regular = "<html>This button is only here for consistency. I hope you don't need an explanation of how this works</html>";
        String massive = "<html>When you make a move on a regular board, active play moves to the corresponding large board (Playing in bottom left corner of the board will move active play to bottom left board). <BR><BR>If you would be sent to a board that is completed/full, you can play anywhere.<BR><BR>Play until three-in-a-row on the massive board</html>";
        String ultimate = "<html>Similar to Massive Tic Tac Toe (See help window in Massive Tic Tac Toe).<BR><BR>Active play proceeds on one massive board until a regular board has a victory. Active play then moves to the corresponding massive board on the larger Ultimate board.<BR><BR>Play until three-in-a-row on the ultimate board<BR><BR>Game will autosave after every move</html>";
        String quantum = "<html>Each turn you 'link' two boards by placing a quantum move in each. This entangles the two positions, and when the quantum state collapses, one of them will have to be the played move.<BR><BR>The quantum state collapses once a loop of links is formed, and then the player who did not just make a move decides how it collapses. This does not take their turn.<BR><BR>If both players have the same amount of three-in-a-rows, then victory is awarded to the player with the lowest, highest number in their row (2 4 6 beats 1 3 7)</html>";
        String quantum3D = "<html>Quantum Tic Tac Toe, but in 3d. By scrolling, you can rotate the 3d boards to get a better view. In this mode, the three in a row can be across any straight line or diagonal.<BR><BR>For more details, see the help window for Quantum Tic Tac Toe</html>";

        if(gameType == 1){
            infoLabel.setText(regular);
        }else if(gameType == 2){
            infoLabel.setText(massive);
        }else if(gameType == 3){
            infoLabel.setText(ultimate);
        }else if(gameType == 4){
            infoLabel.setText(quantum);
        }else if(gameType == 5){
            infoLabel.setText(quantum3D);
        }

        infoLabel.setFont(fontTest);

        setSize(width, height);
        setLocationRelativeTo(null);
        setTitle("Help Window");
        setFont(fontTest);
        setResizable(false);
        setVisible(true);

        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                dispose();
                
            }

        });
    }
}
