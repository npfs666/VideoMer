package com.staligtredan.VideoMer.internet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.staligtredan.VideoMer.modele.EntiteVideo;
import com.staligtredan.VideoMer.util.HtmlParsing;

public class ImdbEnglish implements VideoDataCollector {

	private final static String VERSION = "0.1";
	
	public ImdbEnglish() {

	}

	@Override
	public String getAuthorEmail() {
		return "brendan.jaouen@orange.fr";
	}

	@Override
	public String getVersion() {
		return VERSION;
	}

	@Override
	public EntiteVideo parseData(String choice, int searchMode, String season, String episode) {
		return null;
	}

	@Override
	public ArrayList<String[]> search(String query, int searchMode) {
		
		String searchPattern;
		ArrayList<String[]> result = new ArrayList<String[]>();

		// Si on recherche un film
		if (searchMode == MOVIE)
			searchPattern = "http://www.imdb.com/search/title?title_type=feature&title=";
		// Si on recherche une s�rie
		else if (searchMode == TV_SHOW)
			searchPattern = "http://www.imdb.com/search/title?title_type=tv_series&title=";
		else
			return null;

		try {
			StringBuffer source;
			String tmp, name, year, realisateur, url;

			source = HtmlParsing.getSourceFrom(searchPattern + URLEncoder.encode(query, "UTF-8"));

			if (source.length() != 0) {

				if (HtmlParsing.keepBetween("<table class=\"results\">", "</table>", source, false, false)) {

					tmp = HtmlParsing.getBetween("<td class=\"image\">", "<span class=\"runtime\">",
							source, true, false, true);

					// Boucle sur les r�sultats
					while (!tmp.equals("")) {

						// URL
						url = HtmlParsing.getBetween("='", "title", tmp, true, true);
						url = "http://www.imdb.com" + url;

						// Nom
						name = HtmlParsing.getBetween("<td class=\"title\">", "</a>", tmp, true, false);
						name = HtmlParsing.removeMarkup(name);

						// Ann�e
						year = HtmlParsing.getBetween("<span class=\"year_type\">(", ")</span>", new StringBuffer(tmp), true,
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
						tmp = HtmlParsing.getBetween("<td class=\"image\">", "<span class=\"runtime\">", source, true,
								false, true);
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

	public static void main(String[] args) {

		ImdbEnglish t = new ImdbEnglish();

		t.search("Game of thrones", TV_SHOW);
		
		//Serie s = (Serie) t.parseData("http://www.allocine.fr/series/ficheserie_gen_cserie=3768.html", TV_SHOW, "01","04");

		/*System.out.println(s.getNom());
		System.out.println(s.getNomEpisode());
		System.out.println(s.getActeurs().toString());
		System.out.println(s.getRealisateurs());
		System.out.println(s.getGenre().toString());
		System.out.println(s.getSynopsis());
		System.out.println(s.getPays().toString());
		System.out.println(s.getAffichette());
		System.out.println(s.getPlaytime());
		System.out.println(s.getDate());*/
		
		/*Film s = (Film) t.parseData("http://www.allocine.fr/film/fichefilm_gen_cfilm=119089.html", MOVIE, null, null);

		System.out.println(s.getNom());
		System.out.println(s.getActeurs().toString());
		System.out.println(s.getRealisateurs());
		System.out.println(s.getGenre().toString());
		System.out.println(s.getSynopsis());
		System.out.println(s.getPays().toString());
		System.out.println(s.getAffichette());
		System.out.println(s.getOriginalName());
		System.out.println(s.getPlaytime());
		System.out.println(s.getDate());*/
	}
}
