package com.staligtredan.VideoMer.vue.EcrOptions;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.JXHyperlink;

import com.staligtredan.VideoMer.components.model.RegexListModel;
import com.staligtredan.VideoMer.controleur.DefaultController;
import com.staligtredan.VideoMer.modele.Preferences;

/**
 * JPanel qui contient les regex et leur édition  dans les options
 * 
 * @author Brendan
 * @version 0.16 18/04/2014
 */
public class JPanelAnalyse extends JPanel {

	private static final long serialVersionUID = 457710566978082192L;
	
	private DefaultController controller;



	public JPanelAnalyse( DefaultController controller ) {

		this.controller = controller;

		initComponents();
	}



	/**
	 * Sauvegarde les modifications
	 * 
	 * @param preferences
	 */
	public void savePreferences( Preferences preferences ) {

		preferences.setRegexFileCleanBlank(((RegexListModel<?>) listRegexBlank.getModel()).getList());
		preferences.setRegexFileCleanSpace(((RegexListModel<?>) listRegexSpace.getModel()).getList());
		preferences.setRegexTvShow(((RegexListModel<?>) listRegexTvShow.getModel()).getList());
		preferences.setRegexMovie(((RegexListModel<?>) listRegexMovie.getModel()).getList());
	}



	/**
	 * Charge les options
	 * 
	 * @param preferences
	 */
	public void loadPreferences( final Preferences preferences ) {

		listRegexSpace.setModel(new RegexListModel<String>(preferences.getRegexFileCleanSpace()));
		listRegexBlank.setModel(new RegexListModel<Object>(preferences.getRegexFileCleanBlank()));
		listRegexTvShow.setModel(new RegexListModel<Object>(preferences.getRegexTvShow()));
		listRegexMovie.setModel(new RegexListModel<Object>(preferences.getRegexMovie()));
	}



	private void listRegexTvShowMouseClicked( MouseEvent e ) {

		if ( e.getClickCount() >= 2 ) {

			String res = JOptionPane.showInputDialog(this,
					controller.getBundle().getString("JPanelAnalyse.popupMenuEdit.text"),
					listRegexTvShow.getSelectedValue());

			if ( (res== null) ||res.isEmpty() )
				return;
			((RegexListModel<?>) listRegexTvShow.getModel()).setElementAt(listRegexTvShow.getSelectedIndex(), res);
			listRegexTvShow.updateUI();
		}
	}



	private void listRegexBlankMouseClicked( MouseEvent e ) {

		if ( e.getClickCount() >= 2 ) {

			String res = JOptionPane.showInputDialog(this,
					controller.getBundle().getString("JPanelAnalyse.popupMenuEdit.text"),
					listRegexBlank.getSelectedValue());

			if ( (res== null) ||res.isEmpty() )
				return;
			((RegexListModel<?>) listRegexBlank.getModel()).setElementAt(listRegexBlank.getSelectedIndex(), res);
			listRegexBlank.updateUI();
		}
	}



	private void listRegexSpaceMouseClicked( MouseEvent e ) {

		if ( e.getClickCount() >= 2 ) {

			String res = JOptionPane.showInputDialog(this,
					controller.getBundle().getString("JPanelAnalyse.popupMenuEdit.text"),
					listRegexSpace.getSelectedValue());

			if ( (res== null) ||res.isEmpty() )
				return;
			((RegexListModel<?>) listRegexSpace.getModel()).setElementAt(listRegexSpace.getSelectedIndex(), res);
			listRegexSpace.updateUI();
		}
	}



	private void listRegexMovieMouseClicked( MouseEvent e ) {

		if ( e.getClickCount() >= 2 ) {

			String res = JOptionPane.showInputDialog(this,
					controller.getBundle().getString("JPanelAnalyse.popupMenuEdit.text"),
					listRegexMovie.getSelectedValue());

			if ( (res== null) ||res.isEmpty() )
				return;
			((RegexListModel<?>) listRegexMovie.getModel()).setElementAt(listRegexMovie.getSelectedIndex(), res);
			listRegexMovie.updateUI();
		}
	}



	private void menuItem1ActionPerformed( ActionEvent e ) {

		String res = JOptionPane.showInputDialog(this,
				controller.getBundle().getString("JPanelAnalyse.popupMenuAdd.text"));

		if ( popupMenuEdition.getInvoker().equals(listRegexSpace) ) {

			((RegexListModel<?>) listRegexSpace.getModel()).addElement(res);
			listRegexSpace.updateUI();
		}

		if ( popupMenuEdition.getInvoker().equals(listRegexBlank) ) {

			((RegexListModel<?>) listRegexBlank.getModel()).addElement(res);
			listRegexBlank.updateUI();
		}

		if ( popupMenuEdition.getInvoker().equals(listRegexTvShow) ) {

			((RegexListModel<?>) listRegexTvShow.getModel()).addElement(res);
			listRegexTvShow.updateUI();
		}

		if ( popupMenuEdition.getInvoker().equals(listRegexMovie) ) {

			((RegexListModel<?>) listRegexMovie.getModel()).addElement(res);
			listRegexMovie.updateUI();
		}
	}



	private void popupMenuEditActionPerformed( ActionEvent e ) {

		if ( popupMenuEdition.getInvoker().equals(listRegexSpace) ) {

			
			if( listRegexSpace.getSelectedValuesList().isEmpty() )
				return;
			
			String res = JOptionPane.showInputDialog(this,
					controller.getBundle().getString("JPanelAnalyse.popupMenuEdit.text"),
					listRegexSpace.getSelectedValue());

			if ( (res== null) ||res.isEmpty() )
				return;
			((RegexListModel<?>) listRegexSpace.getModel()).setElementAt(listRegexSpace.getSelectedIndex(), res);
			listRegexSpace.updateUI();
		}

		if ( popupMenuEdition.getInvoker().equals(listRegexBlank) ) {

			if( listRegexSpace.getSelectedValuesList().isEmpty() )
				return;
			
			String res = JOptionPane.showInputDialog(this,
					controller.getBundle().getString("JPanelAnalyse.popupMenuEdit.text"),
					listRegexBlank.getSelectedValue());

			if ( (res== null) ||res.isEmpty() )
				return;
			((RegexListModel<?>) listRegexBlank.getModel()).setElementAt(listRegexBlank.getSelectedIndex(), res);
			listRegexBlank.updateUI();
		}

		if ( popupMenuEdition.getInvoker().equals(listRegexTvShow) ) {

			if( listRegexSpace.getSelectedValuesList().isEmpty() )
				return;
			
			String res = JOptionPane.showInputDialog(this,
					controller.getBundle().getString("JPanelAnalyse.popupMenuEdit.text"),
					listRegexTvShow.getSelectedValue());

			if ( (res== null) ||res.isEmpty() )
				return;
			((RegexListModel<?>) listRegexTvShow.getModel()).setElementAt(listRegexTvShow.getSelectedIndex(), res);
			listRegexTvShow.updateUI();
		}

		if ( popupMenuEdition.getInvoker().equals(listRegexMovie) ) {

			if( listRegexSpace.getSelectedValuesList().isEmpty() )
				return;
			
			String res = JOptionPane.showInputDialog(this,
					controller.getBundle().getString("JPanelAnalyse.popupMenuEdit.text"),
					listRegexMovie.getSelectedValue());

			if ( (res== null) ||res.isEmpty() )
				return;
			((RegexListModel<?>) listRegexMovie.getModel()).setElementAt(listRegexMovie.getSelectedIndex(), res);
			listRegexMovie.updateUI();
		}
	}



	private void popupMenuDeleteActionPerformed( ActionEvent e ) {

		if ( popupMenuEdition.getInvoker().equals(listRegexSpace) ) {

			((RegexListModel<?>) listRegexSpace.getModel()).deleteElement(listRegexSpace.getSelectedIndex());
			listRegexSpace.updateUI();
		}

		if ( popupMenuEdition.getInvoker().equals(listRegexBlank) ) {

			((RegexListModel<?>) listRegexBlank.getModel()).deleteElement(listRegexBlank.getSelectedIndex());
			listRegexBlank.updateUI();
		}

		if ( popupMenuEdition.getInvoker().equals(listRegexTvShow) ) {

			((RegexListModel<?>) listRegexTvShow.getModel()).deleteElement(listRegexTvShow.getSelectedIndex());
			listRegexTvShow.updateUI();
		}

		if ( popupMenuEdition.getInvoker().equals(listRegexMovie) ) {

			((RegexListModel<?>) listRegexMovie.getModel()).deleteElement(listRegexMovie.getSelectedIndex());
			listRegexMovie.updateUI();
		}
	}



	private void initComponents() {

		// JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		panelRegex = new JPanel();
		lblRegexSpace = new JLabel();
		lblRegexBlank = new JLabel();
		scrollPaneRegexSpace = new JScrollPane();
		listRegexSpace = new JList<>();
		scrollPaneRegexBlank = new JScrollPane();
		listRegexBlank = new JList<>();
		lblRegexTvShow = new JLabel();
		lblRegexMovie = new JLabel();
		scrollPaneRegexTvShow = new JScrollPane();
		listRegexTvShow = new JList<>();
		scrollPaneRegexMovie = new JScrollPane();
		listRegexMovie = new JList<>();
		xHyperlink1 = new JXHyperlink();
		popupMenuEdition = new JPopupMenu();
		popupMenuAdd = new JMenuItem();
		popupMenuEdit = new JMenuItem();
		popupMenuDelete = new JMenuItem();
		actionPerformed = new RegexInfoLink();

		//======== this ========

		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

		//======== panelRegex ========
		{
			panelRegex.setLayout(new GridBagLayout());
			((GridBagLayout)panelRegex.getLayout()).columnWidths = new int[] {0, 0, 0};
			((GridBagLayout)panelRegex.getLayout()).rowHeights = new int[] {0, 85, 0, 85, 0, 0};
			((GridBagLayout)panelRegex.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0E-4};
			((GridBagLayout)panelRegex.getLayout()).rowWeights = new double[] {0.0, 1.0, 0.0, 1.0, 0.0, 1.0E-4};

			//---- lblRegexSpace ----
			lblRegexSpace.setMinimumSize(new Dimension(1, 14));
			lblRegexSpace.setPreferredSize(new Dimension(109, 14));
			panelRegex.add(lblRegexSpace, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

			//---- lblRegexBlank ----
			lblRegexBlank.setMinimumSize(new Dimension(1, 14));
			panelRegex.add(lblRegexBlank, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//======== scrollPaneRegexSpace ========
			{

				//---- listRegexSpace ----
				listRegexSpace.setComponentPopupMenu(popupMenuEdition);
				listRegexSpace.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				listRegexSpace.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						listRegexSpaceMouseClicked(e);
					}
				});
				scrollPaneRegexSpace.setViewportView(listRegexSpace);
			}
			panelRegex.add(scrollPaneRegexSpace, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

			//======== scrollPaneRegexBlank ========
			{

				//---- listRegexBlank ----
				listRegexBlank.setComponentPopupMenu(popupMenuEdition);
				listRegexBlank.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				listRegexBlank.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						listRegexBlankMouseClicked(e);
					}
				});
				scrollPaneRegexBlank.setViewportView(listRegexBlank);
			}
			panelRegex.add(scrollPaneRegexBlank, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//---- lblRegexTvShow ----
			lblRegexTvShow.setMinimumSize(new Dimension(1, 14));
			panelRegex.add(lblRegexTvShow, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

			//---- lblRegexMovie ----
			lblRegexMovie.setMinimumSize(new Dimension(1, 14));
			panelRegex.add(lblRegexMovie, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//======== scrollPaneRegexTvShow ========
			{

				//---- listRegexTvShow ----
				listRegexTvShow.setPreferredSize(new Dimension(31, 48));
				listRegexTvShow.setComponentPopupMenu(popupMenuEdition);
				listRegexTvShow.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				listRegexTvShow.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						listRegexTvShowMouseClicked(e);
					}
				});
				scrollPaneRegexTvShow.setViewportView(listRegexTvShow);
			}
			panelRegex.add(scrollPaneRegexTvShow, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

			//======== scrollPaneRegexMovie ========
			{

				//---- listRegexMovie ----
				listRegexMovie.setComponentPopupMenu(popupMenuEdition);
				listRegexMovie.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				listRegexMovie.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						listRegexMovieMouseClicked(e);
					}
				});
				scrollPaneRegexMovie.setViewportView(listRegexMovie);
			}
			panelRegex.add(scrollPaneRegexMovie, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//---- xHyperlink1 ----
			xHyperlink1.setAction(actionPerformed);
			xHyperlink1.setText("Plus d'informations sur les expression r\u00e9guli\u00e8res");
			panelRegex.add(xHyperlink1, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		add(panelRegex, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

		//======== popupMenuEdition ========
		{

			//---- popupMenuAdd ----
			popupMenuAdd.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/add.png")));
			popupMenuAdd.addActionListener(e -> menuItem1ActionPerformed(e));
			popupMenuEdition.add(popupMenuAdd);

			//---- popupMenuEdit ----
			popupMenuEdit.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/edit.png")));
			popupMenuEdit.addActionListener(e -> popupMenuEditActionPerformed(e));
			popupMenuEdition.add(popupMenuEdit);

			//---- popupMenuDelete ----
			popupMenuDelete.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/16x16/delete.png")));
			popupMenuDelete.addActionListener(e -> popupMenuDeleteActionPerformed(e));
			popupMenuEdition.add(popupMenuDelete);
		}

		initComponentsI18n();
		// JFormDesigner - End of component initialization //GEN-END:initComponents
	}



	private void initComponentsI18n() {

		// JFormDesigner - Component i18n initialization - DO NOT MODIFY //GEN-BEGIN:initI18n
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		ResourceBundle bundle = controller.getBundle();
		panelRegex.setBorder(new TitledBorder(bundle.getString("JPanelAnalyse.panelRegex.border")));
		lblRegexSpace.setText(bundle.getString("JPanelAnalyse.lblRegexSpace.text"));
		lblRegexBlank.setText(bundle.getString("JPanelAnalyse.lblRegexBlank.text"));
		lblRegexTvShow.setText(bundle.getString("JPanelAnalyse.lblRegexTvShow.text"));
		lblRegexMovie.setText(bundle.getString("JPanelAnalyse.lblRegexMovie.text"));
		popupMenuAdd.setText(bundle.getString("JPanelAnalyse.popupMenuAdd.text"));
		popupMenuEdit.setText(bundle.getString("JPanelAnalyse.popupMenuEdit.text"));
		popupMenuDelete.setText(bundle.getString("JPanelAnalyse.popupMenuDelete.text"));
		// JFormDesigner - End of component i18n initialization //GEN-END:initI18n
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Mouloud rachid
	private JPanel panelRegex;
	private JLabel lblRegexSpace;
	private JLabel lblRegexBlank;
	private JScrollPane scrollPaneRegexSpace;
	private JList<String> listRegexSpace;
	private JScrollPane scrollPaneRegexBlank;
	private JList<String> listRegexBlank;
	private JLabel lblRegexTvShow;
	private JLabel lblRegexMovie;
	private JScrollPane scrollPaneRegexTvShow;
	private JList<String> listRegexTvShow;
	private JScrollPane scrollPaneRegexMovie;
	private JList<String> listRegexMovie;
	private JXHyperlink xHyperlink1;
	private JPopupMenu popupMenuEdition;
	private JMenuItem popupMenuAdd;
	private JMenuItem popupMenuEdit;
	private JMenuItem popupMenuDelete;
	private RegexInfoLink actionPerformed;
	// JFormDesigner - End of variables declaration //GEN-END:variables

	private class RegexInfoLink extends AbstractAction {


		private static final long serialVersionUID = -7621984411702770511L;



		private RegexInfoLink() {

			// JFormDesigner - Action initialization - DO NOT MODIFY //GEN-BEGIN:initComponents
			// Generated using JFormDesigner Evaluation license - Mouloud rachid
			ResourceBundle bundle = controller.getBundle();

			putValue(NAME, bundle.getString("JPanelAnalyse.actionPerformed.Name"));
			putValue(SHORT_DESCRIPTION, bundle.getString("JPanelAnalyse.actionPerformed.ShortDescription"));
			// JFormDesigner - End of action initialization //GEN-END:initComponents
		}



		public void actionPerformed( ActionEvent e ) {

			try {
				Desktop.getDesktop().browse(new URI((String) getValue(Action.SHORT_DESCRIPTION)));
			}
			catch ( IOException e1 ) {
				e1.printStackTrace();
			}
			catch ( URISyntaxException e1 ) {
				e1.printStackTrace();
			}

		}
	}
}
