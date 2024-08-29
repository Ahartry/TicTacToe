package com.Game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class CButton extends JButton{

    Color color;
    boolean selected = false;

    public void unselect(){
        selected = false;
        repaint();
    }

    public CButton(Color c, CFrame frame){

        color = c;

        setPreferredSize(new Dimension(24, 24));

        setContentAreaFilled(false);
		setFocusable(false);
		setBorderPainted(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
		    
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
				if(isEnabled()){
                    //setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    frame.setColor(c);
                    selected = true;
                    repaint();
                }
		    }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	setCursor(new Cursor(Cursor.HAND_CURSOR));
				repaint();
		    }
            public void mouseExited(java.awt.event.MouseEvent evt) {
		    	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				repaint();
		    }
		    
        });
    }

    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHints(rh);

		g2d.setColor(color);
        g2d.fillRect(0, 0, 24, 24);

        // if(selected){
        //     g2d.setColor(Color.BLACK);
        //     g2d.drawRect(0, 0, 24, 24);
        // }

	}

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(24, 24);
    }

}
