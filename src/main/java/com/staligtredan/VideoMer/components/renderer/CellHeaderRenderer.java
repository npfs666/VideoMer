package com.staligtredan.VideoMer.components.renderer;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * <code>CellHeaderRenderer</code> permet de changer l'affichage de l'entête (titre
 * d'une colonne) d'un JTable.
 * 
 * @version 0.78 19/08/2008
 * @author Brendan Jaouen
 * @since 0.1
 */
public class CellHeaderRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		if( value.equals("true") )
			p.add(new JCheckBox("", true));
		else
			p.add(new JCheckBox("", false));
		
		return p;
	}
}