import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GFrame extends JFrame{

    GPanel bottomPanel;
    
    public GFrame(int gameType){

        System.out.println("\nStarting game type " + gameType + "\n");

        JPanel topPanel = new JPanel();
        JLabel topLabel = new JLabel("Player 1 goes");
        bottomPanel = new GPanel(gameType, topLabel);

        topLabel.setFont(new Font("Sans Serif", Font.BOLD, 30));

        topPanel.setSize(new Dimension(1000, 50));
        topPanel.setBackground(Color.GRAY);
        topPanel.add(topLabel);

        bottomPanel.setSize(new Dimension(1000, 500));
        bottomPanel.requestFocus();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setTitle("Tic Tac Toe");
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel);
        setVisible(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                bottomPanel.resizeVariables(e.getComponent().getWidth(), getContentPane().getHeight() - 50);
            }
            @Override
            public void componentMoved(ComponentEvent e) {
              
            }
        });
    }

    public GPanel getGPanel(){
        return bottomPanel;
    }

}
