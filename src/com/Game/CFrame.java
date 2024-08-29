package com.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class CFrame extends JFrame{

    Color color = Color.GREEN;
    GButton okButton;
    CButton b1;
    CButton b2;
    CButton b3;
    CButton b4;
    CButton b5;
    CButton b6;
    CButton b7;
    CButton b8;

    GFrame frame;

    public CFrame(GFrame frame, Sound sound, Font font, Color color2){

        this.frame = frame;

        JPanel contentPanel = new JPanel(new GridBagLayout());

        JSlider soundSlider = new JSlider();
        soundSlider.setMinimum(0);
        soundSlider.setMaximum(100);
        soundSlider.setOrientation(SwingConstants.HORIZONTAL);
        soundSlider.setMajorTickSpacing(20);
        soundSlider.setMinorTickSpacing(5);
        soundSlider.setPaintTicks(true);
        soundSlider.setPreferredSize(new Dimension(150, 60));
        soundSlider.setSnapToTicks(true);

        JSlider musicSlider = new JSlider();
        musicSlider.setMinimum(0);
        musicSlider.setMaximum(100);
        musicSlider.setOrientation(SwingConstants.HORIZONTAL);
        musicSlider.setMajorTickSpacing(20);
        musicSlider.setMinorTickSpacing(5);
        musicSlider.setPaintTicks(true);
        musicSlider.setPreferredSize(new Dimension(150, 60));
        musicSlider.setSnapToTicks(true);

        JLabel soundLabel = new JLabel();
        JLabel musicLabel = new JLabel();
        Image soundImage = null;
        try {
            soundImage = ImageIO.read(getClass().getClassLoader().getResource("sound4.png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        soundLabel.setIcon(new ImageIcon(soundImage));

        Image musicImage = null;
        try {
            musicImage = ImageIO.read(getClass().getClassLoader().getResource("music4.png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        musicLabel.setIcon(new ImageIcon(musicImage));

        JLabel fullscreenLabel = new JLabel("     Fullscreen:  ");
        fullscreenLabel.setFont(font);
        JCheckBox fullscreenBox = new JCheckBox();

        JPanel fullscPanel = new JPanel();
        fullscPanel.add(fullscreenLabel);
        fullscPanel.add(fullscreenBox);

        okButton = new GButton("Confirm");
        okButton.setSound(sound);
        okButton.setFont(font);
        okButton.setColor(color2);

        JPanel colorPanel = new JPanel(new GridLayout(2, 4, 0, 1));
        colorPanel.setPreferredSize(new Dimension(100, 50));
        JLabel colorLabel = new JLabel();
        colorLabel.setFont(font);
        colorLabel.setText("   Color:");
        b1 = new CButton(new Color(0, 150, 50), this);
        b2 = new CButton(new Color(0, 150, 200), this);
        b3 = new CButton(new Color(0, 50, 200), this);
        b4 = new CButton(new Color(50, 0, 150), this);
        b5 = new CButton(new Color(255, 200, 00), this);
        b6 = new CButton(new Color(255, 100, 0), this);
        b7 = new CButton(new Color(200, 0, 0), this);
        b8 = new CButton(new Color(0, 0, 0), this);

        JPanel c2Panel = new JPanel();
        c2Panel.add(colorLabel);
        c2Panel.add(colorPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        contentPanel.add(soundLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(soundSlider, gbc);
        gbc.gridx = 2;
        contentPanel.add(fullscPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(musicLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(musicSlider, gbc);

        gbc.gridx = 2;
        contentPanel.add(c2Panel, gbc);

        colorPanel.add(b1);
        colorPanel.add(b2);
        colorPanel.add(b3);
        colorPanel.add(b4);
        
        colorPanel.add(b5);
        colorPanel.add(b6);
        colorPanel.add(b7);
        colorPanel.add(b8);

        setLayout(new BorderLayout());

        okButton.setPreferredSize(new Dimension(200, 40));
        JPanel okPanel = new JPanel();
        okPanel.add(okButton);

        add(contentPanel, BorderLayout.NORTH);
        add(okPanel, BorderLayout.SOUTH);

        setSize(450, 200);
        setLocationRelativeTo(null);
        setTitle("Settings");
        setFont(font);
        //setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setColor(color);
                dispose();
                repaint();
            }

        });

    }

    public void setColor(Color color){
        this.color = color;
        okButton.setColor(color);
        repaint();
    }

}
