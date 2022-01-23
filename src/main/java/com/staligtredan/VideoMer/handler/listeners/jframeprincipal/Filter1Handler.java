package com.staligtredan.VideoMer.handler.listeners.jframeprincipal;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXList;

import com.staligtredan.VideoMer.components.model.Filter2ListModel;
import com.staligtredan.VideoMer.controleur.DefaultController;

/**
 * <code>Filter1Handler</code> permet de gérer les événements
 * du Filtre n°1
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
public class Filter1Handler implements ListSelectionListener {

	/**
	 * Le controlleur
	 */
	private DefaultController controller;
	
	/**
	 * La liste suivante (pour faire des cascade, à coup de listener)
	 */
	private JXList xListFilter2;
	
	
	
	public Filter1Handler(DefaultController controller, JXList xListFilter2) {
		this.controller = controller;
		this.xListFilter2 = xListFilter2;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {

		JXList list = (JXList)e.getSource();
		Filter2ListModel model = (Filter2ListModel)xListFilter2.getModel();
		int index = list.getSelectedIndex();
		
		// Mise à jour des données & de l'affichage
		model.filter(index,controller.getBibliotheque().getListe(index));
		//xListFilter2.updateUI();
		xListFilter2.setSelectedIndex(0);
		
		// On signale à la liste suivante de se mettre à jour
		for( ListSelectionListener l : xListFilter2.getListSelectionListeners() ) {
			l.valueChanged(new ListSelectionEvent(xListFilter2, 0, 0, false));
		}
	}
}
