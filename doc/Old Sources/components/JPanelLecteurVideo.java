/*
 * Created by JFormDesigner on Wed Jun 30 18:59:17 CEST 2010
 */

package test.components;

import handler.listeners.VolumeHandler;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXButton;

/**
 * @author kevin red
 */
@SuppressWarnings("serial")
public class JPanelLecteurVideo extends JPanel {
	
	public JPanelLecteurVideo() {
		initComponents();
		setBorder(new EmptyBorder(5, 5, 5, 5));
		
		//sliderVolume.addMouseWheelListener(new VolumeHandler());
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - kevin red
		separator1 = new JSeparator();
		sliderPositionFilm = new JSlider();
		buttonPrecedent = new JXButton();
		buttonPlayPause = new JXButton();
		buttonStop = new JXButton();
		buttonSuivant = new JXButton();
		sliderVolume = new JSlider();

		//======== this ========

		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
		add(separator1, new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- sliderPositionFilm ----
		sliderPositionFilm.setValue(0);
		sliderPositionFilm.setPaintLabels(true);
		sliderPositionFilm.setPaintTicks(true);
		add(sliderPositionFilm, new GridBagConstraints(0, 1, 6, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- buttonPrecedent ----
		buttonPrecedent.setIcon(new ImageIcon(getClass().getResource("/imgs/video/media_step_back.png")));
		add(buttonPrecedent, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 5), 0, 0));

		//---- buttonPlayPause ----
		buttonPlayPause.setIcon(new ImageIcon(getClass().getResource("/imgs/video/media_play.png")));
		add(buttonPlayPause, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 5), 0, 0));

		//---- buttonStop ----
		buttonStop.setIcon(new ImageIcon(getClass().getResource("/imgs/video/media_stop.png")));
		add(buttonStop, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 5), 0, 0));

		//---- buttonSuivant ----
		buttonSuivant.setIcon(new ImageIcon(getClass().getResource("/imgs/video/media_step_forward.png")));
		add(buttonSuivant, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 5), 0, 0));

		//---- sliderVolume ----
		sliderVolume.setMajorTickSpacing(25);
		sliderVolume.setForeground(Color.black);
		sliderVolume.setPaintTicks(true);
		sliderVolume.setPaintLabels(true);
		add(sliderVolume, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - kevin red
	private JSeparator separator1;
	private JSlider sliderPositionFilm;
	private JXButton buttonPrecedent;
	private JXButton buttonPlayPause;
	private JXButton buttonStop;
	private JXButton buttonSuivant;
	private JSlider sliderVolume;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
