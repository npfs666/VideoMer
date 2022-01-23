package components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.SystemColor;

import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicGraphicsUtils;

@SuppressWarnings("serial")
public class DotBorder extends EmptyBorder {
	
	public boolean isBorderOpaque() {
		return true;
	}
	
	public DotBorder(Insets borderInsets) {
		super(borderInsets);
	}
	
	public DotBorder(int top, int left, int bottom, int right) {
		super(top, left, bottom, right);
	}
	
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		Graphics2D g2 = (Graphics2D)g;
		g2.translate(x,y);
		g2.setPaint(new Color(~SystemColor.activeCaption.getRGB()));
		BasicGraphicsUtils.drawDashedRect(g2, 0, 0, w, h);
		g2.translate(-x,-y);
	}
}
