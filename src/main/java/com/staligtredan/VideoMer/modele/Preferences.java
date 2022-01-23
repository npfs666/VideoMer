package com.staligtredan.VideoMer.modele;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.staligtredan.VideoMer.update.modeleXml.Properties;

/**
 * Classe qui contient toutes les préférences du logiciel
 * 
 * @author Brendan
 * @version 0.16 18/04/2014
 * @version 1.0.0 21/01/2015 (MAJ regex pour le default et sqlite '\\' -> '\\\\')
 * @version 1.0.3 10/12/2016 Rajout du champ 'Taille Miniature'
 * @version 2.0   22/12/2016 Loging
 */
public class Preferences implements Cloneable {

	final static Logger logger = LogManager.getLogger(Preferences.class);
	
	/**
	 * Apparence de Java
	 */
	private String lookAndFeel;

	/**
	 * Gestion des fichiers relatif/absolu (fonctionnement clef usb, dd externe, etc)
	 */
	private boolean relatif;

	/**
	 * Surveiller les dossiers au démarrage
	 */
	private boolean surveillerDemarrage;

	/**
	 * Liste des extensions vidéos acceptés
	 */
	private String[] videoExtensions;
	
	/**
	 * Liste des regex à remplacer par un espace
	 */
	private String[] regexFileCleanSpace;
	
	/**
	 * Liste regex à remplacer par rien
	 */
	private String[] regexFileCleanBlank;
	
	/**
	 * Regex pour détecter les films
	 */
	private String[] regexMovie;
	
	/**
	 * Regex pour détecter les séries
	 */
	private String[] regexTvShow;
	
	/**
	 * Langue utilisateur
	 * 
	 */
	private String lang;

	/**
	 * Sauvegarde de la playlist
	 */
	private ArrayList<EntiteVideo> playList;

	/**
	 * Position du curseur de lecture
	 */
	private long currentVideoTime;

	/**
	 * Taille de la fenêtre
	 */
	private Dimension frameDimension;

	/**
	 * Position de la fenêtre
	 */
	private Point frameLocation;

	/**
	 * affichage playlist ?
	 */
	private boolean windowPlaylistShowed;

	/**
	 * Affichage information média ?
	 */
	private boolean windowDetailsShowed;

	/**
	 * Volume
	 */
	private int volume;

	/**
	 * Path mère des film
	 */
	private String moviePath;

	/**
	 * Path mère des séries
	 */
	private String tvShowPath;
	
	/**
	 * Taille des miniatures affichées
	 */
	private int thumbnailSize;


	public Object clone() {

		Object o = null;
		try {
			// On récupère l'instance à renvoyer par l'appel de la
			// méthode super.clone()
			o = super.clone();
		}
		catch ( CloneNotSupportedException cnse ) {
			// Ne devrait jamais arriver car nous implémentons
			// l'interface Cloneable
			//cnse.printStackTrace(System.err);
			logger.error("PB de clonage", cnse);
		}
		// on renvoie le clone
		return o;
	}



	public int getThumbnailSize() {
		return thumbnailSize;
	}



	public void setThumbnailSize(int thumbnailSize) {
		this.thumbnailSize = thumbnailSize;
	}



	/**
	 * @return the playList
	 */
	public ArrayList<EntiteVideo> getPlayList() {

		return playList;
	}



	/**
	 * @param playList
	 *            the playList to set
	 */
	public void setPlayList( ArrayList<EntiteVideo> playlist ) {

		this.playList = new ArrayList<EntiteVideo>(playlist);
	}



	/**
	 * @return the currentVideoTime
	 */
	public long getCurrentVideoTime() {

		return currentVideoTime;
	}



	/**
	 * @param currentVideoTime
	 *            the currentVideoTime to set
	 */
	public void setCurrentVideoTime( long currentVideoTime ) {

		this.currentVideoTime = currentVideoTime;
	}



	/**
	 * @return the frameDimension
	 */
	public Dimension getFrameDimension() {

		return frameDimension;
	}



	/**
	 * @param frameDimension
	 *            the frameDimension to set
	 */
	public void setFrameDimension( Dimension frameDimension ) {

		this.frameDimension = frameDimension;
	}



	/**
	 * @return the frameLocation
	 */
	public Point getFrameLocation() {

		return frameLocation;
	}



	/**
	 * @param frameLocation
	 *            the frameLocation to set
	 */
	public void setFrameLocation( Point frameLocation ) {

		this.frameLocation = frameLocation;
	}



	/**
	 * @return the windowPlaylistShowed
	 */
	public boolean isWindowPlaylistShowed() {

		return windowPlaylistShowed;
	}



	/**
	 * @param windowPlaylistShowed
	 *            the windowPlaylistShowed to set
	 */
	public void setWindowPlaylistShowed( boolean windowPlaylistShowed ) {

		this.windowPlaylistShowed = windowPlaylistShowed;
	}



	/**
	 * @return the windowDetailsShowed
	 */
	public boolean isWindowDetailsShowed() {

		return windowDetailsShowed;
	}



	/**
	 * @param windowDetailsShowed
	 *            the windowDetailsShowed to set
	 */
	public void setWindowDetailsShowed( boolean windowDetailsShowed ) {

		this.windowDetailsShowed = windowDetailsShowed;
	}



	/**
	 * @return the lang
	 */
	public String getLang() {

		return lang;
	}



	/**
	 * @return the lookAndFeel
	 */
	public String getLookAndFeel() {

		return lookAndFeel;
	}



	/**
	 * @return the regexFileCleanBlank
	 */
	public String[] getRegexFileCleanBlank() {

		return regexFileCleanBlank;
	}



	/**
	 * @return the regexFileCleanSpace
	 */
	public String[] getRegexFileCleanSpace() {

		return regexFileCleanSpace;
	}



	/**
	 * @return the regexMovie
	 */
	public String[] getRegexMovie() {

		return regexMovie;
	}



	/**
	 * @return the regexTvShow
	 */
	public String[] getRegexTvShow() {

		return regexTvShow;
	}



	/**
	 * @return the videoExtensions
	 */
	public String[] getVideoExtensions() {

		return videoExtensions;
	}



	/**
	 * @return the relatif
	 */
	public boolean isRelatif() {

		return relatif;
	}



	/**
	 * @return the surveillerDemarrage
	 */
	public boolean isSurveillerDemarrage() {

		return surveillerDemarrage;
	}



	/**
	 * @param lang
	 *            the lang to set
	 */
	public void setLang( String lang ) {

		this.lang = lang;
	}



	/**
	 * @param lookAndFeel
	 *            the lookAndFeel to set
	 */
	public void setLookAndFeel( String lookAndFeel ) {

		this.lookAndFeel = lookAndFeel;
	}



	/**
	 * @param regexFileCleanBlank
	 *            the regexFileCleanBlank to set
	 */
	public void setRegexFileCleanBlank( String[] regexFileCleanBlank ) {

		this.regexFileCleanBlank = regexFileCleanBlank;
	}



	/**
	 * @param regexFileCleanSpace
	 *            the regexFileCleanSpace to set
	 */
	public void setRegexFileCleanSpace( String[] regexFileCleanSpace ) {

		this.regexFileCleanSpace = regexFileCleanSpace;
	}



	/**
	 * @param regexMovie
	 *            the regexMovie to set
	 */
	public void setRegexMovie( String[] regexMovie ) {

		this.regexMovie = regexMovie;
	}



	/**
	 * @param regexTvShow
	 *            the regexTvShow to set
	 */
	public void setRegexTvShow( String[] regexTvShow ) {

		this.regexTvShow = regexTvShow;
	}



	/**
	 * @return the volume
	 */
	public int getVolume() {

		return volume;
	}



	/**
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume( int volume ) {

		this.volume = volume;
	}



	/**
	 * @param relatif
	 *            the relatif to set
	 */
	public void setRelatif( boolean relatif ) {

		this.relatif = relatif;
	}



	/**
	 * @param surveillerDemarrage
	 *            the surveillerDemarrage to set
	 */
	public void setSurveillerDemarrage( boolean surveillerDemarrage ) {

		this.surveillerDemarrage = surveillerDemarrage;
	}



	/**
	 * @param videoExtensions
	 *            the videoExtensions to set
	 */
	public void setVideoExtensions( String[] videoExtensions ) {

		this.videoExtensions = videoExtensions;
	}



	public String getMoviePath() {

		return moviePath;
	}



	public void setMoviePath( String moviePath ) {

		this.moviePath = moviePath;
	}



	public String getTvShowPath() {

		return tvShowPath;
	}



	public void setTvShowPath( String tvShowPath ) {

		this.tvShowPath = tvShowPath;
	}



	/**
	 * Renvoie des préférences par défaut
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Preferences DefaultPreferences() {

		Preferences p = new Preferences();
		p.lookAndFeel = "Windows";
		p.relatif = false;
		p.surveillerDemarrage = false;
		p.regexFileCleanSpace = new String[] { "[-_(){}@\\\\.]" };
		p.regexFileCleanBlank = new String[] { "xvid", "dvdrip", "repack", "ac3", "\\\\[.*\\\\]", "\\\\(.*\\\\)", "720p",
				"x264", "aac", "multi", "1080p", "hdrip", "bluray", "x265", "bdrip", "webrip", "hd" };
		p.regexMovie = new String[] { "(.+)cd[ ]*([0-9]+)", "(.+)" };
		p.regexTvShow = new String[] { "(.+)s([0-9]{1,2})e([0-9]{1,2})(.*)", "(.+)([0-9]{1,2})x([0-9]{1,2})(.*)", "(.+) saison ([0-9]{1,2}) ([0-9]{1,2})(.*)" };
		p.videoExtensions = new String[] { "avi", "mpg", "mkv", "mp4", "mpeg", "divx", "wmv", "m4v" };
		p.moviePath = "";
		p.tvShowPath = "";
		p.playList = new ArrayList<EntiteVideo>();
		p.currentVideoTime = 0;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		p.frameDimension = toolkit.getScreenSize();
		Point pt = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		p.frameLocation = new Point(pt.x - p.frameDimension.width / 2, pt.y - p.frameDimension.height / 2);
		p.windowPlaylistShowed = true;
		p.windowDetailsShowed = true;
		p.thumbnailSize = 2;
		//System.out.println("DefaultPreferences");

		Properties properties = new Properties();
		properties.loadFromFile();
		p.lang = properties.getLanguage();

		return p;
	}
}
