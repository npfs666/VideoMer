package update;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import update.modeleXml.Properties;

public class DefaultController {
	
	/**
	 * 
	 */
	private Properties properties;
	
	/**
	 * Gestionnaire d'affichage de langue
	 */
	private ResourceBundle bundle;
	
	
	
	public DefaultController() {
		
		properties = new Properties();
	}
	
	public void loadPropertiesFile() throws IOException {
		properties.loadFromFile();
	}
	
	/**
	 * Récupère la langue du fichier de config
	 */
	public ResourceBundle getBundle() {
		return bundle;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * Renvoi la liste des langues disponibles
	 */
	public Object[] getListeLangues() {

		ArrayList<Locale> langues = new ArrayList<Locale>();

		// TODO: mettre au clair le dossier langues avant exportations
		File folder = new File("lang");
		
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
		}
		
		return langues.toArray();
	}
	
	/**
	 * Défini la Locale actuelle
	 * 
	 * @return
	 */
	public void setLocale() {

		String[] lang = properties.getLanguage().split("_");

		if( lang.length == 1 )
			Locale.setDefault(new Locale(lang[0]));
		else if( lang.length == 2 )
			Locale.setDefault(new Locale(lang[0], lang[1]));
		
		bundle = ResourceBundle.getBundle("lang/lang", Locale.getDefault());
	}
}
