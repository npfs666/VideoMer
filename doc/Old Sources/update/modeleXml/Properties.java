package update.modeleXml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.swing.UIManager;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Permet la lecture/ecriture de données général au logiciel & au module de MAJ
 * 
 * @author Brendan
 * @version 0.1 23/04/2012
 */
public class Properties {
	
	private static final String propertiesFilePath = "properties.xml";
	
	/**
	 * Langue d'affichage
	 */
	private String language;
	
	/**
	 * systeme d'exploitation (différentes versions de vlc en fonction de l'OS)
	 */
	private int operatingSystem;
	
	/**
	 * Version actuelle du logiciel (de façon générale, chaque élément à ensuite une version perso)
	 */
	private String softwareVersion;
	
	/**
	 * La liste des fichiers installés actuellement (endroit & version)
	 */
	private ArrayList<Library> libraries;
	
	
	
	public Properties() {
		
		language = "";
		operatingSystem = 0;
		softwareVersion = "";
		libraries = new ArrayList<Library>();
	}

	public void defaultProperties() {
		
		language = "en";
		operatingSystem = Library.OS_WINDOWS;
		softwareVersion = "0.0.1";
		
		/*Library f = new Library();
		f.setLibName("DB4O");
		f.setFileName("lib/db4o.jar");
		f.setLibVersion("8.0.184.15484");
		libraries.add(f);
		
		f = new Library();
		f.setLibName("VLCJ");
		f.setFileName("lib/vlcj.jar");
		f.setLibVersion("1.2.2");
		libraries.add(f);

		f = new Library();
		f.setLibName("SwingX");
		f.setFileName("lib/swingx.jar");
		f.setLibVersion("1.6.3");
		libraries.add(f);
		
		f = new Library();
		f.setLibName("log4j");
		f.setFileName("lib/log4j.jar");
		f.setLibVersion("1.2.16");
		libraries.add(f);
		
		f = new Library();
		f.setLibName("JNA");
		f.setFileName("lib/jna.jar");
		f.setLibVersion("3.3.0");
		libraries.add(f);
		
		f = new Library();
		f.setLibName("Platform");
		f.setFileName("lib/platform.jar");
		f.setLibVersion("3.3.0");
		libraries.add(f);
		
		f = new Library();
		f.setLibName("Flexicombo");
		f.setFileName("lib/flexicombo.jar");
		f.setLibVersion("?");
		libraries.add(f);
		
		f = new Library();
		f.setLibName("l2fprod-buttonbar");
		f.setFileName("lib/l2fprod-buttonbar.jar");
		f.setLibVersion("?");
		libraries.add(f);
		
		f = new Library();
		f.setFileName("lib/libvlc.dll");
		f.setLibVersion("2.0.1");
		libraries.add(f);*/
	}
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		Properties  p = new Properties();
		
		p.defaultProperties();
		p.saveToFile();
	}
	
	public void loadFromFile() throws IOException {
		
		XStream xstream = new XStream(new XppDriver());
		
		// Définition des Alias
		xstream.alias("properties",this.getClass());
		xstream.alias("file",Library.class);
		
		// Ouverture du fichier
		File fichier = new File(propertiesFilePath);
		
		InputStreamReader isr = new InputStreamReader(new FileInputStream(fichier), "UTF-8");

		// Lecture du fichier XML
		try {
			Properties tmp = (Properties)xstream.fromXML(isr);
			setLanguage(tmp.getLanguage());
			setVersions(tmp.getVersions());
			setOperatingSystem(tmp.getOperatingSystem());
			setSoftwareVersion(tmp.getSoftwareVersion());
		} 
		finally {
            isr.close();
		}
	}
	
	public void saveToFile() throws IOException {
		
		XStream xstream = new XStream(new XppDriver());
		
		// Définition des Alias
		xstream.alias("properties",this.getClass());
		xstream.alias("file",Library.class);
		
		// Ouverture du fichier avec buffer
		File fichier = new File(propertiesFilePath);
	    
		// Si le fichier n'existe pas on le crée pour éviter les erreurs
		if( !fichier.canWrite() ) {
			fichier.createNewFile();
		}
	    
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fichier), "UTF-8");
		
	    // Enregistrement des données
	    try {
	    	xstream.toXML(this, osw);
	    } 
	    finally {
			osw.close();
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
	public int getOperatingSystem() {
		return operatingSystem;
	}

	/**
	 * @param operatingSystem the operatingSystem to set
	 */
	public void setOperatingSystem(int operatingSystem) {
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
