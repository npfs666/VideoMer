package com.staligtredan.VideoMer.modele;

import java.util.ArrayList;

/**
 * D�fini le mod�le de S�rie
 * 
 * @author Brendan
 * @version 0.1 22/06/2010
 */
public class Serie extends EntiteVideo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 801835200939702609L;

	/**
	 * Guests Star
	 */
	private ArrayList<String> invites;
	
	/**
	 * N� de l'�pisode
	 */
	private int noEpisode;
	
	/**
	 * Nom sp�cifique de l'�pisode
	 */
	private String nomEpisode; 
	
	/**
	 * N� de la saison
	 */
	private int noSaison;
	
	
	
	public Serie() {
		
		super();
		
		nomEpisode = "";
		noSaison = 0;
		noEpisode = 0;
		invites = new ArrayList<String>();
		setCategorie(SERIE);
	}

	/**
	 * @return the invites
	 */
	public ArrayList<String> getInvites() {
		return invites;
	}

	/**
	 * @return the noEpisode
	 */
	public int getNoEpisode() {
		return noEpisode;
	}

	/**
	 * @return the nomEpisode
	 */
	public String getNomEpisode() {
		return nomEpisode;
	}

	/**
	 * @return the noSaison
	 */
	public int getNoSaison() {
		return noSaison;
	}

	/**
	 * @param invites the invites to set
	 */
	public void setInvites(ArrayList<String> invites) {
		this.invites = invites;
	}

	/**
	 * @param noEpisode the noEpisode to set
	 */
	public void setNoEpisode(int noEpisode) {
		this.noEpisode = noEpisode;
	}

	/**
	 * @param nomEpisode the nomEpisode to set
	 */
	public void setNomEpisode(String nomEpisode) {
		this.nomEpisode = nomEpisode;
	}

	/**
	 * @param noSaison the noSaison to set
	 */
	public void setNoSaison(int noSaison) {
		this.noSaison = noSaison;
	}
	
	@Override
	public int compareTo(EntiteVideo o) {
		
		// Comparaison Serie VS Film
		if( o.getClass() == Film.class )
			return nom.compareToIgnoreCase(o.getNom());
		
		// Comparaison Serie VS Serie (prio saison puis �pisode)
		Serie s = (Serie)o;
		if( nom.compareToIgnoreCase(s.getNom()) == 0) {
			if( noSaison == s.getNoSaison() )
				return noEpisode - s.noEpisode;
			else
				return noSaison - s.noSaison;
		} else 		
			return nom.compareToIgnoreCase(s.getNom());
	}
}
