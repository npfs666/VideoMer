package com.staligtredan.VideoMer.components.model;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

/**
 * Modèle de données pour les regex du panneau de préférences.
 * 
 * @author Brendan
 * @version 0.16 18/04/2014
 */
public class RegexListModel<E> extends AbstractListModel<String> {

	private static final long serialVersionUID = -520764352944227421L;

	private ArrayList<String> list;



	public RegexListModel( String[] list ) {

		this.list = new ArrayList<String>();
		for ( String s : list )
			this.list.add(s);
	}



	@Override
	public String getElementAt( int index ) {

		return list.get(index);
	}



	@Override
	public int getSize() {

		return list.size();
	}



	public void setElementAt( int index, String element ) {

		if( index < 0 )
			return;

		list.set(index, element);
	}



	public void addElement( String element ) {

		list.add(element);
	}



	public void deleteElement( int index ) {

		if( index < 0 )
			return;

		list.remove(index);
	}



	public String[] getList() {

		return list.toArray(new String[list.size()]);
	}
}
