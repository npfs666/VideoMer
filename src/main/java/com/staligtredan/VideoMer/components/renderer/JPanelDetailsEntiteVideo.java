package com.staligtredan.VideoMer.components.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.staligtredan.VideoMer.components.model.ThumbnailListModel;
import com.staligtredan.VideoMer.controleur.DefaultController;
import com.staligtredan.VideoMer.modele.Bibliotheque;
import com.staligtredan.VideoMer.util.HtmlParsing;
import com.staligtredan.VideoMer.vue.JFramePrincipal;

/**
 * Nouvelle version du JPanelDetails
 * 
 * @since 2.0.0
 */
public class JPanelDetailsEntiteVideo extends JPanel {

	private static final long serialVersionUID = 7781494837896678005L;

	/**
	 * Référence du controlleur
	 */
	private DefaultController controller;

	private ThumbnailPanel thumbnailPanel;

	private JFramePrincipal principal;



	/**
	 * Constructeur
	 * 
	 * @param principal
	 *            écran principal
	 * @param controller
	 *            controlleur
	 */
	public JPanelDetailsEntiteVideo( JFramePrincipal principal, ThumbnailPanel panel, DefaultController controller ) {

		this.principal = principal;
		this.controller = controller;
		this.thumbnailPanel = panel;
		initComponents();
	}



	/**
	 * Met à jour la liste des acteurs
	 * 
	 * @param acteurs
	 */
	public void setActeurs( ArrayList<String> acteurs ) {

		setList(acteurs, panelActors, 3);
	}



	/**
	 * Met à jour la liste des réalisateurs
	 * 
	 * @param realisateurs
	 */
	public void setRealisateurs( ArrayList<String> realisateurs ) {

		setList(realisateurs, panelDirectors, 1);
	}



	/**
	 * Met à jour la liste des genres
	 * 
	 * @param genres
	 */
	public void setGenres( ArrayList<String> genres ) {

		setList(genres, panelGenre, 0);
	}



	/**
	 * Met à jour la liste des pays
	 * 
	 * @param nationalite
	 */
	public void setNationalite( ArrayList<String> nationalite ) {

		setList(nationalite, panelCountry, 2);
	}



	private void setList( ArrayList<String> list, JPanel panel, final int code ) {

		panel.removeAll();
		panel.updateUI();

		// Création des faux liens
		for ( int i = 0; i < list.size(); i++ ) {
			
			JLabel lbl = new JLabel(" " + list.get(i) + " ");
			lbl.setFont(new Font("Tahoma", Font.BOLD + Font.ITALIC, 12));
			lbl.setBorder(new EmptyBorder(1, 1, 1, 1));
			lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
			lbl.setToolTipText(controller.getBundle().getString("JPanelDetailsEntiteVideo.lblClicToSearch.text"));
			lbl.setBackground(Color.WHITE);
			lbl.setOpaque(true);
			lbl.addMouseListener(new MouseAdapter() {

				public void mouseClicked( MouseEvent e ) {

					JLabel source = (JLabel) e.getSource();
					// principal.setFilter2All();
					// principal.setFilter3(code, source.getText().trim());
					// Bibliotheque.Cat.values()[principal.getFilter1()];
					ThumbnailListModel model = (ThumbnailListModel) thumbnailPanel.getModel();

					if( code == 3 ) {
						model.setData(controller.getBibliotheque().search(source.getText().trim(),
								Bibliotheque.Search.actors, Bibliotheque.Cat.values()[principal.getFilter1()]));
					} else if( code == 2 ) {
						model.setData(controller.getBibliotheque().search(source.getText().trim(),
								Bibliotheque.Search.country, Bibliotheque.Cat.values()[principal.getFilter1()]));
					} else if( code == 1 ) {
						model.setData(controller.getBibliotheque().search(source.getText().trim(),
								Bibliotheque.Search.directors, Bibliotheque.Cat.values()[principal.getFilter1()]));
					} else if( code == 0 ) {
						model.setData(controller.getBibliotheque().search(source.getText().trim(),
								Bibliotheque.Search.genre, Bibliotheque.Cat.values()[principal.getFilter1()]));
					}

					thumbnailPanel.resetSelection();

				}



				public void mouseEntered( MouseEvent e ) {

					JLabel source = (JLabel) e.getSource();
					source.setBorder(new LineBorder(UIManager.getColor("textHighlight"), 1, true));
				}



				public void mouseExited( MouseEvent e ) {

					JLabel source = (JLabel) e.getSource();
					source.setBorder(new EmptyBorder(1, 1, 1, 1));
				}
			});
			panel.add(lbl);
		}
	}



	/**
	 * Met à jour la date de sortie du média
	 * 
	 * @param date
	 */
	public void setDate( String date ) {

		lblDateText.setText(date);
	}



	/**
	 * Met à jour le chemin vers le média
	 * 
	 * @param path
	 */
	public void setPath( String path ) {

		// lblPathData.setText(path);
	}



	/**
	 * Met à jour la durée du média
	 * 
	 * @param playtime
	 */
	public void setPlaytime( String playtime ) {

		lblPlaytimeData.setText(playtime);
	}



	/**
	 * Met à jour le synopsis du média
	 * 
	 * @param synopsis
	 */
	public void setSynopsis( String synopsis ) {

		lblSynopsisText.setText(HtmlParsing.wrapLabelText(lblSynopsisText, synopsis));
	}



	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		panelActors = new JPanel();
		panelDirectors = new JPanel();
		panelGenre = new JPanel();
		panelCountry = new JPanel();
		panelDate = new JPanel();
		lblDateText = new JLabel();
		panelDuration = new JPanel();
		lblPlaytimeData = new JLabel();
		panelPlot = new JPanel();
		lblSynopsisText = new JLabel();
		lblClicToSearch = new JLabel();

		//======== this ========
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(Color.white);

		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

		//======== panelActors ========
		{
			panelActors.setBackground(Color.white);
			panelActors.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		}
		add(panelActors, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//======== panelDirectors ========
		{
			panelDirectors.setBackground(Color.white);
			panelDirectors.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		}
		add(panelDirectors, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== panelGenre ========
		{
			panelGenre.setBackground(Color.white);
			panelGenre.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		}
		add(panelGenre, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//======== panelCountry ========
		{
			panelCountry.setBackground(Color.white);
			panelCountry.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		}
		add(panelCountry, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//======== panelDate ========
		{
			panelDate.setBackground(Color.white);
			panelDate.setLayout(new BorderLayout());

			//---- lblDateText ----
			lblDateText.setBackground(Color.white);
			lblDateText.setOpaque(true);
			panelDate.add(lblDateText, BorderLayout.SOUTH);
		}
		add(panelDate, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//======== panelDuration ========
		{
			panelDuration.setBackground(Color.white);
			panelDuration.setLayout(new BorderLayout());

			//---- lblPlaytimeData ----
			lblPlaytimeData.setOpaque(true);
			lblPlaytimeData.setBackground(Color.white);
			panelDuration.add(lblPlaytimeData, BorderLayout.NORTH);
		}
		add(panelDuration, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== panelPlot ========
		{
			panelPlot.setBackground(Color.white);
			panelPlot.setLayout(new BorderLayout());

			//---- lblSynopsisText ----
			lblSynopsisText.setBackground(Color.white);
			lblSynopsisText.setOpaque(true);
			lblSynopsisText.setFont(new Font("Tahoma", Font.PLAIN, 13));
			panelPlot.add(lblSynopsisText, BorderLayout.CENTER);
		}
		add(panelPlot, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

		initComponentsI18n();
		// //GEN-END:initComponents
	}



	public void initComponentsI18n() {
		// JFormDesigner - Component i18n initialization - DO NOT MODIFY
		// //GEN-BEGIN:initI18n
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		ResourceBundle bundle = controller.getBundle();
		panelActors.setBorder(new TitledBorder(bundle.getString("JPanelDetailsEntiteVideo.panelActors.border")));
		panelDirectors.setBorder(new TitledBorder(bundle.getString("JPanelDetailsEntiteVideo.panelDirectors.border")));
		panelGenre.setBorder(new TitledBorder(bundle.getString("JPanelDetailsEntiteVideo.panelGenre.border")));
		panelCountry.setBorder(new TitledBorder(bundle.getString("JPanelDetailsEntiteVideo.panelCountry.border")));
		panelDate.setBorder(new TitledBorder(bundle.getString("JPanelDetailsEntiteVideo.panelDate.border")));
		panelDuration.setBorder(new TitledBorder(bundle.getString("JPanelDetailsEntiteVideo.panelDuration.border")));
		panelPlot.setBorder(new TitledBorder(bundle.getString("JPanelDetailsEntiteVideo.panelPlot.border")));
		lblClicToSearch.setText(bundle.getString("JPanelDetailsEntiteVideo.lblClicToSearch.text"));
		// //GEN-END:initI18n
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Mouloud rachid
	private JPanel panelActors;
	private JPanel panelDirectors;
	private JPanel panelGenre;
	private JPanel panelCountry;
	private JPanel panelDate;
	private JLabel lblDateText;
	private JPanel panelDuration;
	private JLabel lblPlaytimeData;
	private JPanel panelPlot;
	private JLabel lblSynopsisText;
	private JLabel lblClicToSearch;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
