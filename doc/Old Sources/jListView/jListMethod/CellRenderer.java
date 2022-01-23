/*
 * Created by JFormDesigner on Mon Jun 14 19:43:43 CEST 2010
 */

package test.jListView.jListMethod;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.text.BreakIterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.decorator.ColorHighlighter;

import sun.swing.DefaultLookup;

/**
 * @author kevin red
 */
public class CellRenderer extends JPanel implements ListCellRenderer {

	/**
	 * An empty <code>Border</code>. This field might not be used. To change the
	 * <code>Border</code> used by this renderer override the 
	 * <code>getListCellRendererComponent</code> method and set the border
	 * of the returned component directly.
	 */
	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
	private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
	protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

	public CellRenderer() {
		initComponents();
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

	private void wrapLabelText(JLabel label, String text) {
		FontMetrics fm = label.getFontMetrics(label.getFont());
		Container container = label.getParent();
		int containerWidth = container.getWidth();

		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);

		StringBuffer trial = new StringBuffer();
		StringBuffer real = new StringBuffer("<html>");

		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE;
		start = end, end = boundary.next()) {
			String word = text.substring(start,end);
			trial.append(word);
			int trialWidth = SwingUtilities.computeStringWidth(fm,
					trial.toString());
			if (trialWidth > containerWidth) {
				trial = new StringBuffer(word);
				real.append("<br>");
			}
			real.append(word);
		}

		real.append("</html>");

		label.setText(real.toString());
	}


	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		setComponentOrientation(list.getComponentOrientation());

		Color bg = null;
		Color fg = null;

		// DnD
		JList.DropLocation dropLocation = list.getDropLocation();
		if (dropLocation != null
				&& !dropLocation.isInsert()
				&& dropLocation.getIndex() == index) {

			bg = DefaultLookup.getColor(this, ui, "List.dropCellBackground");
			fg = DefaultLookup.getColor(this, ui, "List.dropCellForeground");

			isSelected = true;
		}

		// Background foreground
		if (isSelected) {
			setBackground(bg == null ? list.getSelectionBackground() : bg);
			setForeground(fg == null ? list.getSelectionForeground() : fg);
		}
		else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		setEnabled(list.isEnabled());
		setFont(list.getFont());

		// Rollover, Cas particulier
		Point p = list.getMousePosition();
		boolean isMouseOver = false;
		if( p != null ) {
			int i = list.locationToIndex(p);
			if( index == i )isMouseOver = true;
		}

		if( cellHasFocus && isSelected && isMouseOver) {
			JXList jx = (JXList)list;
			ColorHighlighter ch = (ColorHighlighter)jx.getHighlighters()[0];
			setBackground(ch.getBackground());
		}

		// Bordure
		Border border;
		if (cellHasFocus && isSelected ) {
			border = DefaultLookup.getBorder(this, ui, "List.focusCellHighlightBorder");
		} else {
			border = getNoFocusBorder();
		}		
		setBorder(border);

		// Données
		Video v = (Video) value;
		label1.setIcon(new ImageIcon(getClass().getResource(v.img)));
		//label2.setText(v.nom);
		xLabel1.setText(v.nom);
		//wrapLabelText(label2, v.nom);

		return this;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - kevin red
		xPanel1 = new JXPanel();
		label1 = new JLabel();
		xPanel2 = new JXPanel();
		xLabel1 = new JXLabel();

		//======== this ========

		// JFormDesigner evaluation mark
		setBorder(new javax.swing.border.CompoundBorder(
			new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
				"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
				java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0E-4};

		//======== xPanel1 ========
		{
			xPanel1.setLayout(new GridBagLayout());
			((GridBagLayout)xPanel1.getLayout()).columnWidths = new int[] {0, 0};
			((GridBagLayout)xPanel1.getLayout()).rowHeights = new int[] {0, 0};
			((GridBagLayout)xPanel1.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
			((GridBagLayout)xPanel1.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

			//---- label1 ----
			label1.setIcon(new ImageIcon(getClass().getResource("/imgs/mini.jpg")));
			label1.setVerticalAlignment(SwingConstants.TOP);
			xPanel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		add(xPanel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));

		//======== xPanel2 ========
		{
			xPanel2.setLayout(new GridBagLayout());
			((GridBagLayout)xPanel2.getLayout()).columnWidths = new int[] {0, 0};
			((GridBagLayout)xPanel2.getLayout()).rowHeights = new int[] {0, 0};
			((GridBagLayout)xPanel2.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
			((GridBagLayout)xPanel2.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

			//---- xLabel1 ----
			xLabel1.setLineWrap(true);
			xPanel2.add(xLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		add(xPanel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - kevin red
	private JXPanel xPanel1;
	private JLabel label1;
	private JXPanel xPanel2;
	private JXLabel xLabel1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
