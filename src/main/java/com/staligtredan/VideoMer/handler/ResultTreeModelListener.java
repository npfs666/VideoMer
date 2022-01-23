package com.staligtredan.VideoMer.handler;

import java.util.EventListener;

/**
 * Défini l'interface pour un objet qui écoute les changements d'un
 * ResultTreeModel
 * 
 * @author Brendan
 * @version 0.1 6/07/2010
 */
public interface ResultTreeModelListener extends EventListener {

	/**
	 * Evenement quand un noeud à été changé
	 * 
	 * @param e
	 */
	void treeNodesChanged( ResultTreeModelEvent e );
}
