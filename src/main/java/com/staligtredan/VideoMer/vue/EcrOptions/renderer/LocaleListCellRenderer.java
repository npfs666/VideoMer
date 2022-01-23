package com.staligtredan.VideoMer.vue.EcrOptions.renderer;

import java.awt.Color;
import java.awt.Component;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.border.Border;


public class LocaleListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = -7618298427071889593L;
	
	//private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    //private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
        setComponentOrientation(list.getComponentOrientation());

        Color bg = null;
        Color fg = null;

        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            bg = UIManager.getColor("List.dropCellBackground");
            fg = UIManager.getColor("List.dropCellForeground");

            isSelected = true;
        }

        if (isSelected) {
            setBackground(bg == null ? list.getSelectionBackground() : bg);
            setForeground(fg == null ? list.getSelectionForeground() : fg);
        }
        else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (value instanceof Icon) {
            setIcon((Icon)value);
            setText("");
        }
        else {
            setIcon(null);
            
            Locale l = (Locale)value;
            String s = l.getDisplayLanguage(l);
            s = s.replaceFirst(".",(s.charAt(0)+"").toUpperCase());
            setText(s);
        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());

        Border border = null;
        if (cellHasFocus) {
            if (isSelected) {

                border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = UIManager.getBorder("List.focusCellHighlightBorder");
            }
        } else {
        	// Changement en 2.0.4 pour préprarer un deprecated
            //border = getNoFocusBorder();
        	border = UIManager.getBorder("List.cellNoFocusBorder");
        }
        setBorder(border);

        return this;
	}

    /* Enlevé en 2.0.4
     * private Border getNoFocusBorder() {
        Border border = UIManager.getBorder("List.cellNoFocusBorder");
        
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
    }*/
}
