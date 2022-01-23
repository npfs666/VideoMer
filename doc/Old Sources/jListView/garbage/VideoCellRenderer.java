/*
 * Created by JFormDesigner on Tue Jun 01 12:58:32 CEST 2010
 */

package test.jListView.garbage;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author kevin red
 */
public class VideoCellRenderer extends JPanel {
	
	public VideoCellRenderer() {
		initComponents();
	}
	
	public VideoCellRenderer(JLabel icon, JLabel text) {
		
		initComponents();
		//label1.setIcon(icon.getIcon());
		//File f = new File(text.getText());
		
		label2.setText(text.getText());
		//label2.setText(text.getText().split("[:]")[0]);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - kevin red
		label1 = new JLabel();
		label2 = new JLabel();

		//======== this ========

		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0E-4};

		//---- label1 ----
		label1.setIcon(new ImageIcon(getClass().getResource("/imgs/mini.jpg")));
		label1.setMaximumSize(new Dimension(60, 60));
		label1.setOpaque(true);
		label1.setPreferredSize(new Dimension(60, 60));
		add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- label2 ----
		label2.setText("Titre du filmeu.avi");
		add(label2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.SOUTH, GridBagConstraints.NONE,
			new Insets(0, 0, 0, 0), 0, 0));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - kevin red
	private JLabel label1;
	private JLabel label2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
