package com.staligtredan.VideoMer.handler.transfert;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import com.staligtredan.VideoMer.modele.EntiteVideo;

/**
 * <code>FilterListTransfertHandler</code> permet de gérer le Drag and Drop du
 * ThumbnailList
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
public class MediaListTransfertHandler extends TransferHandler {

	private static final long serialVersionUID = 5378511781729221271L;



	@Override
	public boolean canImport( TransferHandler.TransferSupport info ) {
		return false;
	}



	@Override
	protected Transferable createTransferable( JComponent c ) {

		JList<?> list = (JList<?>) c;

		ArrayList<EntiteVideo> ev = new ArrayList<EntiteVideo>();

		for ( Object o : list.getSelectedValuesList() ) {
			ev.add((EntiteVideo) o);
		}

		return new ArrayListTransferable(ev);
	}



	@Override
	public int getSourceActions( JComponent c ) {
		return TransferHandler.COPY;
	}
}
