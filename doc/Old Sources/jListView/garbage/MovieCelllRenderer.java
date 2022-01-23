/*
 * Created by JFormDesigner on Tue Jun 01 12:56:31 CEST 2010
 */

package test.jListView.garbage;

import java.awt.Component;

import com.zfqjava.swing.cell.DefaultCellRenderer;

/**
 * @author kevin red
 */
public class MovieCelllRenderer extends DefaultCellRenderer {
	
	@Override
	public Component getComponent() {
		
		//System.out.println(iconLabel.getIcon());
		
		
		return new VideoCellRenderer(iconLabel, textLabel);
	}
}
