package com.staligtredan.VideoMer.modele;

/**
 * Défini le modèle de Film
 * 
 * @author Brendan
 * @version 0.1 22/06/2010
 */
public class Film extends EntiteVideo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4533534546677339961L;
	
	/**
	 * Numéro du support (CD1, CD2, ...)
	 */
	private int numSupport;
	
	
	
	/**
	 * Crée une instance de Film
	 */
	public Film() {
		super();
		setCategorie(FILM);
		numSupport = 0;
	}

	/**
	 * @return the numSupport
	 */
	public int getNumSupport() {
		return numSupport;
	}

	/**
	 * @param numSupport the numSupport to set
	 */
	public void setNumSupport(int numSupport) {
		this.numSupport = numSupport;
	}
	
	@Override
	public int compareTo(EntiteVideo o) {
		return nom.compareToIgnoreCase(o.nom);
	}
}
