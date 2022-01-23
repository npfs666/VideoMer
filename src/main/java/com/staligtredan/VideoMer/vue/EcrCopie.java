package com.staligtredan.VideoMer.vue;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.staligtredan.VideoMer.controleur.DefaultController;
import com.staligtredan.VideoMer.modele.EntiteVideo;

/**
 * JDialog qui permet de copier le contenu d'une playlist vers un autre support
 * 
 * @author Brendan Jaouen
 * 
 * @since   0.17  2/12/2014
 * @version 0.18  7/02/2015
 * @version 1.0.2 8/02/2016 Correction du bouton annuler (ça stope tout maintenant)
 * 							 Amélioration de l'affichage du temps & vitesse (moyenne mobile, et aff. des secondes)
 * @version 2.0.0 22/12/2016 Amélioration de la gestion d'erreurs et loging
 * @version 2.0.2 08/08/2017 Correction du temps restant & vitesse de copie
 */
public class EcrCopie extends JDialog {
	
	private static final long serialVersionUID = -713579023869473462L;

	final static Logger logger = LogManager.getLogger(EcrCopie.class);

	/**
	 * Liste des éléments dans la playlist
	 */
	private ArrayList<EntiteVideo> playlist;
	
	
	private DefaultController controller;
	
	private Thread copyDaemon;
	
	long filesSize = 0;

	
	
	public EcrCopie( Frame owner , DefaultController controller, ArrayList<EntiteVideo> playlist) {

		super(owner);
		this.controller = controller;
		this.playlist = playlist;
		initComponents();
		initView();
		initListeners();
		initData();
	}



	private void initView() {

		// Centrage de l'écran
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		int width = 400, heigh = 210;
		setBounds(p.x - width / 2, p.y - heigh / 2, width, heigh);
	}



	private void initListeners() {

		// Touche escape pour quitter l'écran
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		
		ActionListener actionListener = new ActionListener() {

			public void actionPerformed( ActionEvent actionEvent ) {

				thisWindowClosing(null);
				dispose();
			}
		};
		
		getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
	}



	private void initData() {

		for ( EntiteVideo ev : playlist ) {

			File f = new File(ev.getEmplacement());
			filesSize += f.length();
		}

		lblTailleOccupee.setText(FileUtils.byteCountToDisplaySize(filesSize));
	}



	private void buttonBrowseActionPerformed( ActionEvent e ) {

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setMultiSelectionEnabled(false);

		int returnVal = fc.showOpenDialog(this);
		if ( returnVal == JFileChooser.APPROVE_OPTION ) {

			lblTailleDispo.setText(FileUtils.byteCountToDisplaySize(fc.getSelectedFile().getFreeSpace()));
			textFieldDestinationFolder.setText(fc.getSelectedFile().getPath());

			if( filesSize < fc.getSelectedFile().getFreeSpace() ) {
				buttonCopy.setEnabled(true);
			}
		}
	}



	private void thisWindowClosing( WindowEvent e ) {

		if( copyDaemon != null )
			copyDaemon.interrupt();
	}



	private void buttonCopyActionPerformed( ActionEvent e ) {
		if( copyDaemon == null ) {
			copyDaemon = new ThreadCopy(this);
			copyDaemon.start();
			buttonCopy.setText(controller.getBundle().getString("EcrOptions.buttonCancel.text"));
		} else {
			copyDaemon.interrupt();
		}
	}




	private void initComponents() {

		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		lblDestinationFolder = new JLabel();
		textFieldDestinationFolder = new JTextField();
		buttonBrowse = new JButton();
		lblAvailableSpace = new JLabel();
		lblTailleDispo = new JLabel();
		lblDataSpace = new JLabel();
		lblTailleOccupee = new JLabel();
		progressBar = new JProgressBar();
		lblTimeLeftstr = new JLabel();
		lblTimeLeft = new JLabel();
		lblSpeed = new JLabel();
		buttonBar = new JPanel();
		buttonCopy = new JButton();

		//======== this ========
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				thisWindowClosing(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));

			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new GridBagLayout());
				((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
				((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4};
				contentPanel.add(lblDestinationFolder, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- textFieldDestinationFolder ----
				textFieldDestinationFolder.setEditable(false);
				contentPanel.add(textFieldDestinationFolder, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- buttonBrowse ----
				buttonBrowse.addActionListener(e -> buttonBrowseActionPerformed(e));
				contentPanel.add(buttonBrowse, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				contentPanel.add(lblAvailableSpace, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- lblTailleDispo ----
				lblTailleDispo.setHorizontalAlignment(SwingConstants.RIGHT);
				contentPanel.add(lblTailleDispo, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				contentPanel.add(lblDataSpace, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- lblTailleOccupee ----
				lblTailleOccupee.setHorizontalAlignment(SwingConstants.RIGHT);
				contentPanel.add(lblTailleOccupee, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

				//---- progressBar ----
				progressBar.setStringPainted(true);
				contentPanel.add(progressBar, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				contentPanel.add(lblTimeLeftstr, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));
				contentPanel.add(lblTimeLeft, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));
				contentPanel.add(lblSpeed, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
				((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

				//---- buttonCopy ----
				buttonCopy.setEnabled(false);
				buttonCopy.addActionListener(e -> buttonCopyActionPerformed(e));
				buttonBar.add(buttonCopy, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);

		initComponentsI18n();

		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	public void initComponentsI18n() {

		// JFormDesigner - Component i18n initialization - DO NOT MODIFY  //GEN-BEGIN:initI18n
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		ResourceBundle bundle = controller.getBundle();
		setTitle(bundle.getString("EcrCopie.this.title"));
		lblDestinationFolder.setText(bundle.getString("EcrCopie.lblDestinationFolder.text"));
		buttonBrowse.setText(bundle.getString("EcrCopie.buttonBrowse.text"));
		lblAvailableSpace.setText(bundle.getString("EcrCopie.lblAvailableSpace.text"));
		lblDataSpace.setText(bundle.getString("EcrCopie.lblDataSpace.text"));
		lblTimeLeftstr.setText(bundle.getString("EcrCopie.lblTimeLeftstr.text"));
		buttonCopy.setText(bundle.getString("EcrCopie.buttonCopy.text"));
		// JFormDesigner - End of component i18n initialization  //GEN-END:initI18n
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Mouloud rachid
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel lblDestinationFolder;
	private JTextField textFieldDestinationFolder;
	private JButton buttonBrowse;
	private JLabel lblAvailableSpace;
	private JLabel lblTailleDispo;
	private JLabel lblDataSpace;
	private JLabel lblTailleOccupee;
	private JProgressBar progressBar;
	private JLabel lblTimeLeftstr;
	private JLabel lblTimeLeft;
	private JLabel lblSpeed;
	private JPanel buttonBar;
	private JButton buttonCopy;
	// JFormDesigner - End of variables declaration  //GEN-END:variables




	private class ThreadCopy extends Thread {
		
		/**
		 * N° de l'opération actuelle
		 */
		private int value = 0;
		
		private long length = 0;
		
		// 2.0.2
		private boolean stop = false;
		
		private JDialog parent;
		
		public ThreadCopy(JDialog parent) {
			super();
			this.parent = parent;
		}
		
		
		/**
		 * Mise à jour de la barre de progression avec verrou
		 */
		private synchronized void updateBar() {

			progressBar.setValue(++value);
		}
		
		@Override
		public void run() {

			progressBar.setMaximum(playlist.size());
			lblTimeLeft.setText("999s");
			TimeLeft tl = new TimeLeft();
			
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			executor.scheduleAtFixedRate(tl, 1, 2, TimeUnit.SECONDS);

			for( EntiteVideo ev : playlist ) {
		
				if( ! interrupted() ) {					
					
					// Copie des fichiers
					File sourceFile = new File(ev.getEmplacement());					
					File destFile = new File( textFieldDestinationFolder.getText() + File.separator + sourceFile.getName() );
					
					//FileUtils.copyFile(sourceFile, destFile);
					InputStream in = null;
					OutputStream out = null;
					try {

						in = new FileInputStream(sourceFile);
						out = new FileOutputStream(destFile);

						byte[] buffer = new byte[32 * 1024];

						while ( true && !stop ) {
							int bytesRead = in.read(buffer);
							if( bytesRead <= 0 ) {
								break;
							}
							if( interrupted() )
								stop = true;
								
							out.write(buffer, 0, bytesRead);
							length += 32 * 1024;
						}

					} catch ( IOException e ) {
						logger.error("Erreur copie : " + e.getMessage(), e);
						controller.erreur(e);
						initComponentsI18n();
						progressBar.setValue(0);
						lblTimeLeft.setText("");
						lblSpeed.setText("");
						copyDaemon = null;
						executor.shutdownNow();
						return;
					} finally {
						if( in != null ) {
							try {
								in.close();
							} catch ( IOException e ) {// failsafe
							}
						}
						if( out != null ) {
							try {
								out.close();
							} catch ( IOException e ) {// failsafe
							}
						}
						
						// 2.0.2
						if( stop ) {
							destFile.delete();
							interrupt(); //il est reset qd il est lu, faut remettre le flag
						}
					}	
					
					
					// Possibilité d'ajouter des dossier différents de "subs"
					File subFolder = new File(sourceFile.getParent() + File.separator + "subs");
					
					// Gestion des sous titres (if copie subs activé then)
					if( true && subFolder.exists() && !interrupted() ) {

						for ( File sourceSub : subFolder.listFiles() ) {

							// Si un fichier sous titres correspondant à un fichier existe
							if ( sourceSub.getName().split("\\.")[0].contains(sourceFile.getName().split("\\.")[0]) ) {

								File destSubFolder = new File(textFieldDestinationFolder.getText() + File.separator
										+ "subs");

								File destSub = new File(textFieldDestinationFolder.getText() + File.separator + "subs"
										+ File.separator + sourceSub.getName());

								if ( !destSubFolder.exists() )
									destSubFolder.mkdir();

								try {
									//copyFileUsingStream(sourceSub, destSub);
									FileUtils.copyFile(sourceSub, destSub);
								}
								catch ( IOException e ) {
									logger.error("Erreur copie : "+e.getMessage(), e);
									controller.erreur(e);
									initComponentsI18n();
									progressBar.setValue(0);
									lblTimeLeft.setText("");
									lblSpeed.setText("");
									copyDaemon = null;
									executor.shutdownNow();
									return;
								}
							}
						}
					} else
						interrupt(); //il est reset qd il est lu, faut remettre le flag
					
					updateBar();
				} else {
					
					// Gestion en cas d'annulation
					executor.shutdownNow();
					interrupt();
					
					executor = Executors.newScheduledThreadPool(1);
					executor.schedule(new Runnable() {

						@Override
						public void run() {

							parent.dispose();
							copyDaemon = null;
						}
					}, 250, TimeUnit.MILLISECONDS);

					return;
				}
			}

			executor.shutdownNow();
		}		
	
		
		
		private class TimeLeft implements Runnable {
			
			private int longTab = 4, plusAncien = 0;
			private long t0=0, tmp;
			private long vitesse[], temps[], mmVitesse=0, mmTemps=0;
			
			TimeLeft() {
				
				vitesse = new long[longTab];
				temps = new long[longTab];
				
				for( int i = 0; i < longTab; i++ ) {
					vitesse[i] = 0;
					temps[i] = 0;
				}
				
			}
			
			public void run() {
				
				// vitesse mobile actuelle
	    		tmp = (length - t0)/2;
	    		mmVitesse = mmVitesse + tmp - vitesse[plusAncien];
	    		vitesse[plusAncien] = tmp;
	    		lblSpeed.setText(FileUtils.byteCountToDisplaySize(mmVitesse/longTab)+"/s");
	    		
	    		// temps mobile restant
	    		tmp = (filesSize - length) / tmp;
	    		mmTemps = mmTemps + tmp - temps[plusAncien];
	    		temps[plusAncien] = tmp;
	    		
	    		// buffer circulaire
	    		plusAncien++;
	    		if (plusAncien == longTab) plusAncien = 0;
	    		
	    		// Affichage du temps restant
	    		tmp = mmTemps/longTab;
	    		
	    		if( tmp < 60 ) {
	    			lblTimeLeft.setText(tmp+"s");
		    	}
		    	else if( tmp < 3600 ) {
		    		
		    		long m = tmp/60;
		    		long s = tmp % 60;
		    		lblTimeLeft.setText(m+"m "+s+"s");
		    	}
		    	else if( tmp < 86400 ) {
		    		
		    		long h = tmp/3600;
		    		long m = (tmp - h*3600)/60;
		    		long s = (tmp - h*3600)%60;
		    		lblTimeLeft.setText(h+"h "+m+"m "+s+"s");
		    	}

		    	t0=length;
		    }
		}
	}
}
