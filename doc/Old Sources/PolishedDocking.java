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
public class PolishedDocking extends JFrame
{
  public static final String MAIN_VIEW = "mainViewID";
  public static final String EXPLORER_VIEW = "explorerViewID";
  public static final String INFO_VIEW = "infoViewID";

  public PolishedDocking()
    {
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
      JYDockingView mainView = createMainView(MAIN_VIEW, "MainTitle", "MainTabText", "/resources/icons/file.gif");
      JYDockingView explorerView = createView(EXPLORER_VIEW, "ExplorerTitle", "ExplorerTabText", "/resources/icons/explorer.gif");
      JYDockingView infoView = createView(INFO_VIEW, "InfoTitle", "InfoTabText", "/resources/icons/tip.png");

      JYDockingPort viewport = new JYDockingPort();
      viewport.dock(mainView);
      
      mainView.dock(explorerView, DockingManager.WEST_REGION, .3f);
      mainView.dock(infoView, DockingManager.SOUTH_REGION, .3f);
      
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
            new PolishedDocking();
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
        }
      });
    }

}

