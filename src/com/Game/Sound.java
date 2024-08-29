package com.Game;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class Sound implements LineListener{
	

    public void play(String s) {
		try {
			URL url = this.getClass().getClassLoader().getResource(s);
			
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
		
		Clip c = AudioSystem.getClip();
		c.addLineListener(new Sound());
		c.open(audioStream);
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
}
