package com.staligtredan.VideoMer.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * <code>EntiteVideo</code> réprésente tout fichier vidéo
 * 
 * @author Brendan
 * @version 0.1.0 22/06/2010
 * @version 1.0.0 20/01/2015 (Ajout du membre id)
 */
public class EntiteVideo implements Serializable, Comparable<EntiteVideo> {
	
	
	/**
	 * Identifiant unique pour associer les objets avec la BDD sqlite
	 */
	public int id = -1;

	private static final long serialVersionUID = -2096288718929874176L;

	/**
	 * Catégories
	 */
	public static final int DOCUMENTAIRE	= 1;
	public static final int FILM			= 2;	
	public static final int AUTRE			= 3;
	public static final int SERIE			= 4;
	
	/**
	 * Liste des acteurs
	 */
	protected ArrayList<String> acteurs;
	
	/**
	 * URL de l'affichette
	 */
	protected String affichette;
	
	/**
	 * Catégorie (film, docu, série, ...)
	 */
	protected int categorie;
	
	/**
	 * Année de production/sortie
	 */
	protected String date;
	
	/**
	 * Date de prêt
	 */
	protected Date datePret;
	
	/**
	 * Chemin relatif/absolu du stockage
	 */
	protected String emplacement;
	
	/**
	 * Liste des genre auquel appartiens le film
	 */
	protected ArrayList<String> genre;
	
	/**
	 * Informations complémentaires de l'utilisateur
	 */
	protected String infosUtilisateur;
	
	/**
	 * Nom de l'entité vidéo
	 */
	protected String nom;
	
	/**
	 * Liste des pays
	 */
	protected ArrayList<String> pays;
	
	/**
	 * Personne concernée par le prêt
	 */
	protected String personnePret;

	/**
	 * Liste des réalisateurs
	 */
	protected ArrayList<String> realisateurs;

	/**
	 * Support de stockage
	 */
	protected int support;
	
	/**
	 * Résumé
	 */
	protected String synopsis;
	
	/**
	 * Titre original
	 */
	protected String originalName;
	
	/**
	 * Durée de l'elt
	 */
	protected String playtime;
	
	
	
	/**
	 * Crée une nouvelle entité vidéo
	 */
	public EntiteVideo() {

		acteurs = new ArrayList<String>();
		affichette = "";
		nom = "";
		date = "";
		realisateurs = new ArrayList<String>();
		pays = new ArrayList<String>();
		genre = new ArrayList<String>();
		synopsis = "";
		infosUtilisateur = "";
		support = 0;
		emplacement = "";
		datePret = null;
		personnePret = "";
		originalName = "";
		playtime = "";
		categorie = 0;
	}

	/**
	 * @return the acteurs
	 */
	public ArrayList<String> getActeurs() {
		return acteurs;
	}

	/**
	 * @return the affichette
	 */
	public String getAffichette() {
		return affichette;
	}

	/**
	 * @return the categorie
	 */
	public int getCategorie() {
		return categorie;
	}
	
	/**
	 * @return the annee
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @return the datePret
	 */
	public Date getDatePret() {
		return datePret;
	}
	
	/**
	 * @return the emplacement
	 */
	public String getEmplacement() {
		return emplacement;
	}
	
	/**
	 * @return the genre
	 */
	public ArrayList<String> getGenre() {
		return genre;
	}

	/**
	 * @return the infosUtilisateur
	 */
	public String getInfosUtilisateur() {
		return infosUtilisateur;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	public ArrayList<String> getPays() {
		return pays;
	}

	/**
	 * @return the personnePret
	 */
	public String getPersonnePret() {
		return personnePret;
	}

	/**
	 * @return the realisateur
	 */
	public ArrayList<String> getRealisateurs() {
		return realisateurs;
	}

	/**
	 * @return the support
	 */
	public int getSupport() {
		return support;
	}

	/**
	 * @return the synopsis
	 */
	public String getSynopsis() {
		return synopsis;
	}

	/**
	 * @param acteurs the acteurs to set
	 */
	public void setActeurs(ArrayList<String> acteurs) {
		this.acteurs = acteurs;
	}

	/**
	 * @param affichette the affichette to set
	 */
	public void setAffichette(String affichette) {
		this.affichette = affichette;
	}

	/**
	 * @param categorie the categorie to set
	 */
	public void setCategorie(int categorie) {
		this.categorie = categorie;
	}

	/**
	 * @param annee the annee to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @param datePret the datePret to set
	 */
	public void setDatePret(Date datePret) {
		this.datePret = datePret;
	}

	/**
	 * @param emplacement the emplacement to set
	 */
	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}

	/**
	 * @param genre the genre to set
	 */
	public void setGenre(ArrayList<String> genre) {
		this.genre = genre;
	}

	/**
	 * @param infosUtilisateur the infosUtilisateur to set
	 */
	public void setInfosUtilisateur(String infosUtilisateur) {
		this.infosUtilisateur = infosUtilisateur;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPays(ArrayList<String> pays) {
		this.pays = pays;
	}

	/**
	 * @param personnePret the personnePret to set
	 */
	public void setPersonnePret(String personnePret) {
		this.personnePret = personnePret;
	}

	/**
	 * @param realisateur the realisateur to set
	 */
	public void setRealisateurs(ArrayList<String> realisateur) {
		this.realisateurs = realisateur;
	}

	/**
	 * @param support the support to set
	 */
	public void setSupport(int support) {
		this.support = support;
	}

	/**
	 * @param synopsis the synopsis to set
	 */
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	/**
	 * @return the originalName
	 */
	public String getOriginalName() {
		return originalName;
	}

	/**
	 * @param originalName the originalName to set
	 */
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	/**
	 * @return the playtime
	 */
	public String getPlaytime() {
		return playtime;
	}

	/**
	 * @param playtime the playtime to set
	 */
	public void setPlaytime(String playtime) {
		this.playtime = playtime;
	}

	@Override
	public int compareTo(EntiteVideo o) {
		return nom.compareToIgnoreCase(o.nom);
	}
}
