package com.staligtredan.VideoMer.vue.EcrOptions;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.staligtredan.VideoMer.controleur.DefaultController;
import com.staligtredan.VideoMer.modele.Preferences;
import com.staligtredan.VideoMer.vue.EcrOptions.renderer.LocaleListCellRenderer;

/**
 * JPanel qui contient les options générales
 * 
 * @author Brendan
 * @version 0.16 18/04/2014
 */
public class JPanelGeneral extends JPanel {

	private static final long serialVersionUID = 4035727416665230673L;

	private DefaultController controller;



	public JPanelGeneral( DefaultController controller ) {
		super();
		this.controller = controller;

		initComponents();
	}



	/**
	 * Sauvegarde les modifications
	 * 
	 * @param preferences
	 */
	public void savePreferences( Preferences preferences ) {
		preferences.setLang(((Locale) comboBoxLanguage.getSelectedItem()).getLanguage());
		preferences.setLookAndFeel((String) comboBoxLookAndFeel.getSelectedItem());
		preferences.setRelatif(checkBoxRelative.isSelected());
		preferences.setSurveillerDemarrage(checkBoxFileAnalyser.isSelected());
		preferences.setMoviePath(textFieldMovieLibraryPath.getText());
		preferences.setTvShowPath(textFieldShowLibraryPath.getText());

		ArrayList<String> a = new ArrayList<String>();
		for ( String s : textFieldVideoExtensions.getText().split(",") )
			a.add(s);
		preferences.setVideoExtensions(a.toArray(new String[a.size()]));
	}



	/**
	 * Charge les préférences utilisateur
	 * 
	 * @param preferences
	 */
	public void loadPreferences( Preferences preferences ) {
		comboBoxLanguage.setRenderer(new LocaleListCellRenderer());
		comboBoxLanguage.setModel(new DefaultComboBoxModel<Object>(controller.getListeLangues()));
		comboBoxLanguage.setSelectedItem(new Locale(preferences.getLang()));
		comboBoxLookAndFeel.setModel(new DefaultComboBoxModel<Object>(controller.getListeLookAndFeel()));
		comboBoxLookAndFeel.setSelectedItem(preferences.getLookAndFeel());
		checkBoxFileAnalyser.setSelected(preferences.isSurveillerDemarrage());
		checkBoxRelative.setSelected(preferences.isRelatif());
		textFieldMovieLibraryPath.setText(preferences.getMoviePath());
		textFieldShowLibraryPath.setText(preferences.getTvShowPath());

		String[] ext = preferences.getVideoExtensions();
		String temp = "";
		int i;
		for ( i = 0; i < ext.length - 1; i++ )
			temp = temp + ext[i] + ",";
		temp = temp + ext[i];
		textFieldVideoExtensions.setText(temp);

	}



	private void buttonBrowseMoviePathActionPerformed( ActionEvent e ) {

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogType(JFileChooser.OPEN_DIALOG);

		int returnVal = fc.showOpenDialog(this);
		if( returnVal == JFileChooser.APPROVE_OPTION ) {

			textFieldMovieLibraryPath.setText(fc.getSelectedFile().toString());
		}
	}



	private void buttonBrowseShowPathActionPerformed( ActionEvent e ) {

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogType(JFileChooser.OPEN_DIALOG);

		int returnVal = fc.showOpenDialog(this);
		if( returnVal == JFileChooser.APPROVE_OPTION ) {

			textFieldShowLibraryPath.setText(fc.getSelectedFile().toString());
		}
	}



	private void comboBoxLookAndFeelMouseReleased( MouseEvent e ) {
		lblRestartForLookAndFeel.setVisible(true);
		updateUI();
	}



	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Dada Jajda
		panelView = new JPanel();
		lblLanguage = new JLabel();
		comboBoxLanguage = new JComboBox<>();
		labelLookAndFeel = new JLabel();
		comboBoxLookAndFeel = new JComboBox<>();
		panelBibliotheque = new JPanel();
		checkBoxFileAnalyser = new JCheckBox();
		checkBoxRelative = new JCheckBox();
		lblShowLibPath = new JLabel();
		textFieldMovieLibraryPath = new JTextField();
		buttonBrowseMoviePath = new JButton();
		lblMovieLibPath = new JLabel();
		textFieldShowLibraryPath = new JTextField();
		buttonBrowseShowPath = new JButton();
		lblVideoExtensionsAllowed = new JLabel();
		textFieldVideoExtensions = new JTextField();
		lblRestartForLookAndFeel = new JLabel();

		//======== this ========

		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {500, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0, 0.0, 1.0E-4};

		//======== panelView ========
		{
			panelView.setLayout(new GridBagLayout());
			((GridBagLayout)panelView.getLayout()).columnWidths = new int[] {0, 0, 0};
			((GridBagLayout)panelView.getLayout()).rowHeights = new int[] {0, 0, 0};
			((GridBagLayout)panelView.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
			((GridBagLayout)panelView.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};
			panelView.add(lblLanguage, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 5, 5), 0, 0));
			panelView.add(comboBoxLanguage, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));
			panelView.add(labelLookAndFeel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 0, 5), 0, 0));

			//---- comboBoxLookAndFeel ----
			comboBoxLookAndFeel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					comboBoxLookAndFeelMouseReleased(e);
				}
			});
			panelView.add(comboBoxLookAndFeel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		add(panelView, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== panelBibliotheque ========
		{
			panelBibliotheque.setLayout(new GridBagLayout());
			((GridBagLayout)panelBibliotheque.getLayout()).columnWidths = new int[] {105, 0, 0};
			((GridBagLayout)panelBibliotheque.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
			((GridBagLayout)panelBibliotheque.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0E-4};
			((GridBagLayout)panelBibliotheque.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

			//---- checkBoxFileAnalyser ----
			checkBoxFileAnalyser.setEnabled(false);
			checkBoxFileAnalyser.setVisible(false);
			panelBibliotheque.add(checkBoxFileAnalyser, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));
			panelBibliotheque.add(checkBoxRelative, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));
			panelBibliotheque.add(lblShowLibPath, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));
			panelBibliotheque.add(textFieldMovieLibraryPath, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

			//---- buttonBrowseMoviePath ----
			buttonBrowseMoviePath.addActionListener(e -> buttonBrowseMoviePathActionPerformed(e));
			panelBibliotheque.add(buttonBrowseMoviePath, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));
			panelBibliotheque.add(lblMovieLibPath, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));
			panelBibliotheque.add(textFieldShowLibraryPath, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

			//---- buttonBrowseShowPath ----
			buttonBrowseShowPath.addActionListener(e -> buttonBrowseShowPathActionPerformed(e));
			panelBibliotheque.add(buttonBrowseShowPath, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));
			panelBibliotheque.add(lblVideoExtensionsAllowed, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));
			panelBibliotheque.add(textFieldVideoExtensions, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));
		}
		add(panelBibliotheque, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- lblRestartForLookAndFeel ----
		lblRestartForLookAndFeel.setForeground(Color.red);
		lblRestartForLookAndFeel.setVisible(false);
		add(lblRestartForLookAndFeel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

		initComponentsI18n();
		// //GEN-END:initComponents
	}



	private void initComponentsI18n() {
		// JFormDesigner - Component i18n initialization - DO NOT MODIFY
		// //GEN-BEGIN:initI18n
		// Generated using JFormDesigner Evaluation license - Dada Jajda
		ResourceBundle bundle = controller.getBundle();
		panelView.setBorder(new TitledBorder(bundle.getString("JPanelGeneral.panelView.border")));
		lblLanguage.setText(bundle.getString("JPanelGeneral.lblLanguage.text"));
		labelLookAndFeel.setText(bundle.getString("JPanelGeneral.labelLookAndFeel.text"));
		comboBoxLookAndFeel.setToolTipText(bundle.getString("JPanelGeneral.comboBoxLookAndFeel.toolTipText"));
		panelBibliotheque.setBorder(new TitledBorder(bundle.getString("JPanelGeneral.panelBibliotheque.border")));
		checkBoxFileAnalyser.setText(bundle.getString("JPanelGeneral.checkBoxFileAnalyser.text"));
		checkBoxRelative.setText(bundle.getString("JPanelGeneral.checkBoxRelative.text"));
		checkBoxRelative.setToolTipText(bundle.getString("JPanelGeneral.checkBoxRelative.toolTipText"));
		lblShowLibPath.setText(bundle.getString("JPanelGeneral.lblShowLibPath.text"));
		textFieldMovieLibraryPath.setToolTipText(bundle.getString("JPanelGeneral.textFieldMovieLibraryPath.toolTipText"));
		buttonBrowseMoviePath.setText(bundle.getString("JPanelGeneral.buttonBrowseMoviePath.text"));
		lblMovieLibPath.setText(bundle.getString("JPanelGeneral.lblMovieLibPath.text"));
		textFieldShowLibraryPath.setToolTipText(bundle.getString("JPanelGeneral.textFieldShowLibraryPath.toolTipText"));
		buttonBrowseShowPath.setText(bundle.getString("JPanelGeneral.buttonBrowseMoviePath.text"));
		lblVideoExtensionsAllowed.setText(bundle.getString("JPanelGeneral.lblVideoExtensionsAllowed.text"));
		lblRestartForLookAndFeel.setText(bundle.getString("JPanelGeneral.lblRestartForLookAndFeel.text"));
		// //GEN-END:initI18n
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Dada Jajda
	private JPanel panelView;
	private JLabel lblLanguage;
	private JComboBox<Object> comboBoxLanguage;
	private JLabel labelLookAndFeel;
	private JComboBox<Object> comboBoxLookAndFeel;
	private JPanel panelBibliotheque;
	private JCheckBox checkBoxFileAnalyser;
	private JCheckBox checkBoxRelative;
	private JLabel lblShowLibPath;
	private JTextField textFieldMovieLibraryPath;
	private JButton buttonBrowseMoviePath;
	private JLabel lblMovieLibPath;
	private JTextField textFieldShowLibraryPath;
	private JButton buttonBrowseShowPath;
	private JLabel lblVideoExtensionsAllowed;
	private JTextField textFieldVideoExtensions;
	private JLabel lblRestartForLookAndFeel;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
