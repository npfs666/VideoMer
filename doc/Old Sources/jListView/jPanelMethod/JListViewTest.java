package test.jListView.jPanelMethod;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class JListViewTest extends JFrame {
	
	public JListViewTest() {
		
		setBounds(440, 225, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel jp = new JPanel();
		jp.setBackground(Color.white);
		jp.setPreferredSize(new Dimension(1500,1500));


		/*JPanel panelToScroleOver = new JPanel();
		panelToScroleOver.setLayout(new BorderLayout());
		panelToScroleOver.add(jp);*/


		JScrollPane editorScroll = new JScrollPane(jp);
		editorScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editorScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		/*JTabbedPane documents = new JTabbedPane();
		documents.add("doc1", editorScroll);
		documents.setPreferredSize(new Dimension(50, 500));*/

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(editorScroll);

		//setPreferredSize(new Dimension(300, 300));
		setVisible(true);

	}
	
	public static void main(String[] args) {
		JListViewTest moi = new JListViewTest();
		//moi.setVisible(true);
	}

}
