package com.staligtredan.VideoMer.modele;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.staligtredan.VideoMer.components.model.Filter1ListModel;

/**
 * <code>Bibliotheque</code> représente tous les éléments. Cette classe gère
 * toutes les opérations qu'on peut faire sur une collection (importer,
 * exporter, récupérer la liste des acteurs, réalisateurs, ...)
 * 
 * @author Brendan
 * @since 0.1 6/07/2010
 * @version 1.0.0 20/01/2015 (MAJ pour sqlite)
 * @version 1.0.3 10/12/2016 MAJ pour ne pas inclure EV de la BDD dans une recherche
 * @version 2.0.0 22/12/2016 Loging et erreurs, MAJ de la recherche pour les pseudo liens
 * @version 2.0.2 08/08/2017 Complété la fonction search pour prendre en compte les catégories partout
 */
public class Bibliotheque {

	final static Logger logger = LogManager.getLogger(Bibliotheque.class);
	
	/**
	 * Base de données
	 */
	private ArrayList<EntiteVideo> evList;
	
	/**
	 * Elements de la playlist
	 */
	private ArrayList<EntiteVideo> selection;
	
	/**
	 * Liste des élément du moteurs de recherche
	 */
	public enum Search {all, name, actors, directors, plot, country, genre;}
	
	/**
	 * Liste des élément du moteurs de recherche
	 */
	public enum Cat {any, documentary, movie, anime, tvshow;}
	
	/**
	 * Liste des fichiers présents dans la biblio, pour éviter les doublons par la suite
	 * @since 1.0.3
	 */
	private ArrayList<String> files;
	
	
	/**
	 * Connecteur à la base de données SQLite
	 */
	public SQL sql;
	

	
	/**
	 * Constructeur vide
	 */
	public Bibliotheque() {

		evList = new ArrayList<EntiteVideo>();
		files = new ArrayList<String>();
	}
	
	
	
	/**
	 * Construit une bibliothèque qui contient une liste d'EntiteVideo
	 * 
	 * @param list
	 */
	public Bibliotheque(List<EntiteVideo> list) {
		evList = new ArrayList<EntiteVideo>(list);
		files = new ArrayList<String>();
		for (EntiteVideo entiteVideo : list) {
			files.add(entiteVideo.getEmplacement().substring(entiteVideo.getEmplacement().lastIndexOf(File.separator)+1));
			//System.out.println(entiteVideo.getEmplacement().substring(entiteVideo.getEmplacement().lastIndexOf(File.separator)+1));
		}
	}
	
	
	
	/**
	 * Renvoie la liste des paths des fichiers de la base de données
	 * 
	 * @return
	 */
	public ArrayList<String> getFiles() {
		return files;
	}

	
	
	/**
	 * Ajoute une EntiteVideo à la bibliothèque, et trie
	 * 
	 * @param ev EntiteVideo
	 */
	public void add(EntiteVideo ev) {
		
		// Ajout direct à la base de données
		sql.addEv(ev);
		
		ResultSet rs = sql.query("SELECT last_insert_rowid()");
		
		// Ajout à la liste locale avec rajout de l'id
		try {
			if ( rs.next() ) {
				ev.id = rs.getInt(1);
				evList.add(ev);
				files.add(ev.getEmplacement().substring(ev.getEmplacement().lastIndexOf(File.separator)+1));
				Collections.sort(evList);
			}
		} catch (SQLException e) {
			logger.error("Erreur sql next()", e);
		}
		
	}
	
	public void addMultiple(ArrayList<EntiteVideo> list)  {
		
		ResultSet rs = sql.query("SELECT MAX(id) FROM EV");
		
		int index;
		
		try {
			index = rs.getInt(1) + 1;
			
			for (int count = 0; count < list.size() ; count ++) {
				
				for(int i = 0; (i < 10) && (list.size() > count); i++, count++) {
					
					sql.addBatch(list.get(count));
					list.get(count).id = index + count;
					evList.add(list.get(count));
					files.add(list.get(count).getEmplacement().substring(list.get(count).getEmplacement().lastIndexOf(File.separator)+1));
				}
				
				sql.executeBatch();
			}
			
			Collections.sort(evList);
			
		} catch (SQLException e) {
			logger.error("Erreur sql rs.getInt(1)", e);
		}
	}

	/**
	 * Renvoie un élément de la bibliothèque pour le modifier
	 * 
	 * @param oldMedia Element à modifier
	 * 
	 * @return
	 */
	public EntiteVideo edit(Object oldMedia ) {
		
		return evList.get(evList.indexOf(oldMedia));
	}
	
	/**
	 * Met à jour l'Ev dans la bdd
	 * 
	 * @param ev
	 */
	public void update(EntiteVideo ev) {
		sql.editEv(ev);
	}

	/**
	 * Enl�ve une EntiteVideo de la biblioth�que
	 * 
	 * @param ev
	 */
	public void remove(EntiteVideo ev) {
		
		evList.remove(ev);
		sql.removeEv(ev);
	}

	/**
	 * Search for results inside the database
	 * 
	 * @param pattern string to search
	 * @param search search type
	 * 
	 * @return an ArrayList of results
	 */
	public ArrayList<EntiteVideo> search(String pattern, Search search, Cat cat) {
		
		ArrayList<EntiteVideo> result = new ArrayList<EntiteVideo>();
		
		boolean filter = false;
		int filterV=0;
		
		switch ( cat ) {
		case movie:
			filter = true;
			filterV = EntiteVideo.FILM;
			break;

		case tvshow:
			filter = true;
			filterV = EntiteVideo.SERIE;
			break;
		
		default :
			break;
		}
		
		
		switch (search) {
		
		case all:
			for ( EntiteVideo ev : evList ) {

				for ( String actor : ev.getActeurs() ) {
					if( actor.toLowerCase().contains(pattern.toLowerCase()) && !result.contains(ev) )
						if( filter && (ev.getCategorie() == filterV) ) {
							result.add(ev);
						}else if( !filter ) {
							result.add(ev);
						}
				}

				for ( String director : ev.getRealisateurs() ) {
					if( director.toLowerCase().contains(pattern.toLowerCase()) && !result.contains(ev) )
						if( filter && (ev.getCategorie() == filterV) ) {
							result.add(ev);
						}else if( !filter ) {
							result.add(ev);
						}
				}

				for ( String pays : ev.getPays() ) {
					if( pays.toLowerCase().contains(pattern.toLowerCase()) && !result.contains(ev) )
						if( filter && (ev.getCategorie() == filterV) ) {
							result.add(ev);
						}else if( !filter ) {
							result.add(ev);
						}
				}

				if( ev.getNom().toLowerCase().contains(pattern.toLowerCase()) && !result.contains(ev) )
					if( filter && (ev.getCategorie() == filterV) ) {
						result.add(ev);
					}else if( !filter ) {
						result.add(ev);
					}

				if( ev.getSynopsis().toLowerCase().contains(pattern.toLowerCase()) && !result.contains(ev) )
					if( filter && (ev.getCategorie() == filterV) ) {
						result.add(ev);
					}else if( !filter ) {
						result.add(ev);
					}
			}
			break;
			
		case actors:
			for( EntiteVideo ev : evList ) {
				
				for( String actor : ev.getActeurs() ) {
					if( actor.toLowerCase().contains(pattern.toLowerCase()) ) {
						
						if( filter && (ev.getCategorie() == filterV) ) {
							result.add(ev);
						}else if( !filter ) {
							result.add(ev);
						}
					} 		
				}
				
			}
			break;
		
		case directors:
			for( EntiteVideo ev : evList ) {
				
				for( String director : ev.getRealisateurs() ) {
					if( director.toLowerCase().contains(pattern.toLowerCase()) )
						
						if( filter && (ev.getCategorie() == filterV) ) {
							result.add(ev);
						}else if( !filter ) {
							result.add(ev);
						}
				}
				
			}
			break;
			
		case name:
			for( EntiteVideo ev : evList ) {
				
				if( ev.getNom().toLowerCase().contains(pattern.toLowerCase()) )
					if( filter && (ev.getCategorie() == filterV) ) {
						result.add(ev);
					}else if( !filter ) {
						result.add(ev);
					}
			}
			break;
			
		case plot:
			for( EntiteVideo ev : evList ) {
				
				if( ev.getSynopsis().toLowerCase().contains(pattern.toLowerCase()) )
					if( filter && (ev.getCategorie() == filterV) ) {
						result.add(ev);
					}else if( !filter ) {
						result.add(ev);
					}
			}
			break;
		
		case country:
			for( EntiteVideo ev : evList ) {
				
				for( String pays : ev.getPays() ) {
					if( pays.toLowerCase().contains(pattern.toLowerCase()) )

						if( filter && (ev.getCategorie() == filterV) ) {
							result.add(ev);
						}else if( !filter ) {
							result.add(ev);
						}
				}
				
			}
			break;

		case genre:
			for( EntiteVideo ev : evList ) {
				
				for( String genre : ev.getGenre() ) {
					if( genre.toLowerCase().contains(pattern.toLowerCase()) )
						
						if( filter && (ev.getCategorie() == filterV) ) {
							result.add(ev);
						}else if( !filter ) {
							result.add(ev);
						}
				}
				
			}
			break;
			
		default:
			break;
		}
		
		return result;
	}
	
	/**
	 * Renvoie la liste correspondant à la catégorie
	 * 
	 * @param categorie
	 *            filtre 1
	 * @return
	 */
	public ArrayList<EntiteVideo> getListe(final int categorie) {

		selection = new ArrayList<EntiteVideo>();

		if (categorie == 0)
			return evList;

		for (EntiteVideo ev : evList) {

			if (ev.getCategorie() == categorie)
				selection.add(ev);
		}

		return selection;
	}

	/**
	 * Renvoie la liste des éléments correspondant aux filtres 1 & 2
	 * 
	 * @param categorie
	 *            filtre1
	 * @param filtre
	 *            filtre 2
	 * @param indices
	 * @param values
	 * @return
	 */
	public ArrayList<EntiteVideo> getListe(final int categorie, final int filtre, final int[] indices,
			final Object[] values) {

		// [X] [TOUT]
		if (indices.length > 0 && indices[0] == 0)
			return getListe(categorie);

		selection = new ArrayList<EntiteVideo>();

		for (EntiteVideo ev : evList) {

			// [SERIE] [NOM]
			if (categorie == EntiteVideo.SERIE && ev.getCategorie() == EntiteVideo.SERIE) {

				for (Object o : values) {
					if (ev.getNom().equals(o))
						selection.add(ev);
				}
			}
			// [X] [GENRE]
			else if ((categorie == 0 || ev.getCategorie() == categorie) && filtre == Filter1ListModel.GENRE) {

				for (Object o : values) {
					if (ev.getGenre().contains(o))
						selection.add(ev);
				}
			}
			// [X] [REALISATEUR]
			else if ((categorie == 0 || ev.getCategorie() == categorie) && filtre == Filter1ListModel.REALISATEUR) {

				for (Object o : values) {
					if (ev.getRealisateurs().contains(o))
						selection.add(ev);
				}
			}
			// [X] [ACTEURS]
			else if ((categorie == 0 || ev.getCategorie() == categorie) && filtre == Filter1ListModel.ACTEURS) {

				for (Object o : values) {
					if (ev.getActeurs().contains(o))
						selection.add(ev);
				}
			}
			// [X] [PAYS]
			else if ((categorie == 0 || ev.getCategorie() == categorie) && filtre == Filter1ListModel.PAYS) {

				for (Object o : values) {
					if (ev.getPays().contains(o))
						selection.add(ev);
				}
			}
		}

		return selection;
	}

	/**
	 * Renvoie la liste des éléments correspondant aux filtres 1 & 2 & 3
	 * 
	 * @param categorie
	 *            filtre1
	 * @param filtre
	 *            filtre2
	 * @param indices
	 * @param values
	 * @param filtre2
	 *            filtre 3
	 * @param indices2
	 * @param values2
	 * @return
	 */
	public ArrayList<EntiteVideo> getListe(final int categorie, final int filtre, final int[] indices,
			final Object[] values, final int filtre2, final int[] indices2, final Object[] values2) {

		selection = new ArrayList<EntiteVideo>();

		// [X] [Tout] [Tout]
		if (indices.length > 0 && indices[0] == 0 && indices2.length > 0 && indices2[0] == 0)
			return getListe(categorie);

		// [SERIE] [Tout] [X]
		else if (categorie == EntiteVideo.SERIE && indices.length > 0 && indices[0] == 0) {

			for (EntiteVideo ev : evList) {
				if (ev.getCategorie() == EntiteVideo.SERIE) {
					for (Object o : values2) {
						if (((Serie) ev).getNoSaison() == Integer.valueOf(((String) o).split(" ")[1]))
							selection.add(ev);
					}
				}

			}
		}
		// [X] [Tout] [X]
		else if (indices.length > 0 && indices[0] == 0)
			return getListe(categorie, filtre2, indices2, values2);

		// [X] [X] [Tout]
		else if (indices2.length > 0 && indices2[0] == 0)
			return getListe(categorie, filtre, indices, values);

		else {

			// Requete restante
			// [X] [X] [X]
			for (EntiteVideo ev : evList) {

				// [SERIE] [X] [X]
				if (categorie == EntiteVideo.SERIE && ev.getCategorie() == EntiteVideo.SERIE) {
					for (Object o : values) {
						for (Object o2 : values2) {
							if (((Serie) ev).getNom().equals(o)
									&& ((Serie) ev).getNoSaison() == Integer.valueOf(((String) o2).split(" ")[1]))
								selection.add(ev);
						}
					}
				}
				// [X] [GENRE] [REALISATEUR] (ou vice versa)
				else if ((filtre == Filter1ListModel.GENRE && filtre2 == Filter1ListModel.REALISATEUR)
						|| (filtre2 == Filter1ListModel.GENRE && filtre == Filter1ListModel.REALISATEUR)) {

					for (Object o : values) {
						for (Object o2 : values2) {
							if ((categorie == 0 || categorie == ev.getCategorie())
									&& (ev.getGenre().contains(o) || ev.getRealisateurs().contains(o))
									&& (ev.getGenre().contains(o2) || ev.getRealisateurs().contains(o2))) {
								selection.add(ev);
							}
						}
					}
				}
				// [X] [GENRE] [PAYS] (ou vice versa)
				else if ((filtre == Filter1ListModel.GENRE && filtre2 == Filter1ListModel.PAYS)
						|| (filtre2 == Filter1ListModel.GENRE && filtre == Filter1ListModel.PAYS)) {

					for (Object o : values) {
						for (Object o2 : values2) {
							if ((categorie == 0 || categorie == ev.getCategorie())
									&& (ev.getGenre().contains(o) || ev.getPays().contains(o))
									&& (ev.getGenre().contains(o2) || ev.getPays().contains(o2))) {
								selection.add(ev);
							}
						}
					}
				}
				// [X] [GENRE] [ACTEURS] (ou vice versa)
				else if ((filtre == Filter1ListModel.GENRE && filtre2 == Filter1ListModel.ACTEURS)
						|| (filtre2 == Filter1ListModel.GENRE && filtre == Filter1ListModel.ACTEURS)) {

					for (Object o : values) {
						for (Object o2 : values2) {
							if ((categorie == 0 || categorie == ev.getCategorie())
									&& (ev.getGenre().contains(o) || ev.getActeurs().contains(o))
									&& (ev.getGenre().contains(o2) || ev.getActeurs().contains(o2))) {
								selection.add(ev);
							}
						}
					}
				}
				// [X] [REALISATEUR] [ACTEURS] (ou vice versa)
				else if ((filtre == Filter1ListModel.REALISATEUR && filtre2 == Filter1ListModel.ACTEURS)
						|| (filtre2 == Filter1ListModel.REALISATEUR && filtre == Filter1ListModel.ACTEURS)) {

					for (Object o : values) {
						for (Object o2 : values2) {
							if ((categorie == 0 || categorie == ev.getCategorie())
									&& (ev.getRealisateurs().contains(o) || ev.getActeurs().contains(o))
									&& (ev.getRealisateurs().contains(o2) || ev.getActeurs().contains(o2))) {
								selection.add(ev);
							}
						}
					}
				}
				// [X] [PAYS] [ACTEURS] (ou vice versa)
				else if ((filtre == Filter1ListModel.PAYS && filtre2 == Filter1ListModel.ACTEURS)
						|| (filtre2 == Filter1ListModel.PAYS && filtre == Filter1ListModel.ACTEURS)) {

					for (Object o : values) {
						for (Object o2 : values2) {
							if ((categorie == 0 || categorie == ev.getCategorie())
									&& (ev.getPays().contains(o) || ev.getActeurs().contains(o))
									&& (ev.getPays().contains(o2) || ev.getActeurs().contains(o2))) {
								selection.add(ev);
							}
						}
					}
				}
				// [X] [PAYS] [REALISATEUR] (ou vice versa)
				else if ((filtre == Filter1ListModel.PAYS && filtre2 == Filter1ListModel.ACTEURS)
						|| (filtre2 == Filter1ListModel.PAYS && filtre == Filter1ListModel.ACTEURS)) {

					for (Object o : values) {
						for (Object o2 : values2) {
							if ((categorie == 0 || categorie == ev.getCategorie())
									&& (ev.getPays().contains(o) || ev.getActeurs().contains(o))
									&& (ev.getPays().contains(o2) || ev.getActeurs().contains(o2))) {
								selection.add(ev);
							}
						}
					}
				}
			}
		}

		return selection;
	}
}
