/*
 * Created by JFormDesigner on Wed Jun 02 22:36:40 CEST 2010
 */

package test.jListView.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import sun.swing.DefaultLookup;

/**
 * @author kevin red
 */
@SuppressWarnings("serial")
public class VideoCellRenderer extends JPanel implements ListViewCellRenderer {

	/**
	 * An empty <code>Border</code>. This field might not be used. To change the
	 * <code>Border</code> used by this renderer override the 
	 * <code>getListCellRendererComponent</code> method and set the border
	 * of the returned component directly.
	 */
	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
	private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
	protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

	public VideoCellRenderer() {
		super();
		initComponents();

		setOpaque(true);

		setName("ListView.cellRenderer");
	}

	@Override
	public Component getListViewCellRendererComponent(JListView listView,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		
        Color bg = null;
        Color fg = null;

		if (isSelected) {
			setBackground(bg == null ? listView.getSelectionBackground() : bg);
			setForeground(fg == null ? listView.getSelectionForeground() : fg);
		}
		else {
			setBackground(listView.getBackground());
			setForeground(listView.getForeground());
		}

		Border border = null;
		if (cellHasFocus) {
			if (isSelected) {
				border = DefaultLookup.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
			}
			if (border == null) {
				border = DefaultLookup.getBorder(this, ui, "List.focusCellHighlightBorder");
			}
		} else {
			border = getNoFocusBorder();
		}
		setBorder(border);
		
		
		
		String s = (String)value;

		label2.setText(s);
		
		return this;
	}

	private Border getNoFocusBorder() {
		Border border = DefaultLookup.getBorder(this, ui, "List.cellNoFocusBorder");
		if (System.getSecurityManager() != null) {
			if (border != null) return border;
			return SAFE_NO_FOCUS_BORDER;
		} else {
			if (border != null &&
					(noFocusBorder == null ||
							noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
				return border;
			}
			return noFocusBorder;
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - kevin red
		label1 = new JLabel();
		label2 = new JLabel();

		//======== this ========

		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0E-4};

		//---- label1 ----
		label1.setIcon(new ImageIcon(getClass().getResource("/imgs/mini.jpg")));
		add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- label2 ----
		label2.setText("Nom du fichier");
		add(label2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
			new Insets(0, 0, 0, 0), 0, 0));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}
	
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - kevin red
	private JLabel label1;
	private JLabel label2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
