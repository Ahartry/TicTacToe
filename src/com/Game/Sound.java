package com.Game;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class Sound implements LineListener{
	
	private static boolean soundEffects = true;
	private ExecutorService soundThreadPool = Executors.newFixedThreadPool(10);
    
    public static void setEffects(boolean b) {
    	soundEffects = b;
    }
    
    public static boolean getEffects() {
    	return soundEffects;
    }
//    new URL(this.getClass().getClassLoader().getResource("victory fanfare.wav"), null)
    
    public void play(String s) {
    	if(soundEffects) {
    		// Thread t = new Thread() {
    		// 		public void run() {
    		// 			try {
    		// 	        	URL url = this.getClass().getClassLoader().getResource(s);
    			        	
    		// 	            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
    			        
    		// 	        Clip c = AudioSystem.getClip();
    		// 	        c.addLineListener(new Sound());
    		// 	        c.open(audioStream);
    		// 	        c.start();
    		// 			}catch(Exception a) {a.printStackTrace();}
    		// 		}
    		// };
    		// t.start();
			soundThreadPool.submit(() -> {
				try {
					URL url = this.getClass().getClassLoader().getResource(s);
					
					AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
				
				Clip c = AudioSystem.getClip();
				c.addLineListener(new Sound());
				c.open(audioStream);
				c.start();
				}catch(Exception a) {a.printStackTrace();}
			});

    	}
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
