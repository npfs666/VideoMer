package modele;

import java.util.ArrayList;
import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import components.model.Filter1ListModel;


/**
 * <code>Bibliotheque</code> représente tous les éléments.
 * Cette classe gère toutes les opérations qu'on peut faire
 * sur une collection (importer, exporter, récupérer la liste des
 * acteurs, réalisateurs, ...) 
 * 
 * @author Brendan
 * @version 0.1 6/07/2010
 */
@SuppressWarnings("serial")
public class Bibliotheque {

	/**
	 * Base de données
	 */
	private ObjectContainer db;
	
	
	
	public Bibliotheque(ObjectContainer db) {
		this.db = db;
	}

	/**
	 * 
	 * @param ev
	 */
	public void add(EntiteVideo ev) {
		db.store(ev);
	}

	public void edit() {
		//TODO: trouver l'elt, puis le modifier
	}

	/**
	 * 
	 * @param ev
	 */
	public void remove(EntiteVideo ev) {
		db.delete(ev);
	}

	/**
	 * Renvoie la liste correspondant à la catégorie
	 * 
	 * @param categorie filtre 1
	 * @return
	 */
	public ArrayList<EntiteVideo> getListe(final int categorie) {

		List<EntiteVideo> e = db.query(new Predicate<EntiteVideo>() {
			public boolean match(EntiteVideo c) {
				if(  categorie == 0 || c.getCategorie() == categorie ) return true;
				return false;
			}
		});

		ArrayList<EntiteVideo> ev = new ArrayList<EntiteVideo>(e);

		//downloadImage(ev);
		return ev;
	}

	/**
	 * 	Renvoie la liste des éléments correspondant aux filtres 1 & 2
	 * 
	 * @param categorie filtre1
	 * @param filtre filtre 2
	 * @param indices
	 * @param values
	 * @return
	 */
	public ArrayList<EntiteVideo> getListe(final int categorie, final int filtre, final int[] indices, 
			final Object[] values) {

		if( indices.length > 0 && indices[0] == 0 )
			return getListe(categorie);


		List<EntiteVideo> e = db.query(new Predicate<EntiteVideo>() {
			public boolean match(EntiteVideo c) {

				if( categorie == EntiteVideo.SERIE ) {
					for( Object o : values ) {
						if( c.getNom().equals(o) ) {
							return true;
						}
					}
					return false;
				} else if( filtre == Filter1ListModel.GENRE ) {
					for( Object o : values ) {
						if( (categorie == 0 || categorie == c.getCategorie()) && c.getGenre().contains(o) ) {
							return true;
						}
					}
					return false;
				} else if(filtre == Filter1ListModel.REALISATEUR) {
					for( Object o : values ) {
						if( (categorie == 0 || categorie == c.getCategorie()) && c.getRealisateurs().contains(o) ) {
							return true;
						}
					}
					return false;
				} else if(filtre == Filter1ListModel.ACTEURS) {
					for( Object o : values ) {
						if( (categorie == 0 || categorie == c.getCategorie()) && c.getActeurs().contains(o) ) {
							return true;
						}
					}
					return false;
				} else if(filtre == Filter1ListModel.PAYS) {
					for( Object o : values ) {
						if( (categorie == 0 || categorie == c.getCategorie()) && c.getPays().contains(o) ) {
							return true;
						}
					}
					return false;
				} else 
					return false;
			}
		});

		return new ArrayList<EntiteVideo>(e);
	}

	/**
	 * Renvoie la liste des éléments correspondant aux filtres 1 & 2 & 3
	 * 
	 * @param categorie filtre1
	 * @param filtre filtre2
	 * @param indices
	 * @param values
	 * @param filtre2 filtre 3
	 * @param indices2
	 * @param values2
	 * @return
	 */
	public ArrayList<EntiteVideo> getListe(final int categorie, final int filtre, final int[] indices, 
			final Object[] values, final int filtre2, final int[] indices2, final Object[] values2) {


		// [X] [Tout] [Tout]
		if(indices.length > 0 && indices[0] == 0 && indices2.length > 0 && indices2[0] == 0 )
			return getListe(categorie);

		// [SERIE] [Tout] [X]
		else if( categorie == EntiteVideo.SERIE && indices.length > 0 && indices[0] == 0 ) {
			List<Serie> e = db.query(new Predicate<Serie>() {
				public boolean match(Serie c) {
					for( Object o : values2 ) {
						Serie s = (Serie)c;
						String tmp = ((String)o).split(" ")[1];
						if(s.getNoSaison() == Integer.valueOf(tmp) ) {
							return true;
						}
					}
					return false;
				}
			});
			return new ArrayList<EntiteVideo>(e);
		}
		// [X] [Tout] [X]
		else if( indices.length > 0 && indices[0] == 0 )
			return getListe(categorie, filtre2, indices2, values2);

		// [X] [X] [Tout]
		else if( indices2.length > 0 && indices2[0] == 0 )
			return getListe(categorie, filtre, indices, values);


		// Requete restante
		// [X] [X] [X]
		List<EntiteVideo> e = db.query(new Predicate<EntiteVideo>() {
			public boolean match(EntiteVideo c) {

				if( categorie == EntiteVideo.SERIE ) {
					for( Object o : values ) {
						for( Object o2 : values2 ) {
							Serie s = (Serie)c;
							String tmp = ((String)o2).split(" ")[1];
							if( c.getNom().equals(o) && s.getNoSaison() == Integer.valueOf(tmp) ) {
								return true;
							}
						}
					}
					return false;
					
				} else if( (filtre == Filter1ListModel.GENRE && filtre2 == Filter1ListModel.REALISATEUR) ||
						(filtre2 == Filter1ListModel.GENRE && filtre == Filter1ListModel.REALISATEUR) ) {
					for( Object o : values ) {
						for( Object o2 : values2 ) {
							if( ( categorie == 0 ||categorie == c.getCategorie()) && 
									(c.getGenre().contains(o) || c.getRealisateurs().contains(o)) && 
									(c.getGenre().contains(o2) || c.getRealisateurs().contains(o2)) ) {
								return true;
							}
						}
					}
					return false;
					
				} else if((filtre == Filter1ListModel.GENRE && filtre2 == Filter1ListModel.PAYS) ||
						(filtre2 == Filter1ListModel.GENRE && filtre == Filter1ListModel.PAYS)) {
					for( Object o : values ) {
						for( Object o2 : values2 ) {
							if( ( categorie == 0 ||categorie == c.getCategorie()) && 
									(c.getGenre().contains(o) || c.getPays().contains(o)) && 
									(c.getGenre().contains(o2) || c.getPays().contains(o2)) ) {
								return true;
							}
						}
					}
					return false;
					
				} else if((filtre == Filter1ListModel.REALISATEUR && filtre2 == Filter1ListModel.PAYS) ||
						(filtre2 == Filter1ListModel.REALISATEUR && filtre == Filter1ListModel.PAYS)) {
					for( Object o : values ) {
						for( Object o2 : values2 ) {
							if( ( categorie == 0 ||categorie == c.getCategorie()) && 
									(c.getRealisateurs().contains(o) || c.getPays().contains(o)) && 
									(c.getRealisateurs().contains(o2) || c.getPays().contains(o2)) ) {
								return true;
							}
						}
					}
					return false; 
				} else
					return false;
			}
		});

		return new ArrayList<EntiteVideo>(e);
	}
}
