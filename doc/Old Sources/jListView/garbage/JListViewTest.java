package test.jListView.garbage;


import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import com.zfqjava.swing.JListView;
import com.zfqjava.swing.cell.FileCellRenderer;

public class JListViewTest extends JFrame {
	
	private JListView listView;
	
	
	
	
	
	public JListViewTest() {
		
		setBounds(440, 225, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//FileTableModel ftm = new FileTableModel(new File(System.getProperty("user.home")));
		VideoTableModel vtm = new VideoTableModel();
		
		//System.out.println(ftm.getColumnCount());
		//System.out.println(ftm.getRowCount());
		listView = new JListView(vtm);
		
		//listView.setViewMode(JListView.THUMBNAILS_VIEW_MODE);
		//System.out.println(listView.getCellPreferredSize(JListView.LARGE_ICON_VIEW_MODE));
		listView.setCellPreferredSize(JListView.LARGE_ICON_VIEW_MODE, new Dimension(150,100));
		
		FileCellRenderer fcr= new FileCellRenderer();
		MovieCelllRenderer mcr = new MovieCelllRenderer();
		
		listView.setCellRenderer(mcr);
		
		
		
		//listView.set
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.LINE_AXIS));
		contentPane.add(listView);
	}
	
	public static void main(String[] args) {
		JListViewTest moi = new JListViewTest();
		moi.setVisible(true);
	}

}
