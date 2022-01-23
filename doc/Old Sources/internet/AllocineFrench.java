package com.staligtredan.VideoMer.internet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.staligtredan.VideoMer.modele.EntiteVideo;
import com.staligtredan.VideoMer.modele.Film;
import com.staligtredan.VideoMer.modele.Serie;
import com.staligtredan.VideoMer.util.HtmlParsing;


/**
 * 
 * 
 * @author Brendan
 * @version 0.1.5 16/03/2014
 * @version 0.1.6 2/12/2014  Correction de l'affichette des s�ries sur allocin�
 * @version 1.0.0 20/01/2015 pleins de petites corrections + MAJ pour le sqlite
 * @version 1.0.2 16/02/2016 Petites corrections de parse d'allocin�
 */
public class AllocineFrench implements VideoDataCollector {

	private final static String VERSION = "0.1";
	
	/**
	 * Buffer qui contient les infos g�n�rales d'une s�rie avec comme indice :
	 * URL de base
	 */
	private final HashMap<String, Serie> tvShowGeneralBuffer;

	/**
	 * Buffer qui contient l'url de la liste des �pisodes d'une s�rie avec comme
	 * indice : "URL de base"-"# de la saison"
	 */
	private final HashMap<String, String> tvShowSeasonsBuffer;

	/**
	 * Buffer qui contient les informations d'un �pisode d'une s�rie avec comme
	 * indice : "URL de base"-"# de la saison"-"# de l'�pisode"
	 */
	private final HashMap<String, Serie> tvShowEpisodesBuffer;

	

	public AllocineFrench() {
		tvShowGeneralBuffer = new HashMap<String, Serie>();
		tvShowSeasonsBuffer = new HashMap<String, String>();
		tvShowEpisodesBuffer = new HashMap<String, Serie>();
	}

	@Override
	public String getAuthorEmail() {
		return "npfs666@hotmail.com";
	}

	/**
	 * R�cup�re les informations g�n�rales d'une <code>Serie</code> 
	 * Si les donn�es n'existent pas elles sont recherch�es & ajout�e au buffer
	 * 
	 * @param s
	 * @param choice
	 * @return
	 */
	private boolean getTvShowGeneralData(Serie s, String choice) {

		ArrayList<String> tmpList;
		String posterUrl = "", playtime = "", name = "", country = "", genres = "", directors = "", actors = "", date="";

		if (tvShowGeneralBuffer.containsKey(choice)) {

			Serie tmp = tvShowGeneralBuffer.get(choice);

			s.setNom(tmp.getNom());
			s.setActeurs(tmp.getActeurs());
			s.setRealisateurs(tmp.getRealisateurs());
			s.setAffichette(tmp.getAffichette());
			s.setGenre(tmp.getGenre());
			s.setPays(tmp.getPays());
			s.setDate(tmp.getDate());
			s.setPlaytime(tmp.getPlaytime());

		} else {

			try {
				synchronized (tvShowGeneralBuffer) {
					
					StringBuffer source = HtmlParsing.getSourceFrom(choice, "http://www.allocine.fr");

					HtmlParsing.keepBetween("<div id=\"title\" class=\"titlebar_01 margin_10b\">",
							"<div class='module-actionbar w-bam'>", source, false, false);

					
					// Nom
					name = HtmlParsing.getBetween("<div id=\"title\" class=\"titlebar_01 margin_10b\">", "</span>", source, true, false, false);
					name = HtmlParsing.removeMarkup(name);
					name = HtmlParsing.cleanList(name);
					s.setNom(name.trim());
					
					// R�alisateur
					directors = HtmlParsing.getBetween("Cr��e par", "</td>", source, true, false, false);
					directors = HtmlParsing.removeMarkup(directors);
					directors = HtmlParsing.cleanList(directors);
					directors = HtmlParsing.removeBadChar(directors);

					
					// Date (inside directors)
					Pattern p = Pattern.compile("\\(.+\\)");
					Matcher m = p.matcher(directors.trim());
					
					if( m.find() ) {
						directors = directors.replace(m.group(0), "");
						date = m.group(0).replaceAll("[\\(,\\)]", "");
						s.setDate(date);
					}
					
					tmpList = new ArrayList<String>();
					Collections.addAll(tmpList, directors.split(", "));
					s.setRealisateurs(tmpList);
					// System.out.println("Director : "+directors);

					// Acteurs
					actors = HtmlParsing.getBetween("Avec", "</td>", source, true, false, false);
					actors = HtmlParsing.removeMarkup(actors);
					actors = HtmlParsing.cleanList(actors);
					actors = HtmlParsing.removeBadChar(actors);
					tmpList = new ArrayList<String>();
					Collections.addAll(tmpList, actors.trim().replace("plus", "").split(", "));
					s.setActeurs(tmpList);
					// System.out.println("Acteurs : "+actors);

					// Genres
					genres = HtmlParsing.getBetween("Genre", "</td>", source, true, false, false);
					genres = HtmlParsing.removeMarkup(genres);
					genres = HtmlParsing.cleanList(genres);
					genres = HtmlParsing.removeBadChar(genres);
					tmpList = new ArrayList<String>();
					Collections.addAll(tmpList, genres.trim().split(", "));
					s.setGenre(tmpList);
					// System.out.println("Genre : "+genres);

					// Titre original
					//originalName = HtmlParsing.getBetween("Titre original : ", "<br />", source, true, false, false);
					//s.setOriginalName(originalName);
					// System.out.println("Titre original : "+originalName);

					// Pays
					country = HtmlParsing.getBetween("Nationalit�", "</td>", source, true, true, false);
					country = HtmlParsing.removeMarkup(country);
					country = HtmlParsing.cleanList(country);
					tmpList = new ArrayList<String>();
					Collections.addAll(tmpList, country.trim().split(", "));
					s.setPays(tmpList);
					// System.out.println("Pays : "+country);

					// Dur�e
					playtime = HtmlParsing.getBetween("Format</span>", "</td>", source, true, false, false);
					playtime = HtmlParsing.removeMarkup(playtime);
					s.setPlaytime(playtime);
					// System.out.println("Dur�e : "+playTime);

					// Affichette
					posterUrl = HtmlParsing.getBetween("<meta itemprop=\"image\" content='", "'>", source, true, false, false);

					s.setAffichette(posterUrl);
					
					// System.out.println("Affichette : "+posterUrl);

					// Une fois la recherche faite on ajoute au buffer
					tvShowGeneralBuffer.put(choice, s);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return true;
			}
		}

		return false;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}

	@Override
	public EntiteVideo parseData(String choice, int searchMode, String season, String episode) {

		EntiteVideo result = null;

		if (searchMode == TV_SHOW) {

			result = new Serie();
			boolean toutBuffer = false, lienUniquement = false;
			
			// J'ai tout en buffer
			synchronized (tvShowEpisodesBuffer) {
				toutBuffer = tvShowEpisodesBuffer.containsKey(choice + "-" + Integer.valueOf(season) + "-"
						+ Integer.valueOf(episode));
			}
			synchronized (tvShowSeasonsBuffer) {
				lienUniquement = tvShowSeasonsBuffer.containsKey(choice + "-" + Integer.valueOf(season));
			}

			if (toutBuffer) {

				// je r�cup�re toutes les donn�es de l'�pisode
				getTvShowEpisodeData((Serie) result, choice, Integer.valueOf(season), Integer.valueOf(episode));

				// Puis pour terminer on r�cup�re les infos g�n�rals de la s�rie
				getTvShowGeneralData((Serie) result, choice);
			}

			// J'ai uniquement le lien vers les donn�es de la s�rie
			else if (lienUniquement) {

				// je met les donn�es de la saison en buffer
				setTvShowEpisodesData(choice, Integer.valueOf(season));

				// je r�cup�re toutes les donn�es de l'�pisode
				getTvShowEpisodeData((Serie) result, choice, Integer.valueOf(season), Integer.valueOf(episode));

				// Puis pour terminer on r�cup�re les infos g�n�rals de la s�rie
				getTvShowGeneralData((Serie) result, choice);
			}

			// J'ai rien
			else {

				// On recr�e le lien "Saisons et �pisodes" du menu
				Pattern p = Pattern.compile("http://www.allocine.fr/series/ficheserie_gen_cserie=([0-9]+).html");
				Matcher m = p.matcher(choice);
				m.matches();

				String urlGenralSeason = "http://www.allocine.fr/series/ficheserie-" + m.group(1) + "/saisons/";

				// Je stocke les saison de la s�rie en buffer
				setTvShowSeasonsUrl(choice, urlGenralSeason);
				
				// je met les donn�es de la saison en buffer
				setTvShowEpisodesData(choice, Integer.valueOf(season));

				// je r�cup�re toutes les donn�es de l'�pisode
				getTvShowEpisodeData((Serie) result, choice, Integer.valueOf(season), Integer.valueOf(episode));

				// Puis pour terminer on r�cup�re les infos g�n�rals de la s�rie
				getTvShowGeneralData((Serie) result, choice);
			}

		} else if (searchMode == MOVIE) {

			result = new Film();

			try {
				String tmp;
				ArrayList<String> tmpList;
				StringBuffer source = HtmlParsing.getSourceFrom(choice, getWebsiteUrl());

				if (source.length() == 0)
					return null;

				if (!HtmlParsing.keepBetween("<body", "</body", source, true, false))
					return null;

				// Nom
				tmp = HtmlParsing.getBetween("<div class=\"titlebar-title titlebar-title-lg\">", "</div>", source, true, false,
						false);
				tmp = HtmlParsing.removeMarkup(tmp);
				tmp = HtmlParsing.cleanList(tmp);
				tmp = HtmlParsing.changeHTMLCodes(tmp);
				result.setNom(tmp);

				// Date
				tmp = HtmlParsing.getBetween("Ann�e de production", "</div>", source, true, false, false);
				tmp = HtmlParsing.removeMarkup(tmp);
				tmp = HtmlParsing.cleanList(tmp);
				tmp = HtmlParsing.removeBadChar(tmp);
				result.setDate(tmp);

				// Director
				tmp = HtmlParsing.getBetween(
						"<span itemprop=\"director\" itemscope itemtype=\"http://schema.org/Person\">", "</a>",
						source, true, false, false);
				tmp = HtmlParsing.removeMarkup(tmp);
				tmp = HtmlParsing.cleanList(tmp);
				tmp = HtmlParsing.removeBadChar(tmp);
				tmpList = new ArrayList<String>();
				Collections.addAll(tmpList, tmp.split(","));
				result.setRealisateurs(tmpList);

				// Actors
				tmp = HtmlParsing.getBetween("Avec", "</div>", source, true, false, false);
				tmp = HtmlParsing.removeMarkup(tmp);
				tmp = HtmlParsing.removeBadChar(tmp);	
				tmp = HtmlParsing.cleanList(tmp);
				tmp = tmp.replaceAll("plus", "");
				tmpList = new ArrayList<String>();
				Collections.addAll(tmpList, tmp.split(","));
				result.setActeurs(tmpList);
				// System.out.println("Acteurs : "+tmpList);

				// Country
				tmp = HtmlParsing.getBetween("Nationalit�s", "</div>", source, true, false, false);
				if( tmp.isEmpty() ) tmp = HtmlParsing.getBetween("Nationalit�", "</div>", source, true, false, false);
				tmp = HtmlParsing.removeMarkup(tmp);
				tmp = HtmlParsing.cleanList(tmp);
				tmp = HtmlParsing.removeBadChar(tmp);
				tmpList = new ArrayList<String>();
				Collections.addAll(tmpList, tmp.split(","));
				result.setPays(tmpList);

				// Genre
				tmp = HtmlParsing.getBetween("Genres", "</div>", source, true, false, false);
				if( tmp.isEmpty() ) tmp = HtmlParsing.getBetween("Genre", "</div>", source, true, false, false);
				tmp = HtmlParsing.removeMarkup(tmp);
				tmp = HtmlParsing.cleanList(tmp);
				tmp = HtmlParsing.removeBadChar(tmp);
				tmpList = new ArrayList<String>();
				Collections.addAll(tmpList, tmp.split(","));
				result.setGenre(tmpList);

				// Plot
				tmp = HtmlParsing.getBetween("<div class=\"ovw-synopsis-txt\" itemprop=\"description\">", "</div>", source, true, false, false);
				tmp = HtmlParsing.removeMarkup(tmp);
				tmp = HtmlParsing.cleanList(tmp);
				result.setSynopsis(tmp);

				// Poster
				tmp = HtmlParsing.getBetween("<img class=\"thumbnail-img\" src=\"", "\" alt=", source, true, false, false);
				//tmp = HtmlParsing.getBetween("<img src='", "' ", tmp, true, false);
				result.setAffichette(tmp);
				
				// Original Name
				tmp = HtmlParsing.getBetween("Titre original", "</div>", source, true, false, false);
				tmp = HtmlParsing.removeMarkup(tmp);
				result.setOriginalName(tmp);
				
				// playtime
				tmp = HtmlParsing.getBetween("Date de sortie", "</div>", source, false, true, false);
				tmp = HtmlParsing.getBetween("(", ")", tmp, true, false);
				tmp = HtmlParsing.removeMarkup(tmp);
				result.setPlaytime(tmp);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	@Override
	public ArrayList<String[]> search(String query, int searchMode) {

		// searchMode = TV_SHOW;
		String searchPattern;
		ArrayList<String[]> result = new ArrayList<String[]>();

		// Si on recherche un film
		if (searchMode == MOVIE)
			searchPattern = "http://www.allocine.fr/recherche/1/?q=";
		// Si on recherche une s�rie
		else if (searchMode == TV_SHOW)
			searchPattern = "http://www.allocine.fr/recherche/6/?q=";
		else
			return null;

		try {
			StringBuffer source;
			String tmp, name, year, realisateur, url;

			source = HtmlParsing.getSourceFrom(searchPattern + URLEncoder.encode(query, "UTF-8"));

			if (source.length() != 0) {

				if (HtmlParsing.keepBetween("<table class=\"totalwidth noborder purehtml\">", "</table>", source,
						false, false)) {

					tmp = HtmlParsing.getBetween("<tr><td style=\" vertical-align:top;\">", "<div class=\"hrdotted\">",
							source, true, false, true);

					// Boucle sur les r�sultats
					while (!tmp.equals("")) {

						// URL
						url = HtmlParsing.getBetween("='", ".html", tmp, true, true);
						url = "http://www.allocine.fr" + url;

						// Nom
						name = HtmlParsing.getBetween("<div style=\"margin-top:-5px;\">", "</a>", tmp, true, false);
						name = HtmlParsing.removeMarkup(name);

						// Ann�e
						year = HtmlParsing.getBetween("<span class=\"fs11\">", "<br />", new StringBuffer(tmp), true,
								false, true);
						year = year.trim();
						if (HtmlParsing.isNumeric(year))
							year = " (" + year + ")";
						else
							year = "";

						// R�alisateur
						realisateur = HtmlParsing.getBetween("<span class=\"fs11\">", "<div>", tmp, true, false);
						realisateur = HtmlParsing.getBetween("de ", "<br />", realisateur, false, false);
						realisateur = HtmlParsing.removeMarkup(realisateur);
						realisateur = realisateur.trim();

						result.add(new String[] { name + year + " " + realisateur, url });

						// Lecture r�sultat suivant
						tmp = HtmlParsing.getBetween("<tr><td style=\" vertical-align:top;\">",
								"<div class=\"hrdotted\">", source, true, false, true);
					}
				} else
					result = null;
			} else
				result = null;
		} catch (IOException e) {
			result = null;
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Gets the data from the buffer
	 * 
	 * @param mainUrl
	 *            The TV Show main page URL
	 * @param season
	 *            The TV Show season #
	 * @param episode
	 *            The TV Show episode #
	 */
	private void getTvShowEpisodeData(Serie s, String mainUrl, int season, int episode) {

		Serie tmp;

		synchronized (tvShowEpisodesBuffer) {

			tmp = tvShowEpisodesBuffer.get(mainUrl + "-" + season + "-" + episode);
			
			s.setNomEpisode(tmp.getNomEpisode());
			s.setOriginalName(tmp.getOriginalName());
			s.setSynopsis(tmp.getSynopsis());
			s.setNoSaison(tmp.getNoSaison());
			s.setNoEpisode(tmp.getNoEpisode());
		}
	}

	private String getWebsiteUrl() {
		return "http://www.allocine.fr";
	}

	/**
	 * Sets all the season episodes data to the buffer
	 * 
	 * @param mainUrl
	 *            The TV Show main page URL
	 * @param season
	 *            The TV show season #
	 * @return true if an error occured
	 */
	private boolean setTvShowEpisodesData(String mainUrl, int season) {

		Serie s;

		try {
			synchronized (tvShowEpisodesBuffer) {
				
				StringBuffer source = HtmlParsing.getSourceFrom(tvShowSeasonsBuffer.get(mainUrl + "-" + season),
						getWebsiteUrl());

				HtmlParsing.keepBetween("<tr class=\"episode j_entity_container\">", "</table>", source, false, false);

				String[] tab = HtmlParsing.removeBadChar(source.toString()).split("<tr class=\"episode j_entity_container\">");

				// On ajoute tout au buffer (tant qu'a faire) (�pisode d�croissants)
				for (int i = tab.length-1; i > 0; i--) {

					StringBuffer localSource = new StringBuffer(tab[i]);
					
					s = new Serie();

					s.setNoSaison(season);
					s.setNoEpisode((tab.length- i));
					
					// Nom �pisode (fran�ais)
					String nomEp = HtmlParsing.getBetween("</strong>", "</span>", localSource, true, false, true);
					nomEp = HtmlParsing.removeMarkup(nomEp);
					s.setNomEpisode(nomEp);
					
					// Nom �pisode (VO)
					//s.setOriginalName(HtmlParsing.getBetween("<div class=\"hide\">", "</div>", localSource, true, false,
					//		true));
					
					// Synopsis
					String tmp = HtmlParsing.getBetween("<div class=\"hide\">", "</td>", localSource, true, false,
							true);
					tmp = HtmlParsing.removeMarkup(tmp);
					s.setSynopsis(tmp);
					
	
					tvShowEpisodesBuffer.put(mainUrl + "-" + season + "-" + (tab.length- i), s);
					//System.out.println(s.getNomEpisode() + "     " +s.getSynopsis());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}

		return false;
	}

	/**
	 * Sets all the TV Show seasons URL to the buffer
	 * 
	 * @param mainUrl
	 *            The TV Show main page URL
	 * @param url
	 *            The TV Show season list URL
	 * @return true if an error occured
	 */
	private boolean setTvShowSeasonsUrl(String mainUrl, String url) {

		// Ensuite on va r�cup�rer le lien de la saison
		try {
			// On prot�ge l'acc�s au buffer car multithread�
			synchronized (tvShowSeasonsBuffer) {
				StringBuffer source = HtmlParsing.getSourceFrom(url, getWebsiteUrl());

				HtmlParsing.keepBetween("<ul class=\"list_img_side_content episodes_allSeason_list\">", "</ul>", source, true, false);

				String[] tab = HtmlParsing.removeBadChar(source.toString()).split("</li>");

				String urlSeason = "";

				for (int i = tab.length-1; i >= 0; i--) {
					
					// N� de la saison (patch pour �viter le pb avec les �pisodes hors saison, cf futurama)
					Pattern p = Pattern.compile(".*Saison ([0-9]{1,2}).*");
					Matcher m = p.matcher(tab[i]);
					
					// URL de la saison
					Pattern pp = Pattern.compile(".*<a class=\"no_underline\" href=\"(.*)/\".*");
					Matcher mm = pp.matcher(tab[i]);

					if ( mm.matches() && m.matches() ) {
						urlSeason = "http://www.allocine.fr" + mm.group(1);
						tvShowSeasonsBuffer.put(mainUrl + "-" + m.group(1), urlSeason);
					} else
						;//tvShowSeasonsBuffer.put(mainUrl + "-" + (tab.length - i), url);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}

		return false;
	}

	public static void main(String[] args) {

		AllocineFrench t = new AllocineFrench();

		/*Serie s = (Serie) t.parseData("http://www.allocine.fr/series/ficheserie_gen_cserie=7157.html", TV_SHOW, "3",
				"2");

		System.out.println(s.getNom());
		System.out.println(s.getNomEpisode());
		System.out.println(s.getActeurs().toString());
		System.out.println(s.getRealisateurs());
		System.out.println(s.getGenre().toString());
		System.out.println(s.getSynopsis());
		System.out.println(s.getPays().toString());
		System.out.println(s.getAffichette());
		//System.out.println(s.getOriginalName());
		System.out.println(s.getPlaytime());
		System.out.println(s.getDate());*/

		
		Film s = (Film) t.parseData("http://www.allocine.fr/film/fichefilm_gen_cfilm=225768.html", MOVIE, null, null);

		System.out.println(s.getNom());
		System.out.println(s.getActeurs().toString());
		System.out.println(s.getRealisateurs());
		System.out.println(s.getGenre().toString());
		System.out.println(s.getSynopsis());
		System.out.println(s.getPays().toString());
		System.out.println(s.getAffichette());
		System.out.println(s.getOriginalName());
		System.out.println(s.getPlaytime());
		System.out.println(s.getDate());
	}
}
