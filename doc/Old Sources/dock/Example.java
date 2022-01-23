package test.dock;

import javax.swing.JFrame;

import bibliothek.gui.DockController;
import bibliothek.gui.DockFrontend;
import bibliothek.gui.DockUI;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.station.split.SplitDockGrid;
import bibliothek.gui.dock.themes.ThemeFactory;

public class Example {

	public static void main(String[]args){
		
		
		JFrame frame = new JFrame();
		DockController controller = new DockController();
		
		DockFrontend frontend;
		frontend = new DockFrontend( controller, frame );
		//frontend.setShowHideAction(true);
		//frontend.getController().setTheme(new EclipseTheme());
		for( final ThemeFactory factory : DockUI.getDefaultDockUI().getThemes()){
			System.out.println(factory.getName());
			if( factory.getName().equals("Eclipse") )
				frontend.getController().setTheme( factory.create() );
		}
		
		
		SplitDockStation station = new SplitDockStation();
		controller.add(station);

		
		//DefaultDockable test = new DefaultDockable();
		
		//test.setTitleText("TOUCHE MA BITE !");
		//Container content = test.getContentPane();
		//content.setLayout(new GridLayout(10,5));
		//content.add(new TestPanel()
		//test.add(new TestPanel());
	
		
		SplitDockGrid grid = new SplitDockGrid();
		grid.addDockable(0,0,1,1,new DefaultDockable("Top"));
		grid.addDockable(0,1,1,1,new DefaultDockable("Middle"));
		grid.addDockable(0,2,1,1,new DefaultDockable("Bottom"));
		grid.addDockable(1,0,1,3,new DefaultDockable("Right"));
		station.dropTree(grid.toTree());
		
		
		
		
		frame.add(station.getComponent());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(20,20,400,400);
		frame.setVisible(true);
	}
}
