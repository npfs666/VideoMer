package com.staligtredan.VideoMer.components.renderer;

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
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.staligtredan.VideoMer.components.model.ThumbnailListModel;
import com.staligtredan.VideoMer.controleur.DefaultController;
import com.staligtredan.VideoMer.util.HtmlParsing;
import com.staligtredan.VideoMer.vue.JFramePrincipal;

/**
 * <code>JPanelDetailsEntiteVideo</code> affiche toutes les informations détaillées d'un média lors de sa selection par
 * l'utilisateur
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
public class JPanelDetailsEntiteVideo extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7048261852698225566L;

	/**
	 * Référence de l'écran principal
	 */
	//private JFramePrincipal principal;

	/**
	 * Référence du controlleur
	 */
	private DefaultController controller;

	private ThumbnailPanel thumbnailPanel;


	/**
	 * Constructeur
	 * 
	 * @param principal
	 *            écran principal
	 * @param controller
	 *            controlleur
	 */
	public JPanelDetailsEntiteVideo(ThumbnailPanel panel,  JFramePrincipal principal, DefaultController controller ) {

		//this.principal = principal;
		this.controller = controller;
		this.thumbnailPanel = panel;
		initComponents();
	}



	/**
	 * Met � jour la liste des acteurs
	 * 
	 * @param acteurs
	 */
	public void setActeurs( ArrayList<String> acteurs ) {

		setList(acteurs, panelActeurs, 3);
	}



	/**
	 * Met � jour la liste des r�alisateurs
	 * 
	 * @param realisateurs
	 */
	public void setRealisateurs( ArrayList<String> realisateurs ) {

		setList(realisateurs, panelRealisateurs, 1);
	}



	/**
	 * Met � jour la liste des genres
	 * 
	 * @param genres
	 */
	public void setGenres( ArrayList<String> genres ) {

		setList(genres, panelGenre, 0);
	}



	/**
	 * Met � jour la liste des pays
	 * 
	 * @param nationalite
	 */
	public void setNationalite( ArrayList<String> nationalite ) {

		setList(nationalite, panelNationalite, 2);
	}



	private void setList( ArrayList<String> list, JPanel panel, final int code ) {

		panel.removeAll();
		panel.updateUI();

		// Cr�ation des faux liens
		for ( int i = 0; i < list.size(); i++ ) {
			JLabel lbl = new JLabel(" " + list.get(i) + " ");
			lbl.setFont(new Font("Tahoma", Font.ITALIC, 12));
			lbl.setBorder(new EmptyBorder(1, 1, 1, 1));
			lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
			// TODO internationaliser
			lbl.setToolTipText("Cliquez pour faire une recherche...");
			lbl.setBackground(Color.WHITE);
			lbl.setOpaque(true);
			lbl.addMouseListener(new MouseAdapter() {

				public void mouseClicked( MouseEvent e ) {

					JLabel source = (JLabel) e.getSource();
					//principal.setFilter2All();
					//principal.setFilter3(code, source.getText().trim());
					
					//TODO ICI pour le caca recherche
					ThumbnailListModel model = (ThumbnailListModel) thumbnailPanel.getModel();

					/*if( code == 3 ) {
						model.setData(controller.getBibliotheque().search(source.getText().trim(),
								Bibliotheque.Search.actors));
					} else if( code == 2 ) {
						model.setData(controller.getBibliotheque().search(source.getText().trim(),
								Bibliotheque.Search.country));
					}*/
					
					
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
	 * Met � jour la date de sortie du m�dia
	 * 
	 * @param date
	 */
	public void setDate( String date ) {

		lblDateText.setText(date);
	}



	/**
	 * Met � jour le chemin vers le m�dia
	 * 
	 * @param path
	 */
	public void setPath( String path ) {

		lblPathData.setText(path);
	}



	/**
	 * Met � jour la dur�e du m�dia
	 * 
	 * @param playtime
	 */
	public void setPlaytime( String playtime ) {

		lblPlaytimeData.setText(playtime);
	}



	/**
	 * Met � jour le synopsis du m�dia
	 * 
	 * @param synopsis
	 */
	public void setSynopsis( String synopsis ) {

		lblSynopsisText.setText(HtmlParsing.wrapLabelText(lblSynopsisText, synopsis));
	}



	private void initComponents() {

		// JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		lblActeurs = new JLabel();
		panelActeurs = new JPanel();
		lblRealisateurs = new JLabel();
		panelRealisateurs = new JPanel();
		lblGenre = new JLabel();
		panelGenre = new JPanel();
		lblNationalite = new JLabel();
		panelNationalite = new JPanel();
		lblSynopsis = new JLabel();
		lblSynopsisText = new JLabel();
		lblPath = new JLabel();
		lblPathData = new JLabel();
		lblPlaytime = new JLabel();
		lblPlaytimeData = new JLabel();
		lblDate = new JLabel();
		lblDateText = new JLabel();

		//======== this ========
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(Color.white);

		// JFormDesigner evaluation mark
		setBorder(new javax.swing.border.CompoundBorder(
			new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
				"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
				java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 12, 0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

		//---- lblActeurs ----
		lblActeurs.setVerticalTextPosition(SwingConstants.TOP);
		add(lblActeurs, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//======== panelActeurs ========
		{
			panelActeurs.setBackground(Color.white);
			panelActeurs.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		}
		add(panelActeurs, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- lblRealisateurs ----
		lblRealisateurs.setVerticalTextPosition(SwingConstants.TOP);
		lblRealisateurs.setVerticalAlignment(SwingConstants.TOP);
		add(lblRealisateurs, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//======== panelRealisateurs ========
		{
			panelRealisateurs.setBackground(Color.white);
			panelRealisateurs.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		}
		add(panelRealisateurs, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));
		add(lblGenre, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//======== panelGenre ========
		{
			panelGenre.setBackground(Color.white);
			panelGenre.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		}
		add(panelGenre, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- lblNationalite ----
		lblNationalite.setVerticalTextPosition(SwingConstants.TOP);
		lblNationalite.setVerticalAlignment(SwingConstants.TOP);
		add(lblNationalite, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//======== panelNationalite ========
		{
			panelNationalite.setBackground(Color.white);
			panelNationalite.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		}
		add(panelNationalite, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- lblSynopsis ----
		lblSynopsis.setVerticalTextPosition(SwingConstants.TOP);
		lblSynopsis.setVerticalAlignment(SwingConstants.TOP);
		add(lblSynopsis, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//---- lblSynopsisText ----
		lblSynopsisText.setFont(lblSynopsisText.getFont().deriveFont(lblSynopsisText.getFont().getSize() + 1f));
		lblSynopsisText.setForeground(new Color(0, 94, 168));
		lblSynopsisText.setBackground(Color.white);
		lblSynopsisText.setOpaque(true);
		add(lblSynopsisText, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));
		add(lblPath, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));
		add(lblPathData, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));
		add(lblPlaytime, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));
		add(lblPlaytimeData, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));
		add(lblDate, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 5), 0, 0));
		add(lblDateText, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

		initComponentsI18n();
		// JFormDesigner - End of component initialization //GEN-END:initComponents
	}



	public void initComponentsI18n() {

		// JFormDesigner - Component i18n initialization - DO NOT MODIFY //GEN-BEGIN:initI18n
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		ResourceBundle bundle = controller.getBundle();
		lblActeurs.setText(bundle.getString("JPanelDetailsEntiteVideo.lblActeurs.text"));
		lblRealisateurs.setText(bundle.getString("JPanelDetailsEntiteVideo.lblRealisateurs.text"));
		lblGenre.setText(bundle.getString("JPanelDetailsEntiteVideo.lblGenre.text"));
		lblNationalite.setText(bundle.getString("JPanelDetailsEntiteVideo.lblNationalite.text"));
		lblSynopsis.setText(bundle.getString("JPanelDetailsEntiteVideo.lblSynopsis.text"));
		lblPath.setText(bundle.getString("JPanelDetailsEntiteVideo.lblPath.text"));
		lblPlaytime.setText(bundle.getString("JPanelDetailsEntiteVideo.lblPlaytime.text"));
		lblDate.setText(bundle.getString("JPanelDetailsEntiteVideo.lblDate.text"));
		// JFormDesigner - End of component i18n initialization //GEN-END:initI18n
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Mouloud rachid
	private JLabel lblActeurs;
	private JPanel panelActeurs;
	private JLabel lblRealisateurs;
	private JPanel panelRealisateurs;
	private JLabel lblGenre;
	private JPanel panelGenre;
	private JLabel lblNationalite;
	private JPanel panelNationalite;
	private JLabel lblSynopsis;
	private JLabel lblSynopsisText;
	private JLabel lblPath;
	private JLabel lblPathData;
	private JLabel lblPlaytime;
	private JLabel lblPlaytimeData;
	private JLabel lblDate;
	private JLabel lblDateText;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
