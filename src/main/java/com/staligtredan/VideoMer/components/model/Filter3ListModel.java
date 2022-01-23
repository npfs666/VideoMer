package com.staligtredan.VideoMer.components.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import com.staligtredan.VideoMer.modele.EntiteVideo;
import com.staligtredan.VideoMer.modele.Serie;

/**
 * Modèle de données pour la FiltreListe #3
 * 
 * @author Brendan
 * @version 0.1 6/07/2010
 */
public class Filter3ListModel extends Filter1ListModel {

	private static final long serialVersionUID = -984583597209112146L;

	private ResourceBundle bundle;



	/**
	 * Crée un modèle avec un filtre
	 * 
	 * @param viewFilter
	 *            Filtre de la liste (genre, acteur, pays, ...)
	 */
	public Filter3ListModel( int viewFilter, ResourceBundle bundle ) {

		super(viewFilter);
		this.bundle = bundle;
	}



	/**
	 * Met à jour les données
	 * 
	 * @param index
	 *            catégorie (filtreListe1)
	 * @param videos
	 *            liste des élément correspondants
	 */
	public void filter( int index, ArrayList<EntiteVideo> videos ) {

		view = new ArrayList<String>();
		int i = 0;
		int serieFilter;

		// Si série => affichage spécial
		if( index == EntiteVideo.SERIE )
			serieFilter = SERIE;
		else
			serieFilter = viewFilter;

		// On fais le tri dans les données (suppression des doublons)
		switch ( serieFilter ) {

		case GENRE:

			for ( EntiteVideo ev : videos ) {
				for ( String s : ev.getGenre() ) {
					if( !s.trim().isEmpty() && !view.contains(s) ) {
						view.add(s);
						i++;
					}
				}
			}
			break;

		case SERIE:

			for ( EntiteVideo ev : videos ) {

				String s = "Saison " + ((Serie) ev).getNoSaison();

				if( !view.contains(s) ) {
					view.add(s);
					i++;
				}
			}
			break;

		case REALISATEUR:

			for ( EntiteVideo ev : videos ) {
				for ( String s : ev.getRealisateurs() ) {
					if( !s.trim().isEmpty() && !view.contains(s) ) {
						view.add(s);
						i++;
					}
				}
			}
			break;

		case PAYS:

			for ( EntiteVideo ev : videos ) {

				if( !ev.getPays().isEmpty() ) {
					for ( String s : ev.getPays() ) {
						if( !s.trim().isEmpty() && !view.contains(s) ) {
							view.add(s);
							i++;
						}
					}
				}

			}
			break;

		case ACTEURS:

			for ( EntiteVideo ev : videos ) {

				if( !ev.getActeurs().isEmpty() ) {
					for ( String s : ev.getActeurs() ) {
						if( !s.trim().isEmpty() && !view.contains(s) ) {
							view.add(s);
							i++;
						}
					}
				}

			}
			break;
		}

		Collections.sort(view);
		view.add(0, bundle.getString("EcrPrincipal.strFilterAll.text").replaceAll("XX", Integer.toString(i)));
		fireContentsChanged(this, 0, view.size());
	}
}
