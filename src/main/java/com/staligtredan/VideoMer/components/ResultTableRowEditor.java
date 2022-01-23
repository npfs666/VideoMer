package com.staligtredan.VideoMer.components;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.TreeMap;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import org.jdesktop.swingx.JXTreeTable;

/**
 * <code>ResultTableRowEditor</code> permet de mettre en place un éditeur de
 * cellules de tableau cellule/cellule et non pas par colonne
 * 
 * @author Brendan Jaouen
 * @version 0.1 04/09/2010
 * 
 * @see TableCellEditor
 * @see DefaultCellEditor
 */
public class ResultTableRowEditor implements TableCellEditor {

	private TreeMap<ResultMutableTreeNode, Object> editors;
	private TableCellEditor editor, defaultEditor;
	private JXTreeTable table;



	/**
	 * Constructs a EachRowEditor. create default editor
	 */
	public ResultTableRowEditor( JXTreeTable table ) {

		this.table = table;
		editors = new TreeMap<ResultMutableTreeNode, Object>();
		defaultEditor = new DefaultCellEditor(new JTextField());
	}



	/**
	 * @param row
	 *            table row
	 * @param editor
	 *            table cell editor
	 */
	public void setEditorAt( ResultMutableTreeNode node, TableCellEditor editor ) {

		editors.put(node, editor);
	}



	/**
	 * Renvoie l'editeur d'une cellule
	 * 
	 * @param row
	 *            la ligne de la cellule
	 * @return l'editeur
	 */
	public Object getEditorAt( ResultMutableTreeNode node ) {

		return editors.get(node);
	}



	@Override
	public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row,
			int column ) {

		return editor.getTableCellEditorComponent(table, value, isSelected, row, column);
	}



	@Override
	public Object getCellEditorValue() {

		return editor.getCellEditorValue();
	}



	@Override
	public boolean stopCellEditing() {

		return editor.stopCellEditing();
	}



	@Override
	public void cancelCellEditing() {

		editor.cancelCellEditing();
	}



	@Override
	public boolean isCellEditable( EventObject anEvent ) {

		selectEditor((MouseEvent) anEvent);
		return editor.isCellEditable(anEvent);
	}



	@Override
	public void addCellEditorListener( CellEditorListener l ) {

		editor.addCellEditorListener(l);
	}



	@Override
	public void removeCellEditorListener( CellEditorListener l ) {

		editor.removeCellEditorListener(l);
	}



	@Override
	public boolean shouldSelectCell( EventObject anEvent ) {

		selectEditor((MouseEvent) anEvent);
		return editor.shouldSelectCell(anEvent);
	}



	private void selectEditor( MouseEvent e ) {

		ResultMutableTreeNode node;

		node = new ResultMutableTreeNode(table.getValueAt(table.rowAtPoint(e.getPoint()), 0));

		editor = (TableCellEditor) editors.get(node);

		if( editor == null )
			editor = defaultEditor;
	}
}
