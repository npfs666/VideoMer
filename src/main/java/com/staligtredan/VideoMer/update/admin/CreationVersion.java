package com.staligtredan.VideoMer.update.admin;

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

		/*versions.add("0.1.0");
		versions.add("0.1.1");
		versions.add("0.1.2");
		versions.add("0.1.3");
		versions.add("0.1.4");
		versions.add("0.1.5");
		versions.add("0.1.6");
		versions.add("0.1.7");
		versions.add("0.1.8");
		versions.add("0.1.9");
		versions.add("0.2.0");
		versions.add("0.2.1");
		versions.add("0.2.2");
		versions.add("1.0.0");
		versions.add("1.0.1");
		versions.add("1.0.2");*/
		versions.add("1.0.4");
		versions.add("2.0.0");
		versions.add("2.0.2");
		versions.add("2.0.3");
		
		XStream xstream = new XStream(new XppDriver());
		xstream.alias("VideoManager", List.class);
		xstream.alias("version", String.class);
		File fichier = new File("versionChannel.xml");

		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fichier), "UTF-8");

		// Enregistrement des données
		try {
			xstream.toXML(versions, osw);
		}
		finally {
			osw.close();
		}
	}



	public static void main( String[] args ) throws Exception {

		new CreationVersion();
	}
}
