package com.Game;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class Sound implements LineListener{

    double volume = -10;

    Clip c;
    FloatControl control;

    public void play(String s) {
		try {
			URL url = this.getClass().getClassLoader().getResource(s);
			
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
		
		c = AudioSystem.getClip();
		c.addLineListener(new Sound());
		c.open(audioStream);
        control = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue((float)volume);
		c.start();
		}catch(Exception a) {a.printStackTrace();}
    }

    public void play(String s, boolean loop) {
		try {
			URL url = this.getClass().getClassLoader().getResource(s);
			
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
		
		c = AudioSystem.getClip();
		c.addLineListener(new Sound());
		c.open(audioStream);
        control = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue((float)volume);
        if(loop){
            c.loop(Clip.LOOP_CONTINUOUSLY);
        }
		c.start();
		}catch(Exception a) {a.printStackTrace();}
    }
    
    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
         
        if (type == LineEvent.Type.START) {
//          System.out.println("Playback started.");
        } else if (type == LineEvent.Type.STOP) {
            event.getLine().close();

        }
 
    }

    public void setVolume(double x){
        volume = x;
        control.setValue((float)volume);
    }

    public void presetVolume(double x){
        volume = x;
    }

    public double getVolume(){
        return volume;
    }
}
