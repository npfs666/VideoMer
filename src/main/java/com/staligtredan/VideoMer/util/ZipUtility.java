package com.staligtredan.VideoMer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Class utilitaire pour les fichiers zip
 * 
 * @author Brendan
 * @since 2.0.1 01/01/2017
 */
public class ZipUtility {

	final static byte[] EmptyZip = { 80, 75, 05, 06, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00,
			00 };



	/**
	 * Crée un fichier zip vide
	 * 
	 * @param path
	 * @throws IOException
	 */
	public static void createEmptyZip( String path ) throws IOException {

		FileOutputStream fos = new FileOutputStream(new File(path));
		fos.write(EmptyZip, 0, 22);
		fos.flush();
		fos.close();
	}



	/**
	 * Ajoute des fichiers à un fichier zip
	 * 
	 * @param zFilePath
	 *            Fichier zip source
	 * @param filesToAdd
	 *            Map de fichiers à ajouter (Path du fichier à ajouter, Nom du
	 *            fichier dans le zip)
	 * @throws IOException
	 */
	public static void addToZip( String zFilePath, HashMap<String, String> filesToAdd ) throws IOException {

		File zFile = new File(zFilePath);

		// Création d'un fichier vide si il n'existe pas
		if( !zFile.exists() )
			createEmptyZip(zFilePath);

		ZipFile original = new ZipFile(zFilePath);
		ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream("tmp" + zFilePath));
		Enumeration<?> entries = original.entries();
		byte[] buffer = new byte[4096];

		// Copie des données de l'ancien zip
		while ( entries.hasMoreElements() ) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			// create a new empty ZipEntry
			ZipEntry newEntry = new ZipEntry(entry.getName());
			// outputStream.putNextEntry(entry);
			outputStream.putNextEntry(newEntry);
			InputStream in = original.getInputStream(entry);
			while ( 0 < in.available() ) {
				int read = in.read(buffer);
				outputStream.write(buffer, 0, read);
			}
			in.close();
			outputStream.closeEntry();
		}

		// On rajout les nouveaux fichiers
		for ( Map.Entry<String, String> entry : filesToAdd.entrySet() ) {

			ZipEntry newEntry = new ZipEntry(entry.getValue());
			outputStream.putNextEntry(newEntry);

			InputStream in = new FileInputStream(entry.getKey());
			while ( 0 < in.available() ) {
				int read = in.read(buffer);
				outputStream.write(buffer, 0, read);
			}
			in.close();
			outputStream.closeEntry();
		}

		// On écris le fichier et on renomme
		outputStream.close();
		original.close();

		File orig = new File(zFilePath);
		File tmp = new File("tmp" + zFilePath);
		orig.delete();
		tmp.renameTo(orig);
	}
}
