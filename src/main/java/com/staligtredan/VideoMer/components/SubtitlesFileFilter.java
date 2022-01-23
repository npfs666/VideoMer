package com.staligtredan.VideoMer.components;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filtre de fichiers qui permet de trier les sous titres des autres fichiers
 * 
 * Crée le 10/02/2015 (0.19)
 * 
 * @author Brendan Jaouen
 * @since 0.19 10/02/2015 Création
 */
public class SubtitlesFileFilter implements FilenameFilter {

	/**
	 * Tableau qui contient les extensions autorisées
	 */
	private String[] lowerCaseExtensions;

	/**
	 * Nom du fichier vidéo original (sans extension), qui doit se retrouver
	 * dans le pattern du sous titre
	 */
	private String name;



	/**
	 * 
	 * @param pattern
	 * @param lowerCaseExtensions
	 */
	public SubtitlesFileFilter( String name, String[] lowerCaseExtensions ) {

		this.name = name;
		this.lowerCaseExtensions = lowerCaseExtensions;
	}



	@Override
	public boolean accept( File dir, String name ) {

		File f = new File(dir + File.separator + name);

		if( f.isDirectory() ) {
			return false;
		}

		int i = name.lastIndexOf('.');
		if( i > 0 && i < name.length() - 1 ) {
			String desiredExtension = name.substring(i + 1).toLowerCase();
			for ( String extension : lowerCaseExtensions ) {
				if( desiredExtension.equals(extension.toLowerCase()) ) {
					// Maintenant qu'on a la bonne extension, on peut check le
					// nom du fichier
					// name.substring(0, name.lastIndexOf("."));
					if( name.substring(0, name.lastIndexOf(".")).contains(this.name) )
						return true;
				}
			}
		}

		return false;
	}

}
