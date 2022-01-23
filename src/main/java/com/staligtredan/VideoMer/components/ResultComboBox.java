package com.staligtredan.VideoMer.components;

import java.util.ResourceBundle;

import javax.swing.JComboBox;

import com.staligtredan.VideoMer.controleur.DefaultController;

/**
 * JComboBox représentant le type de vidéo dans l'analyseur de dossier
 * 
 * @author Brendan
 * @version 0.1 26/09/2010
 */
public class ResultComboBox extends JComboBox<Object> {

	private static final long serialVersionUID = -50285651732207843L;

	/**
	 * Traduction de "Film"
	 */
	private String movie;

	/**
	 * Traduction de "Série"
	 */
	private String tvShow;



	public ResultComboBox() {

		super();
	}



	public ResultComboBox( DefaultController controller ) {

		super();

		ResourceBundle bundle = controller.getBundle();

		movie = bundle.getString("JDialogAnalyse.comboBoxTypeMovie");
		tvShow = bundle.getString("JDialogAnalyse.comboBoxTypeTvShow");

		addItem(movie);
		addItem(tvShow);
	}



	/**
	 * @return la traduction de "Film"
	 */
	public String getMovie() {

		return movie;
	}



	/**
	 * @return la traduction de "Série"
	 */
	public String getTvShow() {

		return tvShow;
	}
}
