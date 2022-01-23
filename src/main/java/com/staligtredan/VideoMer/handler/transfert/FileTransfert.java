package com.staligtredan.VideoMer.handler.transfert;

import java.io.File;
import java.util.List;

/**
 * Interface qui permet de gérer les transferts de fichiers
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
public interface FileTransfert {

	/**
	 * Importe une liste de fichiers
	 * 
	 * @param l
	 */
	public void importData( List<File> l );
}
