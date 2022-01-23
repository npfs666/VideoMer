/*
 * Created by JFormDesigner on Sun Apr 15 22:34:49 CEST 2012
 */

package update.vue;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorLevel;

import update.DefaultController;
import update.UpdateManager;
import update.modeleXml.Library;
import update.modeleXml.Update;

/**
 * @author Jean Mouloud
 */
@SuppressWarnings({"unchecked", "rawtypes", "serial"})
public class JFrameUpdate extends JFrame {
	
	private UpdateManager updateManager;
	private ExecutorService es;
	private long downloadedPatchsSize;
	private DefaultController controller;
	private boolean detailsDisplayed;
	private static enum OS  {linux, solaris, windows, macos, unknown;}
	
	/**
	 * Etape de la MAJ (0 pas de fichier properties existant, 1 properties lu, 2 MAj terminée) 
	 */
	private int step;
	
	
	
	
	public JFrameUpdate(DefaultController controler) {
		
		this.controller = controler;
		step = 1;
		downloadedPatchsSize = 0;
		detailsDisplayed = false;
		
		try {
			
			updateManager = new UpdateManager(controller);
			controller.loadPropertiesFile();
			
		} catch (IOException e) {
			
			// => fichier properties manquant
			step = 0;			
			controller.getProperties().defaultProperties();
			
			try {
				controller.getProperties().saveToFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		controller.setLocale();
		
		initComponents();
		
		updateLang();
		comboLang.setRenderer(new LocaleListCellRenderer());
		comboLang.setModel(new DefaultComboBoxModel(controller.getListeLangues()));
		comboLang.setSelectedItem(new Locale(controller.getProperties().getLanguage()));
		
		if( step == 1 ) {
			initData();
			buttonChangelog.setEnabled(true);
			buttonDetails.setEnabled(true);
			buttonStart.setEnabled(true);
		}

		buttonDetailsActionPerformed(null);
	}
	
	private void initData() {
		
		try {
			lblActualVersion.setText(updateManager.getActualVersion());
			lblNewVersion.setText(updateManager.getLastVersion());
			
			ArrayList<Update> al = updateManager.getLatestsUpdates();
			
			if( al.size() == 0 ) 
				lblDownloadFileCount.setText("aucune MAJ dispo");
			else
				lblDownloadFileCount.setText(""+updateManager.getTotalUpdateCount());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private static OS getPlatform() {

		String osName = System.getProperty("os.name").toLowerCase();

		if (osName.contains("win"))
			return OS.windows;

		if (osName.contains("mac"))
			return OS.macos;

		if (osName.contains("solaris"))
			return OS.solaris;

		if (osName.contains("sunos"))
			return OS.solaris;

		if (osName.contains("linux"))
			return OS.linux;

		if (osName.contains("unix"))
			return OS.linux;

		return OS.unknown;
	}
	
	private void buttonDetailsActionPerformed(ActionEvent e) {
		
		detailsDisplayed = !detailsDisplayed;
		
		if( detailsDisplayed ) {
			panelGlobal.remove(panelDetails);
			pack();
		} else {
			panelGlobal.add(panelDetails, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
			pack();
		}
	}

	private void buttonChangelogActionPerformed(ActionEvent e) {
		
	}

	private void buttonStartActionPerformed(ActionEvent e) {
		
		// Téléchargement annulé
		if( buttonStart.getText() == "Annuler" ) {
			
			buttonStart.setText("Commencer");
			es.shutdownNow();
			return;
		} 
		// Téléchargement terminé
		else if( step == 2 ) {
			try {
				Desktop.getDesktop().open(new File("VideoMer.jar"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);
		}
		
		buttonStart.setText("Annuler");
		
		new Thread (new Runnable() {
			
			@Override
			public void run() {
				
				// affichage
				lblTotalSize.setText(((int)(updateManager.getTotalUpdateSize()/1024f)) + " Ko");
				textPaneDetails.setText("");
				
				new File("lib/").mkdir();
				es = Executors.newSingleThreadExecutor();
			
				try {
					
					for( Update u : updateManager.getLatestsUpdates() ) {
						
						for( Library l : u.getFiles() ) {
							
							es.submit(new InstallationLibrairie(l, u.getPatchVersion()));
						}
					}
					
					es.shutdown();
					es.awaitTermination(600, TimeUnit.SECONDS);
					
					// Si annulé on ne met rien à jour
					if( buttonStart.getText() != "Commencer" ) {
						
						progressBar.setValue(progressBar.getMaximum());
						controller.getProperties().setSoftwareVersion(updateManager.getLastVersion());
						controller.getProperties().saveToFile();
						buttonStart.setText("Terminer");
						step = 2;
					}
					
				} catch (Exception e1) {
					JXErrorPane.showDialog(null, new ErrorInfo("ERROR!", e1.getMessage(), null, null, e1, ErrorLevel.WARNING, null));
					e1.printStackTrace();
				}
				
			}
		}).start();
	}

	private void comboOsActionPerformed(ActionEvent e) {
		
		// Mise à jour des preférences
		controller.getProperties().setOperatingSystem(comboOs.getSelectedIndex()+1);
		
		// On débloque certains boutons
		buttonChangelog.setEnabled(true);
		buttonDetails.setEnabled(true);
		buttonStart.setEnabled(true);

		step = 1;
		initData();
	}
	
	private void updateLang() {
		setTitle(controller.getBundle().getString("JFrameUpdate.this.title"));
		lblOs.setText(controller.getBundle().getString("JFrameUpdate.lblOs.text"));
		lblLanguage.setText(controller.getBundle().getString("JFrameUpdate.lblLanguage.text"));
		lblActuelVersionText.setText(controller.getBundle().getString("JFrameUpdate.lblActuelVersionText.text"));
		lblNewVersionText.setText(controller.getBundle().getString("JFrameUpdate.lblNewVersionText.text"));
		lblDownloadFileCountText.setText(controller.getBundle().getString("JFrameUpdate.lblDownloadFileCountText.text"));
		panelDetails.setBorder(new TitledBorder(BorderFactory.createEmptyBorder(), controller.getBundle().getString("JFrameUpdate.panelDetails.border")));
		buttonChangelog.setText(controller.getBundle().getString("JFrameUpdate.buttonChangelog.text"));
		buttonDetails.setText(controller.getBundle().getString("JFrameUpdate.buttonDetails.text"));
		buttonStart.setText(controller.getBundle().getString("JFrameUpdate.buttonStart.text"));
	}

	private void comboLangActionPerformed(ActionEvent e) {
		
		controller.getProperties().setLanguage(((Locale)comboLang.getSelectedItem()).getLanguage());
		
		try {
			controller.getProperties().saveToFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		controller.setLocale();
		updateLang();
	}

	private void thisWindowClosing(WindowEvent e) {
		System.exit(0);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Jean Mouloud
		ResourceBundle bundle = controller.getBundle();
		panelGlobal = new JPanel();
		panelInformations = new JPanel();
		lblOs = new JLabel();
		comboOs = new JComboBox();
		lblLanguage = new JLabel();
		comboLang = new JComboBox();
		lblActuelVersionText = new JLabel();
		lblActualVersion = new JLabel();
		lblNewVersionText = new JLabel();
		lblNewVersion = new JLabel();
		lblDownloadFileCountText = new JLabel();
		lblDownloadFileCount = new JLabel();
		progressBar = new JProgressBar();
		lblDownloadedSize = new JLabel();
		lblTotalSize = new JLabel();
		panelDetails = new JPanel();
		scrollPaneDetails = new JScrollPane();
		textPaneDetails = new JTextPane();
		panelButtons = new JPanel();
		buttonChangelog = new JButton();
		buttonDetails = new JButton();
		buttonStart = new JButton();

		//======== this ========
		setTitle(bundle.getString("JFrameUpdate.this.title"));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				thisWindowClosing(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== panelGlobal ========
		{
			panelGlobal.setBorder(new EmptyBorder(7, 7, 7, 7));
			panelGlobal.setLayout(new GridBagLayout());
			((GridBagLayout)panelGlobal.getLayout()).columnWidths = new int[] {0, 0, 0};
			((GridBagLayout)panelGlobal.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
			((GridBagLayout)panelGlobal.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0E-4};
			((GridBagLayout)panelGlobal.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4};

			//======== panelInformations ========
			{
				panelInformations.setBorder(new EmptyBorder(2, 2, 2, 2));
				panelInformations.setLayout(new GridBagLayout());
				((GridBagLayout)panelInformations.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
				((GridBagLayout)panelInformations.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
				((GridBagLayout)panelInformations.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0E-4};
				((GridBagLayout)panelInformations.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

				//---- lblOs ----
				lblOs.setText(bundle.getString("JFrameUpdate.lblOs.text"));
				panelInformations.add(lblOs, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- comboOs ----
				comboOs.setModel(new DefaultComboBoxModel(new String[] {
					"Windows",
					"Unix",
					"Mac"
				}));
				comboOs.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						comboOsActionPerformed(e);
					}
				});
				panelInformations.add(comboOs, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- lblLanguage ----
				lblLanguage.setText(bundle.getString("JFrameUpdate.lblLanguage.text"));
				panelInformations.add(lblLanguage, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- comboLang ----
				comboLang.setModel(new DefaultComboBoxModel(new String[] {
					"English",
					"French"
				}));
				comboLang.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						comboLangActionPerformed(e);
					}
				});
				panelInformations.add(comboLang, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

				//---- lblActuelVersionText ----
				lblActuelVersionText.setText(bundle.getString("JFrameUpdate.lblActuelVersionText.text"));
				panelInformations.add(lblActuelVersionText, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- lblActualVersion ----
				lblActualVersion.setText(" ");
				panelInformations.add(lblActualVersion, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- lblNewVersionText ----
				lblNewVersionText.setText(bundle.getString("JFrameUpdate.lblNewVersionText.text"));
				panelInformations.add(lblNewVersionText, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- lblNewVersion ----
				lblNewVersion.setText(" ");
				panelInformations.add(lblNewVersion, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

				//---- lblDownloadFileCountText ----
				lblDownloadFileCountText.setText(bundle.getString("JFrameUpdate.lblDownloadFileCountText.text"));
				panelInformations.add(lblDownloadFileCountText, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));

				//---- lblDownloadFileCount ----
				lblDownloadFileCount.setText(" ");
				panelInformations.add(lblDownloadFileCount, new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			panelGlobal.add(panelInformations, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//---- progressBar ----
			progressBar.setStringPainted(true);
			progressBar.setPreferredSize(new Dimension(450, 17));
			progressBar.setMaximum(1000);
			panelGlobal.add(progressBar, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//---- lblDownloadedSize ----
			lblDownloadedSize.setHorizontalAlignment(SwingConstants.RIGHT);
			lblDownloadedSize.setText(" ");
			panelGlobal.add(lblDownloadedSize, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

			//---- lblTotalSize ----
			lblTotalSize.setText(" ");
			panelGlobal.add(lblTotalSize, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//======== panelDetails ========
			{
				panelDetails.setBorder(new TitledBorder(BorderFactory.createEmptyBorder(), bundle.getString("JFrameUpdate.panelDetails.border")));
				panelDetails.setLayout(new GridBagLayout());
				((GridBagLayout)panelDetails.getLayout()).columnWidths = new int[] {0, 0};
				((GridBagLayout)panelDetails.getLayout()).rowHeights = new int[] {0, 0};
				((GridBagLayout)panelDetails.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
				((GridBagLayout)panelDetails.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

				//======== scrollPaneDetails ========
				{

					//---- textPaneDetails ----
					textPaneDetails.setPreferredSize(new Dimension(143, 150));
					scrollPaneDetails.setViewportView(textPaneDetails);
				}
				panelDetails.add(scrollPaneDetails, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			panelGlobal.add(panelDetails, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//======== panelButtons ========
			{
				panelButtons.setBorder(new EmptyBorder(5, 0, 0, 0));
				panelButtons.setLayout(new GridBagLayout());
				((GridBagLayout)panelButtons.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
				((GridBagLayout)panelButtons.getLayout()).rowHeights = new int[] {0, 0};
				((GridBagLayout)panelButtons.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 0.0, 1.0E-4};
				((GridBagLayout)panelButtons.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

				//---- buttonChangelog ----
				buttonChangelog.setText(bundle.getString("JFrameUpdate.buttonChangelog.text"));
				buttonChangelog.setEnabled(false);
				buttonChangelog.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						buttonChangelogActionPerformed(e);
					}
				});
				panelButtons.add(buttonChangelog, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));

				//---- buttonDetails ----
				buttonDetails.setText(bundle.getString("JFrameUpdate.buttonDetails.text"));
				buttonDetails.setEnabled(false);
				buttonDetails.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						buttonDetailsActionPerformed(e);
					}
				});
				panelButtons.add(buttonDetails, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));

				//---- buttonStart ----
				buttonStart.setText(bundle.getString("JFrameUpdate.buttonStart.text"));
				buttonStart.setEnabled(false);
				buttonStart.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						buttonStartActionPerformed(e);
					}
				});
				panelButtons.add(buttonStart, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			panelGlobal.add(panelButtons, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(panelGlobal, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	public static void main(String[] args) throws Exception {

		DefaultController controller = new DefaultController();
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrameUpdate  tmp = new JFrameUpdate(controller);
		tmp.setVisible(true);
	}
	
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Jean Mouloud
	private JPanel panelGlobal;
	private JPanel panelInformations;
	private JLabel lblOs;
	private JComboBox comboOs;
	private JLabel lblLanguage;
	private JComboBox comboLang;
	private JLabel lblActuelVersionText;
	private JLabel lblActualVersion;
	private JLabel lblNewVersionText;
	private JLabel lblNewVersion;
	private JLabel lblDownloadFileCountText;
	private JLabel lblDownloadFileCount;
	private JProgressBar progressBar;
	private JLabel lblDownloadedSize;
	private JLabel lblTotalSize;
	private JPanel panelDetails;
	private JScrollPane scrollPaneDetails;
	private JTextPane textPaneDetails;
	private JPanel panelButtons;
	private JButton buttonChangelog;
	private JButton buttonDetails;
	private JButton buttonStart;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private class InstallationLibrairie implements Runnable {

		private String version;
		private Library newLib;
		
		public InstallationLibrairie(Library newLib, String version) {
			this.newLib = newLib;
			this.version = version;
		}
		
		@Override
		public void run() {

			try {
				
				// Log du téléchargement
				textPaneDetails.replaceSelection("Téléchargement de : "+newLib.getLibName()+ " "+newLib.getLibVersion()+"\n");
				
				// Téléchargement de la version distante
				URL u = new URL(UpdateManager.updateChannel + version + "/" +newLib.getFilePath());
				URLConnection uc = u.openConnection();

				int fileLenght = uc.getContentLength();
				if (fileLenght == -1)
					throw new IOException("Fichier non valide.");

				
				InputStream in = uc.getInputStream();
				FileOutputStream WritenFile = new FileOutputStream("temp");
				byte[] buff = new byte[1024];
				int l = in.read(buff);

				while (l > 0) {
					
					// Affichage
					downloadedPatchsSize += l;
					Double f = ((double)downloadedPatchsSize / updateManager.getTotalUpdateSize() ) * 1000;
					progressBar.setValue(f.intValue());
					lblDownloadedSize.setText(((int)(downloadedPatchsSize/1024.0)) + " /");
					
					WritenFile.write(buff, 0, l);
					l = in.read(buff);
				}
				WritenFile.flush();
				WritenFile.close();
				in.close();
				
				// Effacer l'ancienne bibliothèque & renommer la nouvelle
				// Si core, pas le mm dossier
				File oldLib;
				if( newLib.getLibName().equals("VideoMer") )
					oldLib = new File("./"+newLib.getFilePath());
				else
					oldLib = new File("lib/" + newLib.getFilePath());
				
				if( oldLib.exists() )
					oldLib.delete();
				
				oldLib.getParentFile().mkdirs();
				
				File temp = new File("temp");
				temp.renameTo(oldLib);
				
				boolean newEntry = true;
				// Mettre à jour les propriétés locales
				for( Library lib : controller.getProperties().getVersions() ) {
					
					// Si biblio déjà présente on la met à jour
					if( lib.getLibName().compareToIgnoreCase(newLib.getLibName()) == 0 ) {
						
						int index = controller.getProperties().getVersions().indexOf(lib);
						controller.getProperties().getVersions().get(index).setLibVersion(newLib.getLibVersion());
						controller.getProperties().saveToFile();
						newEntry = false;
					}
				}
				
				// Si biblio non présente on ajoute le tout
				if(newEntry) {
					controller.getProperties().getVersions().add(newLib);
				}
				
				// log de l'install
				textPaneDetails.replaceSelection("Installation terminée...\n");

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// puis on delete la version locale pour la remplacer
		}		
	}
}
