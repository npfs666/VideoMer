package test.jListView.swing;



import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.accessibility.Accessible;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import test.jListView.swing.plaf.ListViewUI;

@SuppressWarnings("serial")
public class JListView extends JComponent implements Scrollable, Accessible {

	/**
	 * @see #getUIClassID
	 * @see #readObject
	 */
	private static final String uiClassID = "ListViewUI";


	private int fixedCellWidth = 150;
	private int fixedCellHeight = 150;
	private Color selectionForeground;
	private Color selectionBackground;
	private ListViewCellRenderer cellRenderer;
	private ListSelectionModel selectionModel;
	private ListModel dataModel;






	public JListView(ListViewModel dataModel)
	{
		if (dataModel == null) {
			throw new IllegalArgumentException("dataModel must be non null");
		}

		// Register with the ToolTipManager so that tooltips from the
		// renderer show through.
		ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
		toolTipManager.registerComponent(this);

		this.dataModel = dataModel;
		selectionModel = createSelectionModel();
		setAutoscrolls(true);
		setOpaque(true);
		updateUI();
	}

	public ListModel getModel() {
		return dataModel;
	}

	public void setModel(ListModel model) {
		dataModel = model;
	}

	/**
	 * 
	 */
	public void updateUI() {

		UIManager.getDefaults().put("ListViewUI", "swing.plaf.basic.BasicListViewUI");

		setUI((ListViewUI)UIManager.getUI(this));

		ListViewCellRenderer renderer = getCellRenderer();
		if (renderer instanceof Component) {
			SwingUtilities.updateComponentTreeUI((Component)renderer);
		}
	}

	/**
	 * 
	 * @return
	 */
	public ListViewUI getUI() {
		return (ListViewUI)ui;
	}

	/**
	 * 
	 * @param ui
	 */
	public void setUI(ListViewUI ui) {
		super.setUI(ui);
	}

	/**
	 * 
	 */
	public String getUIClassID() {
		return "ListViewUI";
	}

	/**
	 * 
	 * @return
	 */
	public ListViewCellRenderer getCellRenderer() {
		return cellRenderer;
	}

	public void setCellRenderer(ListViewCellRenderer cellRenderer) {

		ListViewCellRenderer oldValue = this.cellRenderer;
		this.cellRenderer = cellRenderer;

		/* If the cellRenderer has changed and prototypeCellValue
		 * was set, then recompute fixedCellWidth and fixedCellHeight.
		 */
		/*if ((cellRenderer != null) && !cellRenderer.equals(oldValue)) {
            updateFixedCellSize();
        }*/

		firePropertyChange("cellRenderer", oldValue, cellRenderer);
	}
	
    /**
     * Returns the smallest selected cell index; <i>the selection</i> when only
     * a single item is selected in the list. When multiple items are selected,
     * it is simply the smallest selected index. Returns {@code -1} if there is
     * no selection.
     * <p>
     * This method is a cover that delegates to {@code getMinSelectionIndex}.
     *
     * @return the smallest selected cell index
     * @see #getMinSelectionIndex
     * @see #addListSelectionListener
     */
    public int getSelectedIndex() {
        return selectionModel.getMinSelectionIndex();
    }
	
    /**
     * Returns the lead selection index. This is a cover method that
     * delegates to the method of the same name on the list's selection model.
     *
     * @return the lead selection index
     * @see ListSelectionModel#getLeadSelectionIndex
     * @beaninfo
     * description: The lead selection index.
     */
    public int getLeadSelectionIndex() {
        return getSelectionModel().getLeadSelectionIndex();
    }
    
	/**
	 * Selects a single cell. Does nothing if the given index is greater
	 * than or equal to the model size. This is a convenience method that uses
	 * {@code setSelectionInterval} on the selection model. Refer to the
	 * documentation for the selection model class being used for details on
	 * how values less than {@code 0} are handled.
	 *
	 * @param index the index of the cell to select
	 * @see ListSelectionModel#setSelectionInterval
	 * @see #isSelectedIndex
	 * @see #addListSelectionListener
	 * @beaninfo
	 * description: The index of the selected cell.
	 */
	public void setSelectedIndex(int index) {
		if (index >= getModel().getSize()) {
			return;
		}
		getSelectionModel().setSelectionInterval(index, index);
	}

	/**
	 * Returns an instance of {@code DefaultListSelectionModel}; called
	 * during construction to initialize the list's selection model
	 * property.
	 *
	 * @return a {@code DefaultListSelecitonModel}, used to initialize
	 *         the list's selection model property during construction
	 * @see #setSelectionModel
	 * @see DefaultListSelectionModel
	 */
	protected ListSelectionModel createSelectionModel() {
		return new DefaultListSelectionModel();
	}


	/**
	 * Returns the current selection model. The selection model maintains the
	 * selection state of the list. See the class level documentation for more
	 * details.
	 *
	 * @return the <code>ListSelectionModel</code> that maintains the
	 *         list's selections
	 *
	 * @see #setSelectionModel
	 * @see ListSelectionModel
	 */
	public ListSelectionModel getSelectionModel() {
		return selectionModel;
	}

	/**
	 * @return the fixedCellWidth
	 */
	public int getFixedCellWidth() {
		return fixedCellWidth;
	}

	/**
	 * @param fixedCellWidth the fixedCellWidth to set
	 */
	public void setFixedCellWidth(int fixedCellWidth) {
		this.fixedCellWidth = fixedCellWidth;
	}

	/**
	 * @return the fixedCellHeight
	 */
	public int getFixedCellHeight() {
		return fixedCellHeight;
	}

	/**
	 * @param fixedCellHeight the fixedCellHeight to set
	 */
	public void setFixedCellHeight(int fixedCellHeight) {
		this.fixedCellHeight = fixedCellHeight;
	}

	/**
	 * @return the selectionForeground
	 */
	public Color getSelectionForeground() {
		return selectionForeground;
	}

	/**
	 * @param selectionForeground the selectionForeground to set
	 */
	public void setSelectionForeground(Color selectionForeground) {
		this.selectionForeground = selectionForeground;
	}

	/**
	 * @return the selectionBackground
	 */
	public Color getSelectionBackground() {
		return selectionBackground;
	}

	/**
	 * @param selectionBackground the selectionBackground to set
	 */
	public void setSelectionBackground(Color selectionBackground) {
		this.selectionBackground = selectionBackground;
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// TODO Auto-generated method stub
		return 0;
	}

}
