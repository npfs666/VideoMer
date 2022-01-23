package com.staligtredan.VideoMer.components.renderer;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXList;

import com.staligtredan.VideoMer.components.model.PlaylistModel;
import com.staligtredan.VideoMer.modele.EntiteVideo;

/**
 * <code>JPanelVideoControlLight</code> gère les interractions de lecture/suivant/précédent entre les boutons & la playlist
 * 
 * @author Brendan Jaouen
 * @since 0.1 1/2/2013
 * @version 2.0.0 22/12/2016 MAJ loging
 */
public class JPanelVideoControlLight extends JPanel {

	private static final long serialVersionUID = 7039525091736561997L;
	
	final static Logger logger = LogManager.getLogger(JPanelVideoControlLight.class);

	/**
	 * La playlist
	 */
	private JXList playlist = null;



	/**
	 * Constructeur
	 */
	public JPanelVideoControlLight() {

		initComponents();
	}



	/**
	 * Défini la playlist
	 * 
	 * @param playlist
	 */
	public void setPlayList( JXList playlist ) {

		this.playlist = playlist;
	}



	/**
	 * Lance la lecture d'un média
	 */
	public void play() {

		buttonPlayPauseActionPerformed(null);
	}



	/**
	 * Action sur le bouton précédent
	 * 
	 * @param e
	 */
	private void buttonPreviousActionPerformed( ActionEvent e ) {

		if ( playlist.getSelectedIndex() < playlist.getModel().getSize() - 1 ) {

			// Lecture du suivant
			playlist.setSelectedIndex(playlist.getSelectedIndex() - 1);
			buttonPlayPauseActionPerformed(null);
		}
	}



	/**
	 * Action sur le bouton lecture/pause
	 * 
	 * @param e
	 */
	private void buttonPlayPauseActionPerformed( ActionEvent e ) {

		PlaylistModel model = (PlaylistModel) playlist.getModel();
		final EntiteVideo ev = model.getElement(playlist.getSelectedIndex());

		try {
			File f = new File(ev.getEmplacement());

			Desktop.getDesktop().browse(
					new URI(f.getCanonicalFile().toURI().toString().replace("file:////", "file://")));

		}
		catch ( Exception e1 ) {
			logger.error("Impossible de lancer le fichier : "+ev.getEmplacement(), e);
		}
	}



	/**
	 * Action sur le bouton suivant
	 * 
	 * @param e
	 */
	private void buttonNextActionPerformed( ActionEvent e ) {

		if ( playlist.getSelectedIndex() < playlist.getModel().getSize() - 1 ) {

			// Lecture du suivant
			playlist.setSelectedIndex(playlist.getSelectedIndex() + 1);
			buttonPlayPauseActionPerformed(null);
		}
	}



	private void initComponents() {

		// JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		buttonPrevious = new JXButton();
		buttonPlayPause = new JXButton();
		buttonNext = new JXButton();

		//======== this ========

		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

		//---- buttonPrevious ----
		buttonPrevious.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/32x32/media_step_back.png")));
		buttonPrevious.addActionListener(e -> buttonPreviousActionPerformed(e));
		add(buttonPrevious, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//---- buttonPlayPause ----
		buttonPlayPause.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/32x32/media_play.png")));
		buttonPlayPause.addActionListener(e -> buttonPlayPauseActionPerformed(e));
		add(buttonPlayPause, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));

		//---- buttonNext ----
		buttonNext.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/32x32/media_step_forward.png")));
		buttonNext.addActionListener(e -> buttonNextActionPerformed(e));
		add(buttonNext, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 5), 0, 0));
		// JFormDesigner - End of component initialization //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Mouloud rachid
	private JXButton buttonPrevious;
	private JXButton buttonPlayPause;
	private JXButton buttonNext;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
