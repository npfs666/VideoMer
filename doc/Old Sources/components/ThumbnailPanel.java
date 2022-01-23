/*
 * Created by JFormDesigner on Wed Jul 07 11:54:57 CEST 2010
 */

package components.renderer;

import handler.listeners.ThumbnailHandler;
import handler.transfert.MediaListTransfertHandler;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.UIManager;

import modele.EntiteVideo;

import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

import components.FindBar;
import components.ThumbnailList;
import components.model.ThumbnailListModel;

import controleur.DefaultController;

/**
 * <code>ThumbnailPanel</code> permet d'afficher une JXList et une
 * <code>FindBar</code> avec timer pour faire des recherches
 * à la Linux
 * 
 * @author Brendan Jaouen
 * @version 0.1 1/10/2010
 */
@SuppressWarnings("serial")
public class ThumbnailPanel extends JPanel {

	//private SwingWorker worker;
	private ThumbnailListModel model;
	private DefaultController controller;
	

	public ThumbnailPanel(DefaultController controller) {
		
		this.controller = controller;
		initComponents();

		scrollPaneThumbnailList.getVerticalScrollBar().setUnitIncrement(20);
		scrollPaneThumbnailList.getHorizontalScrollBar().setUnitIncrement(20);

		xListMedia.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, 
				UIManager.getColor("ToggleButton.light"), null));

		xListMedia.setCellRenderer(new ThumbnailCellRenderer());
		xListMedia.setVisibleRowCount(0);
		xListMedia.setFixedCellHeight(150);
		xListMedia.setFixedCellWidth(120);
		xListMedia.setTransferHandler(new MediaListTransfertHandler());

		model = new ThumbnailListModel();
		xListMedia.setModel(model);
		
	/*	Searchable s = new ThumbnailListSearchable(xListMedia);
		xListMedia.setSearchable(s);

		xFindBar.setSearchable(s); 
		xFindBar.addNotify();
		xFindBar.setIcon();
		xFindBar.addButtonListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refreshFindBar();
			}
		});
		xFindBar.addTextFieldListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				refreshFindBar();
			}
		});*/
	}

	/*public void setModel(ListModel<?> model) {
		xListMedia.setModel(model);
	}
	*/
	public ListModel<?> getModel() {
		return xListMedia.getModel();
	}
	
	public void resetSelection() {
		xListMedia.clearSelection();
	}
	
	public void addThumbnailListListener(ThumbnailHandler handler) {
		xListMedia.addMouseListener(handler);
		xListMedia.addListSelectionListener(handler);
		xListMedia.addKeyListener(handler);
	}

	private void thisComponentResized(ComponentEvent e) {
		Rectangle r = getBounds();
		//System.out.println(r);
		scrollPaneThumbnailList.setBounds(0,0,r.width, r.height);
		//xFindBar.setBounds(r.width-xFindBar.getPreferredSize().width-20, 
		//		r.height-xFindBar.getPreferredSize().height-5, 
		//		xFindBar.getPreferredSize().width, xFindBar.getPreferredSize().height);
		xFindBar.setBounds(r.width-xFindBar.getPreferredSize().width-20, 5, 
				xFindBar.getPreferredSize().width, xFindBar.getPreferredSize().height);
	}

	private void xListMediaKeyPressed(KeyEvent e) {

		/*if( e.getKeyCode() == KeyEvent.VK_ENTER ) return;
		
		xFindBar.setText(xFindBar.getText()+e.getKeyChar());
		if( xFindBar.getText().length() > 1 ) {
			xFindBar.requestFocus();
			layeredPane.setLayer(xFindBar, JLayeredPane.MODAL_LAYER);
			timer();
		}*/
	}

	/*private void refreshFindBar() {
		if( worker != null ) {
			worker.interrupt();
			timer();
		}
	}*/

	/** 
	 * Crée et lance le timer
	 */
	/*private void timer() {

		worker = new SwingWorker() {

			@Override
			public Object construct() {

				try {

					if (Thread.interrupted())
						throw new InterruptedException();

					Thread.sleep(5000);
				}
				catch (InterruptedException e) {
					return "Interrupted";  // SwingWorker.get() returns this
				}
				return "All Done";         // or this
			}

			@Override
			public void finished() {

				if( !get().equals("Interrupted") ) {
					xFindBar.setText("");
					layeredPane.setLayer(xFindBar, JLayeredPane.DEFAULT_LAYER);
					xListMedia.grabFocus();
				}

			}
		};
		worker.start();
	}*/

	private void menuItemEditActionPerformed(ActionEvent e) {
		
		for( Object o : xListMedia.getSelectedValues() ) {
			
			EntiteVideo ev = (EntiteVideo)o;
			
			System.out.println(ev.getNom());
		}
	}

	private void menuItemDeleteActionPerformed(ActionEvent e) {
		
		for( Object o : xListMedia.getSelectedValues() ) {
			
			EntiteVideo ev = (EntiteVideo)o;
			
			// On delete de la biblio & de la thumbnailList (effacage local pour éviter une màj)
			model.remove(ev);
			controller.getBibliotheque().remove(ev);
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Jean Mouloud
		ResourceBundle bundle = ResourceBundle.getBundle("lang.lang");
		layeredPane = new JLayeredPane();
		scrollPaneThumbnailList = new JScrollPane();
		xListMedia = new ThumbnailList();
		xFindBar = new FindBar();
		popupMenuRightClic = new JPopupMenu();
		menuItemEdit = new JMenuItem();
		menuItemDelete = new JMenuItem();

		//======== this ========
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				thisComponentResized(e);
			}
		});

		setLayout(new BorderLayout());

		//======== layeredPane ========
		{

			//======== scrollPaneThumbnailList ========
			{
				scrollPaneThumbnailList.setBorder(BorderFactory.createEmptyBorder());

				//---- xListMedia ----
				xListMedia.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				xListMedia.setVisibleRowCount(4);
				xListMedia.setDragEnabled(true);
				xListMedia.setRolloverEnabled(true);
				xListMedia.setComponentPopupMenu(popupMenuRightClic);
				xListMedia.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						xListMediaKeyPressed(e);
					}
				});
				scrollPaneThumbnailList.setViewportView(xListMedia);
			}
			layeredPane.add(scrollPaneThumbnailList, JLayeredPane.PALETTE_LAYER);
			scrollPaneThumbnailList.setBounds(0, 0, 715, 415);

			//---- xFindBar ----
			xFindBar.setBorder(BorderFactory.createEmptyBorder());
			layeredPane.add(xFindBar, JLayeredPane.DEFAULT_LAYER);
			xFindBar.setBounds(300, 365, 406, 45);
		}
		add(layeredPane, BorderLayout.CENTER);

		//======== popupMenuRightClic ========
		{

			//---- menuItemEdit ----
			menuItemEdit.setText(bundle.getString("ThumbnailPanel.menuItemEdit.text"));
			menuItemEdit.setIcon(new ImageIcon(getClass().getResource("/imgs/16x16/edit.png")));
			menuItemEdit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					menuItemEditActionPerformed(e);
				}
			});
			popupMenuRightClic.add(menuItemEdit);

			//---- menuItemDelete ----
			menuItemDelete.setText(bundle.getString("ThumbnailPanel.menuItemDelete.text"));
			menuItemDelete.setIcon(new ImageIcon(getClass().getResource("/imgs/16x16/delete.png")));
			menuItemDelete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					menuItemDeleteActionPerformed(e);
				}
			});
			popupMenuRightClic.add(menuItemDelete);
		}
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Jean Mouloud
	private JLayeredPane layeredPane;
	private JScrollPane scrollPaneThumbnailList;
	private ThumbnailList xListMedia;
	private FindBar xFindBar;
	private JPopupMenu popupMenuRightClic;
	private JMenuItem menuItemEdit;
	private JMenuItem menuItemDelete;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
