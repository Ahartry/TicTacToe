package com.Game;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AFrame extends JFrame{

    JFileChooser chooser;
    Image ximg;
    Image oimg;
    JLabel xlabel;
    JLabel olabel;
    GFrame frame;

    public AFrame(GFrame frame){ 
        this.frame = frame;       
        ximg = frame.getxImage();
        oimg = frame.getoImage();
        JPanel panel = new JPanel(new GridBagLayout());
        xlabel = new JLabel();
        olabel = new JLabel();

        xlabel.setIcon(new ImageIcon(ximg.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH)));
        xlabel.setPreferredSize(new Dimension(128, 128));

        olabel.setIcon(new ImageIcon(oimg.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH)));
        olabel.setPreferredSize(new Dimension(128, 128));

        JButton xbutton = new JButton("x icon");
        JButton obutton = new JButton("o icon");

        chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Image Files", ImageIO.getReaderFileSuffixes()));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(xlabel, gbc);
        gbc.gridx = 1;
        panel.add(olabel, gbc);
        gbc.gridy = 1;
        panel.add(obutton, gbc);
        gbc.gridx = 0;
        panel.add(xbutton, gbc);

        add(panel);

        setSize(440, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Super secret menu of absolute importance that only the select few can ever hope to place their eyes upon, and as such they will never know the answer to Life, the Universe, and Everything");
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        xbutton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Image img = null;
                int returnValue = chooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    try{
                        img = ImageIO.read(selectedFile);
                        ximg = img;
                        frame.setImageX(img);
                        updateLabel(xlabel, img);
                    }catch (Exception ex){
                        System.out.println("Failed to load");
                    }
                }
                
            }

        });

        obutton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Image img = null;
                int returnValue = chooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    try{
                        img = ImageIO.read(selectedFile);
                        oimg = img;
                        frame.setImageO(img);
                        updateLabel(olabel, img);
                    }catch (Exception ex){
                        System.out.println("Failed to load");
                    }
                }
                
            }

        });

    }

    public void updateLabel(JLabel l, Image i){
        l.setIcon(new ImageIcon(i.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH)));
    }

}
