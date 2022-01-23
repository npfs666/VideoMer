package test.jListView.garbage;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class VideoTableModel extends AbstractTableModel {
	
	private ArrayList<String> elts;
	
	public VideoTableModel() {
		elts = new ArrayList<String>();
		
		elts.add("Equilibrium");
		elts.add("Le pacte des loups");
		elts.add("Star Wars : Episode 1");
		elts.add("The Constant Gardener");
		
		//Collections.sort(elts);
	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		//System.out.println(elts.size());
		return elts.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		System.out.println(rowIndex+ " "+columnIndex);
		return elts.get(rowIndex);
	}

}
