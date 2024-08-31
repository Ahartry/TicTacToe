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
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
    int c = 1;
    int secret = 0;
    JSlider soundSlider;
    JSlider musicSlider;
    JCheckBox fullscreenBox;

    float soundV;
    float musicV;
    boolean full;

    GFrame frame;

    public CFrame(GFrame frame, Sound sound, Sound music, Font font, Color color2){
        color = color2;

        this.frame = frame;

        soundV = (float) sound.getVolume();
        musicV = (float) music.getVolume();
        full = frame.getFullscreen();

        JPanel contentPanel = new JPanel(new GridBagLayout());

        soundSlider = new JSlider();
        soundSlider.setMinimum(0);
        soundSlider.setMaximum(100);
        soundSlider.setOrientation(SwingConstants.HORIZONTAL);
        soundSlider.setMajorTickSpacing(20);
        soundSlider.setMinorTickSpacing(5);
        soundSlider.setPaintTicks(true);
        soundSlider.setPreferredSize(new Dimension(150, 60));
        soundSlider.setSnapToTicks(true);
        soundSlider.setValue((int) (sound.getVolume() + 44) * 2);

        musicSlider = new JSlider();
        musicSlider.setMinimum(0);
        musicSlider.setMaximum(100);
        musicSlider.setOrientation(SwingConstants.HORIZONTAL);
        musicSlider.setMajorTickSpacing(20);
        musicSlider.setMinorTickSpacing(5);
        musicSlider.setPaintTicks(true);
        musicSlider.setPreferredSize(new Dimension(150, 60));
        musicSlider.setSnapToTicks(true);
        musicSlider.setValue((int) (music.getVolume() + 44) * 2);

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
        fullscreenBox = new JCheckBox();

        JPanel fullscPanel = new JPanel();
        fullscPanel.add(fullscreenLabel);
        fullscPanel.add(fullscreenBox);
        if(frame.getFullscreen()){
            fullscreenBox.setSelected(true);
        }

        okButton = new GButton("Confirm");
        okButton.setSound(sound);
        okButton.setFont(font);
        okButton.setColor(color2);

        JPanel colorPanel = new JPanel(new GridLayout(2, 4, 0, 1));
        colorPanel.setPreferredSize(new Dimension(100, 50));
        JLabel colorLabel = new JLabel();
        colorLabel.setFont(font);
        colorLabel.setText("   Color:");
        b1 = new CButton(new Color(0, 150, 50), this, 1);
        b2 = new CButton(new Color(0, 150, 200), this, 2);
        b3 = new CButton(new Color(0, 50, 200), this, 3);
        b4 = new CButton(new Color(50, 0, 150), this, 4);
        b5 = new CButton(new Color(255, 200, 00), this, 5);
        b6 = new CButton(new Color(255, 100, 0), this, 6);
        b7 = new CButton(new Color(200, 0, 0), this, 7);
        b8 = new CButton(new Color(0, 0, 0), this, 8);

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
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                sound.setVolume(soundV);
                music.setVolume(musicV);
                dispose();
            }
        });

        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setColor(color);
                frame.setFullscreen(fullscreenBox.isSelected());
                writeConfig();
                dispose();
                repaint();
            }

        });

        musicSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                double v = (musicSlider.getValue() / 2) - 44;
                if(musicSlider.getValue() == 0){
                    v = -80;
                }
                music.setVolume(v);
            }
            
        });

        soundSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                double v = (soundSlider.getValue() / 2) - 44;
                if(soundSlider.getValue() == 0){
                    v = -80;
                }
                sound.setVolume(v);
            }
            
        });
        soundSlider.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {
                sound.play("ping.wav");
            }
            
        });

    }

    public void setColor(Color color, int index){
        this.color = color;
        okButton.setColor(color);
        c = index;
        if(index == 4){
            secret++;
            if(secret == 5){
                secret = 0;
                new AFrame(frame);
            }
        }else{
            secret = 0;
        }
        repaint();
    }

    public void writeConfig(){

        int full = 0;
        if(fullscreenBox.isSelected()){
            full = 1;
        }

        String string = soundSlider.getValue() + " " + musicSlider.getValue() + " " + full + " " + c;

        String jarDir = null;
        try {
            jarDir = frame.getJarContainingFolder(getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Path path = Path.of(jarDir + File.separator + "config.txt");

        File config = path.toFile();
        config.delete();

        try {
            // Create the file and write the content to it
            Files.write(path, string.getBytes(), StandardOpenOption.CREATE);

            System.out.println("Config file recreated successfully.");
        } catch (IOException e) {
            // Handle file creation error
            System.err.println("Error creating the file: " + e.getMessage());
        }

    }

}
