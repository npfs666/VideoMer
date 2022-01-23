package com.staligtredan.VideoMer.vue;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.l2fprod.common.swing.JButtonBar;
import com.staligtredan.VideoMer.controleur.DefaultController;
import com.staligtredan.VideoMer.modele.Preferences;
import com.staligtredan.VideoMer.vue.EcrOptions.JPanelAnalyse;
import com.staligtredan.VideoMer.vue.EcrOptions.JPanelGeneral;

/**
 * JDialog qui contient les différents panneaux contenant les options
 * 
 * @author Brendan
 * @version 0.16 18/04/2014
 */
public class JDialogOptions extends JDialog {

	private static final long serialVersionUID = 153589765243056260L;
	
	private DefaultController controller;
	private JPanelAnalyse jpanelAnalyse;
	private JPanelGeneral jpanelGeneral;
	private Preferences tempPreferences;
	
	public JDialogOptions(Frame owner, DefaultController controller) {
		super(owner);
		this.controller = controller;
		tempPreferences = (Preferences)controller.getPreferences().clone();
		
		initComponents();
		initListeners();
		initData();
	}

	public void initListeners() {
		
		// Touche escape pour quitter l'écran
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		ActionListener actionListener = new ActionListener() {
			  public void actionPerformed(ActionEvent actionEvent) {
			     dispose();
			  }
			};
		getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
	private void initData() {
		jpanelGeneral = new JPanelGeneral(controller);
		jpanelAnalyse = new JPanelAnalyse(controller);
		jpanelGeneral.loadPreferences(tempPreferences);
		jpanelAnalyse.loadPreferences(tempPreferences);
		panelGeneral.add(jpanelGeneral, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		// Centrage de l'écran
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		int width = 600, heigh = 500;
		setBounds(p.x-width/2, p.y-heigh/2, width, heigh);
	}
	
	public Preferences getTempPreferences() {
		return tempPreferences;
	}

	private void buttonCancelActionPerformed(ActionEvent e) {
		dispose();
	}

	private void buttonOkActionPerformed(ActionEvent e) {
		
		// Sauvegarde des préférences temporaires
		jpanelGeneral.savePreferences(tempPreferences);
		jpanelAnalyse.savePreferences(tempPreferences);
		
		// Met à jour le vrai fichier préférences
		controller.setPreferences(tempPreferences);
		
		dispose();
	}

	private void toggleButtonGeneralActionPerformed(ActionEvent e) {
		panelGeneral.remove(0);
		jpanelAnalyse.savePreferences(tempPreferences);
		jpanelGeneral.loadPreferences(tempPreferences);
		panelGeneral.add(jpanelGeneral, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		panelGeneral.updateUI();
		//pack();
	}

	private void toggleButtonAnalyseActionPerformed(ActionEvent e) {
		panelGeneral.remove(0);
		jpanelGeneral.savePreferences(tempPreferences);
		jpanelAnalyse.loadPreferences(tempPreferences);
		panelGeneral.add(jpanelAnalyse, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		panelGeneral.updateUI();
		//pack();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		panelDialog = new JPanel();
		panelGeneral = new JPanel();
		panelButtons = new JPanel();
		buttonOk = new JButton();
		buttonCancel = new JButton();
		buttonBar = new JButtonBar();
		toggleButtonGeneral = new JToggleButton();
		toggleButtonAnalyse = new JToggleButton();

		//======== this ========
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== panelDialog ========
		{
			panelDialog.setBorder(new EmptyBorder(12, 12, 12, 12));
			panelDialog.setLayout(new BorderLayout());

			//======== panelGeneral ========
			{
				panelGeneral.setLayout(new GridBagLayout());
				((GridBagLayout)panelGeneral.getLayout()).columnWidths = new int[] {0, 0};
				((GridBagLayout)panelGeneral.getLayout()).rowHeights = new int[] {0, 0};
				((GridBagLayout)panelGeneral.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
				((GridBagLayout)panelGeneral.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};
			}
			panelDialog.add(panelGeneral, BorderLayout.CENTER);

			//======== panelButtons ========
			{
				panelButtons.setBorder(new EmptyBorder(12, 0, 0, 0));
				panelButtons.setLayout(new GridBagLayout());
				((GridBagLayout)panelButtons.getLayout()).columnWidths = new int[] {0, 85, 80};
				((GridBagLayout)panelButtons.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

				//---- buttonOk ----
				buttonOk.addActionListener(e -> buttonOkActionPerformed(e));
				panelButtons.add(buttonOk, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));

				//---- buttonCancel ----
				buttonCancel.addActionListener(e -> buttonCancelActionPerformed(e));
				panelButtons.add(buttonCancel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			panelDialog.add(panelButtons, BorderLayout.SOUTH);
		}
		contentPane.add(panelDialog, BorderLayout.CENTER);

		//======== buttonBar ========
		{

			//---- toggleButtonGeneral ----
			toggleButtonGeneral.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/32x32/gear_edit.png")));
			toggleButtonGeneral.setSelected(true);
			toggleButtonGeneral.addActionListener(e -> toggleButtonGeneralActionPerformed(e));
			buttonBar.add(toggleButtonGeneral);

			//---- toggleButtonAnalyse ----
			toggleButtonAnalyse.setIcon(new ImageIcon(getClass().getResource("/com/staligtredan/VideoMer/imgs/32x32/gear_view.png")));
			toggleButtonAnalyse.addActionListener(e -> toggleButtonAnalyseActionPerformed(e));
			buttonBar.add(toggleButtonAnalyse);
		}
		contentPane.add(buttonBar, BorderLayout.NORTH);

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(toggleButtonGeneral);
		buttonGroup1.add(toggleButtonAnalyse);

		initComponentsI18n();

		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private void initComponentsI18n() {
		// JFormDesigner - Component i18n initialization - DO NOT MODIFY  //GEN-BEGIN:initI18n
		// Generated using JFormDesigner Evaluation license - Mouloud rachid
		ResourceBundle bundle = controller.getBundle();
		setTitle(bundle.getString("EcrOptions.this.title"));
		buttonOk.setText(bundle.getString("EcrOptions.buttonOk.text"));
		buttonCancel.setText(bundle.getString("EcrOptions.buttonCancel.text"));
		toggleButtonGeneral.setText(bundle.getString("EcrOptions.toggleButtonGeneral.text"));
		toggleButtonAnalyse.setText(bundle.getString("EcrOptions.toggleButtonAnalyse.text"));
		// JFormDesigner - End of component i18n initialization  //GEN-END:initI18n
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JDialogOptions  tmp = new JDialogOptions((JFrame)null, new DefaultController());
		tmp.setVisible(true);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Mouloud rachid
	private JPanel panelDialog;
	private JPanel panelGeneral;
	private JPanel panelButtons;
	private JButton buttonOk;
	private JButton buttonCancel;
	private JButtonBar buttonBar;
	private JToggleButton toggleButtonGeneral;
	private JToggleButton toggleButtonAnalyse;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
