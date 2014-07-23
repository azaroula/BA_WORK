package GHCModel;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;

public class SoundPlayer implements Runnable{
	
	private AudioClip sound;
	private File file;
	
	public SoundPlayer() throws MalformedURLException {
		file = new File( "sounds\\bloop_x.wav" );
		sound = Applet.newAudioClip( file.toURI().toURL() );
				
	}

	@Override
	public void run() {
		sound.play();
	}
}