package update.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import update.modeleXml.Library;
import update.modeleXml.Update;
import util.RelativePath;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class CreationPatch {
	
	static enum OS  {linux, solaris, windows, macos, unknown;}
	final static String version = "0.1.0";
	final static String VlcPluginsPath = "D:\\Mes Documents\\Programmation\\Java\\Video Manager\\lib\\plugins";
	
	
	public CreationPatch() throws IOException {
		
		Update u = new Update();
		
		u.setPatchVersion(version);
		
		ArrayList<Library> arl = new ArrayList<Library>();
		
		/*
		lib/swingx-all-1.6.3.jar 
		lib/db4o-8.0.184.15484.jar 
		lib/log4j-1.2.16.jar 
		lib/jna-3.3.0.jar 
		lib/vlcj-1.2.2.jar 
		lib/platform-3.3.0.jar 
		lib/l2fprod-common-buttonbar.jar 
		lib/flexicombo.jar
		*/
		
		Library l = new Library();
		l.setLibName("SwingX");
		l.setFilePath("swingx-all-1.6.3.jar");
		l.setLibVersion("1.6.3");
		l.setFileSize(1497437);
		//l.setFileSize(new File("").length());
		l.setOperatingSystem(Library.OS_ALL);
		arl.add(l);
		
		l = new Library();
		l.setLibName("Db4o");
		l.setFilePath("db4o-8.0.184.15484.jar");
		l.setLibVersion("8.0.184.15484");
		l.setFileSize(1456898);
		l.setOperatingSystem(Library.OS_ALL);
		arl.add(l);
		
		l = new Library();
		l.setLibName("VLCJ");
		l.setFilePath("vlcj-1.2.2.jar");
		l.setLibVersion("1.2.2");
		l.setFileSize(1456898);
		l.setOperatingSystem(Library.OS_ALL);
		arl.add(l);
		
		l = new Library();
		l.setLibName("JNA Core");
		l.setFilePath("jna-3.3.0.jar");
		l.setLibVersion("3.3.0");
		l.setFileSize(865400);
		l.setOperatingSystem(Library.OS_ALL);
		arl.add(l);
		
		l = new Library();
		l.setLibName("JNA Platform");
		l.setFilePath("platform-3.3.0.jar");
		l.setLibVersion("3.3.0");
		l.setFileSize(841291);
		l.setOperatingSystem(Library.OS_ALL);
		arl.add(l);
		
		l = new Library();
		l.setLibName("L2FProd ButtonBar");
		l.setFilePath("l2fprod-common-buttonbar.jar");
		l.setLibVersion("1.0");
		l.setFileSize(72180);
		l.setOperatingSystem(Library.OS_ALL);
		arl.add(l);
		
		l = new Library();
		l.setLibName("Flexi ComboBox");
		l.setFilePath("flexicombo.jar");
		l.setLibVersion("1.0");
		l.setFileSize(19011);
		l.setOperatingSystem(Library.OS_ALL);
		arl.add(l);
		
		l = new Library();
		l.setLibName("Log4j");
		l.setFilePath("log4j-1.2.16.jar");
		l.setLibVersion("1.2.16");
		l.setFileSize(481534);
		l.setOperatingSystem(Library.OS_ALL);
		arl.add(l);
		
		l = new Library();
		l.setLibName("XMLPull");
		l.setFilePath("xmlpull-1.1.3.1.jar");
		l.setLibVersion("1.1.3.1");
		l.setFileSize(7188);
		l.setOperatingSystem(Library.OS_ALL);
		arl.add(l);
		
		l = new Library();
		l.setLibName("XPP3 min");
		l.setFilePath("xpp3_min-1.1.4c.jar");
		l.setLibVersion("1.1.4c");
		l.setFileSize(24956);
		l.setOperatingSystem(Library.OS_ALL);
		arl.add(l);
		
		l = new Library();
		l.setLibName("XStream");
		l.setFilePath("xstream-1.4.2.jar");
		l.setLibVersion("1.4.2");
		l.setFileSize(481672);
		l.setOperatingSystem(Library.OS_ALL);
		arl.add(l);
		
		l = new Library();
		l.setLibName("LibVLC");
		l.setFilePath("libvlc.dll");
		l.setLibVersion("2.0.0");
		l.setFileSize(111104);
		l.setOperatingSystem(Library.OS_WINDOWS);
		arl.add(l);
		
		l = new Library();
		l.setLibName("LibVlcCore");
		l.setFilePath("libvlccore.dll");
		l.setLibVersion("2.0.0");
		l.setFileSize(2285056);
		l.setOperatingSystem(Library.OS_WINDOWS);
		arl.add(l);
		
		l = new Library();
		l.setLibName("VideoMer");
		l.setFilePath("VideoMer.jar");
		l.setLibVersion("0.1.0");
		l.setFileSize(207953);
		l.setOperatingSystem(Library.OS_ALL);
		arl.add(l);
		
		// Liste des plugins VLC pr windows
		listVlcFiles(arl, new File(VlcPluginsPath));
		
		u.setFiles(arl);
		
		XStream xstream = new XStream(new XppDriver());
		xstream.alias("VideoManager",List.class);
		xstream.alias("update",Update.class);
		xstream.alias("library",Library.class);
		File fichier = new File("doc/update." + version + ".xml");
		
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
	 * Moulinette temporaire
	 * 
	 * @param list
	 * @param folder
	 */
	public void listVlcFiles(ArrayList<Library> list, File folder) {
		
		String[] uselessJunk = {"3dnow", "access_output", "gui", "media_library", "misc", "mux", "notify", "packetizer", "services_discovery", "stream_out", "visualization"};
		List<String> uselessLibs = Arrays.asList(uselessJunk);
		
		for( File f : folder.listFiles() ) {
			
			if( f.isDirectory() ) {
				
				if( !uselessLibs.contains(f.getName()) )
					listVlcFiles(list, f);
			}
			else {
				
				Library l = new Library();
				l.setLibName(f.getName());
				l.setFilePath("plugins/"+RelativePath.getRelativePath(new File(VlcPluginsPath), f).replace("\\", "/"));
				l.setLibVersion("2.0.0");
				l.setFileSize(f.length());
				l.setOperatingSystem(Library.OS_WINDOWS);
				list.add(l);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		
		new CreationPatch();
	}
}
