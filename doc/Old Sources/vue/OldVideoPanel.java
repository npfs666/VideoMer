/*
 * Created by JFormDesigner on Fri Oct 08 16:06:29 CEST 2010
 */

package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;

/**
 * @author franky wild
 */
public class OldVideoPanel extends JFrame {
	public OldVideoPanel() {
		initComponents();
	}

	private void buttonStopActionPerformed(ActionEvent e) {
		/*if( mediaPlayer!= null && mediaPlayer.isPlaying() ) {
			mediaPlayer.release();
			mediaPlayer = null;
			videoFrame.setVisible(false);
			videoFrame = null;
			//mediaPlayerFactory.release();
			//multiSplitPane.remove(videoSurface);
			//multiSplitPane.add(scrollPaneMediaList, "middle");
			//multiSplitPane.updateUI();
		}*/
	}

	private void sliderMediaPositionStateChanged(ChangeEvent e) {
		/*if( mediaPlayer!= null && mediaPlayer.isPlaying() ) {
			//mediaPlayer.setPosition((float)(sliderMediaPosition.getValue()/(double)sliderMediaPosition.getMaximum()));
		}*/
	}

	private void buttonNextActionPerformed(ActionEvent e) {
		/*if( mediaPlayer!= null && mediaPlayer.isPlaying() ) {

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gs = ge.getDefaultScreenDevice();

			//DisplayMode dm = gs.getDisplayMode();
			
			//gs.setDisplayMode(dm);
			
			gs.setFullScreenWindow(videoFrame);
			
			//System.out.println(Toolkit.getDefaultToolkit().getScreenSize());
			//videoFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
			//videoFrame.requestFocus();
			//setVisible(false);
		}*/
	}

	private void buttonPreviousActionPerformed(ActionEvent e) {
		/*if( mediaPlayer!= null && mediaPlayer.isPlaying() ) {
			videoFrame.setSize(mediaPlayer.getVideoDimension());
		}*/
	}
	

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - franky wild
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		xPanelVideoControl = new JXPanel();
		separatorVideoControl = new JSeparator();
		sliderMediaPosition = new JSlider();
		buttonPrevious = new JXButton();
		buttonPlayPause = new JXButton();
		buttonStop = new JXButton();
		buttonNext = new JXButton();
		sliderVolume = new JSlider();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));

			// JFormDesigner evaluation mark
			dialogPane.setBorder(new javax.swing.border.CompoundBorder(
				new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
					"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
					javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
					java.awt.Color.red), dialogPane.getBorder())); dialogPane.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new GridBagLayout());
				((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0};
				((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0};
				((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
				((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

				//======== xPanelVideoControl ========
				{
					xPanelVideoControl.setLayout(new GridBagLayout());
					((GridBagLayout)xPanelVideoControl.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0};
					((GridBagLayout)xPanelVideoControl.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
					((GridBagLayout)xPanelVideoControl.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4};
					((GridBagLayout)xPanelVideoControl.getLayout()).rowWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};
					xPanelVideoControl.add(separatorVideoControl, new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

					//---- sliderMediaPosition ----
					sliderMediaPosition.setValue(0);
					sliderMediaPosition.setPaintLabels(true);
					sliderMediaPosition.setPaintTicks(true);
					sliderMediaPosition.setMaximum(1000);
					sliderMediaPosition.addChangeListener(new ChangeListener() {
						@Override
						public void stateChanged(ChangeEvent e) {
							sliderMediaPositionStateChanged(e);
						}
					});
					xPanelVideoControl.add(sliderMediaPosition, new GridBagConstraints(0, 1, 6, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

					//---- buttonPrevious ----
					buttonPrevious.setIcon(new ImageIcon(getClass().getResource("/imgs/video/media_step_back.png")));
					buttonPrevious.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							buttonPreviousActionPerformed(e);
						}
					});
					xPanelVideoControl.add(buttonPrevious, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

					//---- buttonPlayPause ----
					buttonPlayPause.setIcon(new ImageIcon(getClass().getResource("/imgs/video/media_play.png")));
					xPanelVideoControl.add(buttonPlayPause, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

					//---- buttonStop ----
					buttonStop.setIcon(new ImageIcon(getClass().getResource("/imgs/video/media_stop.png")));
					buttonStop.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							buttonStopActionPerformed(e);
						}
					});
					xPanelVideoControl.add(buttonStop, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

					//---- buttonNext ----
					buttonNext.setIcon(new ImageIcon(getClass().getResource("/imgs/video/media_step_forward.png")));
					buttonNext.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							buttonNextActionPerformed(e);
						}
					});
					xPanelVideoControl.add(buttonNext, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

					//---- sliderVolume ----
					sliderVolume.setMajorTickSpacing(25);
					sliderVolume.setForeground(Color.black);
					sliderVolume.setPaintTicks(true);
					sliderVolume.setPaintLabels(true);
					xPanelVideoControl.add(sliderVolume, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
				}
				contentPanel.add(xPanelVideoControl, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
				((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

				//---- okButton ----
				okButton.setText("OK");
				buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));

				//---- cancelButton ----
				cancelButton.setText("Cancel");
				buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - franky wild
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JXPanel xPanelVideoControl;
	private JSeparator separatorVideoControl;
	private JSlider sliderMediaPosition;
	private JXButton buttonPrevious;
	private JXButton buttonPlayPause;
	private JXButton buttonStop;
	private JXButton buttonNext;
	private JSlider sliderVolume;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
