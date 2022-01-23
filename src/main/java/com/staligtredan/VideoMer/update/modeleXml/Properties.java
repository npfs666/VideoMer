package com.staligtredan.VideoMer.update.modeleXml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

import com.staligtredan.VideoMer.update.modeleXml.Library.OS;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Permet la lecture/ecriture de données générales au logiciel & au module de MAJ
 * 
 * @author Brendan
 * @since 0.1.0 23/04/2012
 * @version 2.0.0 22/12/2016 Intégration de la gestion des exceptions liées au fichier properties
 * 							 Nouveau loging
 */
public class Properties {
	
	/**
	 * Localisation du fichiers de configuration
	 */
	private static final String propertiesFilePath = "properties.xml";
	
	/**
	 * Langue d'affichage
	 */
	private String language;
	
	/**
	 * systeme d'exploitation (diff�rentes versions de vlc en fonction de l'OS)
	 */
	private OS operatingSystem;
	
	/**
	 * Version actuelle du logiciel (de façon générale, chaque élément à ensuite une version perso)
	 */
	private String softwareVersion;
	
	/**
	 * La liste des fichiers install�s actuellement (endroit & version)
	 */
	private ArrayList<Library> libraries;
	
	
	
	public Properties() {
		
		language = "";
		operatingSystem = Library.OS.unknown;
		softwareVersion = "";
		libraries = new ArrayList<Library>();
	}

	/**
	 * Crée des propriétés défaut
	 */
	private void defaultProperties() {
		
		language = Locale.getDefault().getLanguage().toLowerCase();
		operatingSystem = getPlatform();
		softwareVersion = getVersion();
		libraries = new ArrayList<Library>();
		listLibs(libraries);
	}

	
	private String getVersion() {
		
		String ver = "0.0.1";
		
		File folder = new File("lib");
		
		if( !folder.exists() || !folder.isDirectory() )
			return ver;
		
		for ( File f : folder.listFiles() ) {
			
			int i = f.getName().lastIndexOf("-");
			String nom = f.getName().substring(0, i);
			String version = f.getName().substring(i + 1).replaceAll(".jar", "");
			
			if( nom.equals("version") ) {
				return version;
			}
		}
		
		return ver;		
	}


	/**
	 * Fais la liste des librairies présentes dans le dossier ./lib/
	 */
	private void listLibs( ArrayList<Library> arl ) {

		File folder = new File("lib");
		Library l;

		if( !folder.exists() || !folder.isDirectory() )
			return;

		for ( File f : folder.listFiles() ) {

			int i = f.getName().lastIndexOf("-");
			String nom = f.getName().substring(0, i);
			String version = f.getName().substring(i + 1).replaceAll(".jar", "");

			l = new Library();
			l.setLibName(nom);
			l.setFilePath(f.getName());
			l.setLibVersion(version);
			l.setFileSize(f.length());
			l.setOperatingSystem(Library.OS.unknown);
			arl.add(l);
		}
	}



	/**
	 * Renvoie la plateforme actuelle
	 * 
	 * @return
	 */
	private static OS getPlatform() {

		String osName = System.getProperty("os.name").toLowerCase();

		if (osName.contains("win"))
			return OS.windows;

		if (osName.contains("mac"))
			return OS.macos;

		if (osName.contains("solaris"))
			return OS.solaris;

		if (osName.contains("sunos"))
			return OS.solaris;

		if (osName.contains("linux"))
			return OS.linux;

		if (osName.contains("unix"))
			return OS.linux;

		return OS.unknown;
	}
	
	/**
	 * Charge les propriétés depuis le fichier
	 * 
	 * @throws IOException
	 */
	public void loadFromFile() {
		
		// Ouverture du fichier
		File fichier = new File(propertiesFilePath);
		
		if( !fichier.exists() || !fichier.isFile() ) {
			defaultProperties();
			saveToFile();
			System.out.println("Création d'un nouveau fichier properties");
			return;
		}
		
		
		XStream xstream = new XStream(new XppDriver());
		Class<?>[] classes = new Class[] { Library.class, Properties.class };
		XStream.setupDefaultSecurity(xstream);
		xstream.allowTypes(classes);
		
		// Définition des Alias
		xstream.alias("properties",this.getClass());
		xstream.alias("file",Library.class);
		
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(fichier), "UTF-8");
			
			Properties tmp = (Properties)xstream.fromXML(isr);
			setLanguage(tmp.getLanguage());
			setVersions(tmp.getVersions());
			setOperatingSystem(tmp.getOperatingSystem());
			setSoftwareVersion(tmp.getSoftwareVersion());
			
			isr.close();
			
		} catch ( UnsupportedEncodingException e ) {
			//logger.error("Pb structure fichier properties", e);
			System.err.println("Pb structure fichier properties");
			e.printStackTrace();
		} catch ( FileNotFoundException e ) {
			//logger.error("Pb lecture fichier properties", e);
			System.err.println("Pb lecture fichier properties");
			e.printStackTrace();
		} catch ( IOException e ) {
			//logger.error("Pb ecriture fichier properties", e);
			System.err.println("Pb ecriture fichier properties");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sauvegarde les propriétés dans fichier
	 * 
	 * @throws IOException
	 */
	public void saveToFile() {
		
		XStream xstream = new XStream(new XppDriver());
		
		// Définition des Alias
		xstream.alias("properties",this.getClass());
		xstream.alias("file",Library.class);
		
		// Ouverture du fichier avec buffer
		File fichier = new File(propertiesFilePath);
	    
		try {
			
			// Si le fichier n'existe pas on le crée pour éviter les erreurs
			if( !fichier.canWrite() ) {
				fichier.createNewFile();
			}
			
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fichier), "UTF-8");
			
		    // Enregistrement des données
	    	xstream.toXML(this, osw);

			osw.close();
			
		} catch ( UnsupportedEncodingException e ) {
			//logger.error("Pb structure fichier properties", e);
			System.err.println("Pb structure fichier properties");
			e.printStackTrace();
		} catch ( FileNotFoundException e ) {
			//logger.error("Pb lecture fichier properties", e);
			System.err.println("Pb lecture fichier properties");
			e.printStackTrace();
		} catch ( IOException e ) {
			//logger.error("Pb ecriture fichier properties", e);
			System.err.println("Pb ecriture fichier properties");
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the operatingSystem
	 */
	public OS getOperatingSystem() {
		return operatingSystem;
	}

	/**
	 * @param operatingSystem the operatingSystem to set
	 */
	public void setOperatingSystem(OS operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	/**
	 * @return the softwareVersion
	 */
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	/**
	 * @param softwareVersion the softwareVersion to set
	 */
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	/**
	 * @return the versions
	 */
	public ArrayList<Library> getVersions() {
		return libraries;
	}

	/**
	 * @param versions the versions to set
	 */
	public void setVersions(ArrayList<Library> versions) {
		this.libraries = versions;
	}
}
