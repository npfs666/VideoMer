package test.jListView.swing.plaf.basic;


import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI.MouseInputHandler;

import test.jListView.swing.DefaultListViewCellRenderer;
import test.jListView.swing.JListView;
import test.jListView.swing.ListViewCellRenderer;
import test.jListView.swing.plaf.ListViewUI;

public class BasicListViewUI extends ListViewUI {

	protected JListView listView = null;
	protected CellRendererPane rendererPane;
	//protected int cellHeight = -1;
	//protected int cellWidth = -1;
	
    // Listeners that this UI attaches to the JList
    protected FocusListener focusListener;
    protected MouseInputListener mouseInputListener;
    protected ListSelectionListener listSelectionListener;
    protected ListDataListener listDataListener;
    protected PropertyChangeListener propertyChangeListener;
    private Handler handler;

	/**
	 * Number of columns to create.
	 */
	private int columnCount;

	/**
	 * Local cache of JList's component orientation property
	 */
	private boolean isLeftToRight = true;






	/**
	 * Paint the rows that intersect the Graphics objects clipRect.  This
	 * method calls paintCell as necessary.  Subclasses
	 * may want to override these methods.
	 *
	 * @see #paintCell
	 */
	public void paint(Graphics g, JComponent c) {
		
		ListModel dataModel = listView.getModel();
		
		
		int leadIndex = adjustIndex(listView.getLeadSelectionIndex(), listView);
		//System.out.println(listView.getLeadSelectionIndex());

		
		g.setColor(Color.WHITE);
		Rectangle paintBounds = g.getClipBounds();
		g.fillRect(paintBounds.x, paintBounds.y, paintBounds.width, paintBounds.height);
		
		
		double nbEltLigne = Math.ceil(paintBounds.width / listView.getFixedCellWidth());
		
		int index = 0, rowCount = 0, colCount = 0;
		
		ListViewCellRenderer cellRenderer = listView.getCellRenderer();
		
		while( index < dataModel.getSize() ) {
			
			if( colCount == nbEltLigne ) {
				colCount = 0;
				rowCount++;
			}
			
			boolean cellHasFocus = listView.hasFocus() && (leadIndex == index);
	        boolean isSelected = listView.getSelectionModel().isSelectedIndex(index);
			
			Component rendererComponent = 
				cellRenderer.getListViewCellRendererComponent(listView, dataModel.getElementAt(index), index, isSelected, cellHasFocus);
			
			rendererPane.paintComponent(g, rendererComponent, listView, 
					colCount*listView.getFixedCellWidth(), 
					rowCount*listView.getFixedCellHeight(), 150, 150, true);
			
			colCount++;
			index++;
		}
	}

	public void installUI(JComponent c) {

		listView = (JListView)c;

		rendererPane = new CellRendererPane();
		listView.add(rendererPane);
		
		installDefaults();
		installListeners();
	}

	/**
	 * Initialize JList properties, e.g. font, foreground, and background,
	 * and add the CellRendererPane.  The font, foreground, and background
	 * properties are only set if their current value is either null
	 * or a UIResource, other properties are set if the current
	 * value is null.
	 *
	 * @see #uninstallDefaults
	 * @see #installUI
	 * @see CellRendererPane
	 */
	protected void installDefaults() {

		listView.setLayout(null);

		LookAndFeel.installBorder(listView, "List.border");

		LookAndFeel.installColorsAndFont(listView, "List.background", "List.foreground", "List.font");

		LookAndFeel.installProperty(listView, "opaque", Boolean.TRUE);

		//TODO: Ajouter à l'UI Manager la clef ListView.cellRenderer || swing.DefaultListViewRenderer
		if (listView.getCellRenderer() == null) {
			listView.setCellRenderer(new DefaultListViewCellRenderer());
			listView.setCellRenderer((ListViewCellRenderer)(UIManager.get("ListView.cellRenderer")));
		}

		// TODO: A mettre en place pour la selection
		Color sbg = listView.getSelectionBackground();
        if (sbg == null) {
        	listView.setSelectionBackground(UIManager.getColor("List.selectionBackground"));
        }

        Color sfg = listView.getSelectionForeground();
        if (sfg == null) {
        	listView.setSelectionForeground(UIManager.getColor("List.selectionForeground"));
        }
	}
	
	/**
     * Create and install the listeners for the JList, its model, and its
     * selectionModel.  This method is called at installUI() time.
     *
     * @see #installUI
     * @see #uninstallListeners
     */
	protected void installListeners() {
		
		//TODO: A remmettre pour installer le DnD
		/*TransferHandler th = listView.getTransferHandler();
		if (th == null || th instanceof UIResource) {
			listView.setTransferHandler(defaultTransferHandler);
			// default TransferHandler doesn't support drop
			// so we don't want drop handling
			if (listView.getDropTarget() instanceof UIResource) {
				listView.setDropTarget(null);
			}
		}*/

		focusListener = createFocusListener();
		mouseInputListener = getHandler();
		propertyChangeListener = getHandler();
		listSelectionListener = getHandler();
		listDataListener = getHandler();

		listView.addFocusListener(focusListener);
		listView.addMouseListener(mouseInputListener);
		listView.addMouseMotionListener(mouseInputListener);
		listView.addPropertyChangeListener(propertyChangeListener);
		//listView.addKeyListener(getHandler());

		ListModel model = listView.getModel();
		if (model != null) {
			model.addListDataListener(listDataListener);
		}

		ListSelectionModel selectionModel = listView.getSelectionModel();
		if (selectionModel != null) {
			selectionModel.addListSelectionListener(listSelectionListener);
		}
	}
	
    /**
     * Creates a delegate that implements MouseInputListener.
     * The delegate is added to the corresponding java.awt.Component listener 
     * lists at installUI() time. Subclasses can override this method to return 
     * a custom MouseInputListener, e.g.
     * <pre>
     * class MyListUI extends BasicListUI {
     *    protected MouseInputListener <b>createMouseInputListener</b>() {
     *        return new MyMouseInputHandler();
     *    }
     *    public class MyMouseInputHandler extends MouseInputHandler {
     *        public void mouseMoved(MouseEvent e) {
     *            // do some extra work when the mouse moves
     *            super.mouseMoved(e);
     *        }
     *    }
     * }
     * </pre>
     *
     * @see MouseInputHandler
     * @see #installUI
     */
    protected FocusListener createFocusListener() {
        return getHandler();
    }

    private Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }
    
	private static int adjustIndex(int index, JListView listView) {
        return index < listView.getModel().getSize() ? index : -1;
    }
	
	/**
	 * Returns a new instance of BasicListUI.  BasicListUI delegates are
	 * allocated one per JList.
	 *
	 * @return A new ListUI implementation for the Windows look and feel.
	 */
	public static ComponentUI createUI(JComponent listView) {
		return new BasicListViewUI();
	}
	
	
	
	
	
	
	private class Handler implements FocusListener, KeyListener,
    								 ListDataListener, ListSelectionListener,
    								 MouseInputListener, PropertyChangeListener {

		
		@Override
		public void focusGained(FocusEvent e) {

            int leadIndex = adjustIndex(listView.getLeadSelectionIndex(), listView);
            
            System.out.println(leadIndex);
            
            if (leadIndex != -1) {
                //Rectangle r = getCellBounds(listView, leadIndex, leadIndex);
            	Rectangle r = new Rectangle(0, 0, 150, 150);
                if (r != null) {
                	listView.repaint(r.x, r.y, r.width, r.height);
                }
            }
		}

		@Override
		public void focusLost(FocusEvent e) {
			
            int leadIndex = adjustIndex(listView.getLeadSelectionIndex(), listView);
            
            System.out.println(leadIndex);
            
            if (leadIndex != -1) {
                //Rectangle r = getCellBounds(listView, leadIndex, leadIndex);
            	Rectangle r = new Rectangle(0, 0, 150, 150);
                if (r != null) {
                	listView.repaint(r.x, r.y, r.width, r.height);
                }
            }
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void contentsChanged(ListDataEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void intervalAdded(ListDataEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			// TODO Auto-generated method stub
			
		}


		
	}
}
