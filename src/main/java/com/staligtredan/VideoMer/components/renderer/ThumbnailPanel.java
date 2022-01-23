package com.staligtredan.VideoMer.components.renderer;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.UIManager;

import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

import com.staligtredan.VideoMer.components.ThumbnailList;
import com.staligtredan.VideoMer.components.model.ThumbnailListModel;
import com.staligtredan.VideoMer.controleur.DefaultController;
import com.staligtredan.VideoMer.handler.listeners.ThumbnailHandler;
import com.staligtredan.VideoMer.handler.transfert.MediaListTransfertHandler;
import com.staligtredan.VideoMer.modele.EntiteVideo;
import com.staligtredan.VideoMer.vue.JDialogEditeur;

/**
 * <code>ThumbnailPanel</code> permet d'afficher une JXList et gère les interractions sur les média (clic droit etc)
 * 
 * @author Brendan Jaouen
 * @version 0.1.3 2/7/2013
 * @version 1.0.3 10/12/2016 Rajout de la gestion des taille miniatures
 */
public class ThumbnailPanel extends JPanel {

	private static final long serialVersionUID = -1776140798634900558L;

	/**
	 * Le modèle de données
	 */
	private ThumbnailListModel model;

	/**
	 * Le controlleur
	 */
	private DefaultController controller;


	/**
	 * Constructeur vide pour le JFormDesigner
	 */
	public ThumbnailPanel() {
		super();
	}



	/**
	 * Constructeur
	 * 
	 * @param controller
	 */
	public ThumbnailPanel( DefaultController controller ) {

		this.controller = controller;
		initComponents();

		xListMedia.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, UIManager
				.getColor("ToggleButton.light"), null));


		// Taille des miniautres
		if( controller.getPreferences().getThumbnailSize() == 1) {
			miniatureGrand();
		}
		else if( controller.getPreferences().getThumbnailSize() == 2 ) {
			miniatureMoyen();
		}
		else {
			miniaturePetit();
		}

		xListMedia.setVisibleRowCount(0);		
		xListMedia.setTransferHandler(new MediaListTransfertHandler());

		model = new ThumbnailListModel();
		xListMedia.setModel(model);
	}



	/**
	 * Renvoie le modèle de la Xlist
	 * 
	 * @return
	 */
	public ListModel<?> getModel() {

		return xListMedia.getModel();
	}



	/**
	 * ClearSelection
	 */
	public void resetSelection() {

		xListMedia.clearSelection();
	}



	/**
	 * Ajout des listeners
	 * 
	 * @param handler
	 */
	public void addThumbnailListListener( ThumbnailHandler handler ) {

		xListMedia.addMouseListener(handler);
		xListMedia.addListSelectionListener(handler);
		xListMedia.addKeyListener(handler);
	}



	/**
	 * Action générée lors du redimenssionement de la liste
	 */
	private void thisComponentResized( ComponentEvent e ) {

		Rectangle r = getBounds();
		scrollPaneThumbnailList.setBounds(0, 0, r.width, r.height);
	}



	/**
	 * Action sur le bouton Ajouter
	 * 
	 * @param e
	 */
	private void menuItemAddActionPerformed( ActionEvent e ) {

		JDialogEditeur dialog;
		dialog = new JDialogEditeur(controller.getMainFrame(), controller);
		dialog.setVisible(true);
	}



	/**
	 * Action sur le bouton éditer
	 * 
	 * @param e
	 */
	private void menuItemEditActionPerformed( ActionEvent e ) {

		ArrayList<EntiteVideo> list = new ArrayList<EntiteVideo>();
		
		for ( Object o : xListMedia.getSelectedValues() ) {
			list.add((EntiteVideo) o);
		}

		JDialogEditeur dialog;

		// Si liste vide, c'est un ajout de fichier non une �dition
		if ( list.size() == 0 )
			dialog = new JDialogEditeur(controller.getMainFrame(), controller);
		else
			dialog = new JDialogEditeur(controller.getMainFrame(), controller, list);

		dialog.setVisible(true);

		resetSelection();
	}



	/**
	 * Action sur le bouton supprimer
	 * 
	 * @param e
	 */
	private void menuItemDeleteActionPerformed( ActionEvent e ) {

		for ( Object o : xListMedia.getSelectedValues() ) {

			EntiteVideo ev = (EntiteVideo) o;

			// On delete de la biblio & de la thumbnailList (effacage local pour �viter une m�j)
			model.remove(ev);
			controller.getBibliotheque().remove(ev);
		}
	}

	public void miniatureGrand() {
		xListMedia.setFixedCellHeight(270);
		xListMedia.setFixedCellWidth(180);
		scrollPaneThumbnailList.getVerticalScrollBar().setUnitIncrement(30);
		scrollPaneThumbnailList.getHorizontalScrollBar().setUnitIncrement(30);
		controller.getPreferences().setThumbnailSize(1);
		xListMedia.setCellRenderer(new ThumbnailCellRenderer(controller,1));
		//updateUI();
	}

	public void miniatureMoyen() {
		xListMedia.setFixedCellHeight(210);
		xListMedia.setFixedCellWidth(140);
		scrollPaneThumbnailList.getVerticalScrollBar().setUnitIncrement(25);
		scrollPaneThumbnailList.getHorizontalScrollBar().setUnitIncrement(25);
		controller.getPreferences().setThumbnailSize(2);
		xListMedia.setCellRenderer(new ThumbnailCellRenderer(controller,2));
	}
	
	public void miniaturePetit() {
		xListMedia.setFixedCellHeight(150);
		xListMedia.setFixedCellWidth(120);
		scrollPaneThumbnailList.getVerticalScrollBar().setUnitIncrement(20);
		scrollPaneThumbnailList.getHorizontalScrollBar().setUnitIncrement(20);
		controller.getPreferences().setThumbnailSize(3);
		xListMedia.setCellRenderer(new ThumbnailCellRenderer(controller,3));
	}



	private void initComponents() {

		// JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		scrollPaneThumbnailList = new JScrollPane();
		xListMedia = new ThumbnailList();
		popupMenuRightClic = new JPopupMenu();
		menuItemAdd = new JMenuItem();
		menuItemEdit = new JMenuItem();
		menuItemDelete = new JMenuItem();
		lblSeason = new JLabel();

		//======== this ========
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				thisComponentResized(e);
			}
		});
		setLayout(new BorderLayout());

		//======== scrollPaneThumbnailList ========
		{
			scrollPaneThumbnailList.setBorder(BorderFactory.createEmptyBorder());

			//---- xListMedia ----
			xListMedia.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			xListMedia.setVisibleRowCount(4);
			xListMedia.setDragEnabled(true);
			xListMedia.setRolloverEnabled(true);
			xListMedia.setComponentPopupMenu(popupMenuRightClic);
			scrollPaneThumbnailList.setViewportView(xListMedia);
		}
		add(scrollPaneThumbnailList, BorderLayout.CENTER);

		//======== popupMenuRightClic ========
		{

			//---- menuItemAdd ----
			menuItemAdd.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/add.png")));
			menuItemAdd.addActionListener(e -> menuItemAddActionPerformed(e));
			popupMenuRightClic.add(menuItemAdd);

			//---- menuItemEdit ----
			menuItemEdit.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/edit.png")));
			menuItemEdit.addActionListener(e -> menuItemEditActionPerformed(e));
			popupMenuRightClic.add(menuItemEdit);

			//---- menuItemDelete ----
			menuItemDelete.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/delete.png")));
			menuItemDelete.addActionListener(e -> menuItemDeleteActionPerformed(e));
			popupMenuRightClic.add(menuItemDelete);
		}

		initComponentsI18n();
		// JFormDesigner - End of component initialization //GEN-END:initComponents
	}



	private void initComponentsI18n() {

		// JFormDesigner - Component i18n initialization - DO NOT MODIFY //GEN-BEGIN:initI18n
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		ResourceBundle bundle = controller.getBundle();
		menuItemAdd.setText(bundle.getString("ThumbnailPanel.menuItemAdd.text"));
		menuItemEdit.setText(bundle.getString("ThumbnailPanel.menuItemEdit.text"));
		menuItemDelete.setText(bundle.getString("ThumbnailPanel.menuItemDelete.text"));
		lblSeason.setText(bundle.getString("ThumbnailPanel.lblSeason.text"));
		// JFormDesigner - End of component i18n initialization //GEN-END:initI18n
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Mouloud rachid
	private JScrollPane scrollPaneThumbnailList;
	private ThumbnailList xListMedia;
	private JPopupMenu popupMenuRightClic;
	private JMenuItem menuItemAdd;
	private JMenuItem menuItemEdit;
	private JMenuItem menuItemDelete;
	private JLabel lblSeason;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
