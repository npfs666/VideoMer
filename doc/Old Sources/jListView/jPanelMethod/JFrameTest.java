/*
 * Created by JFormDesigner on Mon Jun 14 13:57:57 CEST 2010
 */

package test.jListView.jPanelMethod;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

/**
 * @author kevin red
 */
public class JFrameTest extends JFrame {
	public JFrameTest() {
		initComponents();
		
		setBounds(440, 225, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		xPanel1.setLayout(new ModifiedFlowLayout());
		
		
		xPanel1.setBackground(Color.white);
		
		for(int i=0; i < 50; i++) {
			xPanel1.add(new VideoCellRenderer());
		}
		
		scrollPane1.setWheelScrollingEnabled(true);
		scrollPane1.setViewportView(xPanel1);
		//System.out.println(xPanel1.getPreferredSize());
		
	}

	private void xPanel1MouseWheelMoved(MouseWheelEvent e) {
		//scrollPane1.updateUI();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - kevin red
		scrollPane1 = new JScrollPane();
		xPanel1 = new JListView();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0};
		((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

		//======== scrollPane1 ========
		{
			scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

			//---- xPanel1 ----
			xPanel1.addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					xPanel1MouseWheelMoved(e);
				}
			});
			scrollPane1.setViewportView(xPanel1);
		}
		contentPane.add(scrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
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
	private JListView xPanel1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
