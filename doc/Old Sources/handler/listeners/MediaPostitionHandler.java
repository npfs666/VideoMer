package handler.listeners;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * <code>MediaPostitionHandler</code> permet de gérer les événements
 * du curseur de lecture
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
public class MediaPostitionHandler implements ChangeListener {

	private MediaPlayer mediaPlayer;
	
	/**
	 * Constructeur
	 */
	public MediaPostitionHandler(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider)e.getSource();
		
		if( mediaPlayer != null && mediaPlayer.isPlaying() ) {
			mediaPlayer.setPosition((float)(slider.getValue()/(double)slider.getMaximum()));
		}
	}
}
