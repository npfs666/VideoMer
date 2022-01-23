package com.staligtredan.VideoMer.components;

import java.io.File;
import java.io.FilenameFilter;

/**
 * <code>VideoFileFilter</code> permet de filtrer tous les fichiers vidéos en
 * fonction des extensions
 * 
 * @author Brendan Jaouen
 * @version 0.1 14/09/2010
 */
public class VideoFileFilter implements FilenameFilter {

	/**
	 * Tableau qui contient les extentions valides
	 */
	private String[] lowerCaseExtensions;



	public VideoFileFilter( String[] ext ) {

		lowerCaseExtensions = ext;
	}



	@Override
	public boolean accept( File dir, String name ) {

		File f = new File(dir + File.separator + name);

		if( f.isDirectory() ) {
			return true;
		}

		int i = name.lastIndexOf('.');
		if( i > 0 && i < name.length() - 1 ) {
			String desiredExtension = name.substring(i + 1).toLowerCase();
			for ( String extension : lowerCaseExtensions ) {
				if( desiredExtension.equals(extension.toLowerCase()) ) {
					return true;
				}
			}
		}

		return false;
	}
}
