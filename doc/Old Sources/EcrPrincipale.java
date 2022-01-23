package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import de.javasoft.swing.JYDockingPort;
import de.javasoft.swing.JYDockingView;
import de.javasoft.swing.jydocking.DockingManager;
import de.javasoft.swing.plaf.jydocking.DefaultCloseAction;
import de.javasoft.swing.plaf.jydocking.DefaultFloatAction;
import de.javasoft.swing.plaf.jydocking.DefaultMaximizeAction;
import de.javasoft.swing.plaf.jydocking.DefaultMinimizeAction;

/**
 * Polished simple docking application with icons for each view.
 */
@SuppressWarnings("serial")
public class EcrPrincipale extends JFrame {
	
	private static final String EXPLORER_VIEW = "explorerViewID";
	private static final String MAIN_VIEW = "mainViewID";
	private static final String DETAILS_VIEW = "detailsViewID";
	private static final String PLAYLIST_VIEW = "playlistViewID";

	public EcrPrincipale() {

		super("Polished Docking Demo");
		//enable floating - drag and drop a view as a separate window 
		DockingManager.setFloatingEnabled(true);
		add(createContentPane());
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(640, 480);
		setLocationRelativeTo(null);        //center
		setVisible(true);
	}

	/**
	 * Create content pane with three different views.
	 */
	private JPanel createContentPane()
	{
		//each view ID has to be unique within your application
		JYDockingView explorerView = createView(EXPLORER_VIEW, "ExplorerTitle", "ExplorerTabText", "/imgs/16x16/folder_movie.png");
		JYDockingView mainView = createMainView(MAIN_VIEW, "MainTitle", "MainTabText", "/imgs/16x16/film.png");
		JYDockingView detailsView = createView(DETAILS_VIEW, "DetailsTitle", "InfoTabText", "/imgs/16x16/movie_view.png");
		JYDockingView playlistView = createView(PLAYLIST_VIEW, "PlaylistTitle", "InfoTabText", "/imgs/16x16/clipboard.png");
		
		JYDockingPort viewport = new JYDockingPort();
		viewport.dock(mainView);
		
		mainView.dock(explorerView, DockingManager.NORTH_REGION, .3f);
		mainView.dock(detailsView, DockingManager.SOUTH_REGION, .3f);
		mainView.dock(playlistView, DockingManager.EAST_REGION, .1f);

		JPanel p = new JPanel(new BorderLayout(0, 0));
		p.setBorder(new EmptyBorder(5, 5, 5, 5));
		p.add(viewport, BorderLayout.CENTER);

		return p;
	}

	/**
	 * Create the main view - with blocked center region.
	 */
	public static JYDockingView createMainView(String name, String title, String tabText, String iconLocation)
	{
		JYDockingView view = createView(name, title, tabText, iconLocation);
		//avoid dragging in the center region of the main view
		view.setTerritoryBlocked(DockingManager.CENTER_REGION, true);
		return view;
	}

	/**
	 * Create additional views.
	 */
	public static JYDockingView createView(String name, String title, String tabText, String iconLocation)
	{
		JYDockingView view = new JYDockingView(name, title, tabText);
		initView(view, iconLocation);
		return view;
	}

	/**
	 * Initialize specified view.
	 */
	private static void initView(JYDockingView view, String iconLocation)
	{
		if (view.getID().equals(MAIN_VIEW))
			view.addAction(new DefaultMaximizeAction(view));
		else
		{  
			//add needed action buttons
			view.addAction(new DefaultMinimizeAction(view));
			view.addAction(new DefaultMaximizeAction(view));
			view.addAction(new DefaultFloatAction(view));
			view.addAction(new DefaultCloseAction(view));
		}  

		//add double-click support for maximization 
		view.getTitlebar().addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent evt)
			{
				if (evt.getClickCount() == 2)
				{
					JYDockingView view = (JYDockingView)evt.getComponent().getParent();
					DockingManager.setMaximized(view, !DockingManager.isMaximized(view));
				}
			}
		});

		//Icon icon = new ImageIcon(PolishedDocking.class.getResource(iconLocation));
		//view.setIcon(icon);

		JPanel p = new JPanel(new BorderLayout());
		view.setContentPane(p);
	}
	/**
	 * Static main method for application startup. 
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel");
					new EcrPrincipale();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

}


