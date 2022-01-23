package com.staligtredan.VideoMer.components.model;

/**
 * Classe qui modélise un élément d'une comboBox qui représente les différents
 * choix de films, mais qui conserve son URL.
 * 
 * @author Brendan Jaouen
 * @version 0.1 04/09/2010
 */
public class ResultComboBoxModel {

	/**
	 * Libéllé du résultat
	 */
	private String libelle;

	/**
	 * URL du résultat
	 */
	private String url;



	/**
	 * Constructeur qui crée un choix vide
	 */
	public ResultComboBoxModel() {

		libelle = "";
		url = "";
	}



	@Override
	public String toString() {

		return libelle;
	}



	/**
	 * Est ce que la liste est vide
	 * 
	 * @return
	 */
	public boolean isEmpty() {

		if( libelle.isEmpty() || url.isEmpty() )
			return true;
		else
			return false;
	}



	/**
	 * @return the libelle
	 */
	public String getLibelle() {

		return libelle;
	}



	/**
	 * @param libelle
	 *            the libelle to set
	 */
	public void setLibelle( String libelle ) {

		this.libelle = libelle;
	}



	/**
	 * @return the url
	 */
	public String getUrl() {

		return url;
	}



	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl( String url ) {

		this.url = url;
	}
}