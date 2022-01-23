package handler.listeners;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * <code>VolumeHandler</code> permet de gérer les événements
 * du volume
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
public class VolumeHandler implements MouseWheelListener, ChangeListener {

	private MediaPlayer mediaPlayer;
	
	/**
	 * Constructeur
	 */
	public VolumeHandler(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		JSlider slider = (JSlider)e.getSource();
		slider.setValue(slider.getValue()-5*e.getWheelRotation());
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider)e.getSource();
		mediaPlayer.setVolume(slider.getValue());
	}
}
