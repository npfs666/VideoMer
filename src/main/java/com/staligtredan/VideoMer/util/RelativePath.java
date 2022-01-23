package com.staligtredan.VideoMer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * this class provides functions used to generate a relative path from two absolute paths
 * 
 * @author David M. Howard
 * @version 0.1 14/09/2010
 */
public class RelativePath {

	/**
	 * break a path down into individual elements and add to a list. example : if a path is /a/b/c/d.txt, the breakdown will be
	 * [d.txt,c,b,a]
	 * 
	 * @param f
	 *            input file
	 * @return a List collection with the individual elements of the path in reverse order
	 */
	private static ArrayList<String> getPathList( File f ) {

		ArrayList<String> l = new ArrayList<String>();
		File r;

		try {
			r = f.getCanonicalFile();
			while ( r != null ) {
				l.add(r.getName());
				r = r.getParentFile();
			}
		}
		catch ( IOException e ) {
			e.printStackTrace();
			l = null;
		}
		return l;
	}



	/**
	 * figure out a string representing the relative path of 'f' with respect to 'r'
	 * 
	 * @param r
	 *            home path
	 * @param f
	 *            path of file
	 */
	private static String matchPathLists( ArrayList<String> r, ArrayList<String> f ) {

		int i;
		int j;
		String s;

		// start at the beginning of the lists
		// iterate while both lists are equal
		s = "";
		i = r.size() - 1;
		j = f.size() - 1;

		// first eliminate common root
		while ( (i >= 0) && (j >= 0) && (r.get(i).equals(f.get(j))) ) {
			i--;
			j--;
		}

		// for each remaining level in the home path, add a ..
		for ( ; i >= 0; i-- ) {
			s += ".." + File.separator;
		}

		// for each level in the file path, add the path
		for ( ; j >= 1; j-- ) {
			s += f.get(j) + File.separator;
		}

		// file name
		s += f.get(j);
		return s;
	}



	/**
	 * get relative path of File 'f' with respect to 'home' directory example : home = /a/b/c f = /a/d/e/x.txt s =
	 * getRelativePath(home,f) = ../../d/e/x.txt
	 * 
	 * @param home
	 *            base path, should be a directory, not a file, or it doesn't make sense
	 * @param f
	 *            file to generate path for
	 * @return path from home to f as a string
	 */
	public static String getRelativePath( File home, File f ) {

		ArrayList<String> homelist;
		ArrayList<String> filelist;
		String s;

		homelist = getPathList(home);
		filelist = getPathList(f);
		s = matchPathLists(homelist, filelist);

		return s;
	}



	/**
	 * Copie un fichier (source) vers une destination (dest)
	 * 
	 * @param src
	 *            fichier à copier
	 * @param dest
	 *            destination du fichier
	 * @throws IOException
	 */
	public static void copy( File src, File dest , long length) throws IOException {

		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(dest);

			byte[] buffer = new byte[8 * 1024];

			while ( true ) {
				int bytesRead = in.read(buffer);
				if ( bytesRead <= 0 ) {
					break;
				}
				out.write(buffer, 0, bytesRead);
				length+=8192;
			}
		}
		finally {
			if ( in != null ) {
				try {
					in.close();
				}
				catch ( IOException e ) {
					// failsafe
				}
			}
			if ( out != null ) {
				try {
					out.close();
				}
				catch ( IOException e ) {
					// failsafe
				}
			}
		}
	}
}