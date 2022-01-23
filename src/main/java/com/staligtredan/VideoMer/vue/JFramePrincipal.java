package com.staligtredan.VideoMer.vue;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import com.staligtredan.VideoMer.components.model.Filter2ListModel;
import com.staligtredan.VideoMer.components.model.Filter3ListModel;
import com.staligtredan.VideoMer.components.model.PlaylistModel;
import com.staligtredan.VideoMer.components.model.ThumbnailListModel;
import com.staligtredan.VideoMer.components.renderer.JPanelDetailsEntiteVideo;
import com.staligtredan.VideoMer.components.renderer.JPanelVideoControlLight;
import com.staligtredan.VideoMer.components.renderer.ThumbnailPanel;
import com.staligtredan.VideoMer.controleur.DefaultController;
import com.staligtredan.VideoMer.handler.listeners.ThumbnailHandler;
import com.staligtredan.VideoMer.handler.listeners.jframeprincipal.Filter1Handler;
import com.staligtredan.VideoMer.handler.listeners.jframeprincipal.Filter2Handler;
import com.staligtredan.VideoMer.handler.listeners.jframeprincipal.Filter3Handler;
import com.staligtredan.VideoMer.handler.listeners.jframeprincipal.PlaylistHandler;
import com.staligtredan.VideoMer.handler.transfert.FileTransfert;
import com.staligtredan.VideoMer.handler.transfert.FileTransfertHandler;
import com.staligtredan.VideoMer.handler.transfert.FilterListTransfertHandler;
import com.staligtredan.VideoMer.handler.transfert.PlaylistTransfertHandler;
import com.staligtredan.VideoMer.modele.Bibliotheque;
import com.staligtredan.VideoMer.modele.Bibliotheque.Cat;
import com.staligtredan.VideoMer.modele.EntiteVideo;
import com.staligtredan.VideoMer.modele.Serie;
import com.staligtredan.VideoMer.update.UpdateManager;
import com.staligtredan.VideoMer.update.vue.JDialogChangelogs;

/**
 * JFrame principale de l'application VideoMer (contient les menus et touts les autres éléments)
 * 
 * @author Brendan
 * @since 0.1   6/07/2010
 * @version 0.17  2/12/2014	Ajout du double clic pour ajouter des éléments depuis les filtres à la playlist + correction bug pas de MAJ dispo
 * @version 1.0.0 21/01/2015 Changement du menu a propos (virer l'email)
 * @version 1.0.3 10/12/2015 Ajout du menu afficher -> Miniatures et les liens qui vont avec
 * @version 2.0.0 22/12/2016 Un clic playlist affiche les détails aussi + détection des MAJ au lancemement
 * 							 + MAJ pour panneau détails + loging
 * @version 2.0.1 01/01/2017 Threadisation de la détection de MAJ
 * @version 2.0.2 08/08/2017 Complété la fonction search pour les catégorie selectionné
 * @version 2.0.3 25/02/2018 Traduit le titre menu "patchnote"
 */
public class JFramePrincipal extends JFrame implements FileTransfert {

	private static final long serialVersionUID = -6404276193063707683L;
	
	final static Logger logger = LogManager.getLogger(JFramePrincipal.class);

	private JXMultiSplitPane multiSplitPane;
	private DefaultController controller;
	private Filter2ListModel modelListFilter2;
	private Filter3ListModel modelListFilter3;
	private PlaylistModel modelPlaylist;
	private JPanelDetailsEntiteVideo detailsEntiteVideo;



	/**
	 * Constructeur
	 * 
	 * @param controller
	 */
	public JFramePrincipal( DefaultController controller ) {

		this.controller = controller;

		initComponents();
		initView();
		initListeners();
		initData();
		
		// Vérification si MAJ dispo au lancement
		menuHelpUpdateActionPerformed(null);
	}



	/**
	 * Initialise les éléments graphiques
	 */
	private void initView() {

		ArrayList<BufferedImage> bi = new ArrayList<BufferedImage>();
		// Icone
		try {
			bi.add(ImageIO.read(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/movie.png")));
			bi.add(ImageIO.read(getClass().getResource("/com/staligtredan/VideoMer/imgs/32x32/movie.png")));
			bi.add(ImageIO.read(getClass().getResource("/com/staligtredan/VideoMer/imgs/24x24/movie.png")));
			setIconImages(bi);
		}
		catch ( IOException e ) {
			logger.error("Impossible de charger les images imgs/00x00/movie.png", e);
		}

		// Playlist
		xListPlaylist.setHighlighters(
				new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, xListPlaylist.getSelectionBackground()),
				HighlighterFactory.createAlternateStriping());

		// Filtres
		xListFilter1.setHighlighters(
				new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, xListFilter1.getSelectionBackground()),
				HighlighterFactory.createAlternateStriping());
		xListFilter2.setHighlighters(
				new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, xListFilter2.getSelectionBackground()),
				HighlighterFactory.createAlternateStriping());
		xListFilter3.setHighlighters(
				new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, xListFilter3.getSelectionBackground()),
				HighlighterFactory.createAlternateStriping());

		// Informations
		detailsEntiteVideo = new JPanelDetailsEntiteVideo(this, thumbnailPanel, controller);
		scrollPaneDetails.setViewportView(detailsEntiteVideo);
		scrollPaneDetails.getVerticalScrollBar().setUnitIncrement(20);
		scrollPaneDetails.getHorizontalScrollBar().setUnitIncrement(20);

		// Multisplitpane qui gère l'affichage général
		multiSplitPane = new JXMultiSplitPane();
		multiSplitPane.setDividerSize(4);
		multiSplitPane.setBorder(new EmptyBorder(5, 5, 0, 5));
		multiSplitPane.add(xTitledPanelPlaylist, "right");
		multiSplitPane.add(xPanelFiltre, "middle.top");
		multiSplitPane.add(xTitledPanelMediaList, "middle");
		multiSplitPane.add(xTitledPanelInformations, "middle.bottom");
		multiSplitPane.getMultiSplitLayout().setLayoutByWeight(true);

		comboBoxSearchMode.setModel(new DefaultComboBoxModel<String>(lblComboSearchModeValues.getText().split(",")));

		// Mise en place générale
		Container contentPane = getContentPane();
		contentPane.removeAll();

		setLayout(new GridBagLayout());
		((GridBagLayout) contentPane.getLayout()).columnWidths = new int[] { 0, 0 };
		((GridBagLayout) contentPane.getLayout()).rowHeights = new int[] { 0, 0, 0, 0 };
		((GridBagLayout) contentPane.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
		((GridBagLayout) contentPane.getLayout()).rowWeights = new double[] { 1.0, 0.0, 0.0, 1.0E-4 };

		contentPane.add(multiSplitPane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		contentPane.add(panelVideoControl, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		contentPane.add(xStatusBar, new GridBagConstraints(0, 4, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}



	/**
	 * Initialise les données
	 */
	private void initData() {

		// TODO : Récupérer le filtre depuis les préférences
		modelListFilter2 = new Filter2ListModel(Filter3ListModel.GENRE, controller.getBundle());
		modelListFilter3 = new Filter3ListModel(Filter3ListModel.REALISATEUR, controller.getBundle());
		modelPlaylist = new PlaylistModel(controller.getPreferences().getPlayList());

		xListPlaylist.setModel(modelPlaylist);
		xListFilter2.setModel(modelListFilter2);
		xListFilter3.setModel(modelListFilter3);

		xListFilter1.setSelectedIndex(0);

		// Gestion de l'affichage de la playlist/infos détaillées
		// TODO: monter les templates en constantes pour diminuer le temps de travail lors de la création
		menuViewInformations.setSelected(controller.getPreferences().isWindowDetailsShowed());
		menuViewInformationsActionPerformed(null);
		menuViewPlaylist.setSelected(controller.getPreferences().isWindowPlaylistShowed());
		menuViewPlaylistActionPerformed(null);
		
		if( controller.getPreferences().getThumbnailSize() == 1 )
			menuViewThumbnailLarge.setSelected(true);
		else if( controller.getPreferences().getThumbnailSize() == 2 )
			menuViewThumbnailMedium.setSelected(true);
		else if( controller.getPreferences().getThumbnailSize() == 3 )
			menuViewThumbnailSmall.setSelected(true);
	}



	/**
	 * Crée & ajoute les listeners
	 */
	private void initListeners() {

		// Drag N Drop
		TransferHandler th = new FilterListTransfertHandler(this);
		xListFilter1.setTransferHandler(th);
		xListFilter2.setTransferHandler(th);
		xListFilter3.setTransferHandler(th);
		xListPlaylist.setDropMode(DropMode.INSERT);
		xListPlaylist.setTransferHandler(new PlaylistTransfertHandler());
		textFieldMagicBox.setTransferHandler(new FileTransfertHandler(this));

		// Listeners pour auto MAJ du contenu des listes
		xListFilter1.addListSelectionListener(new Filter1Handler(controller, xListFilter2));
		xListFilter2.addListSelectionListener(new Filter2Handler(controller, xListFilter1, xListFilter3));
		xListFilter3.addListSelectionListener(new Filter3Handler(this, thumbnailPanel));
		thumbnailPanel.addThumbnailListListener(new ThumbnailHandler(this, xListPlaylist, detailsEntiteVideo));
		xListPlaylist.addKeyListener(new PlaylistHandler());
		panelVideoControl.setPlayList(xListPlaylist);
		
		// 2.0.0 | Quand on quitte la playliste on elève le posterPLaylist
		xListPlaylist.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				lblPlaylistPoster.setVisible(false);
			}
		});
		
		// Mouse Listeners pour filtre 1 & 2
		xListFilter3.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked( MouseEvent e ) {

				super.mouseClicked(e);
				if( e.getClickCount() >= 2 ) {
					
					modelPlaylist.add(getSelection());
				}
			}
		});
		xListFilter2.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked( MouseEvent e ) {
				
				super.mouseClicked(e);
				if( e.getClickCount() >= 2 ) {
					
					modelPlaylist.add(getSelection());
				}
			}
		});
	}



	/**
	 * A la fermeture du programme on sauvegarde les préférences utilisateurs
	 * 
	 * @param e
	 */
	private void thisWindowClosing( WindowEvent e ) {
		//MultiSplitLayout.printModel(multiSplitPane.getMultiSplitLayout().getModel());

		controller.getPreferences().setPlayList(((PlaylistModel) xListPlaylist.getModel()).getPlaylist());
		
		controller.getPreferences().setFrameDimension(getSize());
		controller.getPreferences().setFrameLocation(getLocation());
		controller.getPreferences().setWindowPlaylistShowed(menuViewPlaylist.isSelected());
		controller.getPreferences().setWindowDetailsShowed(menuViewInformations.isSelected());		
		controller.closeDatabase();
		System.exit(0);
	}



	public void setTitlePanelInformation( String title ) {

		xTitledPanelInformations.setTitle(title);
	}



	public ArrayList<EntiteVideo> getSelection() {

		ArrayList<EntiteVideo> ev = controller.getBibliotheque().getListe(xListFilter1.getSelectedIndex(),
				modelListFilter2.getViewFilter(), xListFilter2.getSelectedIndices(), xListFilter2.getSelectedValues(),
				modelListFilter3.getViewFilter(), xListFilter3.getSelectedIndices(), xListFilter3.getSelectedValues());

		// Ranges les elts par ordre croissant
		Collections.sort(ev);

		return ev;
	}
	
	public int getFilter1() {
		return xListFilter1.getSelectedIndex();
	}

	public void setDetailsVisible() {
		scrollPaneDetails.setVisible(true);
		
	}

	private void xListPlaylistMouseClicked( MouseEvent e ) {

		if ( e.getClickCount() >= 2 ) {
			panelVideoControl.play();
		}
		
		EntiteVideo ev = modelPlaylist.getElement(xListPlaylist.getSelectedIndex());
		
		if( (ev.getAffichette() == null) || ev.getAffichette().contains("null") ) {
			lblPlaylistPoster.setVisible(false);
			return;	
		}
					
		
		String posterFileName = ev.getAffichette().substring(ev.getAffichette().lastIndexOf("/") + 1);
		File poster = new File("posters/" + posterFileName);
		
		try {

			if( (ev.getAffichette() != null) && !ev.getAffichette().isEmpty() ) {

				BufferedImage img = ImageIO.read(poster);
				// setDetailsVisible();
				lblPlaylistPoster.setIcon(new ImageIcon(img));
				lblPlaylistPoster.setVisible(true);
			} else {

				lblPlaylistPoster.setVisible(false);
			}
		} catch ( IOException e1 ) {
			logger.error("Impossible de lire l'affichette : " + poster.getPath(), e);
		}
		
			
		// Mise à jour de la partie informations sur le média
		detailsEntiteVideo.setSynopsis(ev.getSynopsis());
		detailsEntiteVideo.setRealisateurs(ev.getRealisateurs());
		detailsEntiteVideo.setActeurs(ev.getActeurs());
		detailsEntiteVideo.setNationalite(ev.getPays());
		detailsEntiteVideo.setGenres(ev.getGenre());
		detailsEntiteVideo.setPlaytime(ev.getPlaytime());
		detailsEntiteVideo.setPath(ev.getEmplacement());
		detailsEntiteVideo.setDate(ev.getDate());
		
		// Afficher le chainon dans l'ecr principal
		if( ev.getCategorie() == EntiteVideo.SERIE ) {
			Serie s = (Serie) ev;
			setTitlePanelInformation(ev.getNom() + " >> " + " "+controller.getBundle().getString("ThumbnailPanel.lblSeason.text")+" " + s.getNoSaison() + " >> " + s.getNomEpisode());
		} else 
			setTitlePanelInformation(ev.getNom());
	}



	private void menuFileExitActionPerformed( ActionEvent e ) {

		thisWindowClosing(null);
	}



	private void menuFilePreferencesActionPerformed( ActionEvent e ) {

		JDialogOptions options = new JDialogOptions(this, controller);
		options.setVisible(true);
		initComponentsI18n();
		detailsEntiteVideo.initComponentsI18n();
	}



	private void menuFileFileParsingActionPerformed( ActionEvent e ) {

		JDialogAnalyse analyse = new JDialogAnalyse(this, controller);
		analyse.setVisible(true);

		if ( analyse.getVideoList() != null ) {

			/*for ( EntiteVideo ev : analyse.getVideoList() ) {
				controller.getBibliotheque().add(ev);
			}*/
			controller.getBibliotheque().addMultiple(analyse.getVideoList());
			xListFilter1.getListSelectionListeners()[0].valueChanged(new ListSelectionEvent(xListFilter1, 0, 0, false));
			thumbnailPanel.updateUI();
		}
		analyse = null;
	}



	public void setFilter3( int viewFilter, String text ) {

		popupMenuFilter.setInvoker(xTitledPanelFilter3);

		if ( viewFilter == 3 )
			checkBoxMenuItemActionPerformed(new ActionEvent(checkBoxMenuItemActors, 0, null));
		else if ( viewFilter == 1 )
			checkBoxMenuItemActionPerformed(new ActionEvent(checkBoxMenuItemDirector, 0, null));
		else if ( viewFilter == 2 )
			checkBoxMenuItemActionPerformed(new ActionEvent(checkBoxMenuItemCountry, 0, null));
		else if ( viewFilter == 0 )
			checkBoxMenuItemActionPerformed(new ActionEvent(checkBoxMenuItemGenre, 0, null));

		xListFilter3.setSelectedValue(text, true);
	}

	public void setFilter2All() {

		popupMenuFilter.setInvoker(xTitledPanelFilter2);

		xListFilter2.setSelectedIndex(0);
	}

	private void checkBoxMenuItemActionPerformed( ActionEvent e ) {

		JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
		JPopupMenu popup = (JPopupMenu) item.getParent();
		JXTitledPanel invoker = (JXTitledPanel) popup.getInvoker();

		buttonGroupPopupFilter.setSelected(item.getModel(), true);

		if ( invoker == xTitledPanelFilter2 ) {

			modelListFilter2.setViewFilter(popup.getComponentIndex(item));
			xTitledPanelFilter2.setTitle(item.getText());

			// On signale � la liste de se mettre � jour
			for ( ListSelectionListener l : xListFilter1.getListSelectionListeners() )
				l.valueChanged(new ListSelectionEvent(xListFilter1, 0, 0, false));
		}
		else if ( invoker == xTitledPanelFilter3 ) {

			modelListFilter3.setViewFilter(popup.getComponentIndex(item));
			xTitledPanelFilter3.setTitle(item.getText());

			// On signale à la liste de se mettre à jour
			for ( ListSelectionListener l : xListFilter2.getListSelectionListeners() )
				l.valueChanged(new ListSelectionEvent(xListFilter2, 0, 0, false));
		}
	}



	/**
	 * Bouton d'affichage/ou non de la playlist
	 * 
	 * @param e
	 */
	private void menuViewPlaylistActionPerformed( ActionEvent e ) {

		// Conversion d'un bool en int
		int val = menuViewInformations.isSelected() ? 1 : 0;

		if ( menuViewPlaylist.isSelected() ) {

			// MultiSplitPane
			String layoutDef = "(ROW (COLUMN weight=0.80 (LEAF name=middle.top weight=0.20) "
					+ "(LEAF name=middle weight=0.55) " + "(LEAF name=middle.bottom weight=" + val * 0.25 + ")) "
					+ "(LEAF name=right weight=0.20))";
			MultiSplitLayout modelRoot = new MultiSplitLayout(MultiSplitLayout.parseModel(layoutDef));
			
			//modelRoot.setModel(MultiSplitLayout.parseModel(layoutDef));

			multiSplitPane.getMultiSplitLayout().setModel(modelRoot.getModel());
			multiSplitPane.updateUI();

		}
		else {

			// MultiSplitPane
			String layoutDef = "(ROW (COLUMN weight=0.80 (LEAF name=middle.top weight=0.20) "
					+ "(LEAF name=middle weight=0.55) " + "(LEAF name=middle.bottom weight=" + val * 0.25 + ")) "
					+ "(LEAF name=right weight=0))";
			MultiSplitLayout modelRoot = new MultiSplitLayout(MultiSplitLayout.parseModel(layoutDef));
			//modelRoot.setModel(MultiSplitLayout.parseModel(layoutDef));

			multiSplitPane.getMultiSplitLayout().setModel(modelRoot.getModel());
			multiSplitPane.updateUI();
		}
	}



	/**
	 * Bouton d'affichage/ou non des infos détaillées
	 * 
	 * @param e
	 */
	private void menuViewInformationsActionPerformed( ActionEvent e ) {

		int val = menuViewPlaylist.isSelected() ? 1 : 0;

		if ( menuViewInformations.isSelected() ) {

			// MultiSplitPane
			String layoutDef = "(ROW (COLUMN weight=0.80 (LEAF name=middle.top weight=0.20) "
					+ "(LEAF name=middle weight=0.55) " + "(LEAF name=middle.bottom weight=0.25)) "
					+ "(LEAF name=right weight=" + val * 0.20 + "))";
			MultiSplitLayout modelRoot = new MultiSplitLayout(MultiSplitLayout.parseModel(layoutDef));
			//modelRoot.setModel(MultiSplitLayout.parseModel(layoutDef));

			multiSplitPane.getMultiSplitLayout().setModel(modelRoot.getModel());
			multiSplitPane.updateUI();

		}
		else {

			// MultiSplitPane
			String layoutDef = "(ROW (COLUMN weight=0.80 (LEAF name=middle.top weight=0.20) "
					+ "(LEAF name=middle weight=0.8) " + "(LEAF name=middle.bottom weight=0)) "
					+ "(LEAF name=right weight=" + val * 0.20 + "))";
			MultiSplitLayout modelRoot = new MultiSplitLayout(MultiSplitLayout.parseModel(layoutDef));
			//modelRoot.setModel(MultiSplitLayout.parseModel(layoutDef));

			multiSplitPane.getMultiSplitLayout().setModel(modelRoot.getModel());
			multiSplitPane.updateUI();
		}
	}



	private void buttonClearPlaylistActionPerformed( ActionEvent e ) {

		modelPlaylist.clear();
		xListPlaylist.clearSelection();
	}



	private void createUIComponents() {

		panelVideoControl = new JPanelVideoControlLight();
		thumbnailPanel = new ThumbnailPanel(controller);
	}



	private boolean updateAvailable() {
		
		com.staligtredan.VideoMer.update.DefaultController controller = new com.staligtredan.VideoMer.update.DefaultController();
		controller.loadPropertiesFile();
		UpdateManager um = new UpdateManager(controller);

		if( um.getActualVersion().compareTo(um.getLastVersion()) < 0 )
			return true;
		else
			return false;
	}
	
	private void menuHelpUpdateActionPerformed( ActionEvent e ) {

		// 2.0.1 | Lance un thread pour vérifier les mises à jours
		Thread t = new Thread() {
			public void run() {

				if( updateAvailable() ) {

					int res = JOptionPane.showConfirmDialog(JFramePrincipal.this,
							controller.getBundle().getString("EcrPrincipal.strNewUpdateAvailable.text"),
							controller.getBundle().getString("EcrPrincipal.strUpdate.text"), JOptionPane.YES_NO_OPTION);

					if( res == JOptionPane.YES_OPTION ) {

						// On lance l'appli update.jar pour mettre à jour les
						// fichiers
						try {
							Desktop.getDesktop().open(new File("updater.jar"));
						} catch ( IOException ee ) {
							logger.error("Impossible de lancer updater.jar", e);
						}
						thisWindowClosing(null);
					}
				} else if( e != null )
					JOptionPane.showMessageDialog(JFramePrincipal.this,
							controller.getBundle().getString("EcrPrincipal.strNoUpdate.text"));
			}
		};

		t.start();
	}


	
	private void textFieldSearchKeyPressed( KeyEvent e ) {

		if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {

			ThumbnailListModel model = (ThumbnailListModel) thumbnailPanel.getModel();

			// 2.0.2
			Cat c;
			if( xListFilter1.getSelectedIndex() == 2 )
				c = Cat.movie;
			else if( xListFilter1.getSelectedIndex() == 4 )
				c = Cat.tvshow;
			else
				c = Cat.any;
			
			model.setData(controller.getBibliotheque().search(textFieldSearch.getText(),
					Bibliotheque.Search.values()[comboBoxSearchMode.getSelectedIndex()], c));
		}
	}



	private void textFieldSearchFocusGained( FocusEvent e ) {

		// Pour éviter les erreurs due au retour windows qui génerent un null
		if ( e.getOppositeComponent() == null )
			return;

		// On ne reset pas le texte si c'est pour affiner la recherche
		if ( !e.getOppositeComponent().equals(comboBoxSearchMode) )
			textFieldSearch.setText("");

		textFieldSearch.setForeground(Color.black);
	}



	private void textFieldSearchFocusLost( FocusEvent e ) {

		// Pour éviter les erreurs due au retour windows qui génrèrent un null
		if ( e.getOppositeComponent() == null )
			return;

		// On ne reset pas le texte si c'est pour affiner la recherche
		if ( !e.getOppositeComponent().equals(comboBoxSearchMode) )
			textFieldSearch.setText(controller.getBundle().getString("EcrPrincipal.textFieldSearch.text"));

		textFieldSearch.setForeground(Color.gray);
		thumbnailPanel.resetSelection();
	}



	@Override
	public void importData( List<File> l ) {

		JDialogAnalyse analyse = new JDialogAnalyse(this, controller);
		analyse.addFiles(l);
		analyse.setVisible(true);

		if ( analyse.getVideoList() != null ) {

			/*for ( EntiteVideo ev : analyse.getVideoList() ) {
				controller.getBibliotheque().add(ev);
			}*/
			controller.getBibliotheque().addMultiple(analyse.getVideoList());
			xListFilter1.getListSelectionListeners()[0].valueChanged(new ListSelectionEvent(xListFilter1, 0, 0, false));
			thumbnailPanel.updateUI();
		}
		analyse = null;
	}



	private void comboBoxSearchModeActionPerformed( ActionEvent e ) {
		
		if ( !textFieldSearch.getText().equals(controller.getBundle().getString("EcrPrincipal.textFieldSearch.text")) ) {

			ThumbnailListModel model = (ThumbnailListModel) thumbnailPanel.getModel();

			model.setData(controller.getBibliotheque().search(textFieldSearch.getText(),
					Bibliotheque.Search.values()[comboBoxSearchMode.getSelectedIndex()], Bibliotheque.Cat.any));

		}
	}



	private void menuHelpAboutActionPerformed( ActionEvent e ) {

		com.staligtredan.VideoMer.update.DefaultController controller = new com.staligtredan.VideoMer.update.DefaultController();
		controller.loadPropertiesFile();

		String message = "<html><h2>VideoMer</h2><p>Version : " + controller.getProperties().getSoftwareVersion()
			+ "</p><p>Auteur : GaOu</p><p>https://sourceforge.net/projects/videomer/</html>";
		JOptionPane.showMessageDialog(this, message, "A propos de VideoMer", JOptionPane.PLAIN_MESSAGE);
		
	}

	private void buttonExportPlaylistActionPerformed( ActionEvent e ) {

		EcrCopie copie = new EcrCopie(this, controller, modelPlaylist.getPlaylist());
		//copie.setModal(false);
		copie.setVisible(true);
	}

	private void menuViewThumbnailLargeActionPerformed(ActionEvent e) {
		thumbnailPanel.miniatureGrand();
	}

	private void menuViewThumbnailMediumActionPerformed(ActionEvent e) {
		thumbnailPanel.miniatureMoyen();
	}

	private void menuViewThumbnailSmallActionPerformed(ActionEvent e) {
		thumbnailPanel.miniaturePetit();
	}

	private void menuHelpChangelogActionPerformed(ActionEvent e) {
		
		com.staligtredan.VideoMer.update.DefaultController controller = new com.staligtredan.VideoMer.update.DefaultController();
		controller.loadPropertiesFile();
		controller.setLocale();
		UpdateManager um = new UpdateManager(controller);
		
		JDialogChangelogs jd = new JDialogChangelogs(this, controller,um);
		jd.setVisible(true);
	}



	@SuppressWarnings("serial")
	private void initComponents() {

		// JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Dada Jajda
		createUIComponents();

		menuBar = new JMenuBar();
		menuFile = new JMenu();
		menuFileFileParsing = new JMenuItem();
		menuFilePreferences = new JMenuItem();
		menuFileExit = new JMenuItem();
		menuView = new JMenu();
		menuViewPlaylist = new JCheckBoxMenuItem();
		menuViewInformations = new JCheckBoxMenuItem();
		menuViewThumbnail = new JMenu();
		menuViewThumbnailLarge = new JCheckBoxMenuItem();
		menuViewThumbnailMedium = new JCheckBoxMenuItem();
		menuViewThumbnailSmall = new JCheckBoxMenuItem();
		menuHelp = new JMenu();
		menuHelpUpdate = new JMenuItem();
		menuHelpChangelog = new JMenuItem();
		menuHelpAbout = new JMenuItem();
		xPanelFiltre = new JXPanel();
		xTitledPanelFilter1 = new JXTitledPanel();
		scrollPaneFilter1 = new JScrollPane();
		xListFilter1 = new JXList();
		xTitledPanelFilter2 = new JXTitledPanel();
		scrollPaneFilter2 = new JScrollPane();
		xListFilter2 = new JXList();
		xTitledPanelFilter3 = new JXTitledPanel();
		scrollPaneFilter3 = new JScrollPane();
		xListFilter3 = new JXList();
		xTitledPanelPlaylist = new JXTitledPanel();
		panelPlaylist = new JPanel();
		scrollPanePlaylist1 = new JScrollPane();
		xListPlaylist = new JXList();
		label2 = new JLabel();
		lblPlaylistPoster = new JLabel();
		label1 = new JLabel();
		buttonExportPlaylist = new JButton();
		vSpacer1 = new JPanel(null);
		buttonClearPlaylist = new JButton();
		xTitledPanelMediaList = new JXTitledPanel();
		xTitledPanelInformations = new JXTitledPanel();
		scrollPaneDetails = new JScrollPane();
		xStatusBar = new JXStatusBar();
		textFieldSearch = new JTextField();
		comboBoxSearchMode = new JComboBox<>();
		textFieldMagicBox = new JTextField();
		popupMenuFilter = new JPopupMenu();
		checkBoxMenuItemGenre = new JCheckBoxMenuItem();
		checkBoxMenuItemDirector = new JCheckBoxMenuItem();
		checkBoxMenuItemCountry = new JCheckBoxMenuItem();
		checkBoxMenuItemActors = new JCheckBoxMenuItem();
		lblComboSearchModeValues = new JLabel();
		strNewUpdateAvailable = new JLabel();
		strUpdate = new JLabel();
		strNoUpdate = new JLabel();
		strFilterAll = new JLabel();
		buttonGroupPopupFilter = new ButtonGroup();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				thisWindowClosing(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0, 0};
		((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0E-4};
		((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 0.0, 0.0, 1.0E-4};

		//======== menuBar ========
		{

			//======== menuFile ========
			{

				//---- menuFileFileParsing ----
				menuFileFileParsing.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/movie_view.png")));
				menuFileFileParsing.addActionListener(e -> menuFileFileParsingActionPerformed(e));
				menuFile.add(menuFileFileParsing);

				//---- menuFilePreferences ----
				menuFilePreferences.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/gear.png")));
				menuFilePreferences.addActionListener(e -> menuFilePreferencesActionPerformed(e));
				menuFile.add(menuFilePreferences);

				//---- menuFileExit ----
				menuFileExit.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/exit.png")));
				menuFileExit.addActionListener(e -> menuFileExitActionPerformed(e));
				menuFile.add(menuFileExit);
			}
			menuBar.add(menuFile);

			//======== menuView ========
			{

				//---- menuViewPlaylist ----
				menuViewPlaylist.setSelected(true);
				menuViewPlaylist.addActionListener(e -> menuViewPlaylistActionPerformed(e));
				menuView.add(menuViewPlaylist);

				//---- menuViewInformations ----
				menuViewInformations.setSelected(true);
				menuViewInformations.addActionListener(e -> menuViewInformationsActionPerformed(e));
				menuView.add(menuViewInformations);

				//======== menuViewThumbnail ========
				{

					//---- menuViewThumbnailLarge ----
					menuViewThumbnailLarge.addActionListener(e -> menuViewThumbnailLargeActionPerformed(e));
					menuViewThumbnail.add(menuViewThumbnailLarge);

					//---- menuViewThumbnailMedium ----
					menuViewThumbnailMedium.addActionListener(e -> menuViewThumbnailMediumActionPerformed(e));
					menuViewThumbnail.add(menuViewThumbnailMedium);

					//---- menuViewThumbnailSmall ----
					menuViewThumbnailSmall.addActionListener(e -> menuViewThumbnailSmallActionPerformed(e));
					menuViewThumbnail.add(menuViewThumbnailSmall);
				}
				menuView.add(menuViewThumbnail);
			}
			menuBar.add(menuView);

			//======== menuHelp ========
			{

				//---- menuHelpUpdate ----
				menuHelpUpdate.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/download.png")));
				menuHelpUpdate.addActionListener(e -> menuHelpUpdateActionPerformed(e));
				menuHelp.add(menuHelpUpdate);

				//---- menuHelpChangelog ----
				menuHelpChangelog.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/edit.png")));
				menuHelpChangelog.addActionListener(e -> menuHelpChangelogActionPerformed(e));
				menuHelp.add(menuHelpChangelog);

				//---- menuHelpAbout ----
				menuHelpAbout.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/about.png")));
				menuHelpAbout.addActionListener(e -> menuHelpAboutActionPerformed(e));
				menuHelp.add(menuHelpAbout);
			}
			menuBar.add(menuHelp);
		}
		setJMenuBar(menuBar);

		//======== xPanelFiltre ========
		{
			xPanelFiltre.setBorder(BorderFactory.createEmptyBorder());
			xPanelFiltre.setLayout(new GridLayout());

			//======== xTitledPanelFilter1 ========
			{
				xTitledPanelFilter1.setBorder(new DropShadowBorder(Color.black, 5, 0.5f, 12, true, true, true, true));

				//======== scrollPaneFilter1 ========
				{
					scrollPaneFilter1.setBorder(BorderFactory.createEmptyBorder());

					//---- xListFilter1 ----
					xListFilter1.setModel(new AbstractListModel<String>() {
						String[] values = {
							"Tout",
							"Documentaire",
							"Film",
							"Anime",
							"S\u00e9rie"
						};
						@Override
						public int getSize() { return values.length; }
						@Override
						public String getElementAt(int i) { return values[i]; }
					});
					xListFilter1.setRolloverEnabled(true);
					xListFilter1.setDragEnabled(true);
					xListFilter1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					scrollPaneFilter1.setViewportView(xListFilter1);
				}
				xTitledPanelFilter1.add(scrollPaneFilter1);
			}
			xPanelFiltre.add(xTitledPanelFilter1);

			//======== xTitledPanelFilter2 ========
			{
				xTitledPanelFilter2.setBorder(new DropShadowBorder(Color.black, 5, 0.5f, 12, true, true, true, true));
				xTitledPanelFilter2.setComponentPopupMenu(popupMenuFilter);

				//======== scrollPaneFilter2 ========
				{
					scrollPaneFilter2.setBorder(BorderFactory.createEmptyBorder());

					//---- xListFilter2 ----
					xListFilter2.setRolloverEnabled(true);
					xListFilter2.setDragEnabled(true);
					scrollPaneFilter2.setViewportView(xListFilter2);
				}
				xTitledPanelFilter2.add(scrollPaneFilter2);
			}
			xPanelFiltre.add(xTitledPanelFilter2);

			//======== xTitledPanelFilter3 ========
			{
				xTitledPanelFilter3.setBorder(new DropShadowBorder(Color.black, 5, 0.5f, 12, true, true, true, true));
				xTitledPanelFilter3.setComponentPopupMenu(popupMenuFilter);

				//======== scrollPaneFilter3 ========
				{
					scrollPaneFilter3.setBorder(BorderFactory.createEmptyBorder());

					//---- xListFilter3 ----
					xListFilter3.setRolloverEnabled(true);
					xListFilter3.setDragEnabled(true);
					scrollPaneFilter3.setViewportView(xListFilter3);
				}
				xTitledPanelFilter3.add(scrollPaneFilter3);
			}
			xPanelFiltre.add(xTitledPanelFilter3);
		}
		contentPane.add(xPanelFiltre, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

		//======== xTitledPanelPlaylist ========
		{
			xTitledPanelPlaylist.setBorder(new DropShadowBorder(Color.black, 5, 0.5f, 12, true, true, true, true));

			//======== panelPlaylist ========
			{
				panelPlaylist.setLayout(new GridBagLayout());
				((GridBagLayout)panelPlaylist.getLayout()).columnWidths = new int[] {0, 0};
				((GridBagLayout)panelPlaylist.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
				((GridBagLayout)panelPlaylist.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
				((GridBagLayout)panelPlaylist.getLayout()).rowWeights = new double[] {1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

				//======== scrollPanePlaylist1 ========
				{
					scrollPanePlaylist1.setBorder(BorderFactory.createEmptyBorder());

					//---- xListPlaylist ----
					xListPlaylist.setRolloverEnabled(true);
					xListPlaylist.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							xListPlaylistMouseClicked(e);
						}
					});
					scrollPanePlaylist1.setViewportView(xListPlaylist);
				}
				panelPlaylist.add(scrollPanePlaylist1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));

				//---- label2 ----
				label2.setOpaque(true);
				label2.setBackground(Color.white);
				label2.setMinimumSize(new Dimension(0, 15));
				label2.setMaximumSize(new Dimension(0, 15));
				panelPlaylist.add(label2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));

				//---- lblPlaylistPoster ----
				lblPlaylistPoster.setHorizontalAlignment(SwingConstants.CENTER);
				lblPlaylistPoster.setPreferredSize(new Dimension(66, 300));
				lblPlaylistPoster.setMinimumSize(new Dimension(66, 300));
				lblPlaylistPoster.setBackground(Color.white);
				lblPlaylistPoster.setForeground(Color.white);
				lblPlaylistPoster.setOpaque(true);
				panelPlaylist.add(lblPlaylistPoster, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));

				//---- label1 ----
				label1.setBackground(Color.white);
				label1.setOpaque(true);
				label1.setPreferredSize(new Dimension(0, 15));
				label1.setMinimumSize(new Dimension(0, 15));
				panelPlaylist.add(label1, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));

				//---- buttonExportPlaylist ----
				buttonExportPlaylist.setFont(buttonExportPlaylist.getFont().deriveFont(buttonExportPlaylist.getFont().getSize() - 2f));
				buttonExportPlaylist.addActionListener(e -> buttonExportPlaylistActionPerformed(e));
				panelPlaylist.add(buttonExportPlaylist, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));

				//---- vSpacer1 ----
				vSpacer1.setMinimumSize(new Dimension(12, 5));
				vSpacer1.setPreferredSize(new Dimension(10, 5));
				panelPlaylist.add(vSpacer1, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));

				//---- buttonClearPlaylist ----
				buttonClearPlaylist.setFont(buttonClearPlaylist.getFont().deriveFont(buttonClearPlaylist.getFont().getSize() - 2f));
				buttonClearPlaylist.addActionListener(e -> buttonClearPlaylistActionPerformed(e));
				panelPlaylist.add(buttonClearPlaylist, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			xTitledPanelPlaylist.add(panelPlaylist);
		}
		contentPane.add(xTitledPanelPlaylist, new GridBagConstraints(1, 0, 1, 3, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

		//======== xTitledPanelMediaList ========
		{
			xTitledPanelMediaList.setBorder(new DropShadowBorder(Color.black, 5, 0.5f, 12, true, true, true, true));
			xTitledPanelMediaList.add(thumbnailPanel);
		}
		contentPane.add(xTitledPanelMediaList, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

		//======== xTitledPanelInformations ========
		{
			xTitledPanelInformations.setBorder(new DropShadowBorder(Color.black, 5, 0.5f, 12, true, true, true, true));
			xTitledPanelInformations.setTitleFont(new Font("Tahoma", Font.ITALIC, 11));

			//======== scrollPaneDetails ========
			{
				scrollPaneDetails.setMaximumSize(new Dimension(300, 32767));
			}
			xTitledPanelInformations.add(scrollPaneDetails);
		}
		contentPane.add(xTitledPanelInformations, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

		//---- panelVideoControl ----
		panelVideoControl.setVisible(false);
		contentPane.add(panelVideoControl, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

		//======== xStatusBar ========
		{
			xStatusBar.setLayout(new GridBagLayout());
			((GridBagLayout)xStatusBar.getLayout()).columnWidths = new int[] {110, 0, 0, 0, 0};
			((GridBagLayout)xStatusBar.getLayout()).rowHeights = new int[] {0, 0};
			((GridBagLayout)xStatusBar.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 0.0, 1.0E-4};
			((GridBagLayout)xStatusBar.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

			//---- textFieldSearch ----
			textFieldSearch.setForeground(Color.gray);
			textFieldSearch.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					textFieldSearchKeyPressed(e);
				}
			});
			textFieldSearch.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					textFieldSearchFocusGained(e);
				}
				@Override
				public void focusLost(FocusEvent e) {
					textFieldSearchFocusLost(e);
				}
			});
			xStatusBar.add(textFieldSearch, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 10), 0, 0));

			//---- comboBoxSearchMode ----
			comboBoxSearchMode.addActionListener(e -> comboBoxSearchModeActionPerformed(e));
			xStatusBar.add(comboBoxSearchMode, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 10), 0, 0));

			//---- textFieldMagicBox ----
			textFieldMagicBox.setEditable(false);
			textFieldMagicBox.setEnabled(false);
			textFieldMagicBox.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			textFieldMagicBox.setFocusable(false);
			textFieldMagicBox.setPreferredSize(new Dimension(70, 20));
			xStatusBar.add(textFieldMagicBox, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(xStatusBar, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

		//======== popupMenuFilter ========
		{

			//---- checkBoxMenuItemGenre ----
			checkBoxMenuItemGenre.setSelected(true);
			checkBoxMenuItemGenre.addActionListener(e -> checkBoxMenuItemActionPerformed(e));
			popupMenuFilter.add(checkBoxMenuItemGenre);

			//---- checkBoxMenuItemDirector ----
			checkBoxMenuItemDirector.addActionListener(e -> checkBoxMenuItemActionPerformed(e));
			popupMenuFilter.add(checkBoxMenuItemDirector);

			//---- checkBoxMenuItemCountry ----
			checkBoxMenuItemCountry.addActionListener(e -> checkBoxMenuItemActionPerformed(e));
			popupMenuFilter.add(checkBoxMenuItemCountry);

			//---- checkBoxMenuItemActors ----
			checkBoxMenuItemActors.addActionListener(e -> checkBoxMenuItemActionPerformed(e));
			popupMenuFilter.add(checkBoxMenuItemActors);
		}

		//---- buttonGroupPosters ----
		ButtonGroup buttonGroupPosters = new ButtonGroup();
		buttonGroupPosters.add(menuViewThumbnailLarge);
		buttonGroupPosters.add(menuViewThumbnailMedium);
		buttonGroupPosters.add(menuViewThumbnailSmall);

		//---- buttonGroupPopupFilter ----
		buttonGroupPopupFilter.add(checkBoxMenuItemGenre);
		buttonGroupPopupFilter.add(checkBoxMenuItemDirector);
		buttonGroupPopupFilter.add(checkBoxMenuItemCountry);
		buttonGroupPopupFilter.add(checkBoxMenuItemActors);

		initComponentsI18n();

		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization //GEN-END:initComponents
	}



	private void initComponentsI18n() {

		// JFormDesigner - Component i18n initialization - DO NOT MODIFY //GEN-BEGIN:initI18n
		// Generated using JFormDesigner Evaluation license - Dada Jajda
		ResourceBundle bundle = controller.getBundle();
		setTitle(bundle.getString("EcrPrincipal.this.title"));
		menuFile.setText(bundle.getString("EcrPrincipal.menuFile.text"));
		menuFileFileParsing.setText(bundle.getString("EcrPrincipal.menuFileFileParsing.text"));
		menuFilePreferences.setText(bundle.getString("EcrPrincipal.menuFilePreferences.text"));
		menuFileExit.setText(bundle.getString("EcrPrincipal.menuFileExit.text"));
		menuView.setText(bundle.getString("EcrPrincipal.menuView.text"));
		menuViewPlaylist.setText(bundle.getString("EcrPrincipal.menuViewPlaylist.text"));
		menuViewInformations.setText(bundle.getString("EcrPrincipal.menuViewInformations.text"));
		menuViewThumbnail.setText(bundle.getString("EcrPrincipal.menuViewThumbnail.text"));
		menuViewThumbnailLarge.setText(bundle.getString("EcrPrincipal.menuViewThumbnailLarge.text"));
		menuViewThumbnailMedium.setText(bundle.getString("EcrPrincipal.menuViewThumbnailMedium.text"));
		menuViewThumbnailSmall.setText(bundle.getString("EcrPrincipal.menuViewThumbnailSmall.text"));
		menuHelp.setText(bundle.getString("EcrPrincipal.menuHelp.text"));
		menuHelpUpdate.setText(bundle.getString("EcrPrincipal.menuHelpUpdate.text"));
		menuHelpChangelog.setText(bundle.getString("EcrPrincipal.menuHelpChangelog.text"));
		menuHelpAbout.setText(bundle.getString("EcrPrincipal.menuHelpAbout.text"));
		xTitledPanelFilter1.setTitle(bundle.getString("EcrPrincipal.xTitledPanelFilter1.title"));
		xTitledPanelFilter2.setTitle(bundle.getString("EcrPrincipal.xTitledPanelFilter2.title"));
		xTitledPanelFilter3.setTitle(bundle.getString("EcrPrincipal.xTitledPanelFilter3.title"));
		xTitledPanelPlaylist.setTitle(bundle.getString("EcrPrincipal.xTitledPanelPlaylist.title"));
		buttonExportPlaylist.setText(bundle.getString("EcrPrincipal.buttonExportPlaylist.text"));
		buttonClearPlaylist.setText(bundle.getString("EcrPrincipal.buttonClearPlaylist.text"));
		xTitledPanelInformations.setTitle(bundle.getString("EcrPrincipal.xTitledPanelInformations.title"));
		textFieldSearch.setText(bundle.getString("EcrPrincipal.textFieldSearch.text"));
		textFieldMagicBox.setText(bundle.getString("EcrPrincipal.textFieldMagicBox.text"));
		textFieldMagicBox.setToolTipText(bundle.getString("EcrPrincipal.textFieldMagicBox.toolTipText"));
		checkBoxMenuItemGenre.setText(bundle.getString("EcrPrincipal.checkBoxMenuItemGenre.text"));
		checkBoxMenuItemDirector.setText(bundle.getString("EcrPrincipal.checkBoxMenuItemDirector.text"));
		checkBoxMenuItemCountry.setText(bundle.getString("EcrPrincipal.checkBoxMenuItemCountry.text"));
		checkBoxMenuItemActors.setText(bundle.getString("EcrPrincipal.checkBoxMenuItemActors.text"));
		lblComboSearchModeValues.setText(bundle.getString("EcrPrincipal.lblComboSearchModeValues.text"));
		strNewUpdateAvailable.setText(bundle.getString("EcrPrincipal.strNewUpdateAvailable.text"));
		strUpdate.setText(bundle.getString("EcrPrincipal.strUpdate.text"));
		strNoUpdate.setText(bundle.getString("EcrPrincipal.strNoUpdate.text"));
		strFilterAll.setText(bundle.getString("EcrPrincipal.strFilterAll.text"));
		// JFormDesigner - End of component i18n initialization //GEN-END:initI18n
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Dada Jajda
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenuItem menuFileFileParsing;
	private JMenuItem menuFilePreferences;
	private JMenuItem menuFileExit;
	private JMenu menuView;
	private JCheckBoxMenuItem menuViewPlaylist;
	private JCheckBoxMenuItem menuViewInformations;
	private JMenu menuViewThumbnail;
	private JCheckBoxMenuItem menuViewThumbnailLarge;
	private JCheckBoxMenuItem menuViewThumbnailMedium;
	private JCheckBoxMenuItem menuViewThumbnailSmall;
	private JMenu menuHelp;
	private JMenuItem menuHelpUpdate;
	private JMenuItem menuHelpChangelog;
	private JMenuItem menuHelpAbout;
	private JXPanel xPanelFiltre;
	private JXTitledPanel xTitledPanelFilter1;
	private JScrollPane scrollPaneFilter1;
	private JXList xListFilter1;
	private JXTitledPanel xTitledPanelFilter2;
	private JScrollPane scrollPaneFilter2;
	private JXList xListFilter2;
	private JXTitledPanel xTitledPanelFilter3;
	private JScrollPane scrollPaneFilter3;
	private JXList xListFilter3;
	private JXTitledPanel xTitledPanelPlaylist;
	private JPanel panelPlaylist;
	private JScrollPane scrollPanePlaylist1;
	private JXList xListPlaylist;
	private JLabel label2;
	private JLabel lblPlaylistPoster;
	private JLabel label1;
	private JButton buttonExportPlaylist;
	private JPanel vSpacer1;
	private JButton buttonClearPlaylist;
	private JXTitledPanel xTitledPanelMediaList;
	private ThumbnailPanel thumbnailPanel;
	private JXTitledPanel xTitledPanelInformations;
	private JScrollPane scrollPaneDetails;
	private JPanelVideoControlLight panelVideoControl;
	private JXStatusBar xStatusBar;
	private JTextField textFieldSearch;
	private JComboBox<String> comboBoxSearchMode;
	private JTextField textFieldMagicBox;
	private JPopupMenu popupMenuFilter;
	private JCheckBoxMenuItem checkBoxMenuItemGenre;
	private JCheckBoxMenuItem checkBoxMenuItemDirector;
	private JCheckBoxMenuItem checkBoxMenuItemCountry;
	private JCheckBoxMenuItem checkBoxMenuItemActors;
	private JLabel lblComboSearchModeValues;
	private JLabel strNewUpdateAvailable;
	private JLabel strUpdate;
	private JLabel strNoUpdate;
	private JLabel strFilterAll;
	private ButtonGroup buttonGroupPopupFilter;
	// JFormDesigner - End of variables declaration //GEN-END:variables

}
