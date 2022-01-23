package com.staligtredan.VideoMer.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.staligtredan.VideoMer.update.modeleXml.Library;
import com.staligtredan.VideoMer.update.modeleXml.Update;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Classe qui controle globalement la mise à jour, c'est ici que le travail est fait
 * 
 * @author Brendan
 * @since 0.1 23/04/2012
 * @version 1.0.3 10/12/2016 MAJ vers le stockage arstaligtredan OVH et MAVEN
 * @version 2.0.0 Prise en compte de la MAJ updater.jar
 * 				  Ajout du patchnote
 * 				  Correction globale du système de MAJ
 */
public class UpdateManager {

	//private final static String versionChannel = "http://master.dl.sourceforge.net/project/videomer/updates/versionChannel.xml";
	//private final static String versionChannel = "file:///D:/Mes Documents/Programmation/Java/Video Manager/doc/versionChannel.xml";
	//public final static String updateChannel = "http://master.dl.sourceforge.net/project/videomer/updates/";
	//public final static String updateChannel = "file:///D:/Mes Documents/Programmation/Java/Video Manager/doc/updateChannel/";
	
	private final static String versionChannel = "http://www.arstaligtredan.fr/videomer/versionChannel.xml";
	public final static String updateChannel = "http://www.arstaligtredan.fr/videomer/updates/";
	private final static String patchnoteChannel = "http://www.arstaligtredan.fr/videomer/patchnote";
	
	//private final static String versionChannel = "http://www.arstaligtredan.fr/videomertest/versionChannel.xml";
	//public final static String updateChannel = "http://www.arstaligtredan.fr/videomertest/updates/";
	//private final static String patchnoteChannel = "http://www.arstaligtredan.fr/videomertest/patchnote";
	
	/**
	 * Liste des version disponibles
	 */
	private ArrayList<String> distVersions;
	
	/**
	 * Controlleur
	 */
	private DefaultController controller;
	
	/**
	 * Liste des mises à jour disponibles
	 */
	private ArrayList<Update> availableUpdates;
	
	/**
	 * Taille de la MAJ (progressBar)
	 */
	private long totalUpdateSize;
	
	/**
	 * Nombre de fichiers à mettre à jour (progressBar)
	 */
	private int totalUpdateCount;
	
	
	
	public UpdateManager(DefaultController controller) {
		
		distVersions = new ArrayList<String>();
		availableUpdates = null;
		this.controller = controller;
		totalUpdateSize = 0;
		totalUpdateCount = 0;
	}
	
	/**
	 * Renvoie la version actuelle de l'appli
	 * 
	 * @return
	 */
	public String getActualVersion() {
		return controller.getProperties().getSoftwareVersion();
	}
	
	/**
	 * Renvoie de nombre de fichier à mettre à jour
	 * @return
	 */
	public int getTotalUpdateCount() {
		return totalUpdateCount;
	}
	
	/**
	 * Récupération de la dernière MAJ dispo
	 * 
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String getLastVersion() {
		
		// Téléchargement des versions
		String source = getSourceFrom(versionChannel).toString();

		// Conversion dans une variable
		XStream xstream = new XStream(new XppDriver());
		Class<?>[] classes = new Class[] { List.class, String.class };
		XStream.setupDefaultSecurity(xstream);
		xstream.allowTypes(classes);

		// Définition des Alias
		xstream.alias("VideoManager",List.class);
		xstream.alias("version",String.class);

		// Lecture du fichier XML
		distVersions = (ArrayList<String>) xstream.fromXML(source);

		// On récupère la dernière version et on compare à la version actuelle
		return distVersions.get(distVersions.size()-1);
	}
	
	public String getRealLastVersion() {
		
		String ver = "0.0.0";
		
		for( Update u : availableUpdates ) {
			
			if( u.getPatchVersion().compareTo(ver) > 0 ) {
				ver = u.getPatchVersion();
			}
		}
		
		return ver;
	}
	
	/**
	 * Récupéraction des données distantes d'UNE Update
	 * 
	 * @param version version de l'Update concernée
	 * @return
	 * @throws IOException
	 */
	private ArrayList<Library> getLibraryList(String version) {
		
		String source = getSourceFrom(updateChannel+version+"/update.xml").toString();

		// Conversion dans une variable
		XStream xstream = new XStream(new XppDriver());
		Class<?>[] classes = new Class[] { Update.class, Library.class };
		XStream.setupDefaultSecurity(xstream);
		xstream.allowTypes(classes);
		
		// Définition des Alias
		//xstream.alias("VideoManager",List.class);
		xstream.alias("update",Update.class);
		xstream.alias("library",Library.class);
		
		Update update = (Update) xstream.fromXML(source);
		
		return update.getFiles();

	}
	
	/**
	 * Compile les librairies à mettre à jour, en fonction du nombre d'update dispo
	 * 
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Update> getLatestsUpdates() {
		
		Update u;

		totalUpdateSize = 0;
		totalUpdateCount = 0;
		
		availableUpdates = new ArrayList<Update>();
		ArrayList<Library> libsToInstall = new ArrayList<Library>();
		
		// Liste des versions à installer
		for( int i = distVersions.size()-1; i >= 0; i-- ) {
			if( controller.getProperties().getSoftwareVersion().compareTo(distVersions.get(i)) < 0 ) {
				
				u = new Update(distVersions.get(i));

				for (Library l : getLibraryList(distVersions.get(i))) {
					
					if (!libsToInstall.contains(l)
							&& (l.getOperatingSystem() == Library.OS.unknown || l.getOperatingSystem() == controller.getProperties()
									.getOperatingSystem())) {
						
						// 2.0.0 | On regarde si une version locale plus récente existe (dans ce cas on ignore)
						boolean exist = false;
						for ( Library ll : controller.getProperties().getVersions() ) {

							//System.out.println(ll.getLibName()+"  "+l.getLibName()+"    "+ll.getLibVersion()+"  "+l.getLibVersion());
							if( ll.getLibName().equals(l.getLibName())
									&& (ll.getLibVersion().compareToIgnoreCase(l.getLibVersion()) == 0) ) {
								exist = true;
							}
						}
						
						if (!exist) {
							totalUpdateSize += l.getFileSize();
							totalUpdateCount++;
							u.addLib(l);
							libsToInstall.add(l);
						}
					}
				}
				availableUpdates.add(u);
			}			
		}
		return availableUpdates;
	}
	
	
	
	
	public String getPatchnote() {
		
		String url = patchnoteChannel+"_"+Locale.getDefault().getLanguage().toLowerCase()+".txt";
		
		// Téléchargement de la version distante	
		try {
			URL u = new URL(url);

			URLConnection uc = u.openConnection();

			int fileLenght = uc.getContentLength();
			if (fileLenght == -1) {
				System.err.println("Fichier patchnote vide : "+url);
			}

			Scanner s = new Scanner(uc.getInputStream(), "UTF-8");
			String inputStreamString = s.useDelimiter("\\A").next();
			s.close();
			
			return inputStreamString;
			
		} catch ( MalformedURLException e ) {
			System.err.println("Problème de l'URL du patchnote : "+url);
			e.printStackTrace();
		} catch ( IOException e ) {
			System.err.println("Problème de l'URL du patchnote : "+url);
			e.printStackTrace();
		}
		
		return "";
	}

	
	
	/**
	 * Retourne la taille en octets des MAJ à télécharger
	 * 
	 * @return
	 */
	public long getTotalUpdateSize() {
		return totalUpdateSize;
	}
	
	
	
	/**
	 * Télécharge le contenu d'une page HTML à partir de son lien
	 * 
	 * @param link URL
	 * @return le contenu
	 */
	private static StringBuffer getSourceFrom(String link) {
		
		StringBuffer buff = new StringBuffer();
		
		try {
			// creation d'un objet URL
			URL url = new URL(link);
			// on etablie une connection a cette url
			URLConnection uc = url.openConnection();
			// on y cree un flux de lecture en UTF-8
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
			// on lit le premier bit
			int c = br.read();
			// tant que c n'est pas egale au bit indiquant la fin d'un flux...
			while (c != -1) {
				buff.append((char) c);
				// ...on l'ajoute dasn le StringBuilder...
				c = br.read();
				// ...on lit le suivant
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		return buff;
	}
}
