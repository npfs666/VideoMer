package com.staligtredan.VideoMer.handler.transfert;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.staligtredan.VideoMer.vue.JFramePrincipal;

/**
 * <code>FilterListTransfertHandler</code> permet de gérer le Drag and Drop des
 * filtres 1,2 et 3
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
public class FilterListTransfertHandler extends TransferHandler {

	private static final long serialVersionUID = 2010252703138835893L;

	/**
	 * Instance de du composant source
	 */
	protected JFramePrincipal ecrPrincipal;



	/**
	 * Constructeur
	 */
	public FilterListTransfertHandler( JFramePrincipal ecrPrincipal ) {
		this.ecrPrincipal = ecrPrincipal;
	}



	@Override
	public boolean canImport( TransferHandler.TransferSupport info ) {
		return false;
	}



	@Override
	public int getSourceActions( JComponent c ) {
		return TransferHandler.COPY;
	}



	@Override
	protected Transferable createTransferable( JComponent c ) {
		return new ArrayListTransferable(ecrPrincipal.getSelection());
	}
}
