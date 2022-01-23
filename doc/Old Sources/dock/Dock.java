package test.dock;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import bibliothek.gui.DockFrontend;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.station.split.SplitDockGrid;

public class Dock {
    public static void main( String[] args ){
            JFrame frame = new JFrame( "Demo" );
            DockFrontend frontend = new DockFrontend( frame );
            SplitDockStation station = new SplitDockStation();
            
            frame.add( station.getComponent() );
            frontend.addRoot("station", station );
            
            SplitDockGrid grid = new SplitDockGrid();
            grid.addDockable( 0, 0, 1, 1, createDockable( "Red", Color.RED ) );
            grid.addDockable( 0, 1, 1, 1, createDockable( "Green", Color.GREEN ) );
            grid.addDockable( 1, 0, 1, 1, createDockable( "Blue", Color.BLUE ) );
            grid.addDockable( 1, 1, 1, 1, createDockable( "Yellow", Color.YELLOW ) );
            station.dropTree( grid.toTree() );
            
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frame.setBounds( 20, 20, 400, 400 );
            frame.setVisible( true );
    }
    
    public static Dockable createDockable( String title, Color color ){
            JPanel panel = new JPanel();
            panel.setOpaque( true );
            panel.setBackground( color );
            return new DefaultDockable( panel, title );
    }
}
