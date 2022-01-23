package com.staligtredan.VideoMer.handler.transfert;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import com.staligtredan.VideoMer.modele.EntiteVideo;

/**
 * <code>ArrayListTransferable</code> définis des données qui peuvent être
 * transferées par Drag and Drop
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
public class ArrayListTransferable implements Transferable {

	/**
	 * Données à transférer
	 */
	protected ArrayList<EntiteVideo> data;



	/**
	 * Crée une instance de ArrayListTransferable
	 * 
	 * @param data
	 *            Les données à transférer
	 */
	public ArrayListTransferable( ArrayList<EntiteVideo> data ) {
		this.data = new ArrayList<EntiteVideo>(data);
	}



	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { new DataFlavor(EntiteVideo.class, "EntiteVideo") };
	}



	@Override
	public boolean isDataFlavorSupported( DataFlavor flavor ) {
		if( new DataFlavor(EntiteVideo.class, "EntiteVideo").equals(flavor) )
			return true;
		else
			return false;
	}



	@Override
	public Object getTransferData( DataFlavor flavor ) throws UnsupportedFlavorException, IOException {

		if( !isDataFlavorSupported(flavor) ) {
			throw new UnsupportedFlavorException(flavor);
		}
		return data;
	}
}
