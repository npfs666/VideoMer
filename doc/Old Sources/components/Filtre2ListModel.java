package components.model;

import java.util.ArrayList;
import java.util.Collections;

import modele.EntiteVideo;
import modele.Serie;

/**
 * 
 * @author Brendan
 * @version 0.1
 */
@SuppressWarnings("serial")
public class Filtre2ListModel extends FiltreListModel {	
	
	private ArrayList<String> view;
	//private ArrayList<EntiteVideo> data;
	//private int tmpFilter;

	private int viewFilter;
	
	/**
	 * 
	 * @param viewFilter
	 */
	public Filtre2ListModel(int viewFilter) {
		this.viewFilter = viewFilter;
		view = new ArrayList<String>();
		//data = new ArrayList<EntiteVideo>();
	}
	
	/**
	 * 
	 * @param index
	 * @param videos
	 */
	/*public void setData(int index, ArrayList<EntiteVideo> videos) {
		
		data = new ArrayList<EntiteVideo>(videos);

		filter(index);
	}*/
	
	/**
	 * 
	 * @param index
	 */
	public void filter(int index, ArrayList<EntiteVideo> videos) {

		view = new ArrayList<String>();
		int i = 0;
		int serieFilter;
		
		if(index == EntiteVideo.SERIE)
			serieFilter = SERIE;
		else 
			serieFilter = viewFilter;
		
		switch(serieFilter) {
		
			case GENRE :
				
				for( EntiteVideo ev : videos ) {
					for( String s : ev.getGenre() ) {
						if( !s.trim().isEmpty() && !view.contains(s) ) {
							view.add(s);
							i++;
						}
					}
				}
			break;
			
			case SERIE :
				
				for( EntiteVideo ev : videos ) {
					
					String s = ((Serie)ev).getNom();
					
					if( !view.contains(s) ) {
						view.add(s);
						i++;
					}
				}			
			break;
			
			case REALISATEUR :
				
				for( EntiteVideo ev : videos ) {
					for( String s : ev.getRealisateurs() ) {
						if( !s.trim().isEmpty() && !view.contains(s) ) {
							view.add(s);
							i++;
						}
					}
				}
			break;
		}
		
		Collections.sort(view);
		view.add(0, "Tout ("+i+" Eléments)");
	}
	
	/*@Override
	public ArrayList<EntiteVideo> getDataSelection(Object[] indices) {
		
		ArrayList<EntiteVideo> selection = new ArrayList<EntiteVideo>();
		
		switch(tmpFilter) {
		
		case GENRE :
			
			for( EntiteVideo ev : data ) {
				for( Object o : indices ) {
					//String s = (String)o;
					if( ev.getGenre().contains(o) && !selection.contains(ev) ) {
						selection.add(ev);
					}
				}
			}
		break;
		
		case SERIE :
			
			for( EntiteVideo ev : data ) {
				
				String s = ((Serie)ev).getNom();
				
				if( !view.contains(s) ) {
					view.add(s);
					//i++;
				}
			}			
		break;
		
		case REALISATEUR :
			
			for( EntiteVideo ev : data ) {
				for( String s : ev.getRealisateurs() ) {
					if( !s.trim().isEmpty() && !view.contains(s) ) {
						view.add(s);
						//i++;
					}
				}
			}
		break;
	}

		return selection;	
	}*/
	
	@Override
	public Object getElementAt(int index) {
		return view.get(index);
	}
	
	@Override
	public int getSize() {
		return view.size();
	}

	/**
	 * @return the viewFilter
	 */
	public int getViewFilter() {
		return viewFilter;
	}

	/**
	 * @param viewFilter the viewFilter to set
	 */
	public void setViewFilter(int viewFilter) {
		this.viewFilter = viewFilter;
	}
}
