package test.rubberBand;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//http://terai.xrea.jp/Swing/RubberBanding.html
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.net.URL;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicGraphicsUtils;

@SuppressWarnings("serial")
public class MainPanel extends JPanel{
	
	public MainPanel() {
		super(new BorderLayout());
		DefaultListModel model = new DefaultListModel();
		model.addElement(new ListItem("ADFFDF asd", "wi0054-32.png"));
		model.addElement(new ListItem("test",       "wi0062-32.png"));
		model.addElement(new ListItem("adfasdf",    "wi0063-32.png"));
		model.addElement(new ListItem("Test",       "wi0064-32.png"));
		model.addElement(new ListItem("12345",      "wi0096-32.png"));
		model.addElement(new ListItem("111111",     "wi0054-32.png"));
		model.addElement(new ListItem("22222",      "wi0062-32.png"));
		model.addElement(new ListItem("3333",       "wi0063-32.png"));
		add(new JScrollPane(new MyList2(model)));
		setPreferredSize(new Dimension(320, 200));
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override public void run() {
				createAndShowGUI();
			}
		});
	}
	public static void createAndShowGUI() {
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame("RubberBanding");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MainPanel());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
class ListItem{
	public final ImageIcon nicon;
	public final ImageIcon sicon;
	public final String title;
	public ListItem(String title, String iconfile) {
		URL url = getClass().getResource(iconfile);
		this.nicon = new ImageIcon(url);
		FilteredImageSource fis = new FilteredImageSource(nicon.getImage().getSource(), new SelectedImageFilter());
		JPanel p = new JPanel();
		this.sicon = new ImageIcon(p.createImage(fis));
		this.title = title;
	}
}
class SelectedImageFilter extends RGBImageFilter {
	public SelectedImageFilter() {
		canFilterIndexColorModel = true;
	}
	@Override public int filterRGB(int x, int y, int argb) {
		//Color color = new Color(argb, true);
		//float[] array = new float[4];
		//color.getComponents(array);
		//return new Color(array[0], array[1], array[2]*0.5f, array[3]).getRGB();
		return (argb & 0xffffff00) | ((argb & 0xff) >> 1);
	}
}
@SuppressWarnings("serial")
class DotBorder extends EmptyBorder {
	public boolean isBorderOpaque() {return true;}
	public DotBorder(Insets borderInsets) {
		super(borderInsets);
	}
	public DotBorder(int top, int left, int bottom, int right) {
		super(top, left, bottom, right);
	}
	@Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		Graphics2D g2 = (Graphics2D)g;
		g2.translate(x,y);
		g2.setPaint(new Color(~SystemColor.activeCaption.getRGB()));
		//new Color(200,150,150));
		//g2.setStroke(dashed);
		//g2.drawRect(0, 0, w-1, h-1);
		BasicGraphicsUtils.drawDashedRect(g2, 0, 0, w, h);
		g2.translate(-x,-y);
	}
	//public Insets getBorderInsets()
	//public Insets getBorderInsets(Component c)
	//public Insets getBorderInsets(Component c, Insets insets)
}
// class MyList extends JList {
//     private final JPanel p = new JPanel(new BorderLayout());
//     private final JLabel icon  = new JLabel((Icon)null, JLabel.CENTER);
//     private final JLabel label = new JLabel("", JLabel.CENTER);
//     private final Border dotBorder = new DotBorder(2,2,2,2);
//     private final Border empBorder = BorderFactory.createEmptyBorder(2,2,2,2);
//     private final Color rcolor;
//     private final Color pcolor;
//     private final AlphaComposite alcomp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
//     private final Polygon polygon = new Polygon();
//     private final Line2D line = new Line2D.Double();
//     private Point srcPoint = null;
//     public MyList(ListModel model) {
//         super(model);
//         icon.setOpaque(false);
//         label.setOpaque(true);
//         label.setForeground(getForeground());
//         label.setBackground(getBackground());
//         label.setBorder(empBorder);
//         p.setOpaque(false);
//         p.setBorder(empBorder);
//         p.add(icon);
//         p.add(label, BorderLayout.SOUTH);
//         rcolor = SystemColor.activeCaption;
//         pcolor = makeColor(rcolor);
//         setLayoutOrientation(JList.HORIZONTAL_WRAP);
//         setVisibleRowCount(0);
//         setFixedCellWidth(62);
//         setFixedCellHeight(62);
//         setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
//         setCellRenderer(new ListCellRenderer() {
//             @Override public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//                 ListItem item = (ListItem)getModel().getElementAt(index);
//                 icon.setIcon(isSelected?item.sicon:item.nicon);
//                 label.setText(item.title);
//                 label.setBorder(cellHasFocus?dotBorder:empBorder);
//                 if(isSelected) {
//                     label.setForeground(list.getSelectionForeground());
//                     label.setBackground(list.getSelectionBackground());
//                 }else{
//                     label.setForeground(list.getForeground());
//                     label.setBackground(list.getBackground());
//                 }
//                 return p;
//             }
//         });
//         RubberBandingListener rbl = new RubberBandingListener();
//         addMouseMotionListener(rbl);
//         addMouseListener(rbl);
//     }
//     class RubberBandingListener extends MouseInputAdapter {
//         @Override public void mouseDragged(MouseEvent e) {
//             setFocusable(true);
//             if(srcPoint==null) srcPoint = e.getPoint();
//             Point destPoint = e.getPoint();
//             polygon.reset();
//             polygon.addPoint(srcPoint.x,  srcPoint.y);
//             polygon.addPoint(destPoint.x, srcPoint.y);
//             polygon.addPoint(destPoint.x, destPoint.y);
//             polygon.addPoint(srcPoint.x,  destPoint.y);
//             //setSelectedIndices(getIntersectsIcons(polygon));
//             if(srcPoint.getX()==destPoint.getX() || srcPoint.getY()==destPoint.getY()) {
//                 line.setLine(srcPoint.getX(),srcPoint.getY(),destPoint.getX(),destPoint.getY());
//                 setSelectedIndices(getIntersectsIcons(line));
//             }else{
//                 setSelectedIndices(getIntersectsIcons(polygon));
//             }
//             repaint();
//         }
//         @Override public void mouseReleased(MouseEvent e) {
//             setFocusable(true);
//             srcPoint = null;
//             repaint();
//         }
//         @Override public void mousePressed(MouseEvent e) {
//             int index = locationToIndex(e.getPoint());
//             Rectangle rect = getCellBounds(index,index);
//             if(!rect.contains(e.getPoint())) {
//                 getSelectionModel().setLeadSelectionIndex(getModel().getSize());
//                 clearSelection();
//                 setFocusable(false);
//             }else{
//                 setFocusable(true);
//             }
//         }
//         private int[] getIntersectsIcons(Shape p) {
//             ListModel model = getModel();
//             Vector<Integer> list = new Vector<Integer>(model.getSize());
//             for(int i=0;i<model.getSize();i++) {
//                 Rectangle r = getCellBounds(i,i);
//                 if(p.intersects(r)) {
//                     list.add(i);
//                 }
//             }
//             int[] il = new int[list.size()];
//             for(int i=0;i<list.size();i++) {
//                 il[i] = list.get(i);
//             }
//             return il;
//         }
//     }
//     @Override public void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         if(srcPoint==null) return;
//         Graphics2D g2d = (Graphics2D) g;
//         g2d.setPaint(rcolor);
//         g2d.drawPolygon(polygon);
//         g2d.setComposite(alcomp);
//         g2d.setPaint(pcolor);
//         g2d.fillPolygon(polygon);
//     }
//     private Color makeColor(Color c) {
//         int r = c.getRed();
//         int g = c.getGreen();
//         int b = c.getBlue();
//         return (r>g)
//           ?(r>b)?new Color(r,0,0):new Color(0,0,b)
//           :(g>b)?new Color(0,g,0):new Color(0,0,b);
//     }
// }
@SuppressWarnings("serial")
class MyList2 extends JList {
	private final JPanel p = new JPanel(new BorderLayout());
	private final JLabel icon  = new JLabel((Icon)null, JLabel.CENTER);
	private final JLabel label = new JLabel("", JLabel.CENTER);
	private final Border dotBorder = new DotBorder(2,2,2,2);
	private final Border empBorder = BorderFactory.createEmptyBorder(2,2,2,2);
	private final Color rcolor;
	private final Color pcolor;
	private final AlphaComposite alcomp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
	private final Path2D polygon = new Path2D.Double();
	private Point srcPoint = null;
	public MyList2(ListModel model) {
		super(model);
		icon.setOpaque(false);
		label.setOpaque(true);
		label.setForeground(getForeground());
		label.setBackground(getBackground());
		label.setBorder(empBorder);
		p.setOpaque(false);
		p.setBorder(empBorder);
		p.add(icon);
		p.add(label, BorderLayout.SOUTH);
		rcolor = SystemColor.activeCaption;
		pcolor = makeColor(rcolor);
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(0);
		setFixedCellWidth(62);
		setFixedCellHeight(62);
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		setCellRenderer(new ListCellRenderer() {
			@Override public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				ListItem item = (ListItem)getModel().getElementAt(index);
				icon.setIcon(isSelected?item.sicon:item.nicon);
				label.setText(item.title);
				label.setBorder(cellHasFocus?dotBorder:empBorder);
				if(isSelected) {
					label.setForeground(list.getSelectionForeground());
					label.setBackground(list.getSelectionBackground());
				}else{
					label.setForeground(list.getForeground());
					label.setBackground(list.getBackground());
				}
				return p;
			}
		});
		RubberBandingListener rbl = new RubberBandingListener();
		addMouseMotionListener(rbl);
		addMouseListener(rbl);
	}
	class RubberBandingListener extends MouseAdapter {
		@Override public void mouseDragged(MouseEvent e) {
			setFocusable(true);
			if(srcPoint==null) srcPoint = e.getPoint();
			Point destPoint = e.getPoint();
			polygon.reset();
			polygon.moveTo(srcPoint.x,  srcPoint.y);
			polygon.lineTo(destPoint.x, srcPoint.y);
			polygon.lineTo(destPoint.x, destPoint.y);
			polygon.lineTo(srcPoint.x,  destPoint.y);
			polygon.closePath();
			setSelectedIndices(getIntersectsIcons(polygon));
			repaint();
		}
		@Override public void mouseReleased(MouseEvent e) {
			setFocusable(true);
			srcPoint = null;
			repaint();
		}
		@Override public void mousePressed(MouseEvent e) {
			int index = locationToIndex(e.getPoint());
			Rectangle rect = getCellBounds(index,index);
			if(!rect.contains(e.getPoint())) {
				getSelectionModel().setLeadSelectionIndex(getModel().getSize());
				clearSelection();
				setFocusable(false);
			}else{
				setFocusable(true);
			}
		}
		private int[] getIntersectsIcons(Shape p) {
			ListModel model = getModel();
			Vector<Integer> list = new Vector<Integer>(model.getSize());
			for(int i=0;i<model.getSize();i++) {
				Rectangle r = getCellBounds(i,i);
				if(p.intersects(r)) {
					list.add(i);
				}
			}
			int[] il = new int[list.size()];
			for(int i=0;i<list.size();i++) {
				il[i] = list.get(i);
			}
			return il;
		}
	}
	
	@Override public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(srcPoint==null) return;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(rcolor);
		g2d.draw(polygon);
		g2d.setComposite(alcomp);
		g2d.setPaint(pcolor);
		g2d.fill(polygon);
	}
	private Color makeColor(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		return (r>g)
		?(r>b)?new Color(r,0,0):new Color(0,0,b)
		:(g>b)?new Color(0,g,0):new Color(0,0,b);
	}
}
