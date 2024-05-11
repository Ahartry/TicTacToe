import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HFrame extends JFrame{
    
    public HFrame(int gameType){

        int width = 500;
        int height = 300;

        //I need more explanation space
        if(gameType == 4){
            height *= 1.5;
        }

        JPanel panel = new JPanel();
        JLabel infoLabel = new JLabel();
        infoLabel.setFont(new Font("Sans Serif", Font.BOLD, 18));
        infoLabel.setPreferredSize(new java.awt.Dimension(width, height));
        infoLabel.setVerticalAlignment(SwingConstants.TOP);

        add(panel);

        panel.add(infoLabel);

        //Explanation strings:
        String regular = "<html>This button is only here for consistency. I hope you don't need an explanation of how this works</html>";
        String massive = "<html>When you make a move on a regular board, active play moves to the corresponding large board (Playing in bottom left corner of the board will move active play to bottom left board). <BR><BR>If you would be sent to a board that is completed/full, you can play anywhere.<BR><BR>Play until three-in-a-row on the massive board</html>";
        String ultimate = "<html>Similar to Massive Tic Tac Toe (See help window in Massive Tic Tac Toe).<BR><BR>Active play proceeds on one massive board until a regular board has a victory. Active play then moves to the corresponding massive board on the larger Ultimate board.<BR><BR>Play until three-in-a-row on the ultimate board</html>";
        String quantum = "<html>Each turn you 'link' two boards by placing a quantum move in each. This entangles the two positions, and when the quantum state collapses, one of them will have to be the played move.<BR><BR>The quantum state collapses once a loop of links is formed, and then the player who did not just make a move decides how it collapses. This does not take their turn.<BR><BR>If both players have the same amount of three-in-a-rows, then victory is awarded to the player with the lowest, highest number in their row (2 4 6 beats 1 3 7)</html>";

        if(gameType == 1){
            infoLabel.setText(regular);
        }else if(gameType == 2){
            infoLabel.setText(massive);
        }else if(gameType == 3){
            infoLabel.setText(ultimate);
        }else if(gameType == 4){
            infoLabel.setText(quantum);
        }

        setSize(width, height);
        setLocationRelativeTo(null);
        setTitle("Help Window");
        setResizable(false);
        setVisible(true);
    }
}
