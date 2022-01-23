/*
 * Created by JFormDesigner on Mon Jul 26 16:33:15 CEST 2010
 */

package components.renderer;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modele.EntiteVideo;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorLevel;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import com.sun.jna.NativeLibrary;
import components.model.PlaylistModel;

import controleur.DefaultController;

/**
 * 
 * @author Brendan
 * @version 0.1 1/10/2010
 */
@SuppressWarnings("serial")
public class JPanelVideoControl extends JPanel {

	private DefaultController controller;
	private EmbeddedMediaPlayer mediaPlayer = null;
	private boolean mousePressedPlaying;
	private JXList playlist = null;
	private boolean fullScreen = false;
	private Canvas videoSurface;
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	
	/**
	 * Constructeur 
	 */
	public JPanelVideoControl(DefaultController controller) {
		
		this.controller = controller;
		
		initComponents();
		videoSurface = new Canvas();
		videoFrame.add(videoSurface, BorderLayout.CENTER);
		initListeners();
		initData();
	}
	
	private void initListeners() {
		
		//TODO: prendre en compte le multi plateforme
		NativeLibrary.addSearchPath("libvlc", ".\\lib");
		
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(videoSurface));
		
		mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			
			@Override
			public void playing(MediaPlayer mediaPlayerXX) {
				
				long time = mediaPlayer.getLength();

				// Gestion de la barre de défilement
				sliderMediaPosition.setMaximum(1000);
				sliderVolume.setValue(mediaPlayer.getVolume());
				
				// Affichage de la durée max
				String s = String.format(
						"%02d:%02d:%02d",
						TimeUnit.MILLISECONDS.toHours(time),
						TimeUnit.MILLISECONDS.toMinutes(time)
								- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
						TimeUnit.MILLISECONDS.toSeconds(time)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
				lblMaxTime.setText("/ "+s);
			}

			@Override
			public void error(MediaPlayer mediaPlayer) {
				
				JPanelVideoControl.this.mediaPlayer.stop();
				videoFrame.setVisible(false);
				videoFrame.dispose();
				buttonPlayPause.setIcon(new ImageIcon(getClass().getResource("/imgs/32x32/media_play.png")));
				
				JXErrorPane.showDialog(null, new ErrorInfo("ERROR!", "impossible de lancer cette merde", null, null, null, ErrorLevel.SEVERE, null));
			}
			
			@Override
			public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
				
				videoFrame.setSize(mediaPlayer.getVideoDimension());
			}

			// passer à la lecture du suivant
			@Override
			public void finished(MediaPlayer mediaPlayerXX) {

				buttonStopActionPerformed(null);

				if (mediaPlayer != null && playlist.getSelectedIndex() < playlist.getModel().getSize() - 1) {

					// Lecture du suivant
					playlist.setSelectedIndex(playlist.getSelectedIndex() + 1);
					buttonPlayPauseActionPerformed(null);
				}
			}

			// Release un peu de tout
			@Override
			public void stopped(MediaPlayer mediaPlayer) {
			}
		});
		
		videoFrame.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				// Entrée/Sortie du fullscreen avec la touche "f"
				if( arg0.getKeyCode() == KeyEvent.VK_F ) {
					
					if( !fullScreen )
						videoFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());		
					else
						videoFrame.setSize(mediaPlayer.getVideoDimension());
					
					//videoFrame.toFront();
					fullScreen = !fullScreen;
				}
			}
		});
		videoFrame.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				//sliderMediaPosition.setValue(sliderMediaPosition.getValue()-5*e.getWheelRotation());
				sliderVolume.setValue(sliderVolume.getValue()-5*e.getWheelRotation());
			}
		});
	}
	
	private void initData() {
		
		sliderVolume.setValue(controller.getPreferences().getVolume());
		executorService.scheduleAtFixedRate(new UpdateRunnable(), 0L, 1L, TimeUnit.SECONDS);
	}
	
	public void exit() {
		executorService.shutdownNow();
	}
	
	public void play() {
		buttonPlayPauseActionPerformed(null);
	}
	
	public long getCurrentVideoTime() {
		if( mediaPlayer != null )
			return mediaPlayer.getTime();
		return 0;
	}
	
	public int getVolume() {
		return sliderVolume.getValue();
	}

	public void setPlayList(JXList playlist) {
		this.playlist = playlist;
	}

	private void buttonPreviousActionPerformed(ActionEvent e) {
		
		buttonStopActionPerformed(null);

		if (mediaPlayer != null && playlist.getSelectedIndex() < playlist.getModel().getSize() - 1) {

			// Lecture du suivant
			playlist.setSelectedIndex(playlist.getSelectedIndex() - 1);
			buttonPlayPauseActionPerformed(null);
		}
	}

	private void buttonPlayPauseActionPerformed(ActionEvent e) {

		// Si rien lecture, lecture de la playlist
		if( mediaPlayer.getLength() == -1 ) {
			
			buttonPlayPause.setIcon(new ImageIcon(getClass().getResource("/imgs/32x32/media_pause.png")));
			
			PlaylistModel model = (PlaylistModel)playlist.getModel();
			final EntiteVideo ev = model.getElement(playlist.getSelectedIndex());

			try {
				System.out.println(ev.getEmplacement());
				//mediaPlayer.prepareMedia(new String(ev.getEmplacement().getBytes("UTF-8")));
				
				//videoFrame.setUndecorated(true);
				//videoFrame.setVisible(true);
				//videoFrame.setSize(800, 600);
				
				//mediaPlayer.play();
				Desktop.getDesktop().open(new File(ev.getEmplacement()));
				
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println("erreur test");
				e1.printStackTrace();
			}
		}
		// Si lecture => pause
		else if( mediaPlayer != null && mediaPlayer.isPlaying() ) {
			
			mediaPlayer.pause();
			buttonPlayPause.setIcon(new ImageIcon(getClass().getResource("/imgs/32x32/media_play.png")));
			
		} 
		// Si pause -> relecture
		else if( mediaPlayer != null && !mediaPlayer.isPlaying() ) {
			
			mediaPlayer.play();
			buttonPlayPause.setIcon(new ImageIcon(getClass().getResource("/imgs/32x32/media_pause.png")));
			
		}
	}

	private void buttonStopActionPerformed(ActionEvent e) {
		
		if( mediaPlayer != null ) {
			mediaPlayer.stop();
			videoFrame.setVisible(false);
			videoFrame.dispose();
			buttonPlayPause.setIcon(new ImageIcon(getClass().getResource("/imgs/32x32/media_play.png")));
		}
	}

	private void buttonNextActionPerformed(ActionEvent e) {
		
		buttonStopActionPerformed(null);

		if (mediaPlayer != null && playlist.getSelectedIndex() < playlist.getModel().getSize() - 1) {

			// Lecture du suivant
			playlist.setSelectedIndex(playlist.getSelectedIndex() + 1);
			buttonPlayPauseActionPerformed(null);
		}
	}

	private void sliderVolumeStateChanged(ChangeEvent e) {
		if( mediaPlayer != null )
			mediaPlayer.setVolume(sliderVolume.getValue());
	}

	private void sliderMediaPositionMouseWheelMoved(MouseWheelEvent e) {
		
	}

	private void sliderVolumeMouseWheelMoved(MouseWheelEvent e) {
		sliderVolume.setValue(sliderVolume.getValue()-5*e.getWheelRotation());
	}

	private void sliderMediaPositionMousePressed(MouseEvent e) {

		if (mediaPlayer.isPlaying()) {
			mousePressedPlaying = true;
			mediaPlayer.pause();
		} else {
			mousePressedPlaying = false;
		}
		setSliderBasedPosition();
	}

	private void sliderMediaPositionMouseReleased(MouseEvent e) {
		setSliderBasedPosition();
        updateUIState();
	}
	
	/**
	 * Broken out position setting, handles updating mediaPlayer
	 */
	private void setSliderBasedPosition() {
		
		if ( !mediaPlayer.isSeekable() )
			return;
		
		float positionValue = (float) sliderMediaPosition.getValue() / 1000.0f;
		// Avoid end of file freeze-up
		if (positionValue > 0.99f)
			positionValue = 0.99f;
		
		mediaPlayer.setPosition(positionValue);
	}
	
	private void updateUIState() {
		if (!mediaPlayer.isPlaying()) {
			// Resume play or play a few frames then pause to show current
			// position in video
			mediaPlayer.play();
			if (!mousePressedPlaying) {
				try {
					// Half a second probably gets an iframe
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// Don't care if unblocked early
				}
				mediaPlayer.pause();
			}
		}
		long time = mediaPlayer.getTime();
		int position = (int) (mediaPlayer.getPosition() * 1000.0f);
		
		updateTime(time);
		updatePosition(position);
	}
	
	/**
	 * Met à jour le temps actuel du film
	 * 
	 * @param time
	 */
	private void updateTime(long time) {
		String s = String.format(
				"%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(time),
				TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
				TimeUnit.MILLISECONDS.toSeconds(time)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
		lblActualTime.setText(s + " ");
	}

	/** 
	 * Met à jour la postition du slider Film
	 * 
	 * @param value
	 */
	private void updatePosition(int value) {
		sliderMediaPosition.setValue(value);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Jean Mouloud
		xPanelVideoControl = new JXPanel();
		separatorVideoControl = new JSeparator();
		sliderMediaPosition = new JSlider();
		buttonPrevious = new JXButton();
		buttonPlayPause = new JXButton();
		buttonStop = new JXButton();
		buttonNext = new JXButton();
		lblActualTime = new JLabel();
		lblMaxTime = new JLabel();
		sliderVolume = new JSlider();
		videoFrame = new JFrame();

		//======== this ========
		setLayout(new BorderLayout());

		//======== xPanelVideoControl ========
		{
			xPanelVideoControl.setLayout(new GridBagLayout());
			((GridBagLayout)xPanelVideoControl.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
			((GridBagLayout)xPanelVideoControl.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
			((GridBagLayout)xPanelVideoControl.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 1.0E-4};
			((GridBagLayout)xPanelVideoControl.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
			xPanelVideoControl.add(separatorVideoControl, new GridBagConstraints(0, 0, 7, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

			//---- sliderMediaPosition ----
			sliderMediaPosition.setValue(0);
			sliderMediaPosition.setPaintLabels(true);
			sliderMediaPosition.setPaintTicks(true);
			sliderMediaPosition.setMaximum(1000);
			sliderMediaPosition.addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					sliderMediaPositionMouseWheelMoved(e);
				}
			});
			sliderMediaPosition.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					sliderMediaPositionMousePressed(e);
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					sliderMediaPositionMouseReleased(e);
				}
			});
			xPanelVideoControl.add(sliderMediaPosition, new GridBagConstraints(0, 1, 7, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

			//---- buttonPrevious ----
			buttonPrevious.setIcon(new ImageIcon(getClass().getResource("/imgs/32x32/media_step_back.png")));
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
			buttonPlayPause.setIcon(new ImageIcon(getClass().getResource("/imgs/32x32/media_play.png")));
			buttonPlayPause.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonPlayPauseActionPerformed(e);
				}
			});
			xPanelVideoControl.add(buttonPlayPause, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

			//---- buttonStop ----
			buttonStop.setIcon(new ImageIcon(getClass().getResource("/imgs/32x32/media_stop.png")));
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
			buttonNext.setIcon(new ImageIcon(getClass().getResource("/imgs/32x32/media_step_forward.png")));
			buttonNext.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonNextActionPerformed(e);
				}
			});
			xPanelVideoControl.add(buttonNext, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

			//---- lblActualTime ----
			lblActualTime.setHorizontalAlignment(SwingConstants.RIGHT);
			lblActualTime.setFont(lblActualTime.getFont().deriveFont(lblActualTime.getFont().getStyle() | Font.BOLD, lblActualTime.getFont().getSize() + 2f));
			xPanelVideoControl.add(lblActualTime, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

			//---- lblMaxTime ----
			lblMaxTime.setHorizontalAlignment(SwingConstants.LEFT);
			lblMaxTime.setFont(lblMaxTime.getFont().deriveFont(lblMaxTime.getFont().getStyle() | Font.BOLD, lblMaxTime.getFont().getSize() + 2f));
			xPanelVideoControl.add(lblMaxTime, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

			//---- sliderVolume ----
			sliderVolume.setMajorTickSpacing(25);
			sliderVolume.setForeground(Color.black);
			sliderVolume.setPaintTicks(true);
			sliderVolume.setPaintLabels(true);
			sliderVolume.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					sliderVolumeStateChanged(e);
				}
			});
			sliderVolume.addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					sliderVolumeMouseWheelMoved(e);
				}
			});
			xPanelVideoControl.add(sliderVolume, new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		add(xPanelVideoControl, BorderLayout.CENTER);

		//======== videoFrame ========
		{
			videoFrame.setAlwaysOnTop(true);
			videoFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			Container videoFrameContentPane = videoFrame.getContentPane();
			videoFrameContentPane.setLayout(new BorderLayout());
		}
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Jean Mouloud
	private JXPanel xPanelVideoControl;
	private JSeparator separatorVideoControl;
	private JSlider sliderMediaPosition;
	private JXButton buttonPrevious;
	private JXButton buttonPlayPause;
	private JXButton buttonStop;
	private JXButton buttonNext;
	private JLabel lblActualTime;
	private JLabel lblMaxTime;
	private JSlider sliderVolume;
	private JFrame videoFrame;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private final class UpdateRunnable implements Runnable {

		@Override
		public void run() {
			final long time = mediaPlayer.getTime();
			final int position = (int) (mediaPlayer.getPosition() * 1000.0f);

			// Updates to user interface components must be executed on the
			// Event
			// Dispatch Thread
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (mediaPlayer.isPlaying()) {
						updateTime(time);
						updatePosition(position);
					}
				}
			});
		}
	}
}