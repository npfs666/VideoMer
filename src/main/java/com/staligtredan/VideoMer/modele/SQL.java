package com.staligtredan.VideoMer.modele;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Permet de connecter la partie ArrayList<EntiteVideos> avec la base de données
 * SQLite
 * 
 * @author Brendan
 * @since 1.0.0 20/01/2015
 * @version 1.0.0 20/01/2015 (Création)
 * @version 1.0.2 1/02/2015 test pour arranger le nom de pays (éviter les doublons)
 * 				  12/02/2015 Correction du dérangement de la playlist due à la recherche sql
 * @version 1.0.3 10/12/2016 Rework de la table PREF (id, key, value)
 * 			     			 Et rajout du champ thumbnail
 * @version 2.0.0 22/12/2016 Loging et gestion interne des erreurs
 */
public class SQL {

	final static Logger logger = LogManager.getLogger(SQL.class);
	
	/**
	 * Chemin vers le fichier SQLite
	 */
	private String DBPath = "Chemin aux base de donnée SQLite";

	private Connection connection = null;

	private Statement statement = null;



	public SQL(String dBPath) {
		DBPath = dBPath;
	}



	/**
	 * Connection à la BDD
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void connect() {

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
			statement = connection.createStatement();
		} catch ( ClassNotFoundException e ) {
			logger.error("org.sqlite.JDBC non trouvé, pb de librairie sql", e);
		} catch ( SQLException e ) {
			logger.error("Impossible de se connecter à la base sql", e);
		}
		
	}



	/**
	 * Fermeture de l'acces BDD
	 */
	public void close() {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			logger.error("Pb lors de la fermeture de la bdd", e);
		}
	}



	/**
	 * Création des tables SQL
	 * 
	 * @throws SQLException
	 */
	public void createTables() {

		logger.info("Création des tables");

		try {
			statement
					.executeUpdate("CREATE TABLE `EV` (	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,	`acteurs`	VARCHAR(100),	`affichette`	VARCHAR(255),	`categorie`	INT,	`date`	VARCHAR(50),	`datePret`	VARCHAR(50),	`emplacement`	VARCHAR(100),	`genre`	VARCHAR(100),	`infosUtilisateur`	VARCHAR(255),	`nom`	VARCHAR(100),	`pays`	VARCHAR(100),	`personnePret`	VARCHAR(100),	`realisateurs`	VARCHAR(100),	`support`	INT,	`synopsis`	VARCHAR(500),	`originalName`	VARCHAR(100),	`playtime`	VARCHAR(100),	`invites`	VARCHAR(100),	`noEpisode`	INT,	`nomEpisode`	VARCHAR(100),	`noSaison`	INT,	`numSupport`	INT);");
		} catch ( SQLException e ) {
			logger.error("Impossible de créer la table EV", e);
		}
		
		try {
			statement
					.executeUpdate("CREATE TABLE `PREF` (	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,	`lookAndFeel`	VARCHAR(100),	`relatif`	INT,	`surveillerDemarrage`	INT,	`videoExtensions`	VARCHAR(100),	`regexFileCleanSpace`	VARCHAR(100),	`regexFileCleanBlank`	VARCHAR(100),	`regexMovie`	VARCHAR(500),	`regexTvShow`	VARCHAR(500),	`lang`	VARCHAR(20),	`playList`	VARCHAR(100),	`currentVideoTime`	VARCHAR(20),	`frameDimension`	VARCHAR(10),	`frameLocation`	VARCHAR(10),	`windowPlaylistShowed`	INT,	`windowDetailsShowed`	INT,	`volume`	INT,	`moviePath`	VARCHAR(100),	`tvShowPath`	VARCHAR(100));");
		} catch ( SQLException e ) {
			logger.error("Impossible de créer la table pref", e);
		}
	}
	
	
	
	/**
	 * Crée la table pour le nouveay système de préférences
	 */
	public void createPrefN() {
		
		try {
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS \"PREFN\" (`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,	`key`	TEXT UNIQUE,	`value`	TEXT)");
		} catch ( SQLException e ) {
			logger.error("Impossible de créer la table PREFN", e);
		}
	}



	/**
	 * Requete select vers la SQLite
	 * 
	 * @param requet
	 *            requete sql
	 * @return Un ResultSet des données correspondantes
	 */
	public ResultSet query(String requet) {

		ResultSet resultat = null;

		try {
			resultat = statement.executeQuery(requet);
		} catch (SQLException e) {
			logger.error("Erreur dans la requête : "+requet, e);
		}
		return resultat;
	}



	/**
	 * Mise en forme Objet d'UNE ligne SQL
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private EntiteVideo readOne(ResultSet resultSet) throws SQLException {

		EntiteVideo ev = new EntiteVideo();

		if (resultSet.getInt("categorie") == EntiteVideo.SERIE) {

			Serie s = new Serie();
			s.id = resultSet.getInt("id");
			s.setActeurs(new ArrayList<String>(Arrays.asList(resultSet.getString("acteurs").split(", "))));
			s.setAffichette(resultSet.getString("affichette"));
			s.setDate(resultSet.getString("date"));
			// s.setDatePret(resultSet.getString("datePret"));
			s.setEmplacement(resultSet.getString("emplacement"));
			s.setGenre(new ArrayList<String>(Arrays.asList(resultSet.getString("genre").split(", "))));
			s.setInfosUtilisateur(resultSet.getString("infosUtilisateur"));
			s.setInvites(new ArrayList<String>(Arrays.asList(resultSet.getString("invites").split(", "))));
			s.setNoEpisode(resultSet.getInt("noEpisode"));
			s.setNom(resultSet.getString("nom"));
			s.setNomEpisode(resultSet.getString("nomEpisode"));
			s.setNoSaison(resultSet.getInt("noSaison"));
			s.setOriginalName(resultSet.getString("originalName"));
			
			// TODO TEST MAJ 1.0.2
			ArrayList<String> a = new ArrayList<String>();
			for( String ss : resultSet.getString("pays").split(", ") ) {
				
				if (ss.length() >= 1) {
					ss = ss.trim();
					String cap = ss.substring(0, 1).toUpperCase() + ss.substring(1);
					a.add(cap);
					
				}
			}
			s.setPays(a);
			//s.setPays(new ArrayList<String>(Arrays.asList(resultSet.getString("pays").split(", "))));
			s.setPersonnePret(resultSet.getString("personnePret"));
			s.setPlaytime(resultSet.getString("playtime"));
			s.setRealisateurs(new ArrayList<String>(Arrays.asList(resultSet.getString("realisateurs").split(", "))));
			s.setSupport(resultSet.getInt("support"));
			s.setSynopsis(resultSet.getString("synopsis"));

			return s;

		} else if (resultSet.getInt("categorie") == EntiteVideo.FILM) {

			Film s = new Film();
			s.id = resultSet.getInt("id");
			s.setActeurs(new ArrayList<String>(Arrays.asList(resultSet.getString("acteurs").split(", "))));
			s.setAffichette(resultSet.getString("affichette"));
			s.setDate(resultSet.getString("date"));
			// s.setDatePret(resultSet.getString("datePret"));
			s.setEmplacement(resultSet.getString("emplacement"));
			s.setGenre(new ArrayList<String>(Arrays.asList(resultSet.getString("genre").split(", "))));
			s.setInfosUtilisateur(resultSet.getString("infosUtilisateur"));
			s.setNom(resultSet.getString("nom"));
			s.setNumSupport(resultSet.getInt("numSupport"));
			s.setOriginalName(resultSet.getString("originalName"));
			
			// TODO TEST MAJ 1.0.2
			ArrayList<String> a = new ArrayList<String>();
			for( String ss : resultSet.getString("pays").split(", ") ) {
				
				if (ss.length() >= 1) {
					ss = ss.trim();
					String cap = ss.substring(0, 1).toUpperCase() + ss.substring(1);
					a.add(cap);
					
				}
			}
			s.setPays(a);
			//s.setPays(new ArrayList<String>(Arrays.asList(resultSet.getString("pays").split(", "))));
			s.setPersonnePret(resultSet.getString("personnePret"));
			s.setPlaytime(resultSet.getString("playtime"));
			s.setRealisateurs(new ArrayList<String>(Arrays.asList(resultSet.getString("realisateurs").split(", "))));
			s.setSupport(resultSet.getInt("support"));
			s.setSynopsis(resultSet.getString("synopsis"));

			return s;
		}
		return ev;
	}



	/**
	 * Liste de toutes les entités vidéos de la bdd
	 * 
	 * @return la liste d'entiteVideo
	 */
	public ArrayList<EntiteVideo> readAllEv() {

		ArrayList<EntiteVideo> res = new ArrayList<EntiteVideo>();
		ResultSet resultSet = query("SELECT * FROM EV");
		try {
			while (resultSet.next()) {

				res.add(readOne(resultSet));
			}
		} catch (SQLException e) {
			logger.error("Erreur de readAllEv", e);
		}
		return res;
	}



	/**
	 * Ajoute une EV à la bdd
	 * 
	 * @param ev
	 */
	public void addEv(EntiteVideo ev) {

		String query = "";

		query += "INSERT INTO EV (acteurs, affichette, categorie, date, datePret, emplacement, genre, infosUtilisateur, nom, pays, personnePret, realisateurs, support, synopsis, originalName, playtime, invites, noEpisode, nomEpisode, noSaison, numSupport) VALUES (\""
			+ ev.getActeurs().toString().replaceAll("[\\[\\]]", "")			+ "\",\""
			+ ev.getAffichette()			+ "\",\""
			+ ev.getCategorie()			+ "\",\""
			+ ev.getDate()			+ "\",\""
			+ ev.getDatePret()			+ "\",\""
			+ ev.getEmplacement()			+ "\",\""
			+ ev.getGenre().toString().replaceAll("[\\[\\]]", "")			+ "\",\""
			+ ev.getInfosUtilisateur().replaceAll("\"", "\"\"")			+ "\",\""
			+ ev.getNom().replaceAll("\"", "\"\"")			+ "\",\""
			+ ev.getPays().toString().replaceAll("[\\[\\]]", "")			+ "\",\""
			+ ev.getPersonnePret()			+ "\",\""
			+ ev.getRealisateurs().toString().replaceAll("[\\[\\]]", "")			+ "\",\""
			+ ev.getSupport()			+ "\",\""
			+ ev.getSynopsis().replaceAll("\"", "\"\"")			+ "\",\""
			+ ev.getOriginalName().replaceAll("\"", "\"\"")			+ "\",\""
			+ ev.getPlaytime() + "\",";

		if (ev.getCategorie() == EntiteVideo.SERIE)
			query += "\"" + ((Serie) ev).getInvites().toString().replaceAll("[\\[\\]]", "") + "\",\""
				+ ((Serie) ev).getNoEpisode() + "\",\"" + ((Serie) ev).getNomEpisode().replaceAll("\"", "\"\"") + "\",\""
				+ ((Serie) ev).getNoSaison() + "\",";
		else
			query += "'', '', '', '',";

		if (ev.getCategorie() == EntiteVideo.FILM)
			query += "'" + ((Film) ev).getNumSupport() + "'";
		else
			query += "''";

		query += ")";

		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			logger.error("Erreur lors de l'ajout de : " + ev.getNom() + "   " + ev.getEmplacement(), e);
			logger.error("Requête de l'erreur précédente : "+query);
		}
	}
	
	public void addBatch(EntiteVideo ev) {
		
		String query = "";
		
		try {
			query += "INSERT INTO EV (acteurs, affichette, categorie, date, datePret, emplacement, genre, infosUtilisateur, nom, pays, personnePret, realisateurs, support, synopsis, originalName, playtime, invites, noEpisode, nomEpisode, noSaison, numSupport) VALUES (\""
				+ ev.getActeurs().toString().replaceAll("[\\[\\]]", "")			+ "\",\""
				+ ev.getAffichette()			+ "\",\""
				+ ev.getCategorie()			+ "\",\""
				+ ev.getDate()			+ "\",\""
				+ ev.getDatePret()			+ "\",\""
				+ ev.getEmplacement()			+ "\",\""
				+ ev.getGenre().toString().replaceAll("[\\[\\]]", "")			+ "\",\""
				+ ev.getInfosUtilisateur().replaceAll("\"", "\"\"")			+ "\",\""
				+ ev.getNom().replaceAll("\"", "\"\"")			+ "\",\""
				+ ev.getPays().toString().replaceAll("[\\[\\]]", "")			+ "\",\""
				+ ev.getPersonnePret()			+ "\",\""
				+ ev.getRealisateurs().toString().replaceAll("[\\[\\]]", "")			+ "\",\""
				+ ev.getSupport()			+ "\",\""
				+ ev.getSynopsis().replaceAll("\"", "\"\"")			+ "\",\""
				+ ev.getOriginalName().replaceAll("\"", "\"\"")			+ "\",\""
				+ ev.getPlaytime() + "\",";

			if (ev.getCategorie() == EntiteVideo.SERIE)
				query += "\"" + ((Serie) ev).getInvites().toString().replaceAll("[\\[\\]]", "") + "\",\""
					+ ((Serie) ev).getNoEpisode() + "\",\"" + ((Serie) ev).getNomEpisode().replaceAll("\"", "\"\"") + "\",\""
					+ ((Serie) ev).getNoSaison() + "\",";
			else
				query += "'', '', '', '',";

			if (ev.getCategorie() == EntiteVideo.FILM)
				query += "'" + ((Film) ev).getNumSupport() + "'";
			else
				query += "''";

			query += ")";

			
			statement.addBatch(query);
		} catch (SQLException e) {
			logger.error("Pb requête : "+query);
			e.printStackTrace();
		}
	}

	public void executeBatch() {
		try {
			statement.executeBatch();
		} catch (SQLException e) {
			logger.error("Pb execute batch sql");
		}
	}

	/**
	 * Edition d'une EV
	 * 
	 * @param ev
	 */
	public void editEv(EntiteVideo ev) {

		String query = "UPDATE EV SET acteurs = \"" + ev.getActeurs().toString().replaceAll("[\\[\\]]", "")
			+ "\", affichette = \"" + ev.getAffichette() + "\", categorie = \"" + ev.getCategorie() + "\", date = \""
			+ ev.getDate() + "\",	datePret = \"" + ev.getDatePret() + "\", emplacement = \"" + ev.getEmplacement()
			+ "\", genre = \"" + ev.getGenre().toString().replaceAll("[\\[\\]]", "") + "\", infosUtilisateur = \""
			+ ev.getInfosUtilisateur().replaceAll("\"", "\"\"") + "\",	nom = \"" + ev.getNom().replaceAll("\"", "\"\"") + "\", pays = \""
			+ ev.getPays().toString().replaceAll("[\\[\\]]", "") + "\", personnePret = \"" + ev.getPersonnePret()
			+ "\", realisateurs = \"" + ev.getRealisateurs().toString().replaceAll("[\\[\\]]", "") + "\",	support = \""
			+ ev.getSupport() + "\", synopsis = \"" + ev.getSynopsis().replaceAll("\"", "\"\"") + "\", originalName = \"" + ev.getOriginalName().replaceAll("\"", "\"\"")
			+ "\", playtime = \"" + ev.getPlaytime() + "\", ";

		if (ev.getCategorie() == EntiteVideo.SERIE)
			query += "invites = \"" + ((Serie) ev).getInvites().toString().replaceAll("[\\[\\]]", "")
				+ "\", noEpisode = \"" + ((Serie) ev).getNoEpisode() + "\", nomEpisode = \""
				+ ((Serie) ev).getNomEpisode().replaceAll("\"", "\"\"") + "\", noSaison = \"" + ((Serie) ev).getNoSaison() + "\" ";
		else if (ev.getCategorie() == EntiteVideo.FILM)
			query += "numSupport = \"" + ((Film) ev).getNumSupport() + "\" ";

		query += "WHERE id='" + ev.id + "';";

		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			logger.error("Erreur edit EV : " + query);
		}
	}



	/**
	 * Supression d'une EV dla bdd
	 * 
	 * @param ev
	 */
	public void removeEv(EntiteVideo ev) {

		String query = "DELETE FROM EV WHERE id = '" + ev.id + "'";

		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			logger.error("Erreur delete EV : " + query);
		}
	}



	/**
	 * Lecture de la table préférences
	 * 
	 * @return
	 * @throws IOException
	 */
	public Preferences readPref() throws IOException {

		Preferences p = Preferences.DefaultPreferences();
		ResultSet rs = query("SELECT * FROM PREF");
		try {
			if (rs.next()) {

				p.setLookAndFeel(rs.getString("lookAndFeel"));
				p.setRelatif((rs.getInt("relatif") != 0));
				p.setSurveillerDemarrage((rs.getInt("surveillerDemarrage") != 0));
				p.setVideoExtensions(rs.getString("videoExtensions").split(", "));
				p.setRegexFileCleanSpace(rs.getString("regexFileCleanSpace").split(", "));
				p.setRegexFileCleanBlank(rs.getString("regexFileCleanBlank").split(", "));
				p.setRegexMovie(rs.getString("regexMovie").split(", "));
				p.setRegexTvShow(rs.getString("regexTvShow").split(", "));
				p.setLang(rs.getString("lang"));
				
				p.setCurrentVideoTime(rs.getInt("currentVideoTime"));
				String[] tab = rs.getString("frameDimension").split(",");
				p.setFrameDimension(new Dimension(Integer.valueOf(tab[0]), Integer.valueOf(tab[1])));
				tab = rs.getString("frameLocation").split(",");
				p.setFrameLocation(new Point(Integer.valueOf(tab[0]), Integer.valueOf(tab[1])));
				p.setWindowPlaylistShowed((rs.getInt("windowPlaylistShowed") != 0));
				p.setWindowDetailsShowed((rs.getInt("windowDetailsShowed") != 0));
				p.setVolume(rs.getInt("volume"));
				p.setMoviePath(rs.getString("moviePath"));
				p.setTvShowPath(rs.getString("tvShowPath"));
				
				p.setPlayList(SQLtoEv(rs.getString("playlist")));
			}
		} catch (SQLException e) {
			logger.error("Pb lecture table PREF (vide): ");
		}
		return p;
	}
	
	
	
	/**
	 * Lecture de la table pr�f�rences
	 * 
	 * @return
	 * @throws IOException
	 */
	public Preferences readPrefN() {

		Preferences p = Preferences.DefaultPreferences();
		ResultSet rs = query("SELECT * FROM PREFN");
		String playlist="";
		
		try {
			do {
				
				switch (rs.getString("key")) {

				case "lookAndFeel":
					p.setLookAndFeel(rs.getString("value"));
					break;

				case "relatif":
					p.setRelatif(Integer.valueOf(rs.getString("value")) != 0);
					break;

				case "surveillerDemarrage":
					p.setSurveillerDemarrage(Integer.valueOf(rs.getString("value")) != 0);
					break;

				case "videoExtensions":
					p.setVideoExtensions(rs.getString("value").split(", "));
					break;

				case "regexFileCleanSpace":
					p.setRegexFileCleanSpace(rs.getString("value").split(", "));
					break;

				case "regexFileCleanBlank":
					p.setRegexFileCleanBlank(rs.getString("value").split(", "));
					break;

				case "regexMovie":
					p.setRegexMovie(rs.getString("value").split(", "));
					break;
					
				case "regexTvShow":
					p.setRegexTvShow(rs.getString("value").split(", "));
					break;
					
				case "lang":
					p.setLang(rs.getString("value"));
					break;
					
				case "currentVideoTime":
					p.setCurrentVideoTime(Integer.valueOf(rs.getString("value")));
					break;
					
				case "frameDimension":
					String[] tab = rs.getString("value").split(",");
					p.setFrameDimension(new Dimension(Integer.valueOf(tab[0]), Integer.valueOf(tab[1])));
					break;
					
				case "frameLocation":
					tab = rs.getString("value").split(",");
					p.setFrameLocation(new Point(Integer.valueOf(tab[0]), Integer.valueOf(tab[1])));
					break;
					
				case "windowPlaylistShowed":
					p.setWindowPlaylistShowed(Integer.valueOf(rs.getString("value")) != 0);
					break;
					
				case "windowDetailsShowed":
					p.setWindowDetailsShowed(Integer.valueOf(rs.getString("value")) != 0);
					break;
					
				case "volume":
					p.setVolume(Integer.valueOf(rs.getString("value")));
					break;
					
				case "moviePath":
					p.setMoviePath(rs.getString("value"));
					break;
					
				case "tvShowPath":
					p.setTvShowPath(rs.getString("value"));
					break;
					
				case "playlist":
					//p.setPlayList(SQLtoEv(rs.getString("value")));
					playlist = rs.getString("value");
					break;
					
				case "thumbnailSize":
					p.setThumbnailSize(Integer.valueOf(rs.getString("value")));
					break;
				}
			}while (rs.next());
			
			// Il faut externaliser cette nouvelle requete, sinon elle se passe au milieu de l'autre et fais tout planter
			if( !playlist.isEmpty())
				p.setPlayList(SQLtoEv(playlist));
			
		} catch (SQLException e) {
			logger.error("Pb lecture PREFN : ");
		}
		return p;
	}
	



	/**
	 * transforme une liste d'entit� vid�os (playlist) en suite d'id s�par�s
	 * d'une virgule pour le stockage BDD
	 * 
	 * @param list
	 * @return
	 */
	private String EvtoSQL(ArrayList<EntiteVideo> list) {

		String res = "";

		for (EntiteVideo ev : list) {
			res += ev.id + ",";
		}

		return res;
	}



	/**
	 * Transforme une suite d'IDs en arraylist d'EV (playlist)
	 * 
	 * @param ids
	 * @return
	 */
	private ArrayList<EntiteVideo> SQLtoEv(String ids) {

		ArrayList<EntiteVideo> res = new ArrayList<EntiteVideo>();

		String query = "SELECT * FROM EV WHERE ";

		// Liste vide
		if (ids.isEmpty())
			return res;

		for (String s : ids.split(",")) {

			query += "id = \"" + s + "\" OR ";
		}
		query = query.substring(0, query.length() - 3);
		
		ResultSet resultSet = query(query);

		// 2.0.0 Cas ou la playliste explose en fait tout déconner (+1000)
		if( resultSet == null )
			return res;
		
		try {
			while (resultSet.next()) {
				res.add(readOne(resultSet));
			}
		} catch (SQLException e) {
			logger.error("Erreur SQLtoEv : " + query);
		}
		
		// Petit tri pour remettre la playlist dans le vrai ordre et pas celui du retour sql
		// MAJ 1.0.2
		ArrayList<EntiteVideo> tri = new ArrayList<EntiteVideo>();
		for( String s : ids.split(",") ) {
			
			for( EntiteVideo ev : res ) {
				
				if( Integer.valueOf(s) == ev.id )
					tri.add(ev);
			}
		}
		return tri;
	}


	
	/**
	 * Ajout des préférences à la base de données
	 * 
	 * @param p
	 */
	public void addPrefN(Preferences p) {

		String q="";
		
		try {
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"lookAndFeel\",\""+p.getLookAndFeel()+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"relatif\",\""+(p.isRelatif() ? 1 : 0)+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"surveillerDemarrage\",\""+(p.isSurveillerDemarrage() ? 1 : 0)+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"videoExtensions\",\""+Arrays.toString(p.getVideoExtensions()).replaceAll("[\\[\\]]", "").trim()+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"regexFileCleanSpace\",\""+Arrays.toString(p.getRegexFileCleanSpace()).substring(1, Arrays.toString(p.getRegexFileCleanSpace()).length()-1)+"\");";
			statement.executeUpdate(q);

			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"regexFileCleanBlank\",\""+Arrays.toString(p.getRegexFileCleanBlank()).replaceAll("[\\[\\]]", "")+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"regexMovie\",\""+Arrays.toString(p.getRegexMovie()).substring(1, Arrays.toString(p.getRegexMovie()).length()-1)+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"regexTvShow\",\""+Arrays.toString(p.getRegexTvShow()).substring(1, Arrays.toString(p.getRegexTvShow()).length()-1)+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"lang\",\""+p.getLang()+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"playlist\",\""+EvtoSQL(p.getPlayList())+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"currentVideoTime\",\""+p.getCurrentVideoTime()+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"frameDimension\",\""+(int)p.getFrameDimension().getWidth()+","+(int) p.getFrameDimension().getHeight()+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"frameLocation\",\""+(int)p.getFrameLocation().getX()+","+(int)p.getFrameLocation().getY()+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"windowPlaylistShowed\",\""+(p.isWindowPlaylistShowed() ? 1 : 0)+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"windowDetailsShowed\",\""+(p.isWindowDetailsShowed() ? 1 : 0)+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"volume\",\""+p.getVolume()+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"moviePath\",\""+p.getMoviePath()+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"tvShowPath\",\""+p.getTvShowPath()+"\");";
			statement.executeUpdate(q);
			
			q = "INSERT INTO `PREFN`(`key`,`value`) VALUES (\"thumbnailSize\",\""+p.getThumbnailSize()+"\");";
			statement.executeUpdate(q);
			
			statement.executeUpdate("INSERT INTO `PREFN`(`key`,`value`) VALUES (\"a\",\"aa\");");
			
		} catch (SQLException e1) {
			System.out.println("Pb requete" + q);
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * Edition des préférences dans la bdd
	 * 
	 * @param p
	 */
	public void editPrefN(Preferences p) {
		
		 String q = "UPDATE `PREFN` SET value = (case "
				+ "when key = 'lookAndFeel' then '"+p.getLookAndFeel()+"'  "
				+ "when key = 'relatif' then '"+(p.isRelatif() ? 1 : 0)+"' "
				+ "when key = 'surveillerDemarrage' then '"+(p.isSurveillerDemarrage() ? 1 : 0)+"'	"
				+ "when key = 'videoExtensions' then '"+Arrays.toString(p.getVideoExtensions()).replaceAll("[\\[\\]]", "").trim()+"'  "
				+ "when key = 'regexFileCleanSpace' then '"+Arrays.toString(p.getRegexFileCleanSpace()).substring(1, Arrays.toString(p.getRegexFileCleanSpace()).length()-1)+"'  "
				+ "when key = 'regexFileCleanBlank' then '"+Arrays.toString(p.getRegexFileCleanBlank()).replaceAll("[\\[\\]]", "")+"'  "
				+ "when key = 'regexMovie' then '"+Arrays.toString(p.getRegexMovie()).substring(1, Arrays.toString(p.getRegexMovie()).length()-1)+"'  "
				+ "when key = 'regexTvShow' then '"+Arrays.toString(p.getRegexTvShow()).substring(1, Arrays.toString(p.getRegexTvShow()).length()-1)+"'  "
				+ "when key = 'lang' then '"+p.getLang()+"'  "
				+ "when key = 'playlist' then '"+EvtoSQL(p.getPlayList())+"'  "
				+ "when key = 'currentVideoTime' then '"+p.getCurrentVideoTime()+"'  "
				+ "when key = 'frameDimension' then '"+(int)p.getFrameDimension().getWidth()+","+(int) p.getFrameDimension().getHeight()+"'  "
				+ "when key = 'frameLocation' then '"+(int)p.getFrameLocation().getX()+","+(int)p.getFrameLocation().getY()+"'  "
				+ "when key = 'windowPlaylistShowed' then '"+(p.isWindowPlaylistShowed() ? 1 : 0)+"'  "
				+ "when key = 'windowDetailsShowed' then '"+(p.isWindowDetailsShowed() ? 1 : 0)+"'  "
				+ "when key = 'volume' then '"+p.getVolume()+"'  "
				+ "when key = 'moviePath' then '"+p.getMoviePath()+"'  "
				+ "when key = 'tvShowPath' then '"+p.getTvShowPath()+"'  "
				+ "when key = 'thumbnailSize' then '"+p.getThumbnailSize()+"'  "
				+ "end)";
		
		try {
			statement.executeUpdate(q);
		} catch (SQLException e) {
			logger.error("Erreur editPrefN : " + q);
		}
	}
}
