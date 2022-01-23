package com.staligtredan.VideoMer.handler.listeners.jframeprincipal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import org.jdesktop.swingx.JXList;

import com.staligtredan.VideoMer.components.model.PlaylistModel;

/**
 * Défini une instance pour écouter les frappes clavier
 * de la playlist
 * 
 * @author Brendan
 *
 */
public class PlaylistHandler implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		PlaylistModel pm = (PlaylistModel)((JXList)e.getSource()).getModel();
		JXList playlist = (JXList)e.getSource();		
		
		ArrayList<Integer> al = new ArrayList<Integer>();
		
		if( e.getKeyCode() == KeyEvent.VK_DELETE ) {

			for( int i  : playlist.getSelectedIndices() )
				al.add(i);
			
			for( int i = pm.getSize()-1; i >= 0; i-- ) {
				
				if( al.contains(i) )
					pm.remove(i);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
