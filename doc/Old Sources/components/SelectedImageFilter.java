package components;

import java.awt.image.RGBImageFilter;

/**
 * <code>SelectedImageFilter</code> crée un filtre sur une image
 * pour en changer l'apparence
 * 
 * @author Brendan
 * @version 0.1 6/07/2010
 */
public class SelectedImageFilter extends RGBImageFilter {
	
	public SelectedImageFilter() {
		canFilterIndexColorModel = true;
	}
	
	@Override 
	public int filterRGB(int x, int y, int argb) {
		return (argb & 0xffffff00) | ((argb & 0xff) >> 1);
	}
}