/*
 * Created by JFormDesigner on Tue May 01 15:35:37 CEST 2012
 */

package com.staligtredan.VideoMer.update.admin;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXTable;

/**
 * @author Jean Mouloud
 */
@SuppressWarnings({"serial", "rawtypes"})
public class UpdateCreator extends JFrame {
	public UpdateCreator() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Jean Mouloud
		panel2 = new JPanel();
		panel1 = new JPanel();
		label1 = new JLabel();
		comboBox1 = new JComboBox();
		label2 = new JLabel();
		scrollPane4 = new JScrollPane();
		textPane1 = new JTextPane();
		label3 = new JLabel();
		textField1 = new JTextField();
		button3 = new JButton();
		scrollPane3 = new JScrollPane();
		xTable1 = new JXTable();
		panel3 = new JPanel();
		button2 = new JButton();
		button1 = new JButton();

		//======== this ========
		setTitle("Assistant de cr\u00e9ation de version");
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0};
		((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

		//======== panel2 ========
		{
			panel2.setBorder(new EmptyBorder(5, 5, 5, 5));

			// JFormDesigner evaluation mark
			panel2.setBorder(new javax.swing.border.CompoundBorder(
				new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
					"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
					javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
					java.awt.Color.red), panel2.getBorder())); panel2.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

			panel2.setLayout(new GridBagLayout());
			((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
			((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
			((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
			((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0, 0.0, 1.0E-4};

			//======== panel1 ========
			{
				panel1.setBorder(new TitledBorder("Donn\u00e9s g\u00e9n\u00e9rales :"));
				panel1.setLayout(new GridBagLayout());
				((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0, 0};
				((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
				((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0E-4};
				((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0, 1.0E-4};

				//---- label1 ----
				label1.setText("Version :");
				panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				panel1.add(comboBox1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

				//---- label2 ----
				label2.setText("Changelog :");
				panel1.add(label2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//======== scrollPane4 ========
				{

					//---- textPane1 ----
					textPane1.setText("qsdqsdqsd\nqsd\nqsd\nq\nsdq\nsdqsd");
					scrollPane4.setViewportView(textPane1);
				}
				panel1.add(scrollPane4, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			panel2.add(panel1, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//---- label3 ----
			label3.setText("Libairies :");
			panel2.add(label3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));
			panel2.add(textField1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 5), 0, 0));

			//---- button3 ----
			button3.setText("Parcourir");
			panel2.add(button3, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//======== scrollPane3 ========
			{

				//---- xTable1 ----
				xTable1.setModel(new DefaultTableModel(
					new Object[][] {
						{"Db4o", "db4o.jar", "8.0.17.45.562"},
					},
					new String[] {
						null, null, null
					}
				));
				scrollPane3.setViewportView(xTable1);
			}
			panel2.add(scrollPane3, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

			//======== panel3 ========
			{
				panel3.setLayout(new GridBagLayout());
				((GridBagLayout)panel3.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
				((GridBagLayout)panel3.getLayout()).rowHeights = new int[] {0, 0};
				((GridBagLayout)panel3.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};
				((GridBagLayout)panel3.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

				//---- button2 ----
				button2.setText("Cr\u00e9er");
				panel3.add(button2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));

				//---- button1 ----
				button1.setText("Quitter");
				panel3.add(button1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			panel2.add(panel3, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(panel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Jean Mouloud
	private JPanel panel2;
	private JPanel panel1;
	private JLabel label1;
	private JComboBox comboBox1;
	private JLabel label2;
	private JScrollPane scrollPane4;
	private JTextPane textPane1;
	private JLabel label3;
	private JTextField textField1;
	private JButton button3;
	private JScrollPane scrollPane3;
	private JXTable xTable1;
	private JPanel panel3;
	private JButton button2;
	private JButton button1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
