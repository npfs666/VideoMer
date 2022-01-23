package test.jListView.jListMethod;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

public class Model extends AbstractListModel {
	
	private ArrayList<Video> vidz = new ArrayList<Video>();

	public void addElement(Video e) {
		vidz.add(e);
	}
	
	@Override
	public Object getElementAt(int index) {
		// TODO Auto-generated method stub
		return vidz.get(index);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return vidz.size();
	}

}
