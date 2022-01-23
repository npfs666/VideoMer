package com.staligtredan.VideoMer.components;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import com.staligtredan.VideoMer.components.model.ResultComboBoxModel;

/**
 * Le model utilisé par {@code JXTreeTable}.
 * <p>
 * Ce model est adapté à un {@code JXTreeTable} pour l'écran d'analyse des
 * fichiers vidéos
 * <p>
 * 
 * @see TreeModel
 * @see javax.swing.table.TableModel
 * 
 * @author Brendan Jaouen
 * @version 0.1 6/07/2010
 */
public class ResultMutableTreeNode extends DefaultMutableTreeNode implements Comparable<ResultMutableTreeNode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7960198791124445683L;

	/**
	 * Booléen qui permet de déterminer si cet élément est à traiter
	 */
	private boolean actif;

	/**
	 * Code qui permet de déterminer le n° du CD ou le n° saison/épisode d'un
	 * élément
	 */
	private String code;

	/**
	 * Type (Film, Série) de l'élément
	 */
	private String type;

	/**
	 * Chemin absolu de l'élément
	 */
	private String filePath;

	/**
	 * ComboBox qui contient tous les choix possibles pour l'élément
	 */
	private ResultComboBoxModel choix;



	public ResultMutableTreeNode( Object userObject ) {

		super(userObject);
		actif = false;
		code = "";
		choix = new ResultComboBoxModel();
		type = "";
		filePath = "";
	}



	@Override
	public int compareTo( ResultMutableTreeNode o ) {

		return ((String) userObject).compareTo(((ResultMutableTreeNode) o).getUserObject().toString());
	}



	/**
	 * @return the actif
	 */
	public boolean isActif() {

		return actif;
	}



	/**
	 * @param actif
	 *            the actif to set
	 */
	public void setActif( boolean actif ) {

		this.actif = actif;
	}



	/**
	 * @return the code
	 */
	public String getCode() {

		return code;
	}



	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode( String code ) {

		this.code = code;
	}



	/**
	 * @return the type
	 */
	public String getType() {

		return type;
	}



	/**
	 * @param type
	 *            the type to set
	 */
	public void setType( String type ) {

		this.type = type;
	}



	/**
	 * @return the filePath
	 */
	public String getFilePath() {

		return filePath;
	}



	/**
	 * @param filePath
	 *            the filePath to set
	 */
	public void setFilePath( String filePath ) {

		this.filePath = filePath;
	}



	/**
	 * @return the choix
	 */
	public ResultComboBoxModel getChoix() {

		return choix;
	}



	/**
	 * @param choix
	 *            the choix to set
	 */
	public void setChoix( ResultComboBoxModel choix ) {

		this.choix = choix;
	}
}
