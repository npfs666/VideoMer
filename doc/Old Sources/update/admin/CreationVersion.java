package update.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Classe qui permet la mise à jour/création de la liste des mises à jour
 * 
 * @author Brendan
 *
 */
public class CreationVersion {
	
	ArrayList<String> versions;
	
	public CreationVersion() throws IOException {
		
		versions = new ArrayList<String>();
		
		versions.add("0.1.0");
		
		XStream xstream = new XStream(new XppDriver());
		xstream.alias("VideoManager",List.class);
		xstream.alias("version",String.class);
		File fichier = new File("doc/versionChannel.xml");
		
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fichier), "UTF-8");
		
	    // Enregistrement des données
	    try {
	    	xstream.toXML(versions, osw);
	    } 
	    finally {
			osw.close();
	    }
	}

	
	
	
	public static void main(String[] args) throws Exception {
		
		new CreationVersion();
	}
}
