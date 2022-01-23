package test.jListView.swing;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

public class ListViewModel extends AbstractListModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5127181932303830696L;
	
	/**
	 * 
	 */
	private ArrayList<String> elts;
	
	
	
	
	
	public ListViewModel() {
		elts = new ArrayList<String>();
		
		elts.add("Equilibrium");
		elts.add("Le pacte des loups");
		elts.add("Star Wars : Episode 1");
		elts.add("The Constant Gardener");
	}
	
	@Override
	public Object getElementAt(int index) {
		return elts.get(index);
	}

	@Override
	public int getSize() {
		return elts.size();
	}

}
