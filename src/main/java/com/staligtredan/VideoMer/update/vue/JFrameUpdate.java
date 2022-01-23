package com.staligtredan.VideoMer.update.vue;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
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
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import com.staligtredan.VideoMer.update.DefaultController;
import com.staligtredan.VideoMer.update.UpdateManager;
import com.staligtredan.VideoMer.update.modeleXml.Library;
import com.staligtredan.VideoMer.update.modeleXml.Library.OS;
import com.staligtredan.VideoMer.update.modeleXml.Update;

/**
 * Ecran d'affichage du module de mise � jour
 * 
 * @author Brendan
 * @since 0.1 23/04/2012
 * @version 1.0.3 10/12/2016 Terminé la localization (il manquait des trucs)
 * 							 MAJ maven
 * @version 2.0.0 22/12/2016 Mise en place du système pour aussi pouvoir mettre à jour l'updater
 * 							 réintégration du bouton patchnote
 * 							 Correction des bugs de MAJ
 */
public class JFrameUpdate extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -301319505376560398L;
	
	/**
	 * Le gestionnaire de MAJ
	 */
	private UpdateManager updateManager;
	
	/**
	 * Le thread de téléchargement
	 */
	private ExecutorService es;
	
	/**
	 * Taille des données téléchargées
	 */
	private long downloadedPatchsSize;
	
	/**
	 * Le controlleur
	 */
	private DefaultController controller;
	
	/**
	 * Affichage ou non des détails
	 */
	private boolean detailsDisplayed;
	
	/**
	 * Etape de la MAJ (0 pas de fichier properties existant, 1 properties lu, 2 MAj terminée) 
	 */
	private int step;
	
	
	
	public JFrameUpdate(DefaultController controler) {
		
		this.controller = controler;
		step = 1;
		downloadedPatchsSize = 0;
		detailsDisplayed = false;
		
		updateManager = new UpdateManager(controller);
		controller.loadPropertiesFile();
		
		controller.setLocale();
		
		initComponents();
		initComponentsI18n();
		
		comboLang.setRenderer(new LocaleListCellRenderer());
		//comboLang.setModel(new DefaultComboBoxModel(controller.getListeLangues()));
		comboLang.setModel(new DefaultComboBoxModel<Object>(controller.getListeLangues()));
		comboLang.setSelectedItem(new Locale(controller.getProperties().getLanguage()));
		
		//comboOs.setModel(new DefaultComboBoxModel(OS.values()));
		List<String> enumNames = Stream.of(OS.values()).map(Enum::name).collect(Collectors.toList());
		comboOs.setModel(new DefaultComboBoxModel<Object>(enumNames.toArray()));
		comboOs.setSelectedItem(controller.getProperties().getOperatingSystem());
		
		if( step == 1 ) {
			initData();
			buttonChangelog.setEnabled(true);
			buttonDetails.setEnabled(true);
			buttonStart.setEnabled(true);
		}

		buttonDetailsActionPerformed(null);
	}
	
	/**
	 * Initialisation des données (versions, nombre de fichiers à dl...)
	 */
	private void initData() {

		lblActualVersion.setText(updateManager.getActualVersion());
		lblNewVersion.setText(updateManager.getLastVersion());

		ArrayList<Update> al = updateManager.getLatestsUpdates();

		if( al.size() == 0 )
			lblDownloadFileCount.setText(controller.getBundle().getString("JFrameUpdate.lblNoUpdateAvailable.text"));
		else
			lblDownloadFileCount.setText("" + updateManager.getTotalUpdateCount());

	}
	
	/**
	 * Action sur le bouton details
	 * 
	 * @param e
	 */
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

	/**
	 * Action sur le bouton changelog
	 * 
	 * @param e
	 */
	private void buttonChangelogActionPerformed(ActionEvent e) {
		JDialogChangelogs jd = new JDialogChangelogs(this, controller,updateManager);
		jd.setVisible(true);
	}

	/**
	 * Action sur le bouton démarrer
	 * @param e
	 */
	private void buttonStartActionPerformed(ActionEvent e) {
		
		if( updateManager.getTotalUpdateCount() == 0 ) {
			try {
				Desktop.getDesktop().open(new File("switcher.jar"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		}
		
		// Téléchargement annulé
		if( buttonStart.getText() == controller.getBundle().getString("JFrameUpdate.lblCancel.text") ) {
			
			buttonStart.setText(controller.getBundle().getString("JFrameUpdate.buttonStart.text"));
			es.shutdownNow();
			return;
		} 
		// Téléchargement terminé
		else if( step == 2 ) {
			try {
				Desktop.getDesktop().open(new File("switcher.jar"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		}
		
		buttonStart.setText(controller.getBundle().getString("JFrameUpdate.lblCancel.text"));
		
		new Thread (new Runnable() {
			
			@Override
			public void run() {
				
				// affichage
				lblTotalSize.setText(((int)(updateManager.getTotalUpdateSize()/1024f)) + " Ko");
				textPaneDetails.setText("");
				
				new File("./lib/").mkdir();
				es = Executors.newSingleThreadExecutor();
			
				try {
					
					for( Update u : updateManager.getLatestsUpdates() ) {
						
						for( Library l : u.getFiles() ) {
							
							es.submit(new InstallationLibrairie(l, u.getPatchVersion()));
						}
					}
					
					es.shutdown();
					es.awaitTermination(600, TimeUnit.SECONDS);
					
					// Si annule on ne met rien à jour
					if( buttonStart.getText() != controller.getBundle().getString("JFrameUpdate.buttonStart.text") ) {
						
						progressBar.setValue(progressBar.getMaximum());
						//controller.getProperties().setSoftwareVersion(updateManager.getLastVersion());
						controller.getProperties().setSoftwareVersion(updateManager.getRealLastVersion());
						controller.getProperties().saveToFile();
						buttonStart.setText(controller.getBundle().getString("JFrameUpdate.lblFinish.text"));
						step = 2;
					}
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		}).start();
	}

	/**
	 * Event sur le comboOS
	 * @param e
	 */
	private void comboOsActionPerformed(ActionEvent e) {
		
		// Mise à jour des preférences
		controller.getProperties().setOperatingSystem(OS.valueOf(comboOs.getSelectedItem().toString()));
		//System.out.println(OS.valueOf(comboOs.getSelectedItem().toString()));;
		
		// On débloque certains boutons
		buttonChangelog.setEnabled(true);
		buttonDetails.setEnabled(true);
		buttonStart.setEnabled(true);

		step = 1;
		initData();
	}

	/**
	 * Event sur le comboLang
	 * 
	 * @param e
	 */
	private void comboLangActionPerformed(ActionEvent e) {
		
		controller.getProperties().setLanguage(((Locale)comboLang.getSelectedItem()).getLanguage());

		controller.getProperties().saveToFile();

		controller.setLocale();
		initComponentsI18n();
		initData();
	}

	/**
	 * Event sur la fermenture de l'�cran
	 * 
	 * @param e
	 */
	private void thisWindowClosing(WindowEvent e) {
		controller.getProperties().saveToFile();
		System.exit(0);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		panelGlobal = new JPanel();
		panelInformations = new JPanel();
		lblOs = new JLabel();
		comboOs = new JComboBox<>();
		lblLanguage = new JLabel();
		comboLang = new JComboBox<>();
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
		lblDownloading = new JLabel();
		lblEmptyFile = new JLabel();
		lblDownloadingFinished = new JLabel();
		lblNoUpdateAvailable = new JLabel();
		lblFinish = new JLabel();
		lblCancel = new JLabel();

		//======== this ========
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
				lblOs.setVisible(false);
				panelInformations.add(lblOs, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- comboOs ----
				comboOs.setVisible(false);
				comboOs.addActionListener(e -> comboOsActionPerformed(e));
				panelInformations.add(comboOs, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				panelInformations.add(lblLanguage, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- comboLang ----
				comboLang.addActionListener(e -> comboLangActionPerformed(e));
				panelInformations.add(comboLang, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				panelInformations.add(lblActuelVersionText, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- lblActualVersion ----
				lblActualVersion.setText(" ");
				panelInformations.add(lblActualVersion, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				panelInformations.add(lblNewVersionText, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- lblNewVersion ----
				lblNewVersion.setText(" ");
				panelInformations.add(lblNewVersion, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
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
				buttonChangelog.setEnabled(false);
				buttonChangelog.addActionListener(e -> buttonChangelogActionPerformed(e));
				panelButtons.add(buttonChangelog, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));

				//---- buttonDetails ----
				buttonDetails.setEnabled(false);
				buttonDetails.addActionListener(e -> buttonDetailsActionPerformed(e));
				panelButtons.add(buttonDetails, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));

				//---- buttonStart ----
				buttonStart.setEnabled(false);
				buttonStart.addActionListener(e -> buttonStartActionPerformed(e));
				panelButtons.add(buttonStart, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			panelGlobal.add(panelButtons, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(panelGlobal, BorderLayout.CENTER);

		initComponentsI18n();

		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private void initComponentsI18n() {
		// JFormDesigner - Component i18n initialization - DO NOT MODIFY  //GEN-BEGIN:initI18n
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		ResourceBundle bundle = controller.getBundle();
		setTitle(bundle.getString("JFrameUpdate.this.title"));
		lblOs.setText(bundle.getString("JFrameUpdate.lblOs.text"));
		lblLanguage.setText(bundle.getString("JFrameUpdate.lblLanguage.text"));
		lblActuelVersionText.setText(bundle.getString("JFrameUpdate.lblActuelVersionText.text"));
		lblNewVersionText.setText(bundle.getString("JFrameUpdate.lblNewVersionText.text"));
		lblDownloadFileCountText.setText(bundle.getString("JFrameUpdate.lblDownloadFileCountText.text"));
		panelDetails.setBorder(new TitledBorder(BorderFactory.createEmptyBorder(), bundle.getString("JFrameUpdate.panelDetails.border")));
		buttonChangelog.setText(bundle.getString("JFrameUpdate.buttonChangelog.text"));
		buttonDetails.setText(bundle.getString("JFrameUpdate.buttonDetails.text"));
		buttonStart.setText(bundle.getString("JFrameUpdate.buttonStart.text"));
		lblDownloading.setText(bundle.getString("JFrameUpdate.lblDownloading.text"));
		lblEmptyFile.setText(bundle.getString("JFrameUpdate.lblEmptyFile.text"));
		lblDownloadingFinished.setText(bundle.getString("JFrameUpdate.lblDownloadingFinished.text"));
		lblNoUpdateAvailable.setText(bundle.getString("JFrameUpdate.lblNoUpdateAvailable.text"));
		lblFinish.setText(bundle.getString("JFrameUpdate.lblFinish.text"));
		lblCancel.setText(bundle.getString("JFrameUpdate.lblCancel.text"));
		// JFormDesigner - End of component i18n initialization  //GEN-END:initI18n
	}

	public static void main(String[] args) throws Exception {

		DefaultController controller = new DefaultController();
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrameUpdate  tmp = new JFrameUpdate(controller);
		tmp.setVisible(true);
	}
	
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Mouloud rachid
	private JPanel panelGlobal;
	private JPanel panelInformations;
	private JLabel lblOs;
	private JComboBox<Object> comboOs;
	private JLabel lblLanguage;
	private JComboBox<Object> comboLang;
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
	private JLabel lblDownloading;
	private JLabel lblEmptyFile;
	private JLabel lblDownloadingFinished;
	private JLabel lblNoUpdateAvailable;
	private JLabel lblFinish;
	private JLabel lblCancel;
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
				
				ResourceBundle bundle = controller.getBundle();
				
				// Log du téléchargement
				textPaneDetails.replaceSelection(bundle.getString("JFrameUpdate.lblDownloading.text")+" : "+newLib.getLibName()+ " "+newLib.getLibVersion()+"\n");
				
				// Téléchargement de la version distante
				URL u = new URL(UpdateManager.updateChannel + version + "/" +newLib.getFilePath());
				URLConnection uc = u.openConnection();

				int fileLenght = uc.getContentLength();
				if (fileLenght == -1)
					throw new IOException(bundle.getString("JFrameUpdate.lblDownloadingEmptyFile.text")+". ("+newLib.getLibName()+")");

				
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
				File oldLib, newLibs;
				if( newLib.getLibName().equals("VideoMer") ) {
					oldLib = new File("./" + newLib.getFilePath());
					newLibs = oldLib;
				}
				else if( newLib.getLibName().contains("lang") && newLib.getFilePath().endsWith(".properties")) {
					oldLib = new File("./lang/" + newLib.getFilePath());
					newLibs = oldLib;
				}
				// MAJ 2.0.0
				else if( newLib.getLibName().equals("updater") ) {
					oldLib = new File("./TMP." + newLib.getFilePath());
					newLibs = oldLib;
				}
				else {
					// 2.0.0 | Correction pour enlever les anciennes librairies présentes
					newLibs = new File("./lib/"+ newLib.getFilePath());
					oldLib = newLibs;
					
					for( Library ll : controller.getProperties().getVersions() ) {
						if( ll.getLibName().equals(newLib.getLibName()) ) {
							oldLib = new File("./lib/" + ll.getFilePath());
						}
					}
				}

				if( oldLib.exists() )
					oldLib.delete();
				
				oldLib.getParentFile().mkdirs();
				
				File temp = new File("temp");
				temp.renameTo(newLibs);
				
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
				textPaneDetails.replaceSelection(bundle.getString("JFrameUpdate.lblDownloadingFinished.text")+"...\n");

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
}
