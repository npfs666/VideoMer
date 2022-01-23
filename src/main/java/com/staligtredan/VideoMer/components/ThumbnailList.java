package com.staligtredan.VideoMer.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ListModel;

import org.jdesktop.swingx.JXList;

/**
 * <code>ThumnailList</code> rajoute le rectangle de selection avec la souris à
 * une JXList
 * 
 * @author Brendan
 * @version 0.1 6/07/2010
 */
public class ThumbnailList extends JXList {

	private static final long serialVersionUID = 2111331185474545244L;
	private final AlphaComposite alcomp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
	private final Path2D polygon = new Path2D.Double();
	private final Color rcolor;
	private final Color pcolor;

	private Point srcPoint = null;



	/**
	 * Crée une instance
	 */
	public ThumbnailList() {

		super();

		rcolor = SystemColor.activeCaption;
		pcolor = makeColor(rcolor);

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		RubberBandingListener rbl = new RubberBandingListener();
		addMouseMotionListener(rbl);
		addMouseListener(rbl);
	}



	@Override
	public void paintComponent( Graphics g ) {

		super.paintComponent(g);
		if( srcPoint == null )
			return;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(rcolor);
		g2d.draw(polygon);
		g2d.setComposite(alcomp);
		g2d.setPaint(pcolor);
		g2d.fill(polygon);
	}



	/**
	 * 
	 * @param c
	 * @return
	 */
	private Color makeColor( Color c ) {

		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		return (r > g) ? (r > b) ? new Color(r, 0, 0) : new Color(0, 0, b)
				: (g > b) ? new Color(0, g, 0) : new Color(0, 0, b);
	}

	/**
	 * Listener de souris pour d�ssier le rectangle
	 * 
	 * @author Brendan
	 * @version 0.1 6/07/2010
	 */
	class RubberBandingListener extends MouseAdapter {

		@Override
		public void mouseDragged( MouseEvent e ) {

			if( srcPoint == null )
				return;

			Point destPoint = e.getPoint();
			polygon.reset();
			polygon.moveTo(srcPoint.x, srcPoint.y);
			polygon.lineTo(destPoint.x, srcPoint.y);
			polygon.lineTo(destPoint.x, destPoint.y);
			polygon.lineTo(srcPoint.x, destPoint.y);
			polygon.closePath();
			setSelectedIndices(getIntersectsIcons(polygon));
			repaint();
		}



		@Override
		public void mouseReleased( MouseEvent e ) {

			srcPoint = null;
			repaint();
		}



		@Override
		public void mousePressed( MouseEvent e ) {

			if( e.getButton() == MouseEvent.BUTTON3 )
				srcPoint = e.getPoint();
		}



		/**
		 * 
		 * @param p
		 * @return
		 */
		private int[] getIntersectsIcons( Shape p ) {

			ListModel<?> model = getModel();
			Vector<Integer> list = new Vector<Integer>(model.getSize());
			for ( int i = 0; i < model.getSize(); i++ ) {
				Rectangle r = getCellBounds(i, i);
				if( p.intersects(r) ) {
					list.add(i);
				}
			}
			int[] il = new int[list.size()];
			for ( int i = 0; i < list.size(); i++ ) {
				il[i] = list.get(i);
			}
			return il;
		}
	}
}
