package com.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class SFrame extends JFrame{

    boolean clicked = false;
    
    public SFrame(GFrame frame, boolean bot, int depth, int game){

        Font font = new Font(null);
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("font.ttf");
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        //set name based on other folders
        String path = System.getProperty("user.dir") + File.separator + "Saves" + File.separator + Integer.toString(game);
        File file = new File(path);
        
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        ArrayList<String> saveList = new ArrayList<String>(Arrays.asList(directories));
        int autoTally = 0;
        for(int i = 0; i < saveList.size(); i++){
            //bootleg solution
            if(saveList.get(i).startsWith("Game ") && Character.isDigit(saveList.get(i).charAt(saveList.get(i).length()-1))){
                autoTally++;
            }
        }
        //sets the new folder name
        autoTally++;

        JTextField nameField = new JTextField(10);
        nameField.setText("Game " + autoTally);
        nameField.setForeground(new Color(150, 150, 150));

        GButton okButton = new GButton("Start");
        okButton.setFont(font);

        nameField.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(!clicked){
                    setFocusable(true);
                    clicked = true;
                    nameField.setText("");
                    repaint();
                    revalidate();
                    nameField.setForeground(Color.BLACK);
                }           
            }
        });

        Dimension d = okButton.getPreferredSize();
        d.width = 120;
        okButton.setPreferredSize(d); 

        nameField.setFont(font);

        JLabel label = new JLabel("Enter game name for autosaves");
        label.setFont(font);

        gbc.gridy = 0;
        add(label, gbc);

        gbc.gridy = 1;
        add(nameField, gbc);

        gbc.gridy = 2;
        add(Box.createRigidArea(new Dimension(0, 30)), gbc);

        gbc.gridy = 3;
        add(okButton, gbc);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 200);
        setLocationRelativeTo(null);
        setTitle("Set Game Name");
        setVisible(true);
        setResizable(false);

        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String output = file.toString() + File.separator + nameField.getText();
                File outputFile = new File(output);
                File move0 = new File(output + File.separator + "move0");

                try {
                    dispose();
                    new File(output).mkdirs();
                    move0.createNewFile();
                    frame.setupGame(3, bot);
                    frame.setDepth(depth);
                    frame.getGPanel().setOutputDir(outputFile);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        });

        nameField.getDocument().addDocumentListener(new DocumentListener(){
    
            public void changedUpdate(DocumentEvent arg0){
                editCheck(arg0);
            }

            public void insertUpdate(DocumentEvent arg0){
                editCheck(arg0);
            }

            public void removeUpdate(DocumentEvent arg0){
                editCheck(arg0);
            }

            public void editCheck(DocumentEvent arg0){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if(!clicked){
                            setFocusable(true);
                            clicked = true;
                            try {
                                nameField.setText(arg0.getDocument().getText(nameField.getText().length() - 1, 1));
                            } catch (BadLocationException e) {
                                e.printStackTrace();
                            }
                            repaint();
                            revalidate();
                            nameField.setForeground(Color.BLACK);
                        }
                    }
                });
            }
        });
    }
}
