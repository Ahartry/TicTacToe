package com.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JLabel;

public class GButton extends JButton{
	private static final long serialVersionUID = 1L;
	
	private JLabel text = new JLabel();
	private GToolTip toolTip;
	private String subtext = null;
	private boolean usingTooltip = false;
	private volatile boolean toolTipActive = false;
	
	private enum Status{MAIN, PRESSED, HOVER}
	private Status status = Status.MAIN;
	
	private int paddingX = 0;
	private int paddingY = 0;
	
	private Color mainColor = new Color(0,150,50,255);
	private Color clickedColor = new Color(0,120,20,255);
	private Color hoverColor = new Color(0,200,100,255);
	private Color disabledColor = new Color(180, 180, 180, 255);
	
	private volatile boolean hovering = false;
	private volatile boolean clicked = false;

	public void setText(String t) {
		text.setText(t);
	}

	public void setSubtext(String input){
		subtext = input;
		usingTooltip = true;
	}


	public GButton(String t) {
		addActionListener(new mainActionListener());
		setLayout(new BorderLayout());
		text.setText(t);
		text.setForeground(Color.WHITE);
		text.setHorizontalAlignment(JLabel.CENTER);
		text.setVerticalAlignment(JLabel.CENTER);
        this.size(22);
		this.add(text, BorderLayout.CENTER);
		
		setContentAreaFilled(false);
		setFocusable(false);
		setBorderPainted(false);
		
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	if(isEnabled()) {
					status = Status.HOVER;
					hovering = true;

					if(usingTooltip){
						Thread thread = new Thread(){
							long start = System.currentTimeMillis();
							long end = start + 500;
							public void run(){
								while(true){
									if(!hovering){
										break;
									}

									if(clicked){
										clicked = false;
										break;
									}
	
									if(System.currentTimeMillis() >= end){
										toolTipActive = true;
										toolTip = new GToolTip(subtext, MouseInfo.getPointerInfo().getLocation());
										
										//System.out.println("Showing");
										break;
									}

									try {
										Thread.sleep(10);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
						};
						
						thread.start();
					}

		    	}
		    	setCursor(new Cursor(Cursor.HAND_CURSOR));
				repaint();
		    }
		    
		    public void mouseReleased(java.awt.event.MouseEvent evt) {
				status = Status.MAIN;
				if(!hovering){
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				if(toolTipActive){
					toolTip.kill();
				}
				clicked = true;
				repaint();
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	if(isEnabled()) {
					hovering = false;
					status = Status.MAIN;
		    	}

				//System.out.println("Exited, " + toolTipActive);
				if(toolTipActive){
					//System.out.println("Attempting to kill");
					toolTipActive = false;
					toolTip.kill();
					toolTip = null;
				}
		    	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				repaint();
		    }
		    
		    public void mousePressed(java.awt.event.MouseEvent evt) {
		    	if(evt.getButton() == MouseEvent.BUTTON1) {
					status = Status.PRESSED;
		    	}
				repaint();
		    }

			public void mouseMoved(MouseEvent evt){
			}
		});
	}
	
	public void size(int si) {
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont((float) si);
			text.setFont(font);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void padding(int x, int y) {
		paddingX = x;
		paddingY = y;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHints(rh);

		switch(status) {
		case HOVER:
			g2d.setColor(hoverColor);
			break;
		case PRESSED:
			g2d.setColor(clickedColor);
			break;
		default:
			g2d.setColor(mainColor);
			break;
		}
		
		if(isEnabled() == false) {
			g2d.setColor(disabledColor);
		}

		g2d.fillRoundRect(1+paddingX,1+paddingY,getWidth()-paddingX*2-2,getHeight()-paddingY*2-2,30,30);

	}
	
	private class mainActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
	    	//new Sound().play("ping.wav");
		}
		
	}
}