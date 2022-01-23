package components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JLayeredPane;

/**
 * 
 * @author Brendan
 * @version 0.1 28/06/2010
 */
@SuppressWarnings("serial")
public class ZoomLayeredPane extends JLayeredPane {
	
	private double zoom;
	
	public ZoomLayeredPane(double initialZoom) {
        zoom = initialZoom;
    }
	
	public void paint(Graphics g) {
        super.paintComponent(g); // clears background
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform backup = g2.getTransform();
        g2.scale(zoom, zoom);
        super.paint(g);
        g2.setTransform(backup);
    }
	
	public boolean isOptimizedDrawingEnabled() {
        return false;
    }
	
	public Dimension getPreferredSize() {
        Dimension unzoomed
          //= getLayout().preferredLayoutSize(this);
        	= super.getPreferredSize();
        Dimension zoomed
          = new Dimension((int) ((double) unzoomed.width*zoom),
                          (int) ((double) unzoomed.height*zoom));
        //System.out.println("PreferredSize: Unzoomed "+unzoomed);
       // System.out.println("PreferredSize: Zoomed "+zoomed);
        return zoomed;
    }
	
	public void setZoom(double nZoom) {
		zoom = nZoom;
	}
}
