package com.staligtredan.VideoMer.handler.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXList;

import com.staligtredan.VideoMer.components.ThumbnailList;
import com.staligtredan.VideoMer.components.model.PlaylistModel;
import com.staligtredan.VideoMer.components.renderer.JPanelDetailsEntiteVideo;
import com.staligtredan.VideoMer.modele.EntiteVideo;
import com.staligtredan.VideoMer.modele.Serie;
import com.staligtredan.VideoMer.vue.JFramePrincipal;

/**
 * <code>ThumbnailHandler</code> permet de gérer les événements
 * du ThumbnailList
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
public class ThumbnailHandler extends MouseAdapter implements ListSelectionListener, KeyListener {
	
	/**
	 * La playlist
	 */
	private JXList playlist;
	
	/**
	 * Le panneau d'affichage des détails
	 */
	private JPanelDetailsEntiteVideo detailsVideo;
	
	private JFramePrincipal mainFrame;
	
	
	
	/**
	 * Constructeur
	 */
	public ThumbnailHandler(JFramePrincipal mainFrame, JXList playlist, JPanelDetailsEntiteVideo detailsVideo) {
		this.mainFrame = mainFrame;
		this.playlist = playlist;
		this.detailsVideo = detailsVideo;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		ThumbnailList list = (ThumbnailList)e.getSource();
		EntiteVideo ev = (EntiteVideo)list.getSelectedValue();
		
		if( ev == null )
			return;
		
		//mainFrame.setDetailsVisible();
		
		// Mise à jour de la partie informations sur le média
		detailsVideo.setRealisateurs(ev.getRealisateurs());
		detailsVideo.setActeurs(ev.getActeurs());
		detailsVideo.setNationalite(ev.getPays());
		detailsVideo.setGenres(ev.getGenre());
		detailsVideo.setPlaytime(ev.getPlaytime());
		detailsVideo.setPath(ev.getEmplacement());
		detailsVideo.setDate(ev.getDate());
		detailsVideo.setSynopsis(ev.getSynopsis());
		
		// Afficher le chainon dans l'ecr principal
		if( ev.getCategorie() == EntiteVideo.SERIE ) {
			Serie s = (Serie) ev;
			// TODO INternationalizer
			mainFrame.setTitlePanelInformation(ev.getNom() + " >> " + " Saison " + s.getNoSaison() + " >> " + s.getNomEpisode());
		} else 
			mainFrame.setTitlePanelInformation(ev.getNom());		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {		
		
		ThumbnailList list = (ThumbnailList)e.getSource();
		
		// Si double clic -> ajout à la playlist
		if( !list.isSelectionEmpty() && e.getClickCount() >= 2 ) {
			
			PlaylistModel playlistModel = (PlaylistModel)playlist.getModel();
			
			// On ajoute l'élément cliqué
			ArrayList<EntiteVideo> videos = new ArrayList<EntiteVideo>();
			videos.add((EntiteVideo)list.getSelectedValue());
			playlistModel.add(videos);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		
		ThumbnailList list = (ThumbnailList)e.getSource();
		
		if( e.getKeyCode() == KeyEvent.VK_ENTER && !list.isSelectionEmpty() ) {
			
			PlaylistModel playlistModel = (PlaylistModel)playlist.getModel();
			
			// On vide la liste actuelle
			playlistModel.clear();
			
			// On ajoute l'élément cliqué
			ArrayList<EntiteVideo> videos = new ArrayList<EntiteVideo>();
			for( Object o : list.getSelectedValues() )
				videos.add((EntiteVideo)o);
			playlistModel.add(videos);
			
			// MAJ de l'affichage
			playlist.updateUI();
			playlist.setSelectedIndex(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
