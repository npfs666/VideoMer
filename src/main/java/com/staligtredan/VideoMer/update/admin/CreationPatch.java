package com.staligtredan.VideoMer.update.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.staligtredan.VideoMer.update.modeleXml.Library;
import com.staligtredan.VideoMer.update.modeleXml.Update;
import com.staligtredan.VideoMer.util.RelativePath;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

@SuppressWarnings("unused")
public class CreationPatch {

	final static String version = "2.0.3";
	final static String VlcPluginsPath = "D:\\Mes Documents\\Programmation\\Java\\Video Manager\\lib\\plugins";
	final static String LibsPath = "D:\\Mes Documents\\Programmation\\Java\\VideoMer\\target\\lib";
	final static String LibsPath2 = "D:\\Mes Documents\\Programmation\\Java\\VideoMer\\doc\\libs";


	public CreationPatch() throws IOException {

		Update u = new Update();

		u.setPatchVersion(version);

		ArrayList<Library> arl = new ArrayList<Library>();


		
		//listLibs(arl);

		Library l = new Library();
		l.setLibName("VideoMer");
		l.setFilePath("VideoMer.jar");
		l.setLibVersion(version);
		l.setFileSize(309039);
		l.setOperatingSystem(Library.OS.unknown);
		arl.add(l);
		
		l = new Library();
		l.setLibName("version");
		l.setFilePath("version-"+version);
		l.setLibVersion(version);
		l.setFileSize(7);
		l.setOperatingSystem(Library.OS.unknown);
		arl.add(l);
		
		/*l = new Library();
		l.setLibName("updater");
		l.setFilePath("updater.jar");
		l.setLibVersion(version);
		l.setFileSize(600000);
		l.setOperatingSystem(Library.OS.unknown);*/
		//arl.add(l);

		//listLangFiles(arl, new File("./src/lang/"), version);

		// Liste des plugins VLC pr windows
		// listVlcFiles(arl, new File(VlcPluginsPath));

		listLibs(arl);
		
		u.setFiles(arl);

		XStream xstream = new XStream(new XppDriver());
		xstream.alias("VideoManager", List.class);
		xstream.alias("update", Update.class);
		xstream.alias("library", Library.class);
		File fichier = new File("update." + version + ".xml");

		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fichier), "UTF-8");

		// Enregistrement des données
		try {
			xstream.toXML(u, osw);
		}
		finally {
			osw.close();
		}
	}
	
	/**
	 * 
	 */
	private void listLibs(ArrayList<Library> arl) {
		
		File folder = new File(LibsPath2);
		Library l;
		
		for ( File f : folder.listFiles() ) {
			
			int i = f.getName().lastIndexOf("-");
			String nom = f.getName().substring(0, i);
			String version = f.getName().substring(i+1).replaceAll(".jar", "");
			
			System.out.println(nom +"  "+version);
			
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
	 * Crée la liste des fichiers langue à mettre à jour à partir d'un dossier
	 * 
	 * @param list
	 * @param folder
	 * @param version
	 */
	private void listLangFiles( ArrayList<Library> list, File folder, String version ) {

		for ( File f : folder.listFiles() ) {

			if ( f.isDirectory() ) {

				listLangFiles(list, f, version);
			}
			else {

				Library l = new Library();
				l.setLibName(f.getName());
				l.setFilePath(RelativePath.getRelativePath(folder, f).replace("\\", "/"));
				l.setLibVersion(version);
				l.setFileSize(f.length());
				l.setOperatingSystem(Library.OS.unknown);
				list.add(l);
			}
		}
	}



	/**
	 * Moulinette temporaire
	 * 
	 * @param list
	 * @param folder
	 */
	private void listVlcFiles( ArrayList<Library> list, File folder ) {

		for ( File f : folder.listFiles() ) {

			Library l = new Library();
			l.setLibName(f.getName());
			l.setFilePath("plugins/" + RelativePath.getRelativePath(new File(VlcPluginsPath), f).replace("\\", "/"));
			l.setLibVersion("2.0.0");
			l.setFileSize(f.length());
			l.setOperatingSystem(Library.OS.windows);
			list.add(l);
		}
	}



	public static void main( String[] args ) throws Exception {

		new CreationPatch();
	}
}
