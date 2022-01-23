package com.staligtredan.VideoMer.components.model;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

/**
 * Modèle de données pour la FiltreListe #1
 * 
 * @author Brendan
 * @version 0.1 6/07/2010
 */
public abstract class Filter1ListModel extends AbstractListModel<Object> {

	private static final long serialVersionUID = -7084687591321850457L;

	/**
	 * Liste des tris disponible à l'utilisateur
	 */
	public final static int GENRE = 0;
	public final static int REALISATEUR = 1;
	public final static int PAYS = 2;
	public final static int ACTEURS = 3; 
	public final static int SERIE = 4;

	/**
	 * Elements à afficher
	 */
	protected ArrayList<String> view;

	/**
	 * Filtre de la liste (genre, acteur, pays, ...)
	 */
	protected int viewFilter;



	/**
	 * Cr�e un modèle avec un filtre
	 * 
	 * @param viewFilter
	 *            Filtre de la liste (genre, acteur, pays, ...)
	 */
	public Filter1ListModel( int viewFilter ) {

		this.viewFilter = viewFilter;
		view = new ArrayList<String>();
	}



	@Override
	public Object getElementAt( int index ) {

		return view.get(index);
	}



	@Override
	public int getSize() {

		return view.size();
	}



	/**
	 * @return the viewFilter
	 */
	public int getViewFilter() {

		return viewFilter;
	}



	/**
	 * @param viewFilter
	 *            the viewFilter to set
	 */
	public void setViewFilter( int viewFilter ) {

		this.viewFilter = viewFilter;
	}
}
