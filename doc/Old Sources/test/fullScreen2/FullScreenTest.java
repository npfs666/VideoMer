package test.fullScreen2;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;

import com.sun.jna.NativeLibrary;

/**
 * Simple full-screen test.
 */
public class FullScreenTest extends VlcjTest {

	private EmbeddedMediaPlayer mediaPlayer;

	public static void main(final String[] args) {
		if (args.length != 1) {
			System.err.println("Specify a single MRL");
			System.exit(1);
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new FullScreenTest(args);
			}
		});
	}

	public FullScreenTest(String[] args) {
		Canvas c = new Canvas();
		c.setBackground(Color.black);

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(c, BorderLayout.CENTER);

		final JFrame f = new JFrame("VLCJ");
		// f.setIconImage(new
		// ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
		f.setContentPane(p);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		// f.setUndecorated(true);

		NativeLibrary.addSearchPath("libvlc", ".\\lib");
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		mediaPlayer = mediaPlayerFactory
				.newEmbeddedMediaPlayer(new FullScreenStrategy() {

					@Override
					public void enterFullScreenMode() {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								// f.dispose();
								// f.setUndecorated(true);
								// f.setBounds(0, 0, 1920, 1080);
								f.toFront();
								f.setVisible(true);
							}
						});
					}

					@Override
					public void exitFullScreenMode() {

					}

					@Override
					public boolean isFullScreenMode() {
						return false;
					}
				});

		JButton b = new JButton("text");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.setFullScreen(true);
			}
		});
		p.add(b, BorderLayout.SOUTH);

		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));

		f.setVisible(true);

		mediaPlayer.setFullScreen(true);
		f.toFront();
		mediaPlayer.startMedia(args[0]);
		// GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(f);
	}
}
