package test.jListView.swing;

import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class JListViewTest extends JFrame {
	
	ListViewCellRenderer cellRenderer;
	
	public JListViewTest() {
		
		setBounds(440, 225, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JListView listView = new JListView(new ListViewModel());
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(listView);
		cellRenderer = new VideoCellRenderer();
		listView.setCellRenderer(cellRenderer);
		//listView.setCellRenderer(new DefaultListViewCellRenderer());
		
		
		//ListCellRenderer sef = (ListCellRenderer)(UIManager.get("List.cellRenderer"));
		//System.out.println(UIManager.get("List.cellRenderer"));
		//UIManager.put("ListView.cellRenderer", new DefaultListViewCellRenderer());
		//UIDefaults aa = new UIDefaults();
		//aa.put("ListView.cellRenderer", new DefaultListViewCellRenderer());
		//System.out.println(UIManager.get("ListView.cellRenderer"));
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.LINE_AXIS));
		contentPane.add(listView);
		JButton b = new JButton("bouton");
		//contentPane.add(b);
		
		//System.out.println(listView.isPreferredSizeSet()+"   "+listView.getPreferredSize());
		listView.setSelectedIndex(2);
	}
	
	public static void main(String[] args) {
		JListViewTest moi = new JListViewTest();
		moi.setVisible(true);
	}

}
