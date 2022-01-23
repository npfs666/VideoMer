/*
 * Created by JFormDesigner on Mon Jun 14 13:57:57 CEST 2010
 */

package test.jListView.jListMethod;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

/**
 * @author kevin red
 */
public class JFrameTest extends JFrame {

	
	public JFrameTest() {
		initComponents();

		setBounds(440, 225, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Model model = new Model();
		model.addElement(new Video("Equilibrium", "/imgs/equiil.jpg"));
		model.addElement(new Video("Kick Ass", "/imgs/kickass.jpg"));
		model.addElement(new Video("Star Wars: Episode I - The Phantom Menace", "/imgs/sw1.jpg"));
		//model.addElement(new Video("Star wars 3", ""));
		//model.addElement(new Video("Star wars 4", ""));

		//list1 = new JList(cells);
		list1.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		//list1.setFixedCellHeight(25);
		//list1.setFixedCellWidth( 25 );
		list1.setCellRenderer(new CellRenderer());
		list1.setModel(model);
		list1.setVisibleRowCount(1);
		list1.setRolloverEnabled(true);
		list1.setFixedCellHeight(200);
		list1.setFixedCellWidth(200);
		
		
		list1.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, 
				new Color(150, 255, 200), null));
		
		
		scrollPane1.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane1.getHorizontalScrollBar().setUnitIncrement(20);
		
		

		
	}



	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - kevin red
		scrollPane1 = new JScrollPane();
		list1 = new JXList();
		textArea1 = new JTextArea();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0E-4};

		//======== scrollPane1 ========
		{

			//---- list1 ----
			list1.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			list1.setDragEnabled(true);
			list1.setDoubleBuffered(true);
			list1.setRolloverEnabled(true);
			scrollPane1.setViewportView(list1);
		}
		contentPane.add(scrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- textArea1 ----
		textArea1.setWrapStyleWord(true);
		contentPane.add(textArea1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	public static void main(String[] args) {
		JFrameTest moi = new JFrameTest();
		moi.setVisible(true);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - kevin red
	private JScrollPane scrollPane1;
	private JXList list1;
	private JTextArea textArea1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
