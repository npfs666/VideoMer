package test.fullScreen2;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibDwmApi;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class ExclusiveFullScreenTest extends VlcjTest {

    public static void main(final String[] args) {
        if(args.length != 1) {
            System.err.println("Specify a single MRL");
            System.exit(1);
        }
        NativeLibrary.addSearchPath("libvlc", ".\\lib");
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        try {
            LibDwmApi.INSTANCE.DwmEnableComposition(LibDwmApi.DWM_EC_DISABLECOMPOSITION);
        }
        catch(Throwable t) {
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    LibDwmApi.INSTANCE.DwmEnableComposition(LibDwmApi.DWM_EC_ENABLECOMPOSITION);
                }
                catch(Throwable t) {
                }
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ExclusiveFullScreenTest(args);
            }
        });
    }

    @SuppressWarnings("serial")
    public ExclusiveFullScreenTest(String[] args) {
        Canvas c = new Canvas();
        c.setBackground(Color.red);

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(c, BorderLayout.CENTER);

        JFrame f = new JFrame();
        //f.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        f.setContentPane(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(800, 600);

        final MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory("--no-video-title-show");
        final EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();

        mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));

        p.getActionMap().put("start", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.play();
            }
        });

        p.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "start");

        // Go directly to full-screen exclusive mode, do not use the media player
        // full screen strategy to do it. If you have multiple screens then you
        // need to provide a way to choose the desired screen device here
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(f);

        mediaPlayer.prepareMedia(args[0]);
    }
}