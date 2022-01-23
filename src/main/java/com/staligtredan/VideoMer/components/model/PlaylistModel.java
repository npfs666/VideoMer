package com.staligtredan.VideoMer.components.model;

import java.util.ArrayList;
import java.util.Formatter;

import javax.swing.AbstractListModel;

import com.staligtredan.VideoMer.modele.EntiteVideo;
import com.staligtredan.VideoMer.modele.Film;
import com.staligtredan.VideoMer.modele.Serie;

/**
 * Modèle de données pour la playlist
 * 
 * @author Brendan
 * @version 0.1 6/07/2010
 */
public class PlaylistModel extends AbstractListModel<Object> {

	private static final long serialVersionUID = 5943221906832816414L;

	/**
	 * Liste des éléments dans la playlist
	 */
	private ArrayList<EntiteVideo> playlist;



	public PlaylistModel( ArrayList<EntiteVideo> playlist ) {

		if( playlist != null )
			this.playlist = playlist;
		else
			playlist = new ArrayList<EntiteVideo>();

	}



	/**
	 * Ajoute des éléments � la fin
	 * 
	 * @param videos
	 */
	public void add( ArrayList<EntiteVideo> videos ) {

		playlist.addAll(videos);
		fireContentsChanged(this, 0, playlist.size());
	}



	/**
	 * Ajoute des éléments à l'endroit spécifié
	 * 
	 * @param index
	 *            position
	 * @param videos
	 */
	public void add( int index, ArrayList<EntiteVideo> videos ) {

		playlist.addAll(index, videos);
		fireContentsChanged(this, index, playlist.size());
	}



	/**
	 * Vide la playlist
	 */
	public void clear() {

		playlist.clear();
		fireContentsChanged(this, 0, playlist.size());
	}



	/**
	 * El�ve l'élément à l'index désigné
	 * 
	 * @param index
	 */
	public void remove( int index ) {

		playlist.remove(index);
		fireContentsChanged(this, 0, playlist.size());
	}



	/**
	 * Renvoie la playlist actuelle
	 * 
	 * @return
	 */
	public ArrayList<EntiteVideo> getPlaylist() {

		return playlist;
	}



	/**
	 * Renvoie l'<code>EntiteVideo</code> se trouvant � l'index
	 * 
	 * @param index
	 * @return
	 */
	public EntiteVideo getElement( int index ) {

		if( index < playlist.size() )
			return playlist.get(index);
		else
			return null;
	}



	@Override
	public int getSize() {

		return playlist.size();
	}



	@Override
	public Object getElementAt( int index ) {

		String tmp = new String();

		if( playlist.get(index).getClass() == Serie.class ) {

			Serie s = (Serie) playlist.get(index);
			Formatter fSaison = new Formatter(new StringBuilder());
			Formatter fEpisode = new Formatter(new StringBuilder());
			fSaison.format("%02d", s.getNoSaison());
			fEpisode.format("%02d", s.getNoEpisode());
			tmp = " S" + fSaison + "E" + fEpisode;

			fSaison.close();
			fEpisode.close();

		} else if( ((Film) playlist.get(index)).getNumSupport() != 0 )
			tmp = " CD" + ((Film) playlist.get(index)).getNumSupport();

		return playlist.get(index).getNom() + tmp;

	}

}
