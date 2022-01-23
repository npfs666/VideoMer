package com.staligtredan.VideoMer.vue;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.staligtredan.VideoMer.components.CategoryComboBox;
import com.staligtredan.VideoMer.components.JTextFieldAuto;
import com.staligtredan.VideoMer.components.model.ResultComboBoxModel;
import com.staligtredan.VideoMer.controleur.DefaultController;
import com.staligtredan.VideoMer.internet.TMDB;
import com.staligtredan.VideoMer.internet.VideoDataCollector;
import com.staligtredan.VideoMer.modele.EntiteVideo;
import com.staligtredan.VideoMer.modele.Film;
import com.staligtredan.VideoMer.modele.Serie;
import com.staligtredan.VideoMer.util.FlexiComboBox;
import com.staligtredan.VideoMer.util.RelativePath;

/**
 * Ecran d'ajout/édition de média
 * 
 * @author Brendan Jaouen
 * @version 0.1 8/11/2012
 * @version 1.0.0 21/01/2015 (MAJ mineur pour sqlite)
 * @update 1.0.3 10/12/2016 MAJ mineur pour gérér les nouveaux posters
 * @verion 2.0.2 08/08/2017 Possibilité de changer le nom de l'épisode rajouté
 */
public class JDialogEditeur extends JDialog {
	
	private static final long serialVersionUID = -6325201958686118735L;

	/**
	 * Liste pour complétion auto des pays
	 */
	private ArrayList<String> countryList;
	
	/**
	 * Liste pour complétion auto des acteurs
	 */
	private ArrayList<String> actorsList;
	
	/**
	 * Liste pour complétion auto des réalisateurs
	 */
	private ArrayList<String> directorsList;
	
	/**code
	 * Liste pour complétion auto des genres
	 */
	private ArrayList<String> genreList;
	
	/**
	 * Le controlleur
	 */
	private DefaultController controller;
	
	/**
	 * Le parseur de donn�es pour les film, s�ries...
	 */
	private VideoDataCollector bddInternet;
	
	/**
	 * Un buffer pour les séries déjà recherchées
	 */
	private HashMap<String, FlexiComboBox> tvShowBuffer;
	
	/**
	 * La liste des éléments à éditer
	 */
	private ArrayList<EntiteVideo> listToEdit;

	
	
	/**
	 * Constructeur vide (ajout de fichier)
	 * 
	 * @param owner
	 * @param controller
	 */
	public JDialogEditeur(Frame owner, DefaultController controller) {
		super(owner);
		this.controller = controller;
		initComponents();
		initListeners();

		HashMap<String, String> categList = new HashMap<String, String>();
		categList.put("JDialogAnalyse.comboBoxTypeMovie", String.valueOf(EntiteVideo.FILM) );
		categList.put("JDialogAnalyse.comboBoxTypeTvShow", String.valueOf(EntiteVideo.SERIE) );
		categList.put("JDialogEditeur.lblMultipleValues.text", "0" );
		comboCategory.setModel((new CategoryComboBox<ResultComboBoxModel>(controller, categList)).getModel());

		tvShowBuffer = new HashMap<String, FlexiComboBox>();
		
		bddInternet = new TMDB(controller.getPreferences().getLang());
		
		// Auto-complétion
		countryList = new ArrayList<String>();
		actorsList = new ArrayList<String>();
		directorsList = new ArrayList<String>();
		genreList = new ArrayList<String>();
		listAutoCompletionData();
		//createOverlayTextField();
		comboCategory.setSelectedIndex(0);
	}

	/**
	 * Constructeur pour éditer une liste d'<code>EntiteVideo</code>
	 * 
	 * @param owner
	 * @param controller
	 * @param listToEdit
	 */
	public JDialogEditeur(Frame owner, DefaultController controller, ArrayList<EntiteVideo> listToEdit) {
		this(owner, controller);
		this.listToEdit = listToEdit;
		
		initData();
	}
	
	private void initListeners() {

		// Touche escape pour quitter l'�cran
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				dispose();
			}
		};
		getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	private void initData() {
		
		EntiteVideo reference = new EntiteVideo();
		boolean title = true, actor = true, director = true, genre = true, country = true, poster = true, category = true, path = true, plot = true, playtime = true, date = true;

		// Lister les élts commun entre les vidéos et afficher
		for (EntiteVideo ev : listToEdit) {

			// Check identique titre
			if (title == true && reference.getNom().isEmpty())
				reference.setNom(ev.getNom());
			else if (title == true && !reference.getNom().equals(ev.getNom()))
				title = false;

			// Check identique acteurs
			if (actor == true && reference.getActeurs().isEmpty())
				reference.setActeurs(ev.getActeurs());
			else if (actor == true && !reference.getActeurs().equals(ev.getActeurs()))
				actor = false;

			// Check identique Réalisateur
			if (director == true && reference.getRealisateurs().isEmpty())
				reference.setRealisateurs(ev.getRealisateurs());
			else if (director == true && !reference.getRealisateurs().equals(ev.getRealisateurs()))
				director = false;

			// Check identique genre
			if (genre == true && reference.getGenre().isEmpty())
				reference.setGenre(ev.getGenre());
			else if (genre == true && !reference.getGenre().equals(ev.getGenre()))
				genre = false;

			// Check identique nationalité
			if (country == true && reference.getPays().isEmpty())
				reference.setPays(ev.getPays());
			else if (country == true && !reference.getPays().equals(ev.getPays()))
				country = false;
			
			// Check identique poster
			if (poster == true && reference.getAffichette().isEmpty())
				reference.setAffichette(ev.getAffichette());
			else if (poster == true && !reference.getAffichette().equals(ev.getAffichette()))
				poster = false;
			
			// Check identique catégorie
			if (category == true && reference.getCategorie() == 0 )
				reference.setCategorie(ev.getCategorie());
			else if (category == true && reference.getCategorie() != ev.getCategorie())
				category = false;

			// Check identique path
			if (path == true && reference.getEmplacement().isEmpty())
				reference.setEmplacement(ev.getEmplacement());
			else if (path == true && !reference.getEmplacement().equals(ev.getEmplacement()))
				path = false;

			// Check identique plot
			if (plot == true && reference.getSynopsis().isEmpty())
				reference.setSynopsis(ev.getSynopsis());
			else if (plot == true && !reference.getSynopsis().equals(ev.getSynopsis()))
				plot = false;

			// Check identique playtime
			if (playtime == true && reference.getPlaytime().isEmpty())
				reference.setPlaytime(ev.getPlaytime());
			else if (playtime == true && !reference.getPlaytime().equals(ev.getPlaytime()))
				playtime = false;

			// Check identique date
			if (date == true && reference.getDate().isEmpty())
				reference.setDate(ev.getDate());
			else if (date == true && !reference.getDate().equals(ev.getDate()))
				date = false;
		}

		
		// On affiche ensuite les élts commun uniquement
		if (title == true)
			textFieldTitle.setText(reference.getNom());
		else
			textFieldTitle.setText(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text"));
		
		if (actor == true)
			textFieldActors.setText(clearArrayListString(reference.getActeurs().toString()));
		else
			textFieldActors.setText(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text"));
		
		if (director == true)
			textFieldDirectors.setText(clearArrayListString(reference.getRealisateurs().toString()));
		else
			textFieldDirectors.setText(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text"));
		
		if (genre == true)
			textFieldGenre.setText(clearArrayListString(reference.getGenre().toString()));
		else
			textFieldGenre.setText(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text"));
		
		if (country == true)
			textFieldCountry.setText(clearArrayListString(reference.getPays().toString()));
		else
			textFieldCountry.setText(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text"));
		
		if (poster == true)
			chargeImage(reference.getAffichette());
		else {
			lblPoster.setIcon(null);
			lblPoster.setText(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text"));
		}
		
		if( category == true ) {
			
			for( int index = 0; index < comboCategory.getModel().getSize(); index ++ ) {
				ResultComboBoxModel res = comboCategory.getModel().getElementAt(index);

				if( Integer.valueOf(res.getUrl()) == reference.getCategorie() )
					comboCategory.setSelectedIndex(index);
			}
		}
		else
			comboCategory.setSelectedIndex(0);//Mutiple values
		
		if (path == true)
			textFieldPath.setText(reference.getEmplacement());
		else
			textFieldPath.setText(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text"));
		
		if (plot == true)
			textAreaPlot.setText(reference.getSynopsis());
		else
			textAreaPlot.setText(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text"));
		
		if (playtime == true)
			textFieldDuration.setText(reference.getPlaytime());
		else
			textFieldDuration.setText(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text"));
		
		if (date == true)
			textFieldDate.setText(reference.getDate());
		else
			textFieldDate.setText(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text"));
	
		
		// TODO Cas particuliers non traité pour les séries (code et nom ép)
		if( listToEdit.size() > 1 ) {
			textFieldCode.setText(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text"));
			textFieldEpisodeName.setText(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text"));
		}
		else {

			if( listToEdit.get(0).getCategorie() == EntiteVideo.FILM ) {

				Film f = (Film) listToEdit.get(0);
				if( f.getNumSupport() != 0 )
					textFieldCode.setText("CD " + f.getNumSupport());
				
			} else if( listToEdit.get(0).getCategorie() == EntiteVideo.SERIE ) {

				Serie s = (Serie) listToEdit.get(0);
				textFieldCode.setText("S" + s.getNoSaison() + "E" + s.getNoEpisode());
				textFieldEpisodeName.setText(s.getNomEpisode());
			}			
		}
	}
	
	/**
	 * Listage de toutes les listes (acteurs, réalisateurs, pays, genres), triage et on active l'auto-complétion sur les JTextField
	 */
	private void listAutoCompletionData() {

		ArrayList<EntiteVideo> videos = controller.getBibliotheque().getListe(0);

		// Liste générale des genres
		for (EntiteVideo ev : videos) {
			for (String s : ev.getGenre()) {
				if (!s.trim().isEmpty() && !genreList.contains(s)) {
					genreList.add(s);
				}
			}
		}

		// Liste générale des réalisateurs 
		for (EntiteVideo ev : videos) {
			for (String s : ev.getRealisateurs()) {
				if (!s.trim().isEmpty() && !directorsList.contains(s)) {
					directorsList.add(s);
				}
			}
		}

		// Liste générale des nationalités
		for (EntiteVideo ev : videos) {

			if (!ev.getPays().isEmpty()) {
				for (String s : ev.getPays()) {
					if (!s.trim().isEmpty() && !countryList.contains(s)) {
						countryList.add(s);
					}
				}
			}

		}

		// Liste générale des acteurs
		for (EntiteVideo ev : videos) {

			if (!ev.getActeurs().isEmpty()) {
				for (String s : ev.getActeurs()) {
					if (!s.trim().isEmpty() && !actorsList.contains(s)) {
						actorsList.add(s);
					}
				}
			}

		}
		
		Collections.sort(actorsList);
		Collections.sort(genreList);
		Collections.sort(countryList);
		Collections.sort(directorsList);
		
		// Activations sur les JTextField
		textFieldActors.enableAutoCompletion(actorsList);
		textFieldGenre.enableAutoCompletion(genreList);
		textFieldDirectors.enableAutoCompletion(directorsList);
		textFieldCountry.enableAutoCompletion(countryList);
	}
	
	/**
	 * Etape 1 :
	 * 
	 * Recherche un elt vidéo sur Internet pour récupérer une liste de vidéos
	 * qui corresponds potentiellement
	 * 
	 * @param node
	 *            Elt à rechercher
	 */
	public void rechercherTitreOperation(String title) {

		ArrayList<String[]> resultat;
		ResultComboBoxModel item = (ResultComboBoxModel) comboCategory.getSelectedItem();

		// Si on recherche un Film

		if( Integer.valueOf(item.getUrl()) == EntiteVideo.FILM) {

			resultat = bddInternet.search(title, VideoDataCollector.MOVIE);

			// Si résultat -> on renseigne la combobox
			if (resultat != null && resultat.size() > 0) {

				ArrayList<ResultComboBoxModel> listeChoix = new ArrayList<ResultComboBoxModel>();

				for (String[] tab : resultat) {
					ResultComboBoxModel choix = new ResultComboBoxModel();
					choix.setLibelle(tab[0]);
					choix.setUrl(tab[1]);
					listeChoix.add(choix);
				}

				comboBoxResults.setEnabled(true);
				DefaultComboBoxModel<Object> aModel = new DefaultComboBoxModel<Object>(listeChoix.toArray());
				comboBoxResults.setShowTips(true);
				comboBoxResults.setModel(aModel);
			} 
			else {
				String noFileFound = controller.getBundle().getString("JDialogEditeur.lblNoResults.text");
				comboBoxResults.setModel(new DefaultComboBoxModel<Object>(new String[] {noFileFound}));
			}
		}

		// Si on recherche une série
		else if (Integer.valueOf(item.getUrl()) == EntiteVideo.SERIE ) {

			synchronized (tvShowBuffer) {

				if (tvShowBuffer.containsKey(title)) {
					comboBoxResults = tvShowBuffer.get(title);

				} else {

					resultat = bddInternet.search(title, VideoDataCollector.TV_SHOW);

					// Si résultat -> on renseigne la combobox
					if (resultat != null && resultat.size() > 0) {

						ArrayList<ResultComboBoxModel> listeChoix = new ArrayList<ResultComboBoxModel>();

						for (String[] tab : resultat) {
							ResultComboBoxModel choix = new ResultComboBoxModel();
							choix.setLibelle(tab[0]);
							choix.setUrl(tab[1]);
							listeChoix.add(choix);
						}

						FlexiComboBox comboChoix = new FlexiComboBox(listeChoix.toArray());
						comboChoix.setShowTips(true);

						comboBoxResults.setEnabled(true);
						DefaultComboBoxModel<Object> aModel = new DefaultComboBoxModel<Object>(listeChoix.toArray());
						comboBoxResults.setShowTips(true);
						comboBoxResults.setModel(aModel);

						tvShowBuffer.put(title, comboChoix);
					}
					// Aucun résultat
					else {
						String noFileFound = controller.getBundle().getString("JDialogEditeur.lblNoResults.text");
						comboBoxResults.setModel(new DefaultComboBoxModel<Object>(new String[] {noFileFound}));
					}
				}
			}

		}
	}

	/**
	 * Etape 2 :
	 * 
	 * Recherche les informations détaillée de chaque elt en fonction du choix
	 * de l'utilisateur
	 * 
	 * Non multithreadable à cause d'une mauvaise conception de la recherche
	 * internet qui stocke des données
	 * 
	 * @param node
	 *            Elt à rechercher
	 */
	public void rechercherInfosOperation(String Url) {

		ResultComboBoxModel item = (ResultComboBoxModel) comboCategory.getSelectedItem();
		
		// Film
		if( Integer.valueOf(item.getUrl()) == EntiteVideo.FILM) {

			Film f = (Film) bddInternet.parseData(Url, VideoDataCollector.MOVIE, "", "");

			// Recherche des infos d�taill�es du Media
			if (f != null) {

				// Affichage
				textFieldTitle.setText(f.getNom());
				chargeImage(f.getAffichette());
				textFieldActors.setText(clearArrayListString(f.getActeurs().toString()));
				textFieldDirectors.setText(clearArrayListString(f.getRealisateurs().toString()));
				textFieldGenre.setText(clearArrayListString(f.getGenre().toString()));
				textFieldCountry.setText(clearArrayListString(f.getPays().toString()));
				textAreaPlot.setText(f.getSynopsis());
				textFieldDuration.setText(f.getPlaytime());
				textFieldDate.setText(f.getDate());
			}
		}
		// Série
		else if (Integer.valueOf(item.getUrl()) == EntiteVideo.SERIE) {

			String season = "", episode = "";
			Pattern p = Pattern.compile("s([0-9]{1,2})e([0-9]{1,2})");
			Matcher m = p.matcher(textFieldCode.getText().toLowerCase());
			m.matches();
			season = m.group(1);
			episode = m.group(2);

			Serie s = (Serie) bddInternet.parseData(Url, VideoDataCollector.TV_SHOW, season, episode);

			// Recherche des infos détaillées du Media
			if (s != null) {

				// Affichage
				textFieldTitle.setText(s.getNom());
				chargeImage(s.getAffichette());
				textFieldEpisodeName.setText(s.getNomEpisode());
				textFieldActors.setText(clearArrayListString(s.getActeurs().toString()));
				textFieldDirectors.setText(clearArrayListString(s.getRealisateurs().toString()));
				textFieldGenre.setText(clearArrayListString(s.getGenre().toString()));
				textFieldCountry.setText(clearArrayListString(s.getPays().toString()));
				textAreaPlot.setText(s.getSynopsis());
				textFieldDuration.setText(s.getPlaytime());
				textFieldDate.setText(s.getDate());
			}
		}
	}

	private String clearArrayListString(String stringToClear) {
		
		return stringToClear.substring(1, stringToClear.length() - 1);
	}
	
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JDialogEditeur tmp = new JDialogEditeur(null, new DefaultController());
		tmp.setVisible(true);
	}

	/**
	 * Action sur le bouton rechercher
	 * 
	 * @param e
	 */
	private void buttonSearchActionPerformed(ActionEvent e) {

		if( !textFieldTitle.getText().isEmpty() )
			rechercherTitreOperation(textFieldTitle.getText());

	}

	/**
	 * Action sur la liste des résultats
	 * 
	 * @param e
	 */
	private void comboBoxResultsActionPerformed(ActionEvent e) {

		ResultComboBoxModel res = (ResultComboBoxModel) comboBoxResults.getSelectedItem();

		rechercherInfosOperation(res.getUrl());

	}
	
	/**
	 * Charges les images du disque dur au bufferImage
	 */
	private void chargeImage(String url) {

		// Si url vide
		if (url == null || url.trim().isEmpty() || url.contains("null")) {
			lblPoster.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/empty_poster.jpg")));
			return;
		}

		// Si image à charger du DD au buffer
		String posterFileName = url.substring(url.lastIndexOf("/") + 1);
		File f = new File("posters/" + posterFileName);
		TelechargeImage dl;
		// Si image stockée sur le disque dur & non vide
		if (f.isFile() && f.length() != 0) {
			try {

				BufferedImage img = ImageIO.read(f);
				ImageIcon ii = new ImageIcon(img);
				lblPoster.setIcon(ii);
				lblPoster.setToolTipText(url);

			} catch (Exception e) {
				lblPoster.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/empty_poster.jpg")));
			}
		}
		
		// Si image à télécharger
		else {
			if( f.length() == 0 ) f.delete();
			if( !f.isFile() ) {
				dl = new TelechargeImage(url);
				dl.lanceLongTraitement();
			}
		}
	}

	/**
	 * Action bouton Annuler
	 * 
	 * @param e
	 */
	private void cancelButtonActionPerformed(ActionEvent e) {
		dispose();
	}

	/**
	 * Action bouton Ok
	 * 
	 * @param e
	 */
	private void okButtonActionPerformed(ActionEvent e) {
		
		// Cas d'ajout de fichier manuellement
		if ( listToEdit == null || listToEdit.isEmpty() ) {
			
			ResultComboBoxModel item = (ResultComboBoxModel) comboCategory.getSelectedItem();
			
			// Film
			if( Integer.valueOf(item.getUrl()) == EntiteVideo.FILM ) {
				
				Film newMedia = new Film();
				
				newMedia.setActeurs(textFieldActors.toArray());
				newMedia.setAffichette(lblPoster.getToolTipText());
				newMedia.setEmplacement(textFieldPath.getText());
				newMedia.setGenre(textFieldGenre.toArray());
				newMedia.setRealisateurs(textFieldDirectors.toArray());
				newMedia.setNom(textFieldTitle.getText());
				newMedia.setSynopsis(textAreaPlot.getText());
				newMedia.setPays(textFieldCountry.toArray());
				newMedia.setDate(textFieldDate.getText());
				newMedia.setPlaytime(textFieldDuration.getText());
				
				controller.getBibliotheque().add(newMedia);
			
			// Serie
			} else if ( Integer.valueOf(item.getUrl()) == EntiteVideo.SERIE ) {
				
				Serie newMedia = new Serie();
				
				String season = "", episode = "";
				Pattern p = Pattern.compile("s([0-9]{1,2})e([0-9]{1,2})");
				Matcher m = p.matcher(textFieldCode.getText().toLowerCase());
				m.matches();
				season = m.group(1);
				episode = m.group(2);
				
				newMedia.setNoSaison(Integer.valueOf(season));
				newMedia.setNoEpisode(Integer.valueOf(episode));
				newMedia.setActeurs(textFieldActors.toArray());
				newMedia.setAffichette(lblPoster.getToolTipText());
				newMedia.setEmplacement(textFieldPath.getText());
				newMedia.setGenre(textFieldGenre.toArray());
				newMedia.setRealisateurs(textFieldDirectors.toArray());
				newMedia.setNom(textFieldTitle.getText());
				newMedia.setNomEpisode(textFieldEpisodeName.getText());
				newMedia.setSynopsis(textAreaPlot.getText());
				newMedia.setPays(textFieldCountry.toArray());
				newMedia.setDate(textFieldDate.getText());
				newMedia.setPlaytime(textFieldDuration.getText());
				
				controller.getBibliotheque().add(newMedia);
			}
			
			
		}

		// Cas d'une édition de liste
		else {
			
			// Sauvegarde des données
			for (EntiteVideo ev : listToEdit) {

				//2.0.2
				if( ev.getClass() == Serie.class ) {
					
					// Si nom épisode non multiple
					if( ! textFieldEpisodeName.getText().equals(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text")) ) {
						 ((Serie)controller.getBibliotheque().edit(ev)).setNomEpisode(textFieldEpisodeName.getText());
					}
				}
				
				// Si titre non multiple
				if( ! textFieldTitle.getText().equals(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text")) ) {
					 controller.getBibliotheque().edit(ev).setNom(textFieldTitle.getText());
				}
				
				// Si acteurs non multiple
				if( ! textFieldActors.getText().equals(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text")) ) {
					 
					controller.getBibliotheque().edit(ev).setActeurs(textFieldActors.toArray());
				}
				
				// Si réalisateur non multiple
				if( ! textFieldDirectors.getText().equals(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text")) ) {
					
					controller.getBibliotheque().edit(ev).setRealisateurs(textFieldDirectors.toArray());
				}
				
				// Si genre non multiple
				if( ! textFieldGenre.getText().equals(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text")) ) {

					controller.getBibliotheque().edit(ev).setGenre(textFieldGenre.toArray());
				}
				
				// Si pays non multiple
				if( ! textFieldCountry.getText().equals(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text")) ) {

					controller.getBibliotheque().edit(ev).setPays(textFieldCountry.toArray());
				}
				
				// Si poster non multiple
				if( ! lblPoster.getText().equals(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text")) ) {
					 
					controller.getBibliotheque().edit(ev).setAffichette(lblPoster.getToolTipText());
				}
				
				// Si categorie non multiple
				if( comboCategory.getSelectedIndex() != 0 ) {
					
					ResultComboBoxModel res = (ResultComboBoxModel) comboCategory.getSelectedItem();
					controller.getBibliotheque().edit(ev).setCategorie(Integer.valueOf(res.getUrl()));
				} 
				
				// Si path non multiple
				if( ! textFieldPath.getText().equals(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text")) ) {
					
					controller.getBibliotheque().edit(ev).setEmplacement(textFieldPath.getText());
				}
				
				// Si plot non multiple
				if( ! textAreaPlot.getText().equals(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text")) ) {
					
					controller.getBibliotheque().edit(ev).setSynopsis(textAreaPlot.getText());
				} 
				
				// Si playtime non multiple
				if( ! textFieldDuration.getText().equals(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text")) ) {
					
					controller.getBibliotheque().edit(ev).setPlaytime(textFieldDuration.getText());
				}
				
				// Si date non multiple
				if( ! textFieldDate.getText().equals(controller.getBundle().getString("JDialogEditeur.lblMultipleValues.text")) ) {
					
					controller.getBibliotheque().edit(ev).setDate(textFieldDate.getText());
				}
				
				// Mise à jour dans la BDD suite aux modifs
				controller.getBibliotheque().update(ev);
			}
		}		
		
		dispose();
	}

	private void textFieldTitleKeyReleased(KeyEvent e) {
		if( e.getKeyCode() == KeyEvent.VK_ENTER )
			buttonSearchActionPerformed(null);
	}

	private void comboCategoryActionPerformed(ActionEvent e) {
		
		ResultComboBoxModel item = (ResultComboBoxModel) comboCategory.getSelectedItem();
		
		if( Integer.valueOf(item.getUrl()) == EntiteVideo.SERIE ) {
			
			//---- lblEpisodeName ----
			//lblEpisodeName.setText(controller.getBundle().getString("JDialogEditeur.lblEpisodeName.text"));
			panelDetails.add(lblEpisodeName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));
			panelDetails.add(textFieldEpisodeName, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));
			
		} else {
			
			//---- lblEpisodeName ----
			panelDetails.remove(lblEpisodeName);
			panelDetails.remove(textFieldEpisodeName);
		}
		panelDetails.updateUI();
	}

	private void lblPosterMouseClicked(MouseEvent e) {
		
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setMultiSelectionEnabled(false);

		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			chargeImage("file:/"+fc.getSelectedFile().getAbsolutePath());
		}
	}

	private void buttoBrowseActionPerformed(ActionEvent e) {

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setMultiSelectionEnabled(false);

		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			
			if( controller.getPreferences().isRelatif() )
				textFieldPath.setText(RelativePath.getRelativePath(new File("."), new File(fc.getSelectedFile().getAbsolutePath())));
			else
				textFieldPath.setText(fc.getSelectedFile().getAbsolutePath());
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Dada Jajda
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		panelGlobalInfos = new JPanel();
		lblTitle = new JLabel();
		textFieldTitle = new JTextField();
		lblCategory = new JLabel();
		comboCategory = new JComboBox<>();
		lblCode = new JLabel();
		textFieldCode = new JTextField();
		lblSearchEngine = new JLabel();
		comboBoxSearchEngine = new JComboBox<>();
		buttonSearch = new JButton();
		lblResults = new JLabel();
		comboBoxResults = new FlexiComboBox();
		lblPoster = new JLabel();
		panelDetails = new JPanel();
		lblEpisodeName = new JLabel();
		textFieldEpisodeName = new JTextField();
		lblActors = new JLabel();
		textFieldActors = new JTextFieldAuto();
		lblDirectors = new JLabel();
		textFieldDirectors = new JTextFieldAuto();
		lblGenre = new JLabel();
		textFieldGenre = new JTextFieldAuto();
		lblCountry = new JLabel();
		textFieldCountry = new JTextFieldAuto();
		lblPlot = new JLabel();
		scrollPane1 = new JScrollPane();
		textAreaPlot = new JTextArea();
		lblPath = new JLabel();
		textFieldPath = new JTextField();
		buttoBrowse = new JButton();
		lblDuration = new JLabel();
		textFieldDuration = new JTextField();
		lblDate = new JLabel();
		textFieldDate = new JTextField();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		lblMultipleValues = new JLabel();
		lblNoResults = new JLabel();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(7, 7, 7, 7));
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new GridBagLayout());
				((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0E-4};
				((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0E-4};

				//======== panelGlobalInfos ========
				{
					panelGlobalInfos.setLayout(new GridBagLayout());
					((GridBagLayout)panelGlobalInfos.getLayout()).columnWidths = new int[] {85, 300, 0};
					((GridBagLayout)panelGlobalInfos.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
					((GridBagLayout)panelGlobalInfos.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
					((GridBagLayout)panelGlobalInfos.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
					panelGlobalInfos.add(lblTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));

					//---- textFieldTitle ----
					textFieldTitle.addKeyListener(new KeyAdapter() {
						@Override
						public void keyReleased(KeyEvent e) {
							textFieldTitleKeyReleased(e);
						}
					});
					panelGlobalInfos.add(textFieldTitle, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 0), 0, 0));
					panelGlobalInfos.add(lblCategory, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));

					//---- comboCategory ----
					comboCategory.addActionListener(e -> comboCategoryActionPerformed(e));
					panelGlobalInfos.add(comboCategory, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 0), 0, 0));
					panelGlobalInfos.add(lblCode, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));
					panelGlobalInfos.add(textFieldCode, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 0), 0, 0));

					//---- lblSearchEngine ----
					lblSearchEngine.setEnabled(false);
					lblSearchEngine.setVisible(false);
					panelGlobalInfos.add(lblSearchEngine, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));

					//---- comboBoxSearchEngine ----
					comboBoxSearchEngine.setEnabled(false);
					comboBoxSearchEngine.setVisible(false);
					panelGlobalInfos.add(comboBoxSearchEngine, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 0), 0, 0));

					//---- buttonSearch ----
					buttonSearch.addActionListener(e -> buttonSearchActionPerformed(e));
					panelGlobalInfos.add(buttonSearch, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 0), 0, 0));

					//---- lblResults ----
					lblResults.setEnabled(false);
					panelGlobalInfos.add(lblResults, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 5), 0, 0));

					//---- comboBoxResults ----
					comboBoxResults.setEnabled(false);
					comboBoxResults.addActionListener(e -> comboBoxResultsActionPerformed(e));
					panelGlobalInfos.add(comboBoxResults, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
				}
				contentPanel.add(panelGlobalInfos, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- lblPoster ----
				lblPoster.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/empty_poster.jpg")));
				lblPoster.setMaximumSize(new Dimension(200, 300));
				lblPoster.setMinimumSize(new Dimension(200, 300));
				lblPoster.setPreferredSize(new Dimension(200, 300));
				lblPoster.setHorizontalAlignment(SwingConstants.CENTER);
				lblPoster.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						lblPosterMouseClicked(e);
					}
				});
				contentPanel.add(lblPoster, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

				//======== panelDetails ========
				{
					panelDetails.setLayout(new GridBagLayout());
					((GridBagLayout)panelDetails.getLayout()).columnWidths = new int[] {90, 0, 0, 0};
					((GridBagLayout)panelDetails.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
					((GridBagLayout)panelDetails.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
					((GridBagLayout)panelDetails.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0E-4};
					panelDetails.add(lblEpisodeName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));
					panelDetails.add(textFieldEpisodeName, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 0), 0, 0));
					panelDetails.add(lblActors, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));
					panelDetails.add(textFieldActors, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 0), 0, 0));
					panelDetails.add(lblDirectors, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));
					panelDetails.add(textFieldDirectors, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 0), 0, 0));
					panelDetails.add(lblGenre, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));
					panelDetails.add(textFieldGenre, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 0), 0, 0));
					panelDetails.add(lblCountry, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));
					panelDetails.add(textFieldCountry, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 0), 0, 0));
					panelDetails.add(lblPlot, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));

					//======== scrollPane1 ========
					{

						//---- textAreaPlot ----
						textAreaPlot.setRows(4);
						scrollPane1.setViewportView(textAreaPlot);
					}
					panelDetails.add(scrollPane1, new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 0), 0, 0));
					panelDetails.add(lblPath, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));
					panelDetails.add(textFieldPath, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));

					//---- buttoBrowse ----
					buttoBrowse.addActionListener(e -> buttoBrowseActionPerformed(e));
					panelDetails.add(buttoBrowse, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 0), 0, 0));
					panelDetails.add(lblDuration, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));
					panelDetails.add(textFieldDuration, new GridBagConstraints(1, 7, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 0), 0, 0));
					panelDetails.add(lblDate, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 5), 0, 0));
					panelDetails.add(textFieldDate, new GridBagConstraints(1, 8, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
				}
				contentPanel.add(panelDetails, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
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
				okButton.addActionListener(e -> okButtonActionPerformed(e));
				buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));

				//---- cancelButton ----
				cancelButton.setText("Annuler");
				cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
				buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);

		initComponentsI18n();

		pack();
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	private void initComponentsI18n() {
		// JFormDesigner - Component i18n initialization - DO NOT MODIFY  //GEN-BEGIN:initI18n
		// Generated using JFormDesigner Evaluation license - Dada Jajda
		ResourceBundle bundle = controller.getBundle();
		setTitle(bundle.getString("JDialogEditeur.this.title"));
		panelGlobalInfos.setBorder(new TitledBorder(bundle.getString("JDialogEditeur.panelGlobalInfos.border")));
		lblTitle.setText(bundle.getString("JDialogEditeur.lblTitle.text"));
		lblCategory.setText(bundle.getString("JDialogEditeur.lblCategory.text"));
		lblCode.setText(bundle.getString("JDialogEditeur.lblCode.text"));
		textFieldCode.setToolTipText(bundle.getString("JDialogEditeur.textFieldCode.toolTipText"));
		lblSearchEngine.setText(bundle.getString("JDialogEditeur.lblSearchEngine.text"));
		buttonSearch.setText(bundle.getString("JDialogEditeur.buttonSearch.text"));
		lblResults.setText(bundle.getString("JDialogEditeur.lblResults.text"));
		panelDetails.setBorder(new TitledBorder(bundle.getString("JDialogEditeur.panelDetails.border")));
		lblEpisodeName.setText(bundle.getString("JDialogEditeur.lblEpisodeName.text"));
		lblActors.setText(bundle.getString("JDialogEditeur.lblActors.text"));
		lblDirectors.setText(bundle.getString("JDialogEditeur.lblDirectors.text"));
		lblGenre.setText(bundle.getString("JDialogEditeur.lblGenre.text"));
		lblCountry.setText(bundle.getString("JDialogEditeur.lblCountry.text"));
		lblPlot.setText(bundle.getString("JDialogEditeur.lblPlot.text"));
		lblPath.setText(bundle.getString("JDialogEditeur.lblPath.text"));
		buttoBrowse.setText(bundle.getString("JDialogEditeur.buttoBrowse.text"));
		lblDuration.setText(bundle.getString("JDialogEditeur.lblDuration.text"));
		lblDate.setText(bundle.getString("JDialogEditeur.lblDate.text"));
		lblMultipleValues.setText(bundle.getString("JDialogEditeur.lblMultipleValues.text"));
		lblNoResults.setText(bundle.getString("JDialogEditeur.lblNoResults.text"));
		// JFormDesigner - End of component i18n initialization  //GEN-END:initI18n
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Dada Jajda
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JPanel panelGlobalInfos;
	private JLabel lblTitle;
	private JTextField textFieldTitle;
	private JLabel lblCategory;
	private JComboBox<ResultComboBoxModel> comboCategory;
	private JLabel lblCode;
	private JTextField textFieldCode;
	private JLabel lblSearchEngine;
	private JComboBox<?> comboBoxSearchEngine;
	private JButton buttonSearch;
	private JLabel lblResults;
	private FlexiComboBox comboBoxResults;
	private JLabel lblPoster;
	private JPanel panelDetails;
	private JLabel lblEpisodeName;
	private JTextField textFieldEpisodeName;
	private JLabel lblActors;
	private JTextFieldAuto textFieldActors;
	private JLabel lblDirectors;
	private JTextFieldAuto textFieldDirectors;
	private JLabel lblGenre;
	private JTextFieldAuto textFieldGenre;
	private JLabel lblCountry;
	private JTextFieldAuto textFieldCountry;
	private JLabel lblPlot;
	private JScrollPane scrollPane1;
	private JTextArea textAreaPlot;
	private JLabel lblPath;
	private JTextField textFieldPath;
	private JButton buttoBrowse;
	private JLabel lblDuration;
	private JTextField textFieldDuration;
	private JLabel lblDate;
	private JTextField textFieldDate;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	private JLabel lblMultipleValues;
	private JLabel lblNoResults;
	// JFormDesigner - End of variables declaration //GEN-END:variables
	/**
	 * Classe de taitement long avec un nouveau thread
	 * 
	 * @author Brendan Jaouen
	 * @since 0.86
	 * @version 0.89 18/08/2009
	 */
	class TelechargeImage implements Runnable {

		private String url;

		public TelechargeImage(String url) {
			this.url = url;
		}

		/**
		 * Crée un nouveau Thread et appelle la methode start
		 */
		public void lanceLongTraitement() {

			Thread t = new Thread(this);
			t.start();
		}

		public String getUrl() {
			return url;
		}

		/**
		 * methode de l'interface Runnable lance un nouveau thread qui va
		 * executer le code de la methode longTraitement
		 */
		public void run() {

			// Télécharge une image depuis le site & la sauvegarde sur le DD
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

				ImageIcon ii = new ImageIcon(dimg);
				
				String ext = url.substring(url.lastIndexOf(".")).toLowerCase();
				String posterFileName = url.substring(url.lastIndexOf("/") + 1);
				File f = new File("posters/" + posterFileName);
				
				ImageIO.write(dimg, ext.replaceAll("[.]", ""), f);
				
				lblPoster.setIcon(ii);
				lblPoster.setToolTipText(url);
				lblPoster.updateUI();

			} catch (IOException e) {
				lblPoster.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/empty_poster.jpg")));
			}
		}
	}
}
