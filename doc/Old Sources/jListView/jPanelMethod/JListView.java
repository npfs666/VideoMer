package test.jListView.jPanelMethod;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.Scrollable;

import org.jdesktop.swingx.JXPanel;

public class JListView extends JXPanel implements Scrollable {
	
	public JListView() {
		super();

	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		// TODO Auto-generated method stub
		//return getLayout().preferredLayoutSize(this);
		return getPreferredSize();
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// TODO Auto-generated method stub
		return 16;
	}

}
