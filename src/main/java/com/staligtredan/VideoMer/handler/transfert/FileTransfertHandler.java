package com.staligtredan.VideoMer.handler.transfert;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * Handler pour rendre des JComponent compatible avec le tranfert de fichier en
 * Drag & Drop
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
public class FileTransfertHandler extends TransferHandler {

	private static final long serialVersionUID = -6927186760605311196L;

	/**
	 * Composant compatible avec le Drag & Drop
	 */
	private FileTransfert component;



	/**
	 * Crée un handler sur un JComponent, afin de le rendre compatible avec le
	 * tranfert de fichiers en Drag&Drop
	 * 
	 * @param component
	 *            Composant concerné
	 */
	public FileTransfertHandler( FileTransfert component ) {

		this.component = component;
	}



	@Override
	public boolean canImport( TransferHandler.TransferSupport info ) {

		if( !info.isDataFlavorSupported(DataFlavor.javaFileListFlavor) )
			return false;
		else
			return true;
	}



	@Override
	public int getSourceActions( JComponent c ) {
		return TransferHandler.COPY;
	}



	@SuppressWarnings("unchecked")
	@Override
	public boolean importData( TransferHandler.TransferSupport info ) {

		Transferable t = info.getTransferable();

		try {
			if( t.getTransferData(DataFlavor.javaFileListFlavor) instanceof List<?> ) {
				List<File> l = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

				component.importData(l);
			}
		} catch ( UnsupportedFlavorException e ) {
			return false;
		} catch ( IOException e ) {
			return false;
		}

		return true;
	}
}
