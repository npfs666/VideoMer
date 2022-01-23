/*
 * $Id: ListViewDemo.java,v 1.11 2005/11/17 04:12:32 zfq Exp $
 *
 * Copyright (C) 2001-2003 Extreme Component, Inc. All rights reserved.
 * Use is subject to license terms. 
 */
package test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableModel;

import com.zfqjava.swing.JBean;
import com.zfqjava.swing.JListView;
import com.zfqjava.swing.cell.FileCellEditor;
import com.zfqjava.swing.cell.FileCellRenderer;
import com.zfqjava.swing.model.FileTableModel;
/**
 * JListView Demo
 *
 * @author $Author: zfq $
 * @version $Revision: 1.11 $ $Date: 2005/11/17 04:12:32 $
 */
@SuppressWarnings("serial")
public class ListViewDemo extends JPanel {

	private JListView listView;

	public ListViewDemo() {
		
		//UIManager.put("FileView.directoryIcon", new ImageIcon(getClass().getResource("/imgs/16x16/folder_movie.png")));
		//UIManager.put("FileView.fileIcon", new ImageIcon(getClass().getResource("/imgs/16x16/movie.png")));
		setLayout(new BorderLayout());
		listView = createListView();
		add(listView, BorderLayout.CENTER);
		add(createEastPanel(), BorderLayout.EAST);
	}

	private JListView createListView() {
		
		final JListView listView = new JListView(createTableModel());
		listView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listView.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Runnable runner = new Runnable() {
					public void run() {
						Object o = listView.getSelectedValue();
						if(o instanceof File) {
							File f = (File)o;
							if(f.isDirectory()) {
								listView.setListData(new FileTableModel(f));
								listView.setCursor(Cursor.getDefaultCursor());
								return;
							} 
						} 
						listView.setCursor(Cursor.getDefaultCursor());
					}
				};
				SwingUtilities.invokeLater(runner);		    
				System.out.println("action fires: " + e.getActionCommand());
			}
		});
		listView.setCellRenderer(new FileCellRenderer());
		listView.setCellEditor(new FileCellEditor());
		return listView;
	}

	private TableModel createTableModel() {
		return new FileTableModel(new File(System.getProperty("user.home")));
	}

	private JPanel createEastPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(createPropertyPanel(), BorderLayout.NORTH);
		return panel;
	}

	private JPanel createPropertyPanel() {
		JPanel panel = new JPanel();

		Border titledBorder = BorderFactory.createTitledBorder("Change Property");
		Border emptyBorder = BorderFactory.createEmptyBorder(12, 12, 12, 12);
		panel.setBorder(BorderFactory.createCompoundBorder(titledBorder, emptyBorder));
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		panel.setLayout(gridbag);

		c.weightx = 0.5;
		c.weighty = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0, 0, 5, 0);

		JLabel label = new JLabel("View Mode: ");
		addPanel(panel, c, label);

		Object[] viewModes = { "Small Icon", "Large Icon", "List", "Thumbnails", "Details" };
		final JComboBox viewModeCB = new JComboBox(viewModes);
		viewModeCB.setSelectedIndex(1); // default view mode
		viewModeCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String mode = (String)viewModeCB.getSelectedItem();
				if(mode.equals("Small Icon")) {
					listView.setViewMode(JListView.SMALL_ICON_VIEW_MODE);
				} else if(mode.equals("Large Icon")) {
					listView.setViewMode(JListView.LARGE_ICON_VIEW_MODE);
				} else if(mode.equals("List")) {
					listView.setViewMode(JListView.LIST_VIEW_MODE);
				} else if(mode.equals("Thumbnails")) {
					listView.setViewMode(JListView.THUMBNAILS_VIEW_MODE);
				} else if(mode.equals("Details")) {
					listView.setViewMode(JListView.DETAILS_VIEW_MODE);
				}
			}
		});
		label.setLabelFor(viewModeCB);
		c.gridwidth = GridBagConstraints.REMAINDER;
		addPanel(panel, c, viewModeCB);

		JButton b = new JButton("Choose Directory");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retValue = fc.showOpenDialog(null);
				if(retValue == JFileChooser.APPROVE_OPTION) {
					TableModel model = new FileTableModel(fc.getSelectedFile());
					listView.setListData(model);
				}
			}
		});
		addPanel(panel, c, b);
		c.gridwidth = GridBagConstraints.REMAINDER;

		b = new JButton("Choose Background Image");
		b.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int retValue = fc.showOpenDialog(null);
				if(retValue == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					if(file != null) {
						try {
							Image img = new ImageIcon(file.toURL()).getImage();
							listView.putClientProperty("JListView.backgroundImage", img);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, ex);
						}
					}
				}
			}
		});
		addPanel(panel, c, b);
		c.gridwidth = GridBagConstraints.REMAINDER;

		b = new JButton("Show Desktop");
		b.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				File desktop = FileSystemView.getFileSystemView().getHomeDirectory();
				JListView listView = createListView();
				listView.putClientProperty("JListView.backgroundImage", ListViewDemo.this.listView.getClientProperty("JListView.backgroundImage"));
				// listView.setViewMode(ListViewDemo.this.listView.getViewMode());
				listView.setViewMode(JListView.LARGE_ICON_VIEW_MODE);
				listView.putClientProperty("JListView.layoutOrientation", new Integer(1));
				listView.setListData(new FileTableModel(desktop));
				listView.setBackground(SystemColor.desktop);
				listView.setForeground(SystemColor.textText);
				listView.setBorder(new EmptyBorder(0, 0, 0, 0));
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				JWindow w = new JWindow();
				w.getContentPane().add(listView);
				w.pack();
				w.setSize(d);
				w.show();
			}
		});
		addPanel(panel, c, b);		
		return panel;
	}

	private static void addPanel(Container container, GridBagConstraints c, Component component) {
		GridBagLayout gridbag = (GridBagLayout)container.getLayout();
		gridbag.setConstraints(component, c);
		container.add(component);
	}

	public static void main(String[] args) {
		DemoPanel demoPanel = new DemoPanel();
		demoPanel.addDemo(new ListViewDemo());
		demoPanel.setTitle("JListView Demo");
		demoPanel.setDefaultCloseOperation(JBean.EXIT_ON_CLOSE);
		demoPanel.showFrame();
	}
}
