package com.staligtredan.VideoMer.controleur;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.staligtredan.VideoMer.modele.Bibliotheque;
import com.staligtredan.VideoMer.modele.Preferences;
import com.staligtredan.VideoMer.modele.SQL;
import com.staligtredan.VideoMer.update.modeleXml.Properties;
import com.staligtredan.VideoMer.vue.JFramePrincipal;

/**
 * Le controleur permet de stocker toutes les données "Globales" au projet
 * (la langue, les options, etc..) <br />
 * 
 * @author Brendan Jaouen
 * @since 0.1 1/10/2010
 * @version 1.0.0 20/01/2015 (MAJ de db4o vers SQLite)
 * @version 1.0.3 10/12/2016 MAJ pour les nouvelles préférences
 * 						 	 Changement du getListeLangue
 * @version 2.0.0 20/12/2016 Loging et gestion d'erreurs générales
 * 							 DB4O exclus complètement
 */
public class DefaultController {

	public static String SQLITE_FILENAME = "sql.db";
	
	final static Logger logger = LogManager.getLogger(DefaultController.class);

	
	/**
	 * Base de données SQL
	 */
	private SQL sql;

	/**
	 * Préférences utilisateur du logiciel
	 */
	private Preferences preferences;
	
	/**
	 * Gestionnaire de la Bibliothèque
	 */
	private Bibliotheque bibliotheque;
	
	/**
	 * Gestionnaire d'affichage de langue
	 */
	private ResourceBundle bundle;
	
	/**
	 * Stocke l'écran principal
	 */
	private JFramePrincipal mainFrame;

	

	/**
	 * Crée une instance du controlleur
	 */
	public DefaultController() {
		
		// Si MAJ vers sqlite on met un flag ou nouvelle install
		File fsql = new File(SQLITE_FILENAME);
		boolean flag = false;
		
		if( !fsql.exists() ) {
			flag = true;
		}

		sql = new SQL(SQLITE_FILENAME);
		sql.connect();
		sql.createPrefN();

		// Si sqlite nouveau on crée les tables et ajout des préférences par défaut
		if( flag ) {

			sql.createTables();
			sql.addPrefN(Preferences.DefaultPreferences());
		}
		
		// Créations des dossiers nécéssaires
		createDefaultFolders();
		
		// Si changement vers le nouveau système de préférences
		try {
			if( ! sql.query("SELECT * FROM PREFN").next() ) {
				
				sql.addPrefN(sql.readPref());
			}
		} catch (SQLException e) {
			logger.error("Impossible de lire la table prefN : SELECT * FROM PREFN", e);
		} catch (IOException e) {
			logger.error("Impossible de lire le fichier properties", e);
		}
		
		// Lecture des préférences
		preferences = sql.readPrefN();
		
		// Lecture des entités videos
		bibliotheque = new Bibliotheque(sql.readAllEv());
		bibliotheque.sql = sql;
		
		// Chargement des langues
		setLocale();
	}

	
	
	//-------------------------//
	//        Général          //
	//-------------------------//
	
	
	
	/**
	 * Crée les dossiers nécéssaires au fonctionnement
	 */
	private void createDefaultFolders() {
		
		// Dossier Look&Feel
		//File lookAndFeel = new File("lookandfeel/");
		//if( !lookAndFeel.exists() ) lookAndFeel.mkdir();
		
		// Dossier affichettes
		File posters = new File("posters/");
		if( !posters.exists() ) posters.mkdir();
	}

	/**
	 * Récupère la langue du fichier de config
	 */
	public ResourceBundle getBundle() {
		return bundle;
	}

	/**
	 * Défini la Locale actuelle
	 * 
	 * @return
	 */
	private void setLocale() {

		String[] lang = preferences.getLang().split("_");

		if( lang.length == 1 )
			Locale.setDefault(new Locale(lang[0]));
		else if( lang.length == 2 )
			Locale.setDefault(new Locale(lang[0], lang[1]));
		
		bundle = ResourceBundle.getBundle("com/staligtredan/VideoMer/lang/lang", Locale.getDefault());
	}

	/**
	 * Ferme la BDD
	 */
	public void closeDatabase() {
		
		Properties properties = new Properties();
		properties.loadFromFile();
		properties.setLanguage(preferences.getLang());
		properties.saveToFile();

		sql.editPrefN(preferences);

		sql.close();
	}
	
	/**
	 * Renvoie la Bibliotheque
	 * 
	 * @return the bibliotheque
	 */
	public Bibliotheque getBibliotheque() {
		return bibliotheque;
	}
	
	/**
	 * Affiche une boite de dialogue avec l'erreur en question
	 */
	public void erreur( Throwable e ) {
		JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * @return Instance le la fenêtre principale
	 */
	public JFramePrincipal getMainFrame() {
		return mainFrame;
	}

	/**
	 * @param mainFrame Instance le la fenêtre principale
	 */
	public void setMainFrame(JFramePrincipal mainFrame) {
		this.mainFrame = mainFrame;
	}

	

	//-------------------------//
	//     Actions Options     //
	//-------------------------//

	

	/**
	 * Renvoie les préférences
	 * 
	 * @return the preferences
	 */
	public Preferences getPreferences() {
		return preferences;
	}
	
	/**
	 * Met à jour les préférences
	 * 
	 * @param preferences
	 */
	public void setPreferences(Preferences preferences) {
		this.preferences = preferences;
		setLocale();
	}

	/**
	 * Renvoi la liste des langues disponibles
	 */
	public Object[] getListeLangues() {

		ArrayList<Locale> langues = new ArrayList<Locale>();
		
		Locale locale = new Locale("fr");
		for( Locale l : Locale.getAvailableLocales() ) {
			if( l.equals(locale) ) {
				langues.add(l);
			}
		}
		
		locale = new Locale("en");
		for( Locale l : Locale.getAvailableLocales() ) {
			if( l.equals(locale) ) {
				langues.add(l);
			}
		}
		
		/*File folder = new File("lang");
		
		for( File f : folder.listFiles() ) {
			
			if( f.getName().matches("lang_[a-z]{2}.properties") ) {
				Pattern p = Pattern.compile("lang_([a-z]{2}).properties");
				Matcher m = p.matcher(f.getName());
				m.find();
				Locale locale = new Locale(m.group(1));

				for( Locale l : Locale.getAvailableLocales() ) {
					if( l.equals(locale) ) {
						langues.add(l);
					}
				}				
			} else if ( f.getName().matches("lang_[a-z]{2}_[A-Z]{2}.properties") ) {
				Pattern p = Pattern.compile("lang_([a-z]{2})_([A-Z]{2}).properties");
				Matcher m = p.matcher(f.getName());
				m.find();
				Locale locale = new Locale(m.group(1), m.group(2));
				
				for( Locale l : Locale.getAvailableLocales() ) {
					if( l.equals(locale) ) {
						langues.add(l);
					}
				}
			}
		}*/
		
		//Collections.sort(langues);
		return langues.toArray();
	}



	//-------------------------//
	//   	 Look and Feel     //
	//-------------------------//



	/**
	 * Renvoie la liste des Look And Feel disponible
	 */
	public Object[] getListeLookAndFeel() {

		String[] laf = new String[UIManager.getInstalledLookAndFeels().length];

		int i = 0;
		for( LookAndFeelInfo lf : UIManager.getInstalledLookAndFeels() ) {
			laf[i++] = lf.getName();
		}

		return laf;
	}

	/**
	 * Renvoie la classe du Look & Feel natif selon son nom
	 * Si rien n'est défini on renvoie le style par défaut.
	 */
	public String getLookAndFeel() {
		for( LookAndFeelInfo lf : UIManager.getInstalledLookAndFeels() ) {
			if( lf.getName().equalsIgnoreCase(preferences.getLookAndFeel()) )
				return lf.getClassName();
		}
		return "javax.swing.plaf.metal.MetalLookAndFeel";
	}

	/**
	 * Installe tous les l&f disponibles dans le dossier de skins
	 * 
	 * @throws IOException 
	 */
	/*public void installLookAndFeels() throws IOException  {

		// Recherche & installation des Look And Feel dispo dans le dossier skin.		
		File[] children = new File("lookandfeel/").listFiles();
		//Pour chaque fichier du repertoire des look&feel
		for(int j = 0; j < children.length; j++){
			//Si c'est un fichier jar
			if(children[j].toString().endsWith(".jar"))
				installLookAndFeel(children[j].getName().replace(".jar", ""));

		}
	}*/

	/**
	 * Cette m�thode permet de naviguer dans les �l�ments d'un .jar
	 * pour y retrouver les look & feel qui s'y trouvent et de les installer. <br>
	 * Si l'un d'eux correspond � celui � appliquer il est appliqu�
	 * 
	 * @param jarName Nom du fichier .jar � explorer
	 * 
	 * @return Vrai si un l&f a �t� appliqu�
	 * @throws IOException 
	 */
	/*private void installLookAndFeel(String jarName) throws IOException {

		URLClassLoader loader;
		String tmp;
		Class<?> tmpClass;
		Enumeration<JarEntry> e;

		File laf = new File("lookandfeel/"+jarName+".jar");

		//On crée un nouveau URLClassLoader pour charger le jar qui se trouve ne dehors du CLASSPATH
		loader = new URLClassLoader(new URL[] {laf.toURI().toURL()});

		//On charge le jar en mémoire
		JarFile jar = new JarFile(laf.getAbsolutePath());

		e = jar.entries();

		//Pour chaque fichier du jar
		while( e.hasMoreElements() ) {

			tmp = e.nextElement().toString();

			//On vérifie que le fichier courant est un .class 
			if( tmp.endsWith(".class") ) {

				tmp = tmp.substring(0,tmp.length()-6);
				tmp = tmp.replaceAll("/",".");
				String nom = tmp.substring(tmp.lastIndexOf(".")+1);

				try {
					tmpClass = Class.forName(tmp ,true,loader);
					tmpClass.asSubclass(BasicLookAndFeel.class);
					UIManager.installLookAndFeel(nom, tmp);
				} catch (ClassNotFoundException e1) {
					logger.error("Erreur chargement l&f", e);
				}
			}
		}
		jar.close();
	}*/
}
