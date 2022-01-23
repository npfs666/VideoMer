package com.staligtredan.VideoMer.handler;

import java.util.EventObject;

import com.staligtredan.VideoMer.components.ResultMutableTreeNode;

/**
 * Encapsule les informations correspondant à un changement d'un noeud (ligne)
 * d'un ResultTreeModel
 * 
 * @author Brendan
 * 
 * @version 0.1 6/07/2010
 */
public class ResultTreeModelEvent extends EventObject {

	private static final long serialVersionUID = -5345083953454510785L;

	/**
	 * Le noeud ou à eu lieu l'Event
	 */
	protected ResultMutableTreeNode node;



	/**
	 * Crée un événement
	 * 
	 * @param source
	 *            Objet de l'événement
	 * @param node
	 *            Noeud (ligne) de l'événement
	 */
	public ResultTreeModelEvent( Object source, ResultMutableTreeNode node ) {
		super(source);

		this.node = node;
	}



	/**
	 * @return the node
	 */
	public ResultMutableTreeNode getNode() {
		return node;
	}
}
