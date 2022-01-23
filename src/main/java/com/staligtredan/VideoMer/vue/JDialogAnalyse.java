package com.staligtredan.VideoMer.vue;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.plaf.basic.BasicStatusBarUI;

import com.staligtredan.VideoMer.components.ResultComboBox;
import com.staligtredan.VideoMer.components.ResultMutableTreeNode;
import com.staligtredan.VideoMer.components.ResultTableRowEditor;
import com.staligtredan.VideoMer.components.SubtitlesFileFilter;
import com.staligtredan.VideoMer.components.model.ResultComboBoxModel;
import com.staligtredan.VideoMer.components.model.ResultTreeTableModel;
import com.staligtredan.VideoMer.components.renderer.CellHeaderRenderer;
import com.staligtredan.VideoMer.controleur.DefaultController;
import com.staligtredan.VideoMer.handler.ResultTreeModelEvent;
import com.staligtredan.VideoMer.handler.ResultTreeModelListener;
import com.staligtredan.VideoMer.handler.transfert.FileTransfert;
import com.staligtredan.VideoMer.handler.transfert.FileTransfertHandler;
import com.staligtredan.VideoMer.internet.TMDB;
import com.staligtredan.VideoMer.internet.VideoDataCollector;
import com.staligtredan.VideoMer.modele.EntiteVideo;
import com.staligtredan.VideoMer.modele.Film;
import com.staligtredan.VideoMer.modele.Serie;
import com.staligtredan.VideoMer.util.FlexiComboBox;
import com.staligtredan.VideoMer.util.RelativePath;

/**
 * JDialog d'analyse des fichiers vidéos

 * @author Brendan
 * @version 0.16  18/04/2014
 * @version 0.19  10/02/2015 ajout de l'analyse des sous titres
 * @version 1.0.3 10/12/2016 MAJ pour les nouveaux posters
 * 							 MAJ TMDB et amélioration des recherches globalement
 * @version 2.0.0 22/12/2016 Loging
 * @version 2.0.1 01/01/2017 Correction du système qui empèche de rajouter des fichiers déjà presents dans la biblio
 * @version 2.0.3 24/02/2018 Correction/ajout des traductions 
 * 							 Ajout d'un popup qui indique qu'un fichier est vérouillé
 * 							 Ajout de barre d'avancement et de vitesse de copie + temps restant, pour les copies de fichiers
 * 							 Le bouton annuler fonctionne maintenant correctement, et permet de repartir
 */
public class JDialogAnalyse extends JDialog implements FileTransfert {

	private static final long serialVersionUID = 5388632121633463092L;
	
	final static Logger logger = LogManager.getLogger(JDialogAnalyse.class);

	private DefaultController controller;

	private ResultTreeTableModel xTreeTableModel;
	private ResultTableRowEditor rowEditor;
	private ResultComboBox resultComboBox;
	private int etape;
	private HashMap<String, FlexiComboBox> tvShowBuffer;
	private VideoDataCollector bddInternet;
	private ArrayList<EntiteVideo> videoList;

	private MouseAdapter ma;
	private ResultTreeModelListener rtl;
	
	private TraitementLong progression;
	private long fileLength=0;
	private long fileLengthTotal = 0;
	private long allLength=0;
	private long allLengthTotal = 0;
	



	public JDialogAnalyse( Frame owner, DefaultController controller ) {

		super(owner);
		this.controller = controller;

		initComponents();
		initView();
		initListeners();
		initData();
	}



	private void initView() {

		// Centrage de l'écran
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		int width = 800, heigh = 500;
		setBounds(p.x - width / 2, p.y - heigh / 2, width, heigh);

		xTreeTableModel = new ResultTreeTableModel(controller.getPreferences().getVideoExtensions());
		xTreeTableResultats.setTreeTableModel(xTreeTableModel);
		resultComboBox = new ResultComboBox(controller);
		xTreeTableResultats.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(resultComboBox));
		
		// Renderer pour afficher le titre de la colonne 1
		xTreeTableResultats.getColumnModel().getColumn(1).setHeaderRenderer(new CellHeaderRenderer());
		
		// Taille des colonnes
		xTreeTableResultats.getColumnModel().getColumn(1).setMaxWidth(30);
		xTreeTableResultats.getColumnModel().getColumn(2).setMaxWidth(80);
		xTreeTableResultats.getColumnModel().getColumn(3).setMaxWidth(80);

		rowEditor = new ResultTableRowEditor(xTreeTableResultats);
		xTreeTableResultats.getColumnModel().getColumn(4).setCellEditor(rowEditor);
		xTreeTableResultats.setHighlighters(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null,
				xTreeTableResultats.getSelectionBackground()), HighlighterFactory.createAlternateStriping());

		xStatusBar.putClientProperty(BasicStatusBarUI.AUTO_ADD_SEPARATOR, false);

		// Le check ranger fichier n'est activé que si les options sont définies
		if ( controller.getPreferences().getMoviePath().isEmpty()
				&& controller.getPreferences().getTvShowPath().isEmpty() )
			checkBoxAutoStore.setEnabled(false);
	}



	private void initListeners() {

		// Mouse listener pour gérer les cochage/décochage de masse
		ma = new MouseAdapter() {

			public void mouseClicked( MouseEvent e ) {

				int index = xTreeTableResultats.getTableHeader().getColumnModel().getColumnIndexAtX(e.getX());

				if ( index != 1 )
					return;

				if ( xTreeTableResultats.getColumnModel().getColumn(1).getHeaderValue().equals("true") ) {
					xTreeTableResultats.getColumnModel().getColumn(1).setHeaderValue("false");
					xTreeTableModel.setAllActif(false);
				}
				else {
					xTreeTableResultats.getColumnModel().getColumn(1).setHeaderValue("true");
					xTreeTableModel.setAllActif(true);
				}
			}
		};
		xTreeTableResultats.getTableHeader().addMouseListener(ma);
		xTreeTableResultats.setTransferHandler(new FileTransfertHandler(this));

		// listener pour gérer la modification de nom
		rtl = new ResultTreeModelListener() {

			@Override
			public void treeNodesChanged( ResultTreeModelEvent e ) {

				// On réactive le fichier renommé
				xTreeTableModel.setValueAt(true, e.getNode(), 1);

				// Si la recherche de titres à déjà été effectuée on recherche ce dernier
				if ( etape == 2 )
					searchVideoName(e.getNode());

				xTreeTableResultats.updateUI();
			}
		};
		xTreeTableModel.addResultTreeModelListener(rtl);

		// Touche escape pour quitter l'écran
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		ActionListener actionListener = new ActionListener() {

			public void actionPerformed( ActionEvent actionEvent ) {

				dispose();
			}
		};
		getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
	}



	private void initData() {

		tvShowBuffer = new HashMap<String, FlexiComboBox>();
		videoList = new ArrayList<EntiteVideo>();
		bddInternet = new TMDB(controller.getPreferences().getLang());
		etape = 1;
	}



	/**
	 * Ajoute des fichiers dans l'analyseur
	 * 
	 * @param files
	 */
	public void addFiles( List<File> files ) {

		xTreeTableModel.addFiles(files);

		xTreeTableModel.updateTree();
		xTreeTableResultats.updateUI();
		updateTable();
		xTreeTableResultats.updateUI();
		xTreeTableResultats.expandAll();
	}



	/**
	 * Lance l'analyse du nom des fichiers pour en extraire des informations
	 * nom, CD1 CD2.., série ou film, S01E01...
	 * 
	 * Tout ceçi est fait dans un thread à part
	 */
	private void updateTable() {

		TraitementLong progression = new TraitementLong(controller.getBundle().getString(
				"JDialogAnalyse.strParseFiles.text"), "parseFiles", 10);
		progression.lanceLongTraitement();
	}



	/**
	 * Etape 1 :
	 * 
	 * Analyser le noms des fichiers vidéos pour nettoyer le superflu et décortiquer les données pour les séries
	 * 
	 */
	public void parseFiles( ResultMutableTreeNode node ) {

		File nom = new File(node.getFilePath());
		
		// Si dossier ou noeud déjà analysé
		if ( nom.isDirectory() && node.isActif() )
			return;

		String file = "";
		int i = nom.getName().lastIndexOf(".");

		// Si le fichier a une extension
		if ( i != -1 ) {
			file = nom.getName().substring(0, i).toLowerCase();
			node.setActif(true);
		}
		else
			return;
		
		// 1.0.3 | C'est ici qu'on évite les fichiers déjà existants de la biblio
		if( controller.getBibliotheque().getFiles().contains(nom.getName()) ){
			System.out.println(nom.getName());
			node.setActif(false);
			node.removeFromParent();
			
			return;
		}

		// Etape 1 : supprimer le superflu [-_(){}@\\.]
		for ( String s : controller.getPreferences().getRegexFileCleanBlank() )
			file = file.replaceAll(s, "");
		for ( String s : controller.getPreferences().getRegexFileCleanSpace() ) {
			file = file.replaceAll(s, " ");
		}

		// On vire tous les espaces en doublons
		file = file.replaceAll("\\s+", " ");
		
		// Etape 2 :
		// si regex = sére => type = série
		for ( String regex : controller.getPreferences().getRegexTvShow() ) {
			//System.out.println(regex + "    "+file);
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(file);

			if ( m.find() ) {
				node.setType(resultComboBox.getTvShow());
				node.setCode("S" + m.group(2) + "E" + m.group(3));
				node.setUserObject(m.group(1));
				
				return;
			}
		}

		// si regex = film => type = film
		for ( String regex : controller.getPreferences().getRegexMovie() ) {

			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(file);

			if ( m.find() ) {
				node.setType(resultComboBox.getMovie());

				if ( m.groupCount() == 2 )
					node.setCode("CD" + m.group(2));
				node.setUserObject(m.group(1));
				
				return;
			}
		}

		node.setActif(false);
	}



	/**
	 * Etape 2 :
	 * 
	 * Recherche un elt vidéo sur Internet pour récupérer une liste de vidéos qui corresponds potentiellement
	 * 
	 * @param node
	 *            Elt à rechercher
	 */
	public void searchVideoName( ResultMutableTreeNode node ) {

		if ( !node.isActif() )
			return;

		String nom = node.getUserObject().toString();
		ArrayList<String[]> resultat;

		// Si on recherche un Film
		if ( node.getType().equals(resultComboBox.getMovie()) ) {

			resultat = bddInternet.search(nom, VideoDataCollector.MOVIE);

			// Si résultat -> on renseigne la combobox
			if ( resultat != null && resultat.size() > 0 ) {

				ArrayList<ResultComboBoxModel> listeChoix = new ArrayList<ResultComboBoxModel>();

				for ( String[] tab : resultat ) {
					ResultComboBoxModel choix = new ResultComboBoxModel();
					choix.setLibelle(tab[0]);
					choix.setUrl(tab[1]);
					listeChoix.add(choix);
				}

				FlexiComboBox comboChoix = new FlexiComboBox(listeChoix.toArray());
				comboChoix.setShowTips(true);

				rowEditor.setEditorAt(node, new DefaultCellEditor(comboChoix));
				xTreeTableModel.setValueAt(comboChoix.getSelectedItem(), node, 4);
				// tvShowBuffer.put(nom, comboChoix);
			}
			// Aucun résultat
			else {
				xTreeTableModel.setValueAt(false, node, 1);
				xTreeTableModel.setValueAt(new ResultComboBoxModel(), node, 4);
				rowEditor.setEditorAt(node, new DefaultCellEditor(new JTextField()));
			}

		}
		// Si on recherche une série
		else if ( node.getType().equals(resultComboBox.getTvShow()) ) {

			synchronized ( tvShowBuffer ) {

				if ( tvShowBuffer.containsKey(nom) ) {

					rowEditor.setEditorAt(node, new DefaultCellEditor(tvShowBuffer.get(nom)));
					xTreeTableModel.setValueAt(tvShowBuffer.get(nom).getSelectedItem(), node, 4);
				}
				else {

					resultat = bddInternet.search(nom, VideoDataCollector.TV_SHOW);

					// Si résultat -> on renseigne la combobox
					if ( resultat != null && resultat.size() > 0 ) {

						ArrayList<ResultComboBoxModel> listeChoix = new ArrayList<ResultComboBoxModel>();

						for ( String[] tab : resultat ) {
							ResultComboBoxModel choix = new ResultComboBoxModel();
							choix.setLibelle(tab[0]);
							choix.setUrl(tab[1]);
							listeChoix.add(choix);
						}

						FlexiComboBox comboChoix = new FlexiComboBox(listeChoix.toArray());
						comboChoix.setShowTips(true);

						rowEditor.setEditorAt(node, new DefaultCellEditor(comboChoix));
						xTreeTableModel.setValueAt(comboChoix.getSelectedItem(), node, 4);
						tvShowBuffer.put(nom, comboChoix);
					}
					// Aucun résultat
					else {
						xTreeTableModel.setValueAt(false, node, 1);
						xTreeTableModel.setValueAt(new ResultComboBoxModel(), node, 4);
						rowEditor.setEditorAt(node, new DefaultCellEditor(new JTextField()));
					}
				}
			}

		}
	}



	/**
	 * Télécharge une image miniature et la stocke sur le DD
	 * 
	 * @param url
	 * @param fileName
	 * @param filePath
	 * @since 1.0.3 10/12/2016
	 */
	private void downloadImg(String url, String fileName, String filePath) {

		File f = new File(filePath);

		if (f.exists())
			return;

		// Sauvegarde de l'image sur le disque dur
		try {
			BufferedImage img = ImageIO.read(new URL(url));

			// resize
			int w = img.getWidth();
			int h = img.getHeight();
			int newH = 300;
			double newWi = (w * 300.0) / h;
			int newW = (int) newWi;

			BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
			Graphics2D g = dimg.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
			g.dispose();

			ImageIO.write(dimg, url.substring(url.lastIndexOf(".") + 1), f);

		} catch (IOException e) {
			logger.error("Impossible de lire l'URL : "+url+" OU Impossible d'écrire le fichier : "+f.getPath());
		}
	}
	
	/**
	 * Etape 3 :
	 * 
	 * Recherche les informations détaillée de chaque elt en fonction du choix de l'utilisateur
	 * 
	 * Non multithreadable à cause d'une mauvaise conception de la recherche internet qui stocke des données
	 * 
	 * @param node
	 *            Elt à rechercher
	 */
	public void parseVideoData( ResultMutableTreeNode node ) {

		if ( !node.isActif() )
			return;

		if ( node.getType().equals(resultComboBox.getMovie()) ) {

			Film f = (Film) bddInternet.parseData(node.getChoix().getUrl(), VideoDataCollector.MOVIE, "", "");

			// Recherche des infos détaillées du Media
			if ( f != null ) {

				// Chemin relatif ou non
				String path = node.getFilePath();
				if ( controller.getPreferences().isRelatif() && !checkBoxAutoStore.isSelected()
						&& !checkBoxFileRename.isSelected() )
					path = RelativePath.getRelativePath(new File("."), new File(node.getFilePath()));

				f.setEmplacement(path);

				// Est-ce qu'il y a plusieurs CD du même film
				if ( !node.getCode().isEmpty() )
					f.setNumSupport(Integer.decode(node.getCode().substring(2)));

				videoList.add(f);
				
				if( (f.getAffichette() != null) && !f.getAffichette().isEmpty() )
					downloadImg(f.getAffichette(), f.getAffichette().substring(f.getAffichette().lastIndexOf("/")), "posters/"+f.getAffichette().substring(f.getAffichette().lastIndexOf("/")));
			}
		}
		else if ( node.getType().equals(resultComboBox.getTvShow()) ) {

			String season = "", episode = "";
			Pattern p = Pattern.compile("s([0-9]{1,2})e([0-9]{1,2})");
			Matcher m = p.matcher(node.getCode().toLowerCase());
			m.matches();
			season = m.group(1);
			episode = m.group(2);

			Serie s = (Serie) bddInternet.parseData(node.getChoix().getUrl(), VideoDataCollector.TV_SHOW, season,
					episode);

			// Recherche des infos détaillées du Media
			if ( s != null ) {

				// Chemin relatif ou non
				String path = node.getFilePath();
				if ( controller.getPreferences().isRelatif() && !checkBoxAutoStore.isSelected()
						&& !checkBoxFileRename.isSelected() )
					path = RelativePath.getRelativePath(new File("."), new File(node.getFilePath()));

				s.setEmplacement(path);
				videoList.add(s);

				if( (s.getAffichette() != null) && !s.getAffichette().isEmpty() )
					downloadImg(s.getAffichette(), s.getAffichette().substring(s.getAffichette().lastIndexOf("/")), "posters/"+s.getAffichette().substring(s.getAffichette().lastIndexOf("/")));
			}
		}
	}



	/**
	 * Met à jour le nom du fichier et/ou le déplace dans la bibliothèque mutlimédia Dépends des préférences utilisateur
	 * 
	 * @param ev
	 *            Fichier à renommer/déplacer
	 * @return
	 */
	public void fileUpdating( EntiteVideo ev ) {

		File f = new File(ev.getEmplacement());
		String filePath = f.getPath();
		String ext = f.getName().substring(f.getName().lastIndexOf(".")).toLowerCase();

		// 2.0.3 : Vérification avant le rename/copie de fichier
		// Il faut avoir les droits en écriture, lancerun popup pour laisser le temps à l'utilisateur
		if( checkBoxFileRename.isSelected() || checkBoxAutoStore.isSelected() ) {
			
			if( ! f.renameTo(f) ) {

				JOptionPane.showMessageDialog(this,
						controller.getBundle().getString("JDialogAnalyse.strFileLocked1.text") + " :\n" + f.getName()
							+ "\n" + controller.getBundle().getString("JDialogAnalyse.strFileLocked2.text"),
						controller.getBundle().getString("JDialogAnalyse.strFileLockedTitle.text"),
						JOptionPane.WARNING_MESSAGE);
			}
		}
		
		if ( ev.getClass().equals(Serie.class) ) {

			Serie s = (Serie) ev;

			Formatter fSaison = new Formatter(new StringBuilder());
			Formatter fSaisonL = new Formatter(new StringBuilder());
			Formatter fEpisode = new Formatter(new StringBuilder());
			fSaisonL.format("%d", s.getNoSaison());
			fSaison.format("%02d", s.getNoSaison());
			fEpisode.format("%02d", s.getNoEpisode());

			// On est obligé de vérifier qu'aucun charactère interdit n'est présent dans le nom du fichier
			String regex = "[|:?<>/\\*]";
			String nomSerie = s.getNom(), nomEpisode = s.getNomEpisode();
			nomSerie = nomSerie.replaceAll(regex, "");
			nomEpisode = nomEpisode.replaceAll(regex, "");

			/**
			 * Zone renaming
			 */
			if ( checkBoxFileRename.isSelected() ) {

				filePath = f.getParentFile() + File.separator + nomSerie + " S" + fSaison + "E" + fEpisode + " - "
						+ nomEpisode;

				File file = new File(filePath + ext);

				// Gestion des sous titres
				String[] subsExt = {"srt"};
				String nameWithoutExt = f.getName().substring(0, f.getName().lastIndexOf("."));
				SubtitlesFileFilter sff = new SubtitlesFileFilter(nameWithoutExt, subsExt);	
				
				for( File ff :  f.getParentFile().listFiles(sff) ) {

					String fin = ff.getName().split(nameWithoutExt)[1];
					File dest = new File(filePath + fin);
					ff.renameTo(dest);
				}				
				
				f.renameTo(file);
				f = file;
			}

			/**
			 * Zone déplacement
			 */

			if ( checkBoxAutoStore.isSelected() ) {

				// TODO A mettre dans les options, Options pour le formattage du nom (voir exemple dessous)
				// Dossier : ${nom} - Saison ${noSaison}
				// Fichier : ${nom} S${noSaison,2}E${noEpisode,2} - ${nom}
				final String tvShowFolder = nomSerie;
				final String tvShowSeasonFolder = nomSerie + " - Season " + fSaisonL;

				String destFolder = controller.getPreferences().getTvShowPath() + File.separator + tvShowFolder
						+ File.separator + tvShowSeasonFolder;
				new File(destFolder).mkdirs();

				File destFile = new File(destFolder + File.separator + f.getName());

				// Gestion des sous titres
				String[] subsExt = {"srt"};
				String nameWithoutExt = f.getName().substring(0, f.getName().lastIndexOf("."));
				SubtitlesFileFilter sff = new SubtitlesFileFilter(nameWithoutExt, subsExt);	
				
				String destFold = destFolder + File.separator + "subs";
				new File(destFold).mkdirs();
				
				for( File ff :  f.getParentFile().listFiles(sff) ) {

					File dest = new File(destFold + File.separator + ff.getName());
					
					ff.renameTo(dest);
				}
				
				//f.renameTo(destFile);
				// 2.0.3
				copyFileStream(f, destFile);
				
				// Si fichier copié on supprime l'original
				if( f.length() == destFile.length() ) {
					f.delete();
				}
				
				f = destFile;
				filePath = f.getPath();

			}

			fSaisonL.close();
			fSaison.close();
			fEpisode.close();

		}
		else if ( ev.getClass().equals(Film.class) ) {

			Film movie = (Film) ev;

			// On est obligé de vérifier qu'aucun charactère interdit n'est
			// présent dans le nom du fichier
			String regex = "[|:?<>/\\*]";
			String nom = movie.getNom();
			nom = nom.replaceAll(regex, "");

			/**
			 * Zone renaming
			 */
			if ( checkBoxFileRename.isSelected() ) {

				// Fichier vidéo
				if ( movie.getNumSupport() == 0 )
					filePath = f.getParentFile() + File.separator + nom;
				else
					filePath = f.getParentFile() + File.separator + nom + " CD" + movie.getNumSupport();

				File file = new File(filePath + ext);
				
				// Gestion des sous titres
				String[] subsExt = {"srt"};
				String nameWithoutExt = f.getName().substring(0, f.getName().lastIndexOf("."));
				SubtitlesFileFilter sff = new SubtitlesFileFilter(nameWithoutExt, subsExt);
				
				for( File ff :  f.getParentFile().listFiles(sff) ) {

					String fin = ff.getName().split(nameWithoutExt)[1];
					File dest = new File(filePath + fin);
					ff.renameTo(dest);
				}				

				f.renameTo(file);
				f = file;
				filePath = f.getPath();
			}

			/**
			 * Zone déplacement
			 */
			if ( checkBoxAutoStore.isSelected() ) {

				String destFolder = controller.getPreferences().getMoviePath();
				new File(destFolder).mkdirs();

				File destFile = new File(destFolder + File.separator + f.getName());

				// Gestion des sous titres
				// TODO: Ici pour ajouter des extensions de sous titres autorisées
				String[] subsExt = {"srt"};
				String nameWithoutExt = f.getName().substring(0, f.getName().lastIndexOf("."));
				SubtitlesFileFilter sff = new SubtitlesFileFilter(nameWithoutExt, subsExt);	
				
				String destFold = destFolder + File.separator + "subs";
				new File(destFold).mkdirs();
				
				for( File ff :  f.getParentFile().listFiles(sff) ) {

					File dest = new File(destFold + File.separator + ff.getName());
					
					ff.renameTo(dest);
				}
				
				//f.renameTo(destFile);
				// test 2.0.3
				copyFileStream(f, destFile);
				
				// Si fichier copié on supprime l'original
				if( f.length() == destFile.length() ) {
					f.delete();
				}
				
				f = destFile;
				filePath = f.getPath();
			}
		}

		// Chemin relatif ou non
		if ( controller.getPreferences().isRelatif() )
			filePath = RelativePath.getRelativePath(new File("."), f);

		ev.setEmplacement(filePath);
	}


	private void copyFileStream(File source, File dest) {
		
		InputStream in = null;
		OutputStream out = null;
		lblFileName.setText(source.getName());
		fileLengthTotal = source.length();
		fileLength = 0;
		try {

			in = new FileInputStream(source);
			out = new FileOutputStream(dest);

			byte[] buffer = new byte[32 * 1024];

			while ( true ) {
				int bytesRead = in.read(buffer);
				if( bytesRead <= 0 ) {
					break;
				}
				
				// 2.0.3
				if( progression.isStoped()  ) {
					break;
				}
					
				out.write(buffer, 0, bytesRead);
				fileLength += 32 * 1024;
				allLength += 32*1024;
			}

		} catch ( IOException e ) {
			e.printStackTrace();
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
			// 2.0.3, if canceled the distant file is deleted
			if( progression.isStoped()  ) {
				dest.delete();
			}
		}
	}
	

	/**
	 * 
	 * 
	 * @param e
	 */

	private void xTreeTableResultatsKeyTyped( KeyEvent e ) {

		if ( e.getKeyChar() == KeyEvent.VK_DELETE ) {

			String path = xTreeTableResultats.getTreeSelectionModel().getSelectionPath().getLastPathComponent()
					.toString();
			if ( xTreeTableModel.delFile(new File(path)) )
				xTreeTableModel.updateTree();

			// xTreeTableResultats.updateUI();
		}
	}



	/**
	 * Action bouton d'ajout d'éléments vidéo
	 * 
	 * @param e
	 */
	private void buttonBrowseActionPerformed( ActionEvent e ) {

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setMultiSelectionEnabled(true);

		int returnVal = fc.showOpenDialog(this);
		if ( returnVal == JFileChooser.APPROVE_OPTION ) {

			for ( File f : fc.getSelectedFiles() ) {
				xTreeTableModel.addFile(f);
			}

			xTreeTableModel.updateTree();
			updateTable();
			xTreeTableResultats.updateUI();
			xTreeTableResultats.expandAll();
		}
	}



	/**
	 * Action bouton qui gère les différentes étapes de la recherche
	 * 
	 * @param e
	 */
	private void buttonStepActionPerformed( ActionEvent e ) {

		

		switch ( etape ) {

		// Recherche des titres
		case 1:

			etape = 2;

			progression = new TraitementLong(
					controller.getBundle().getString("JDialogAnalyse.strSearchVideoName.text"), "searchVideoName", 2);
			progression.lanceLongTraitement();
			break;

		// Recherche des infos détaillées
		case 2:

			etape = 3;

			progression = new TraitementLong(controller.getBundle().getString("JDialogAnalyse.strParseVideoData.text"),
					"parseVideoData", 2);
			progression.lanceLongTraitement();
			break;

		// Mise à jour des fichiers
		case 3:

			etape = 4;
			progression = new TraitementLong(controller.getBundle().getString("JDialogAnalyse.strFileUpdating.text"),
					"fileUpdating", 1);
			progression.lanceLongTraitement();
			break;
		}
	}



	/**
	 * Renvoie la liste d'éléments recherchés
	 * 
	 * @return null si rien
	 */
	public ArrayList<EntiteVideo> getVideoList() {

		return videoList;
	}



	private void buttonOkActionPerformed( ActionEvent e ) {

		thisWindowClosing(null);
	}



	private void buttonCancelActionPerformed( ActionEvent e ) {

		if( progression != null ) 
			progression.interrupt();
		//videoList = null;
		//thisWindowClosing(null);
	}



	private void thisWindowClosing( WindowEvent e ) {

		controller = null;
		xTreeTableModel = null;
		rowEditor = null;
		resultComboBox = null;
		tvShowBuffer = null;
		bddInternet = null;
		ma = null;
		rtl = null;

		dispose();
	}



	@Override
	public void importData( List<File> l ) {

		addFiles(l);
	}



	private void initComponents() {

		// JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Dada Jajda
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		panelParametres = new JPanel();
		buttonBrowse = new JButton();
		checkBoxFileRename = new JCheckBox();
		checkBoxAutoStore = new JCheckBox();
		buttonStep = new JButton();
		scrollPane = new JScrollPane();
		xTreeTableResultats = new JXTreeTable();
		buttonBar = new JPanel();
		buttonOk = new JButton();
		buttonCancel = new JButton();
		xStatusBar = new JXStatusBar();
		lblFileName = new JLabel();
		lblFileSpeed = new JLabel();
		lblFileTimeLeft = new JLabel();
		progressBar2 = new JProgressBar();
		lblInformations = new JLabel();
		lblTotalTimeLeft = new JLabel();
		progressBar = new JProgressBar();
		lblProgress = new JLabel();
		strParseFiles = new JLabel();
		strSearchVideoName = new JLabel();
		strParseVideoData = new JLabel();
		strFileUpdating = new JLabel();
		strButtonStep2 = new JLabel();
		strButtonStep3 = new JLabel();
		strButtonStep4 = new JLabel();
		strButtonStep4bis = new JLabel();
		strButtonStep5 = new JLabel();
		strInfoParseFilesEnded = new JLabel();
		strInfoSearchVideoNameEnded = new JLabel();
		strInfoParseVideoDataEnded = new JLabel();
		strInfoEnd = new JLabel();
		strError = new JLabel();
		strFileUpdateEnd = new JLabel();
		strFileLocked1 = new JLabel();
		strFileLocked2 = new JLabel();
		strFileLockedTitle = new JLabel();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
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
			dialogPane.setBorder(new EmptyBorder(7, 7, 7, 7));
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new GridBagLayout());
				((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0};
				((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
				((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0, 1.0E-4};

				//======== panelParametres ========
				{
					panelParametres.setLayout(new GridBagLayout());
					((GridBagLayout)panelParametres.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
					((GridBagLayout)panelParametres.getLayout()).rowHeights = new int[] {0, 0};
					((GridBagLayout)panelParametres.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0, 1.0E-4};
					((GridBagLayout)panelParametres.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

					//---- buttonBrowse ----
					buttonBrowse.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/add.png")));
					buttonBrowse.addActionListener(e -> buttonBrowseActionPerformed(e));
					panelParametres.add(buttonBrowse, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 5), 0, 0));
					panelParametres.add(checkBoxFileRename, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 5), 0, 0));
					panelParametres.add(checkBoxAutoStore, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
				}
				contentPanel.add(panelParametres, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

				//---- buttonStep ----
				buttonStep.setEnabled(false);
				buttonStep.addActionListener(e -> buttonStepActionPerformed(e));
				contentPanel.add(buttonStep, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

				//======== scrollPane ========
				{

					//---- xTreeTableResultats ----
					xTreeTableResultats.setAutoCreateRowSorter(true);
					xTreeTableResultats.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
							xTreeTableResultatsKeyTyped(e);
						}
					});
					scrollPane.setViewportView(xTreeTableResultats);
				}
				contentPanel.add(scrollPane, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
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

				//---- buttonOk ----
				buttonOk.setEnabled(false);
				buttonOk.addActionListener(e -> buttonOkActionPerformed(e));
				buttonBar.add(buttonOk, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));

				//---- buttonCancel ----
				buttonCancel.addActionListener(e -> buttonCancelActionPerformed(e));
				buttonBar.add(buttonCancel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.PAGE_END);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);

		//======== xStatusBar ========
		{
			xStatusBar.setLayout(new GridBagLayout());
			((GridBagLayout)xStatusBar.getLayout()).columnWidths = new int[] {0, 65, 65, 0, 0, 0};
			((GridBagLayout)xStatusBar.getLayout()).rowHeights = new int[] {0, 0, 0};
			((GridBagLayout)xStatusBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
			((GridBagLayout)xStatusBar.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

			//---- lblFileName ----
			lblFileName.setVisible(false);
			xStatusBar.add(lblFileName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

			//---- lblFileSpeed ----
			lblFileSpeed.setVisible(false);
			xStatusBar.add(lblFileSpeed, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

			//---- lblFileTimeLeft ----
			lblFileTimeLeft.setVisible(false);
			xStatusBar.add(lblFileTimeLeft, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

			//---- progressBar2 ----
			progressBar2.setStringPainted(true);
			progressBar2.setVisible(false);
			xStatusBar.add(progressBar2, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));
			xStatusBar.add(lblInformations, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));

			//---- lblTotalTimeLeft ----
			lblTotalTimeLeft.setVisible(false);
			xStatusBar.add(lblTotalTimeLeft, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));

			//---- progressBar ----
			progressBar.setStringPainted(true);
			xStatusBar.add(progressBar, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));
			xStatusBar.add(lblProgress, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(xStatusBar, BorderLayout.SOUTH);

		initComponentsI18n();

		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization //GEN-END:initComponents
	}



	private void initComponentsI18n() {

		// JFormDesigner - Component i18n initialization - DO NOT MODIFY //GEN-BEGIN:initI18n
		// Generated using JFormDesigner Evaluation license - Dada Jajda
		ResourceBundle bundle = controller.getBundle();
		setTitle(bundle.getString("JDialogAnalyse.this.title"));
		panelParametres.setBorder(new TitledBorder(bundle.getString("JDialogAnalyse.panelParametres.border")));
		buttonBrowse.setText(bundle.getString("JDialogAnalyse.buttonBrowse.text"));
		buttonBrowse.setToolTipText(bundle.getString("JDialogAnalyse.buttonBrowse.toolTipText"));
		checkBoxFileRename.setText(bundle.getString("JDialogAnalyse.checkBoxFileRename.text"));
		checkBoxFileRename.setToolTipText(bundle.getString("JDialogAnalyse.checkBoxFileRename.toolTipText"));
		checkBoxAutoStore.setText(bundle.getString("JDialogAnalyse.checkBoxAutoStore.text"));
		checkBoxAutoStore.setToolTipText(bundle.getString("JDialogAnalyse.checkBoxAutoStore.toolTipText"));
		buttonStep.setText(bundle.getString("JDialogAnalyse.buttonStep.text"));
		buttonOk.setText(bundle.getString("JDialogAnalyse.buttonOk.text"));
		buttonCancel.setText(bundle.getString("JDialogAnalyse.buttonCancel.text"));
		strParseFiles.setText(bundle.getString("JDialogAnalyse.strParseFiles.text"));
		strSearchVideoName.setText(bundle.getString("JDialogAnalyse.strSearchVideoName.text"));
		strParseVideoData.setText(bundle.getString("JDialogAnalyse.strParseVideoData.text"));
		strFileUpdating.setText(bundle.getString("JDialogAnalyse.strFileUpdating.text"));
		strButtonStep2.setText(bundle.getString("JDialogAnalyse.strButtonStep2.text"));
		strButtonStep3.setText(bundle.getString("JDialogAnalyse.strButtonStep3.text"));
		strButtonStep4.setText(bundle.getString("JDialogAnalyse.strButtonStep4.text"));
		strButtonStep4bis.setText(bundle.getString("JDialogAnalyse.strButtonStep4bis.text"));
		strButtonStep5.setText(bundle.getString("JDialogAnalyse.strButtonStep5.text"));
		strInfoParseFilesEnded.setText(bundle.getString("JDialogAnalyse.strInfoParseFilesEnded.text"));
		strInfoSearchVideoNameEnded.setText(bundle.getString("JDialogAnalyse.strInfoSearchVideoNameEnded.text"));
		strInfoParseVideoDataEnded.setText(bundle.getString("JDialogAnalyse.strInfoParseVideoDataEnded.text"));
		strInfoEnd.setText(bundle.getString("JDialogAnalyse.strInfoEnd.text"));
		strError.setText(bundle.getString("JDialogAnalyse.strError.text"));
		strFileUpdateEnd.setText(bundle.getString("JDialogAnalyse.strFileUpdateEnd.text"));
		strFileLocked1.setText(bundle.getString("JDialogAnalyse.strFileLocked1.text"));
		strFileLocked2.setText(bundle.getString("JDialogAnalyse.strFileLocked2.text"));
		strFileLockedTitle.setText(bundle.getString("JDialogAnalyse.strFileLockedTitle.text"));
		// JFormDesigner - End of component i18n initialization //GEN-END:initI18n
	}



	public static void main( String[] args ) throws Exception {

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JDialogAnalyse tmp = new JDialogAnalyse((JFrame) null, new DefaultController());
		tmp.setVisible(true);
	}

	/**
	 * Classe de taitement long avec un nouveau thread Pilote plusieurs threads
	 * 
	 * @author Brendan Jaouen
	 * @version 0.1 22/09/2010
	 */
	private class TraitementLong implements Runnable {

		/**
		 * Nom de la méthode longue à traiter
		 */
		private String nomMethode;

		/**
		 * N° de l'opération actuelle
		 */
		private int value = 0;

		/**
		 * Classe qui gàre l'execution des Threads
		 */
		private ExecutorService es;
		
		private boolean stoped = false;



		/**
		 * Crée une instance de <code>TraitementLong</code>
		 * 
		 * @param message
		 * @param nomMethode
		 * @param nbIterations
		 */
		public TraitementLong( String message, String nomMethode, int nbThread ) {

			es = Executors.newFixedThreadPool(nbThread);
			lblInformations.setText(message);
			this.nomMethode = nomMethode;
			progressBar.setValue(0);
			value = 0;
			stoped = false;
			progressBar.setMaximum(xTreeTableModel.getActiveRowCount());
		}



		/**
		 * cree un nouveau Thread et appelle la methode start Cf une documentation sur les Thread si vous ne comprennez pas le
		 * mecanisme ....
		 */
		private void lanceLongTraitement() {

			Thread t = new Thread(this);
			t.start();
			// maintenant nous rendons la main au processus d'evenement
		}
		
		
		public void interrupt() {
			es.shutdownNow();
			stoped = true;
		}
		
		
		public boolean isStoped() {
			return stoped;
		}


		@Override
		public void run() {

			ResultMutableTreeNode root = (ResultMutableTreeNode) xTreeTableModel.getRoot();

			// Gestion de la fin de traitement
			switch ( etape ) {

			case 1:

				buttonStep.setEnabled(false);

				listChildren(root);
				es.shutdown();
				try {
					es.awaitTermination(10, TimeUnit.MINUTES);
					// If not stoped next step, if stoped stay at same step
					if( !stoped ) {
						
						buttonStep.setText(controller.getBundle().getString("JDialogAnalyse.strButtonStep2.text"));
						buttonStep.setEnabled(true);
						xTreeTableResultats.updateUI();
						lblInformations.setText(
								controller.getBundle().getString("JDialogAnalyse.strInfoParseFilesEnded.text"));
					} else {
						buttonStep.setEnabled(true);
					}
					
				}
				catch ( InterruptedException e ) {
					buttonStep.setText(controller.getBundle().getString("JDialogAnalyse.strError.text"));
					logger.error("Erreur timeout lors de l'analyse étape 1 ", e);
				}

				break;

			case 2:
				buttonStep.setEnabled(false);
				buttonBrowse.setEnabled(false);

				lblProgress.setText("0/" + progressBar.getMaximum());
				listChildren(root);
				es.shutdown();
				try {
					es.awaitTermination(30, TimeUnit.MINUTES);
					
					// If not stoped next step, if stoped stay at same step
					if( !stoped ) {
						buttonStep.setText(controller.getBundle().getString("JDialogAnalyse.strButtonStep3.text"));
						buttonStep.setEnabled(true);
						lblInformations.setText(controller.getBundle().getString(
								"JDialogAnalyse.strInfoSearchVideoNameEnded.text"));
					}else {
						buttonStep.setEnabled(true);
						etape = 1;
					}
					
				}
				catch ( InterruptedException e ) {
					buttonStep.setText(controller.getBundle().getString("JDialogAnalyse.strError.text"));
					logger.error("Erreur timeout lors de l'analyse étape 2 ", e);
				}

				break;

			case 3:

				buttonStep.setEnabled(false);
				checkBoxFileRename.setEnabled(false);
				checkBoxAutoStore.setEnabled(false);

				lblProgress.setText("0/" + progressBar.getMaximum());
				listChildren(root);
				es.shutdown();
				try {
					es.awaitTermination(30, TimeUnit.MINUTES);
					
					// If not stoped next step, if stoped stay at same step
					if( !stoped ) {
						if ( checkBoxFileRename.isSelected() || checkBoxAutoStore.isSelected() ) {
							buttonStep.setText(controller.getBundle().getString("JDialogAnalyse.strButtonStep4.text"));
							buttonStep.setEnabled(true);
							lblInformations.setText(controller.getBundle().getString(
									"JDialogAnalyse.strInfoParseVideoDataEnded.text"));
						}
						else {
							buttonStep.setText(controller.getBundle().getString("JDialogAnalyse.strButtonStep4bis.text"));
							buttonStep.setEnabled(false);
							buttonOk.setEnabled(true);
							lblInformations.setText(controller.getBundle().getString("JDialogAnalyse.strInfoEnd.text"));
						}
					}else {
						buttonStep.setEnabled(true);
						etape=2;
					}
					
				}
				catch ( InterruptedException e ) {
					buttonStep.setText(controller.getBundle().getString("JDialogAnalyse.strError.text"));
					logger.error("Erreur timeout lors de l'analyse étape 3 ", e);
				}

				break;

			case 4:

				buttonStep.setEnabled(false);

				TimeLeft tl = new TimeLeft();
				ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
				executor.scheduleAtFixedRate(tl, 50, 500, TimeUnit.MILLISECONDS);
				
				lblProgress.setText("0/" + progressBar.getMaximum());
				allLength = 0;
				allLengthTotal = 0;
				listEntiteVideo();
				es.shutdown();
				try {
					es.awaitTermination(30, TimeUnit.MINUTES);
					
					// If not stoped next step, if stoped stay at same step
					executor.shutdownNow();
					lblFileSpeed.setText("");
					lblFileTimeLeft.setText("");
					lblFileName.setText("");
					lblTotalTimeLeft.setText("");
					progressBar2.setValue(100);
					
					if( !stoped ) {
						
						buttonStep.setText(controller.getBundle().getString("JDialogAnalyse.strButtonStep5.text"));
						buttonOk.setEnabled(true);
						lblInformations.setText(controller.getBundle().getString("JDialogAnalyse.strFileUpdateEnd.text"));
					}else {
						buttonStep.setEnabled(true);
						etape=3;
					}
					
				}
				catch ( InterruptedException e ) {
					buttonStep.setText(controller.getBundle().getString("JDialogAnalyse.strError.text"));
					logger.error("Erreur timeout lors de l'analyse étape 4 ", e);
				}

				break;
			}

			lblProgress.setText(value + "/" + progressBar.getMaximum());
			progressBar.setMaximum(1);
			progressBar.setValue(1);
		}



		/**
		 * Liste tous les (sous)éléments d'un noeud Multithreading
		 * 
		 * @param node
		 */
		private void listChildren( ResultMutableTreeNode node ) {

			for ( Enumeration<?> e = node.children(); e.hasMoreElements(); ) {

				ResultMutableTreeNode elt = (ResultMutableTreeNode) e.nextElement();

				if ( !elt.isLeaf() )
					listChildren(elt);
				else {
					es.submit(new OperationLongue(elt, null));
				}
			}
		}



		/**
		 * 
		 */
		private void listEntiteVideo() {

			for ( EntiteVideo ev : videoList ) {
				File f = new File(ev.getEmplacement());
				allLengthTotal += f.length();
				es.submit(new OperationLongue(null, ev));
			}
		}

		/**
		 * Classe de traitement d'un opération longue, pilotée par le traitement long Permet le multithreading
		 * 
		 * @author Brendan Jaouen
		 * @version 2.0.3 24/02/2018
		 */
		private class OperationLongue implements Runnable {

			private ResultMutableTreeNode node;
			private EntiteVideo ev;



			public OperationLongue( ResultMutableTreeNode node, EntiteVideo ev ) {

				this.node = node;
				this.ev = ev;
				
			}



			/**
			 * Mise à jour de la barre de progression avec verrou
			 */
			private synchronized void updateBar() {

				progressBar.setValue(++value);
				lblProgress.setText(value + "/" + progressBar.getMaximum());
			}



			@Override
			public void run() {

				try {

					Method m;

					if ( ev == null ) {

						m = JDialogAnalyse.class.getMethod(nomMethode, ResultMutableTreeNode.class);
						m.invoke(JDialogAnalyse.this, node);
						
						if( node.isActif() )
							updateBar();
					}
					else {
						
						m = JDialogAnalyse.class.getMethod(nomMethode, EntiteVideo.class);
						m.invoke(JDialogAnalyse.this, ev);
						
						updateBar();
					}

					
					synchronized ( this ) {
						notify();
					}

				}
				catch ( Exception e ) {
					logger.error("Erreur de lancement des analyses methode : "+nomMethode+" et node : "+node.getFilePath(), e);
					//e.printStackTrace();
				}
			}
		}
	
		
		/**
		 * 
		 * @author Brendan Jaouen
		 * @since 2.0.3 24/02/2018
		 */
		private class TimeLeft implements Runnable {

			private int longTab = 3, longTabAll = 6, plusAncien = 0, plusAncienAll = 0;;
			private long t0 = 0, tmp, tmpAll;
			private long vitesse[], tempsAll[], temps[], mmVitesse = 0, mmTemps = 0, mmTempsAll = 0;



			TimeLeft() {

				vitesse = new long[longTab];
				temps = new long[longTab];
				tempsAll = new long[longTabAll];

				for ( int i = 0; i < longTab; i++ ) {
					vitesse[i] = 0;
					temps[i] = 0;
				}
				
				for( int i = 0; i < longTabAll; i++ ) {
					tempsAll[i] = 0;
				}
				
				lblFileSpeed.setVisible(true);
				lblFileTimeLeft.setVisible(true);
				lblTotalTimeLeft.setVisible(true);
				progressBar2.setVisible(true);
				lblFileName.setVisible(true);
			}



			public void run() {

				if( (fileLengthTotal == 0) || (fileLength==0) || (fileLength == t0) )
					return;

				
				
				// si passage au fichier suivant, on reset les moyennes
				if( fileLength < t0 ) {
					for ( int i = 0; i < longTab; i++ ) {
						vitesse[i] = 0;
						temps[i] = 0;
					}
					t0=0;
					plusAncien = 0;
					mmVitesse = 0;
					mmTemps = 0;
				}
				
				// vitesse mobile actuelle
				tmp = (fileLength - t0) * 2;
				mmVitesse = mmVitesse + tmp - vitesse[plusAncien];
				vitesse[plusAncien] = tmp;
				String speed = "";
				speed = FileUtils.byteCountToDisplaySize(mmVitesse / longTab) + "/s";
				lblFileSpeed.setText(speed);

				// temps all mobile restant
				tmpAll = (allLengthTotal - allLength) / tmp;
				mmTempsAll = mmTempsAll + tmpAll - tempsAll[plusAncienAll];
				tempsAll[plusAncienAll] = tmpAll;
				// buffer circulaire all
				plusAncienAll++;
				if( plusAncienAll == longTabAll )
					plusAncienAll = 0;
				// Affichage du temps restant all
				tmpAll = mmTempsAll / longTabAll;
				lblTotalTimeLeft.setText(timeLeftReadable(tmpAll));
				
				
				// temps mobile restant
				tmp = (fileLengthTotal - fileLength) / tmp;
				mmTemps = mmTemps + tmp - temps[plusAncien];
				temps[plusAncien] = tmp;

				// buffer circulaire
				plusAncien++;
				if( plusAncien == longTab )
					plusAncien = 0;

				// Affichage du temps restant
				tmp = mmTemps / longTab;

				

				lblFileTimeLeft.setText(timeLeftReadable(tmp));
				
				double d = (double)fileLength/fileLengthTotal;
				d *= 100;
				progressBar2.setValue((int)Math.round(d));
				t0 = fileLength;
			}
			
			private String timeLeftReadable(long time) {
				
				String timeLeft="";
				
				if( time < 60 ) {
					timeLeft = time + "s";
				} else if( time < 3600 ) {

					long m = time / 60;
					long s = time % 60;
					timeLeft = m + "m " + s + "s";
				} else if( time < 86400 ) {

					long h = time / 3600;
					long m = (time - h * 3600) / 60;
					long s = (time - h * 3600) % 60;
					timeLeft = h + "h " + m + "m " + s + "s";
				}
				
				return timeLeft;
			}
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Dada Jajda
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JPanel panelParametres;
	private JButton buttonBrowse;
	private JCheckBox checkBoxFileRename;
	private JCheckBox checkBoxAutoStore;
	private JButton buttonStep;
	private JScrollPane scrollPane;
	private JXTreeTable xTreeTableResultats;
	private JPanel buttonBar;
	private JButton buttonOk;
	private JButton buttonCancel;
	private JXStatusBar xStatusBar;
	private JLabel lblFileName;
	private JLabel lblFileSpeed;
	private JLabel lblFileTimeLeft;
	private JProgressBar progressBar2;
	private JLabel lblInformations;
	private JLabel lblTotalTimeLeft;
	private JProgressBar progressBar;
	private JLabel lblProgress;
	private JLabel strParseFiles;
	private JLabel strSearchVideoName;
	private JLabel strParseVideoData;
	private JLabel strFileUpdating;
	private JLabel strButtonStep2;
	private JLabel strButtonStep3;
	private JLabel strButtonStep4;
	private JLabel strButtonStep4bis;
	private JLabel strButtonStep5;
	private JLabel strInfoParseFilesEnded;
	private JLabel strInfoSearchVideoNameEnded;
	private JLabel strInfoParseVideoDataEnded;
	private JLabel strInfoEnd;
	private JLabel strError;
	private JLabel strFileUpdateEnd;
	private JLabel strFileLocked1;
	private JLabel strFileLocked2;
	private JLabel strFileLockedTitle;
	// JFormDesigner - End of variables declaration //GEN-END:variables

}
