package com.staligtredan.VideoMer.update;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import com.staligtredan.VideoMer.update.modeleXml.Properties;

/**
 * Controlleur de l'appli de mise � jour
 * 
 * @author Brendan
 * @version 0.1 23/04/2012
 */
public class DefaultController {
	
	/**
	 * Fichier configuration du logiciel
	 */
	private Properties properties;
	
	/**
	 * Gestionnaire d'affichage de langue
	 */
	private ResourceBundle bundle;
	
	
	
	public DefaultController() {
		
		properties = new Properties();
		//setLocale();
	}
	
	/**
	 * Charge les préférences depuis le fichier 
	 * 
	 * @throws IOException
	 */
	public void loadPropertiesFile() {
		properties.loadFromFile();
	}
	
	/**
	 * Récupère la langue du fichier de config
	 */
	public ResourceBundle getBundle() {
		return bundle;
	}
	
	/**
	 * Renvoie les préférences
	 * 
	 * @return
	 */
	public Properties getProperties() {
		return properties;
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
		
		return langues.toArray(new Object[langues.size()]);
	}
	
	/**
	 * D�fini la Locale actuelle
	 * 
	 * @return
	 */
	public void setLocale() {

		String[] lang = properties.getLanguage().split("_");

		if( lang.length == 1 )
			Locale.setDefault(new Locale(lang[0]));
		else if( lang.length == 2 )
			Locale.setDefault(new Locale(lang[0], lang[1]));
		
		bundle = ResourceBundle.getBundle("com/staligtredan/VideoMer/lang/lang", Locale.getDefault());
	}
}
