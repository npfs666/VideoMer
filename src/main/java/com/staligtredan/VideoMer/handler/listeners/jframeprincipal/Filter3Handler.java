package com.staligtredan.VideoMer.handler.listeners.jframeprincipal;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.staligtredan.VideoMer.components.model.ThumbnailListModel;
import com.staligtredan.VideoMer.components.renderer.ThumbnailPanel;
import com.staligtredan.VideoMer.vue.JFramePrincipal;

/**
 * <code>Filter3Handler</code> permet de gérer les événements
 * du Filtre n°3
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
public class Filter3Handler implements ListSelectionListener {

	/**
	 * Le panneau des miniatures
	 */
	private ThumbnailPanel thumbnailPanel;
	
	/**
	 * Instance de l'écran principal
	 */
	private JFramePrincipal jFramePrincipal;
	
	
	
	public Filter3Handler(JFramePrincipal jFramePrincipal, ThumbnailPanel thumbnailPanel) {
		this.jFramePrincipal = jFramePrincipal;
		this.thumbnailPanel = thumbnailPanel;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		ThumbnailListModel model = (ThumbnailListModel)thumbnailPanel.getModel();
		model.setData(jFramePrincipal.getSelection());
		thumbnailPanel.resetSelection();
	}
}
