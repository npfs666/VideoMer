package modele;

import java.util.ArrayList;
import java.util.Date;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;


public class RemplirBDD {
	
	@SuppressWarnings("deprecation")
	public RemplirBDD() {

		ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "db.db4o");
		ArrayList<String> liste;
		
		// Films
		Film f1 = new Film();
		f1.setNom("Equilibrium");
		f1.setDate(new Date(2003, 7, 9));
		liste = new ArrayList<String>();
		liste.add("Christian Bale");liste.add("Emily Watson");liste.add("Taye Diggs");
		f1.setActeurs(liste);
		liste = new ArrayList<String>();
		liste.add("Science fiction");liste.add("Thriller");liste.add("Action");
		f1.setGenre(liste);
		f1.setAffichette("http://images.allocine.fr/r_160_214/b_1_cfd7e1/medias/nmedia/18/35/10/12/affiche.jpg");
		liste = new ArrayList<String>();
		liste.add("Kurt Wimmer");
		f1.setRealisateurs(liste);
		f1.setEmplacement("K:\\Films\\Equilibrium.avi");
		liste = new ArrayList<String>();
		liste.add("Amérique");
		f1.setPays(liste);
		db.store(f1);
		
		Film f2 = new Film();
		f2.setNom("3h10 pour Yuma");
		f2.setDate(new Date(2008, 3, 26));
		liste = new ArrayList<String>();
		liste.add("Russell Crowe");liste.add("Christian Bale");liste.add("Peter Fonda");
		f2.setActeurs(liste);
		liste = new ArrayList<String>();
		liste.add("Western");liste.add("Drame");
		f2.setGenre(liste);
		f2.setAffichette("http://images.allocine.fr/r_160_214/b_1_cfd7e1/medias/nmedia/18/64/46/69/18922562.jpg");
		liste = new ArrayList<String>();
		liste.add("James Mangold");
		f2.setRealisateurs(liste);
		f2.setEmplacement("K:\\Films\\Gangs Of New York.avi");
		liste = new ArrayList<String>();
		liste.add("Amérique");liste.add("Mexique");
		f2.setPays(liste);
		db.store(f2);
		
		Film f3 = new Film();
		f3.setNom("2012");
		f3.setDate(new Date(2009, 11, 11));
		liste = new ArrayList<String>();
		liste.add("John Cusack");liste.add("Chiwetel Ejiofor");liste.add("Amanda Peet");
		f3.setActeurs(liste);
		liste = new ArrayList<String>();
		liste.add("Science fiction");
		f3.setGenre(liste);
		f3.setAffichette("http://images.allocine.fr/r_160_214/b_1_cfd7e1/medias/nmedia/18/68/10/09/19186143.jpg");
		liste = new ArrayList<String>();
		liste.add("Roland Emmerich");
		f3.setRealisateurs(liste);
		f3.setEmplacement("K:\\Films\\2012.avi");
		liste = new ArrayList<String>();
		liste.add("Amérique");liste.add("Chine");
		f3.setPays(liste);
		db.store(f3);
		
		Film f4 = new Film();
		f4.setNom("Casino Royale");
		f4.setDate(new Date(2006, 11, 22));
		liste = new ArrayList<String>();
		liste.add("Daniel Craig");liste.add("Eva Green");liste.add("Mads Mikkelsen");
		f4.setActeurs(liste);
		liste = new ArrayList<String>();
		liste.add("Espionnage");liste.add("Thriller");liste.add("Action");
		f4.setGenre(liste);
		f4.setAffichette("http://images.allocine.fr/r_160_214/b_1_cfd7e1/medias/nmedia/18/36/12/35/18674702.jpg");
		liste = new ArrayList<String>();
		liste.add("Martin Campbell  ");
		f4.setRealisateurs(liste);
		f4.setEmplacement("K:\\Films\\Casino Royale.avi");
		liste = new ArrayList<String>();
		liste.add("Amérique");liste.add("Grande-Bretagne");
		f4.setPays(liste);
		db.store(f4);
		
		Film f5 = new Film();
		f5.setNom("Coco avant Chanel");
		f5.setDate(new Date(2009, 4, 22));
		liste = new ArrayList<String>();
		liste.add("Audrey Tautou");liste.add("Benoît Poelvoorde");liste.add("Alessandro Nivola");
		f5.setActeurs(liste);
		liste = new ArrayList<String>();
		liste.add("Biopic");
		f5.setGenre(liste);
		f5.setAffichette("http://images.allocine.fr/r_160_214/b_1_cfd7e1/medias/nmedia/18/67/61/34/19063894.jpg");
		liste = new ArrayList<String>();
		liste.add("Anne Fontaine");
		f5.setRealisateurs(liste);
		f5.setEmplacement("K:\\Films\\Coco Avant Chanel.mp4");
		liste = new ArrayList<String>();
		liste.add("France");
		f5.setPays(liste);
		db.store(f5);
		
		
		
		// Séries
		Serie s1 = new Serie();
		s1.setNom("How I Met Your Mother");
		liste = new ArrayList<String>();
		liste.add("Josh Radnor");liste.add("Jason Segel");liste.add("Alyson Hannigan");
		s1.setActeurs(liste);
		liste = new ArrayList<String>();
		liste.add("Comédie");
		s1.setGenre(liste);
		s1.setAffichette("http://thetvdb.com/banners/_cache/posters/75760-2.jpg");
		liste = new ArrayList<String>();
		liste.add("Carter Bays");liste.add("Craig Thomas");
		s1.setRealisateurs(liste);
		s1.setNoEpisode(1);
		s1.setNoSaison(1);
		liste = new ArrayList<String>();
		liste.add("Amérique");
		s1.setPays(liste);
		db.store(s1);
		
		Serie s2 = new Serie();
		s2.setNom("Chuck");
		liste = new ArrayList<String>();
		liste.add("Zachary Levi");liste.add("Yvonne Strahovski");liste.add("Adam Baldwin");
		s2.setActeurs(liste);
		liste = new ArrayList<String>();
		liste.add("Comédie");liste.add("Espionnage");liste.add("Action");
		s2.setGenre(liste);
		s2.setAffichette("http://thetvdb.com/banners/_cache/posters/80348-1.jpg");
		liste = new ArrayList<String>();
		liste.add("Josh Schwartz");liste.add("Chris Fedak");
		s2.setRealisateurs(liste);
		s2.setNoEpisode(15);
		s2.setNoSaison(1);
		liste = new ArrayList<String>();
		liste.add("Amérique");
		s1.setPays(liste);
		db.store(s2);
		
		
		db.close();
	}
	
    /**
     * Fonction main() qui permet de lancer le programme
     * 
     * @param args
     */
	public static void main(String[] args) {
		new RemplirBDD();
	}
}
