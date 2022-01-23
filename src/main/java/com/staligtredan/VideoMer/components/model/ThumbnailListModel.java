package com.staligtredan.VideoMer.components.model;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

import com.staligtredan.VideoMer.modele.EntiteVideo;

/**
 * Modèle de données pour un <code>ThumbnailList</code>
 * 
 * @author Brendan
 * @version 0.1 6/07/2010
 */
public class ThumbnailListModel extends AbstractListModel<Object> {

	private static final long serialVersionUID = 6952130296955291169L;

	/**
	 * Données de la liste
	 */
	private ArrayList<EntiteVideo> data;



	/**
	 * Constructeur
	 */
	public ThumbnailListModel() {

		data = new ArrayList<EntiteVideo>();
	}



	/**
	 * Charge les données
	 * 
	 * @param videos
	 *            Eléments à afficher
	 */
	public void setData( ArrayList<EntiteVideo> videos ) {

		data = new ArrayList<EntiteVideo>(videos);
		fireContentsChanged(this, 0, data.size());
	}



	/**
	 * Enlève un élément de la liste
	 * 
	 * @param ev
	 *            EntiteVideo à enlever
	 */
	public void remove( EntiteVideo ev ) {

		data.remove(ev);
		fireContentsChanged(this, 0, data.size());
	}



	@Override
	public Object getElementAt( int index ) {

		return data.get(index);
	}



	@Override
	public int getSize() {

		return data.size();
	}
}
