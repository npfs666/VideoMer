package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import de.javasoft.swing.JYDockingPort;
import de.javasoft.swing.JYDockingView;
import de.javasoft.swing.jydocking.DockingManager;
import de.javasoft.swing.plaf.jydocking.DefaultCloseAction;
import de.javasoft.swing.plaf.jydocking.DefaultMinimizeAction;

/**
 * A simple Docking application used to demonstrate and explain  
 * some basic functionalities.
 */
@SuppressWarnings("serial")
public class SimpleDocking extends JFrame
{
  public SimpleDocking()
  {
    super("Simple Docking Demo");
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
    //create views - each view ID has to be unique within your application
    JYDockingView mainView = createMainView("mainViewName", "MainTitle", "MainTabText");
    JYDockingView explorerView = createView("explorerViewName", "ExplorerTitle", "ExplorerTabText");
    JYDockingView infoView = createView("infoViewName", "InfoTitle", "InfoTabText");

    //create main docking port - the container for the MainView.
    JYDockingPort viewport = new JYDockingPort();
    viewport.dock(mainView);

    //add sub views
    mainView.dock(explorerView, DockingManager.WEST_REGION, .3f);
    mainView.dock(infoView, DockingManager.SOUTH_REGION, .75f);
    
    JPanel p = new JPanel(new BorderLayout(0, 0));
    p.setBorder(new EmptyBorder(5, 5, 5, 5));
    p.add(viewport, BorderLayout.CENTER);

    return p;
  }

  /**
   * Create the main view - with blocked center region.
   */
  public static JYDockingView createMainView(String name, String title, String tabText)
  {
    JYDockingView view = createView(name, title, tabText);
    //avoid dragging in the center region of the main view
    view.setTerritoryBlocked(DockingManager.CENTER_REGION, true);
    return view;
  }

  /**
   * Create additional views.
   */
  public static JYDockingView createView(String name, String title, String tabText)
  {
    JYDockingView view = new JYDockingView(name, title, tabText);
    initView(view);
    return view;
  }

  /**
   * Initialize specified view.
   */
  private static void initView(JYDockingView view)
  {
    //add action buttons to the titlebar
    view.addAction(new DefaultMinimizeAction(view));
    view.addAction(new DefaultCloseAction(view));

    //set an empty panel as view container/component 
    JPanel p = new JPanel(new BorderLayout());
    p.add(new JTree());
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
          //UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel");
          UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
          new SimpleDocking();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    });
  }
}

