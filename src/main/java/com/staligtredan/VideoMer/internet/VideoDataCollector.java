package com.staligtredan.VideoMer.internet;

import java.util.ArrayList;

import com.staligtredan.VideoMer.modele.EntiteVideo;

/**
 * Cette interface permet de cr�er un nouveau script de rapatriement de 
 * donn�es depuis n'importe quel site internet
 * 
 * @author Brendan Jaouen
 * @version 0.1 3/08/2010
 */
public interface VideoDataCollector {
	
	public static int MOVIE = 1;
	public static int TV_SHOW = 2;
	
	/**
	 * 
	 * @return a <code>String</code> containing the author email
	 */
	public String getAuthorEmail();
	
	/**
	 * 
	 * @return a <code>String</code> containing script version
	 */
	public String getVersion();
	
	/**
	 * 
	 * @return true if this script can get movies data
	 */
	//public boolean isMovieCapable();
	
	/**
	 * 
	 * @return true if this script can get tv show data
	 */
	//public boolean isTvShowCapable();
	
	/**
	 * Gets de detailed data for a Media
	 * 
	 * @param choice link of the media
	 * @param searchMode TV_SHOW or MOVIE
	 * @param season season number (if concerned)
	 * @param episode (if concerned)
	 * 
	 * @return The media filled with data
	 */
	public EntiteVideo parseData(String choice, int searchMode, String season, String episode);
	
	/**
	 * Search the query and get the result
	 * Set the mode in TV_SHOW or MOVIE
	 * 
	 * @param query String to search
	 * @param searchMode TV_SHOW or MOVIE
	 * 
	 * @return An ArrayList of results Item[X] = {Display result, URL of detailed info}
	 */
	public ArrayList<String[]> search(String query, int searchMode);
	
}
