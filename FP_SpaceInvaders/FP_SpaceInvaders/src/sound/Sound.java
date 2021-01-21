package sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	private Clip clip;
	
	public Sound(String soundFileName) {
		this.setFile(soundFileName);
	}

	public void setFile(String soundFileName){
		try{
			File file = new File(soundFileName);
			AudioInputStream sound = AudioSystem.getAudioInputStream(file);	
			this.clip = AudioSystem.getClip();
			this.clip.open(sound);
		}
		catch(Exception e){
		}
	}
	
	public void play(){
		clip.setFramePosition(0);
		clip.start();
	}
}