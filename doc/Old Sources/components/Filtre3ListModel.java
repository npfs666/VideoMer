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
public class Filtre3ListModel extends FiltreListModel {
	
	private ArrayList<String> view;
	//private ArrayList<EntiteVideo> data;
	//private int tmpFilter;

	private int viewFilter;

	/**
	 * 
	 * @param viewFilter
	 */
	public Filtre3ListModel(int viewFilter) {
		this.viewFilter = viewFilter;
		view = new ArrayList<String>();
		//data = new ArrayList<EntiteVideo>();
	}
	
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

				String s = "Saison " + ((Serie)ev).getNoSaison();

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
	
	/**
	 * 
	 * @param index
	 * @param videos
	 */
	/*public void setData(int index, ArrayList<EntiteVideo> videos) {

		data = new ArrayList<EntiteVideo>(videos);

		filter(index, 0, null, null);
	}*/

	/**
	 * 
	 * @param dataFilter
	 * @param filter
	 * @return
	 */
	/*private ArrayList<EntiteVideo> filterData(int dataFilter, Object[] filter) {

		ArrayList<EntiteVideo> data = new ArrayList<EntiteVideo>();

		for( Object o : filter ) {
			
			String filt = (String)o;
			
			switch(dataFilter) {

			case GENRE :
				for( EntiteVideo ev : this.data ) {
					for( String s : ev.getGenre() ) {
						if( s.equalsIgnoreCase(filt) ) {
							data.add(ev);
						}
					}
				}
				break;

			case SERIE :

				for( EntiteVideo ev : this.data ) {

					String s = ((Serie)ev).getNom();
					if( s.equalsIgnoreCase(filt) ) {
						data.add(ev);
					}
				}
				break;


			case REALISATEUR :
				for( EntiteVideo ev : this.data ) {
					for( String s : ev.getRealisateurs() ) {
						if( s.equalsIgnoreCase(filt) ) {
							data.add(ev);
						}
					}
				}
				break;
			}
		}
		
		return data;
	}*/

	/**
	 * 
	 * @param filter1Index
	 * @param dataFilter
	 * @param index
	 * @param filter
	 */
	/*public void filter(int index, int dataFilter, int[] indices, Object[] filter) {

		view = new ArrayList<String>();
		ArrayList<EntiteVideo> tmp;
		int i = 0;

		if( dataFilter > 0 && indices.length > 0 && indices[0] > 0 )
			tmp = filterData(dataFilter, filter);
		else
			tmp = data;

		if(index == EntiteVideo.SERIE)
			tmpFilter = SERIE;
		else 
			tmpFilter = viewFilter;

		switch(tmpFilter) {

		case GENRE :

			for( EntiteVideo ev : tmp ) {
				for( String s : ev.getGenre() ) {
					if( !s.trim().isEmpty() && !view.contains(s) ) {
						view.add(s);
						i++;
					}
				}
			}
			break;

		case SERIE :

			for( EntiteVideo ev : data ) {

				String s = "Saison " + ((Serie)ev).getNoSaison();

				if( !view.contains(s) ) {
					view.add(s);
					i++;
				}
			}			
			break;

		case REALISATEUR :

			for( EntiteVideo ev : tmp ) {
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
	}*/

	/*@Override
	public ArrayList<EntiteVideo> getDataSelection(Object[] indices) {
		
		ArrayList<EntiteVideo> selection = new ArrayList<EntiteVideo>();
		//System.out.println(tmpFilter);
		switch(tmpFilter) {
		
		case GENRE :
			
			for( EntiteVideo ev : data ) {
				for( Object o : indices ) {
					//String s = (String)o;
					//System.out.println(o);
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
				for( Object o : indices ) {
					//String s = (String)o;
					//System.out.println(o);
					if( ev.getRealisateurs().contains(o) && !selection.contains(ev) ) {
						selection.add(ev);
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
