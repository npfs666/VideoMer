package com.staligtredan.VideoMer.handler.transfert;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import com.staligtredan.VideoMer.components.model.PlaylistModel;
import com.staligtredan.VideoMer.modele.EntiteVideo;

/**
 * <code>PlaylistTransfertHandler</code> permet de gérer le Drag and Drop de la
 * playlist
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
public class PlaylistTransfertHandler extends TransferHandler {

	private static final long serialVersionUID = 4387060360430478374L;



	@Override
	public boolean canImport( TransferHandler.TransferSupport info ) {
		if( !info.isDataFlavorSupported(new DataFlavor(EntiteVideo.class, "EntiteVideo")) ) {
			return false;
		}
		return true;
	}



	@Override
	public int getSourceActions( JComponent c ) {
		return TransferHandler.COPY;
	}



	@SuppressWarnings("unchecked")
	@Override
	public boolean importData( TransferHandler.TransferSupport info ) {

		if( !info.isDrop() ) {
			return false;
		}

		JList<?> list = (JList<?>) info.getComponent();
		PlaylistModel listModel = (PlaylistModel) list.getModel();

		// fetch the drop location
		JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
		int index = dl.getIndex();

		Transferable t = info.getTransferable();
		ArrayList<EntiteVideo> videos;

		try {

			if( t.getTransferData(new DataFlavor(EntiteVideo.class, "EntiteVideo")) instanceof ArrayList<?> ) {
				videos = (ArrayList<EntiteVideo>) t.getTransferData(new DataFlavor(EntiteVideo.class, "EntiteVideo"));

				listModel.add(index, videos);

				list.updateUI();
			}
		} catch ( Exception e ) {
			return false;
		}
		return true;
	}
}
