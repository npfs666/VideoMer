package update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import update.modeleXml.Library;
import update.modeleXml.Update;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class UpdateManager {

	private final static String versionChannel = "http://heanet.dl.sourceforge.net/project/videomer/updates/versionChannel.xml";
	//private final static String versionChannel = "file:///D:/Mes Documents/Programmation/Java/Video Manager/doc/versionChannel.xml";
	public final static String updateChannel = "http://heanet.dl.sourceforge.net/project/videomer/updates/";
	//public final static String updateChannel = "file:///D:/Mes Documents/Programmation/Java/Video Manager/doc/updateChannel/";
	
	private ArrayList<String> distVersions;
	private DefaultController controller;
	private ArrayList<Update> availableUpdates;
	private long totalUpdateSize;
	private int totalUpdateCount;
	
	public UpdateManager(DefaultController controller) {
		
		distVersions = new ArrayList<String>();
		availableUpdates = null;
		this.controller = controller;
		totalUpdateSize = 0;
		totalUpdateCount = 0;
	}
	
	public String getActualVersion() {
		return controller.getProperties().getSoftwareVersion();
	}
	
	public int getTotalUpdateCount() {
		return totalUpdateCount;
	}
	
	@SuppressWarnings("unchecked")
	public String getLastVersion() throws IOException {
		
		// Téléchargement du fichier des versions
		String source = getSourceFrom(versionChannel).toString();

		// Conversion dans une variable
		XStream xstream = new XStream(new XppDriver());

		// Définition des Alias
		xstream.alias("VideoManager",List.class);
		xstream.alias("version",String.class);

		// Lecture du fichier XML
		distVersions = (ArrayList<String>) xstream.fromXML(source);

		// On récupère la dernière version et on compare à la version actuelle
		return distVersions.get(distVersions.size()-1);
	}
	
	private ArrayList<Library> getLibraryList(String version) throws IOException {
		
		String source = getSourceFrom(updateChannel+version+"/update.xml").toString();

		// Conversion dans une variable
		XStream xstream = new XStream(new XppDriver());
		
		// Définition des Alias
		xstream.alias("VideoManager",List.class);
		xstream.alias("update",Update.class);
		xstream.alias("library",Library.class);
		
		Update update = (Update) xstream.fromXML(source);
		
		return update.getFiles();

	}
	
	public ArrayList<Update> getLatestsUpdates() throws IOException {
		
		Update u;

		// Si données déjà recherchées
		//if( availableUpdates != null )
		//	return availableUpdates;

		totalUpdateSize = 0;
		totalUpdateCount = 0;
		
		availableUpdates = new ArrayList<Update>();
		
		// Liste des versions à installer
		for( int i = distVersions.size()-1; i >= 0; i-- ) {
			if( controller.getProperties().getSoftwareVersion().compareTo(distVersions.get(i)) < 0 ) {
				
				u = new Update(distVersions.get(i));

				for (Library l : getLibraryList(distVersions.get(i))) {
					
					if (!availableUpdates.contains(l)
							&& (l.getOperatingSystem() == 0 || l.getOperatingSystem() == controller.getProperties()
									.getOperatingSystem())) {
						totalUpdateSize += l.getFileSize();
						totalUpdateCount++;
						u.addLib(l);
					}
				}
				availableUpdates.add(u);
			}			
		}

		return availableUpdates;
	}
	
	public long getTotalUpdateSize() {
		return totalUpdateSize;
	}
	
	/**
	 * Télécharge le contenu d'une page HTML à partir de son lien
	 * 
	 * @param link URL
	 * @return le contenu
	 */
	private static StringBuffer getSourceFrom(String link) throws IOException {
		
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
		}
		
		return buff;
	}
	
	public static void main(String[] args) throws Exception {
		UpdateManager  tmp = new UpdateManager(new DefaultController());
		
		System.out.println(tmp.getLastVersion());
		tmp.getLatestsUpdates();
	}
}
