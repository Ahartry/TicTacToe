package com.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class GToolTip extends JFrame{

    JLabel textLabel;
    int length = 0;
    boolean active = true;

    public GToolTip(String text, Point mouse){

        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(15f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        textLabel = new JLabel();
        textLabel.setFont(font);
        textLabel.setText(text);

        length = (int) (text.length() * 7.8);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setLocation((int) mouse.getX() + 15, (int) mouse.getY() + 20);
        setShape(new RoundRectangle2D.Double(0, 0, length, 20, 5, 5));
        getRootPane().setBorder(new LineBorder(Color.black, 2, true));
        setSize(length, 20);
        add(textLabel);
        setVisible(true);
        //System.out.println("tooltip created");

        Thread thread = new Thread(){
            public void run(){
                int counter = 0;
                while(true){
                    Point loc = MouseInfo.getPointerInfo().getLocation();
                    setLocation((int) loc.getX() + 15, (int) loc.getY() + 20);
                    counter++;
                    if(!active){
                        kill();
                        break;
                    }
                    if(counter > 300){
                        kill();
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

    public void kill(){
        //System.out.println("kill");
        active = false;
        this.dispose();
    }

}
