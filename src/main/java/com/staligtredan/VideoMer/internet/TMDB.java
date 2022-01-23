package com.staligtredan.VideoMer.internet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.staligtredan.VideoMer.modele.EntiteVideo;
import com.staligtredan.VideoMer.modele.Film;
import com.staligtredan.VideoMer.modele.Serie;
import com.staligtredan.VideoMer.vue.JFramePrincipal;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.TmdbTV.TvMethod;
import info.movito.themoviedbapi.TvResultsPage;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.ProductionCountry;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.Person;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.people.PersonCrew;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;

/**
 * Nouveau API de récup d'info via the movie database
 * 
 * @author Brendan
 * @since 1.0.3 10/12/2016 (Création)
 * @version 2.0.0 22/12/2016 Loging
 */
@SuppressWarnings("unused")
public class TMDB implements VideoDataCollector {
	
	final static Logger logger = LogManager.getLogger(TMDB.class);

	private TmdbApi api;
	private String lang;
	private final static String VERSION = "1.0";
	private final static int MAX_ACTOR_LIST = 5;
	private final static int MAX_DIRECTOR_LIST = 3;

	/**
	 * Buffer qui contient les infos générales d'une série avec comme indice :
	 * URL de base
	 */
	private final HashMap<String, Serie> tvShowGeneralBuffer;

	/**
	 * Buffer qui contient l'url de la liste des épisodes d'une série avec comme
	 * indice : "URL de base"-"# de la saison"
	 */
	private final HashMap<String, TvSeason> tvShowSeasonsBuffer;

	/**
	 * Buffer qui contient les informations d'un épisode d'une série avec comme
	 * indice : "URL de base"-"# de la saison"-"# de l'épisode"
	 */
	private final HashMap<String, ArrayList<String>> tvShowActorsBuffer;



	public TMDB( String lang ) {
		api = new TmdbApi("e9e4c23bf1aa18c85f2771257bdc5fce");
		this.lang = lang;
		tvShowGeneralBuffer = new HashMap<String, Serie>();
		tvShowSeasonsBuffer = new HashMap<String, TvSeason>();
		tvShowActorsBuffer = new HashMap<String, ArrayList<String>>();
	}



	@Override
	public String getAuthorEmail() {
		return "va chier";
	}



	@Override
	public String getVersion() {
		return VERSION;
	}



	/**
	 * Récupération des données série
	 * 
	 * @param choice
	 *            ID Tmdb
	 * @param s
	 *            Objet à modifier
	 */
	private void getTvShowData( String choice, Serie s ) {

		ArrayList<String> tmp;

		synchronized ( tvShowGeneralBuffer ) {

			// Si les données n'existent pas en buffer, on les y met
			if( !tvShowGeneralBuffer.containsKey(choice) ) {

				Serie result = new Serie();

				TvSeries ser = api.getTvSeries().getSeries(Integer.parseInt(choice), lang, TvMethod.credits);

				result.setNom(ser.getName());

				tmp = new ArrayList<>();
				for ( int i = 0; (ser.getCreatedBy().size() > i) && (i < MAX_DIRECTOR_LIST); i++ ) {
					tmp.add(ser.getCreatedBy().get(i).getName());
				}
				result.setRealisateurs(tmp);

				List<Integer> l = ser.getEpisodeRuntime();
				if( !l.isEmpty() )
					result.setPlaytime(l.get(0).toString());

				tmp = new ArrayList<>();
				for ( Genre g : ser.getGenres() ) {
					tmp.add(g.getName() + "");
				}
				result.setGenre(tmp);

				tmp = new ArrayList<>();
				for ( String g : ser.getOriginCountry() ) {
					tmp.add(g.toLowerCase());
				}
				result.setPays(tmp);

				if( ser.getFirstAirDate() != null )
					result.setDate(ser.getFirstAirDate().split("-")[0]);

				List<PersonCast> c = ser.getCredits().getCast();

				ArrayList<String> acteurs = new ArrayList<>();
				for ( int i = 0; (c.size() > i) && (i < MAX_ACTOR_LIST); i++ ) {

					acteurs.add(c.get(i).getName());

				}

				tvShowActorsBuffer.put(choice, acteurs);
				tvShowGeneralBuffer.put(choice, result);
			}

			// Du coup peut importe, les résultats sont maintenant dispo en
			// buffer
			s.setActeurs(tvShowActorsBuffer.get(choice));

			// Du coup peut importe, les résultats sont maintenant dispo en
			// buffer
			Serie t = tvShowGeneralBuffer.get(choice);

			s.setNom(t.getNom());
			s.setRealisateurs(t.getRealisateurs());
			s.setPlaytime(t.getPlaytime());
			s.setGenre(t.getGenre());
			s.setPays(t.getPays());
			s.setDate(t.getDate());
		}
	}



	/**
	 * Récupération des données épisode
	 * 
	 * @param choice
	 *            ID tmdb
	 * @param s
	 *            Objet à modifier
	 * @param season
	 *            Numéro de saison
	 * @param episode
	 *            Numéro épisode
	 */
	private void getTvShowEpisodeData( String choice, Serie s, String season, String episode ) {

		// Si les données n'existent pas en buffer, on les y met
		synchronized ( tvShowSeasonsBuffer ) {

			if( !tvShowSeasonsBuffer.containsKey(choice + "-" + season) ) {

				TvSeason ts = api.getTvSeasons().getSeason(Integer.parseInt(choice), Integer.parseInt(season), lang);

				tvShowSeasonsBuffer.put(choice + "-" + season, ts);
			}

			// Du coup peut importe, les résultats sont maintenant dispo en
			// buffer
			TvSeason ts = tvShowSeasonsBuffer.get(choice + "-" + season);

			try {
				TvEpisode ep = ts.getEpisodes().get(Integer.parseInt(episode) - 1);

				if( (ts.getPosterPath() != null) && !ts.getPosterPath().isEmpty() ) {
					s.setAffichette("https://image.tmdb.org/t/p/w300" + ts.getPosterPath());
				} else {
					s.setAffichette("");
				}

				s.setNomEpisode(ep.getName());
				s.setSynopsis(ep.getOverview());
				s.setNoSaison(Integer.parseInt(season));
				s.setNoEpisode(Integer.parseInt(episode));
				
			} catch ( Exception e ) {
				logger.error("Impossible de trouver/ajouter cet épisode : " + s.getNom() + " saison "
						+ ts.getSeasonNumber() + "  ep" + episode, e);
			}
		}
	}



	/**
	 * Récupération de la liste des acteurs
	 * 
	 * @param choice
	 *            ID tmdb
	 * @param s
	 *            Objet à modifier
	 */
	/*
	 * private void getTvShowActorList( String choice, Serie s ) {
	 * 
	 * synchronized ( tvShowActorsBuffer ) {
	 * 
	 * if( !tvShowActorsBuffer.containsKey(choice) ) {
	 * 
	 * List<PersonCast> c =
	 * api.getTvSeries().getCredits(Integer.parseInt(choice), lang).getCast();
	 * 
	 * ArrayList<String> acteurs = new ArrayList<>(); for ( int i = 0; (c.size()
	 * > i) && (i < MAX_ACTOR_LIST); i++ ) {
	 * 
	 * acteurs.add(c.get(i).getName());
	 * 
	 * }
	 * 
	 * tvShowActorsBuffer.put(choice, acteurs); }
	 * 
	 * // Du coup peut importe, les résultats sont maintenant dispo en // buffer
	 * s.setActeurs(tvShowActorsBuffer.get(choice)); } }
	 */

	private void getMovieData( String choice, Film f ) {

		ArrayList<String> tmp;

		// Appel à l'API pour récupérer les infos globales d'un film
		MovieDb mov = api.getMovies().getMovie(Integer.valueOf(choice), lang, MovieMethod.credits);

		f.setNom(mov.getTitle());
		f.setOriginalName(mov.getOriginalTitle());
		if( mov.getOverview() != null )
			f.setSynopsis(mov.getOverview());

		tmp = new ArrayList<>();
		for ( Genre g : mov.getGenres() ) {
			tmp.add(g.getName() + "");
		}
		f.setGenre(tmp);

		if( (mov.getPosterPath() != null) && !mov.getPosterPath().isEmpty() ) {
			f.setAffichette("https://image.tmdb.org/t/p/w300" + mov.getPosterPath());
		} else {
			f.setAffichette("");
		}

		f.setPlaytime(String.valueOf(mov.getRuntime()));

		tmp = new ArrayList<>();
		for ( ProductionCountry g : mov.getProductionCountries() ) {
			tmp.add(g.getIsoCode().toLowerCase());
		}
		f.setPays(tmp);

		f.setDate(mov.getReleaseDate().split("-")[0]);

		// APPEL à l'API pour récupérer la liste des acteurs & réalisateur
		// Credits c = api.getMovies().getCredits(Integer.valueOf(choice));
		Credits c = mov.getCredits();

		tmp = new ArrayList<>();
		for ( int i = 0; (c.getCast().size() > i) && (i < MAX_ACTOR_LIST); i++ ) {
			tmp.add(c.getCast().get(i).getName());
		}
		f.setActeurs(tmp);

		tmp = new ArrayList<>();
		for ( int i = 0, j = 0; (c.getCrew().size() > i) && (j < MAX_DIRECTOR_LIST); i++ ) {

			if( c.getCrew().get(i).getJob().equals("Director") ) {
				tmp.add(c.getCrew().get(i).getName());
				j++;
			}
		}
		f.setRealisateurs(tmp);

	}



	@Override
	public EntiteVideo parseData( String choice, int searchMode, String season, String episode ) {

		EntiteVideo result = null;

		if( searchMode == TV_SHOW ) {

			result = new Serie();

			// récupération des données générales
			getTvShowData(choice, (Serie) result);

			// récupération des données épisode
			getTvShowEpisodeData(choice, (Serie) result, season, episode);

			// récupération de la liste des acteurs
			// getTvShowActorList(choice, (Serie) result);

		}

		else if( searchMode == MOVIE ) {

			result = new Film();

			getMovieData(choice, (Film) result);
			// printMovie((Film) result);
		}

		return result;
	}



	@Override
	public ArrayList<String[]> search( String query, int searchMode ) {

		// Si on recherche un film
		if( searchMode == MOVIE )
			return searchMovie(query);
		// Si on recherche une s�rie
		else if( searchMode == TV_SHOW )
			return searchTv(query);
		else
			return null;
	}



	/**
	 * 
	 * @param query
	 * @return
	 */
	private ArrayList<String[]> searchMovie( String query ) {

		String title, year = "", realisateur = "", url;
		ArrayList<String[]> result = new ArrayList<String[]>();

		MovieResultsPage res = api.getSearch().searchMovie(query, 0, lang, false, 0);

		for ( MovieDb movieDb : res.getResults() ) {

			title = movieDb.getTitle();

			if( (movieDb.getReleaseDate() != null) && !movieDb.getReleaseDate().isEmpty() )
				year = " (" + movieDb.getReleaseDate().split("-")[0] + ") ";

			/*
			 * List<PersonCrew> list =
			 * api.getMovies().getCredits(movieDb.getId()).getCrew(); for
			 * (PersonCrew personCrew : list) {
			 * 
			 * if (personCrew.getJob().equals("Director")) { realisateur =
			 * personCrew.getName(); } }
			 */
			url = movieDb.getId() + "";

			result.add(new String[] { title + year + realisateur, url });

			// System.out.println(title + year + realisateur);
		}
		return result;
	}



	/**
	 * 
	 * @param query
	 * @return
	 */
	private ArrayList<String[]> searchTv( String query ) {

		String title, year = "", realisateur = "", url;
		ArrayList<String[]> result = new ArrayList<String[]>();

		TvResultsPage res = api.getSearch().searchTv(query, lang, 0);

		for ( TvSeries tvSeries : res ) {

			title = tvSeries.getName();

			if( (tvSeries.getFirstAirDate() != null) && !tvSeries.getFirstAirDate().isEmpty() )
				year = " (" + tvSeries.getFirstAirDate().split("-")[0] + ") ";

			tvSeries.getCreatedBy();

			/*
			 * List<Person> list = api.getTvSeries().getSeries(tvSeries.getId(),
			 * lang).getCreatedBy();
			 * 
			 * if (list.size() != 0) realisateur = list.get(0).getName();
			 */

			url = tvSeries.getId() + "";

			result.add(new String[] { title + year + realisateur, url });

			// System.out.println(title + year + realisateur);
		}
		return result;
	}



	private void printMovie( Film f ) {
		System.out.println(f.getNom());
		System.out.println(f.getActeurs().toString());
		System.out.println(f.getRealisateurs());
		System.out.println(f.getGenre().toString());
		System.out.println(f.getSynopsis());
		System.out.println(f.getPays().toString());
		System.out.println(f.getAffichette());
		System.out.println(f.getOriginalName());
		System.out.println(f.getPlaytime());
		System.out.println(f.getDate());
	}



	private void printTv( Serie s ) {
		System.out.println(s.getNom());
		System.out.println(s.getNomEpisode());
		System.out.println(s.getActeurs().toString());
		System.out.println(s.getRealisateurs());
		System.out.println(s.getGenre().toString());
		System.out.println(s.getSynopsis());
		System.out.println(s.getPays().toString());
		System.out.println(s.getAffichette());
		// System.out.println(s.getOriginalName());
		System.out.println(s.getPlaytime());
		System.out.println(s.getDate());
	}



	public static void main( String[] args ) {

		TMDB t = new TMDB("fr");

		 ArrayList<String[]> tmp = t.search("antartica", MOVIE);
		 
		 for ( String[] strings : tmp ) {
			System.out.println(strings[0] + "  |  "+strings[1]);
		}
		 

		/*
		 * Serie s = (Serie) t.parseData("1418", TV_SHOW, 7+"", 8+"");
		 * 
		 * System.out.println(s.getNom());
		 * System.out.println(s.getNomEpisode());
		 * System.out.println(s.getActeurs().toString());
		 * System.out.println(s.getRealisateurs());
		 * System.out.println(s.getGenre().toString());
		 * System.out.println(s.getSynopsis());
		 * System.out.println(s.getPays().toString());
		 * System.out.println(s.getAffichette());
		 * //System.out.println(s.getOriginalName());
		 * System.out.println(s.getPlaytime()); System.out.println(s.getDate());
		 */

		/*Film f = (Film) t.parseData("550", MOVIE, null, null);

		System.out.println(f.getNom());
		System.out.println(f.getActeurs().toString());
		System.out.println(f.getRealisateurs());
		System.out.println(f.getGenre().toString());
		System.out.println(f.getSynopsis());
		System.out.println(f.getPays().toString());
		System.out.println(f.getAffichette());
		System.out.println(f.getOriginalName());
		System.out.println(f.getPlaytime());
		System.out.println(f.getDate());*/

	}
}
