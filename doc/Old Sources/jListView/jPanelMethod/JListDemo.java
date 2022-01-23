package test.jListView.jPanelMethod;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class JListDemo extends JFrame
{
 
    public static void  main( String[] args )
    {
        JListDemo test  = new JListDemo();
        test.setSize( new Dimension( 100, 100) );
        test.show();
    }
 
    private JPopupMenu popup;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
 
    public JListDemo()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch( Exception e )
        {
        }
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        Icon[] icons = new Icon[50];
        for( int i = 0; i < 50 ; i++ )
        {
            icons[i] = new XIcon();
        }
        JList list = new JList( icons );
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        list.setFixedCellHeight(25);
        list.setFixedCellWidth( 25 );
        this.getContentPane().add( new JScrollPane( list ) );
 
    }
    private  int counter;
 
 
    class XIcon implements Icon
    {
 
        private int count;
 
        public XIcon()
        {
            this.count = ++counter;
        }
        /**
         * Draw the icon at the specified location.  Icon implementations
         * may use the Component argument to get properties useful for
         * painting, e.g. the foreground or background color.
         */
        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            g.setColor( Color.red );
            g.drawLine( 0,0, 16,16 );
            g.drawLine( 0,16, 16, 0 );
            g.setColor( Color.black );
            g.drawRect( 0, 0, 16,16);
            g.drawString( ""+count, 8, 16 );
        }
        /**
         * Returns the icon's width.
         *
         * @return an int specifying the fixed width of the icon.
         */
        public int getIconWidth()
        {
           return( 16 );
        }
        /**
         * Returns the icon's height.
         *
         * @return an int specifying the fixed height of the icon.
         */
        public int getIconHeight()
        {
            return( 16 );
        }
    }
 
}
