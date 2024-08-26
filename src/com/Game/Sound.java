package com.Game;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class Sound implements LineListener{
	
    private static boolean soundEffects = true;
    
//    public static final Clip victory = loadSound("victory fanfare.wav");
//    public static final Clip clicked = loadSound("ping.wav");
//    public static final Clip kazoo = loadSound("two kazoo fanfare.wav");
//    public static final Clip used = loadSound("wet-click.wav");
//    public static final Clip pickUp = loadSound("Dealing Card.wav");
//    public static final Clip removed = loadSound("ui confirmation alert-a1.wav");
//    
//    private static Clip loadSound(String s) {
//    	try {
//    	InputStream in = Sound.class.getResourceAsStream(s);
//    	
//        AudioInputStream audioStream = AudioSystem.getAudioInputStream(in);
//        
//        Clip c = AudioSystem.getClip();
//        c.addLineListener(new Sound());
//        c.open(audioStream);
//        return c;
//    	}catch(Exception a) {
//    		System.out.println("There was an error loading the audio file");
//    	}
//    	return null;
//    }
    
    public static void setEffects(boolean b) {
    	soundEffects = b;
    }
    
    public static boolean getEffects() {
    	return soundEffects;
    }
//    new URL(this.getClass().getClassLoader().getResource("victory fanfare.wav"), null)
    
    public void play(String s) {
    	if(soundEffects) {
    		Thread t = new Thread() {
    				public void run() {
    					try {
    			        	URL url = this.getClass().getClassLoader().getResource(s);
    			        	
    			            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
    			        
    			        Clip c = AudioSystem.getClip();
    			        c.addLineListener(new Sound());
    			        c.open(audioStream);
    			        c.start();
    					}catch(Exception a) {a.printStackTrace();}
    				}
    		};
    		t.start();
    	}
    }
    
    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
         
        if (type == LineEvent.Type.START) {
//            System.out.println("Playback started.");
        } else if (type == LineEvent.Type.STOP) {
                	event.getLine().close();
//                    System.out.println("Playback completed.");

        }
 
    }
}
