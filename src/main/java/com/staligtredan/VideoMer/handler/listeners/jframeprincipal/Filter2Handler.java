package com.staligtredan.VideoMer.handler.listeners.jframeprincipal;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXList;

import com.staligtredan.VideoMer.components.model.Filter2ListModel;
import com.staligtredan.VideoMer.components.model.Filter3ListModel;
import com.staligtredan.VideoMer.controleur.DefaultController;

/**
 * <code>Filter2Handler</code> permet de gérer les événements
 * du Filtre n°2
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
public class Filter2Handler implements ListSelectionListener {

	/**
	 * Le controlleur
	 */
	private DefaultController controller;
	
	/**
	 * La liste précédente
	 */
	private JXList xListFilter1;
	
	/**
	 * La liste suivante
	 */
	private JXList xListFilter3;
	
	
	
	public Filter2Handler(DefaultController controller, JXList xListFilter1,JXList xListFilter3) {
		this.controller = controller;
		this.xListFilter1 = xListFilter1;
		this.xListFilter3 = xListFilter3;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {

		JXList list = (JXList)e.getSource();
		Filter2ListModel model2 = (Filter2ListModel)list.getModel();
		Filter3ListModel model3 = (Filter3ListModel)xListFilter3.getModel();
		int index = xListFilter1.getSelectedIndex();
		
		// Mise à jour des données & de l'affichage
		model3.filter(index, controller.getBibliotheque().getListe(index, model2.getViewFilter(), 
				list.getSelectedIndices(),list.getSelectedValues()));
		//xListFilter3.updateUI();
		xListFilter3.setSelectedIndex(0);
		
		// On signale à la liste suivante de se mettre à jour
		for( ListSelectionListener l : xListFilter3.getListSelectionListeners() ) {
			l.valueChanged(new ListSelectionEvent(xListFilter3, 0, 0, false));
		}
	}
}
