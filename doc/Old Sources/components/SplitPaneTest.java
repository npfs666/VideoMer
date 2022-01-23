package test;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitLayout.Node;

public class SplitPaneTest
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());
       
        JXMultiSplitPane splitPane = new JXMultiSplitPane();
        String layout = "(ROW (LEAF name=criteria weight=0.3) (LEAF name=results weight=0.6) (LEAF name=actions weight=0.1))";
        splitPane.getMultiSplitLayout().setModel(MultiSplitLayout.parseModel(layout));
        splitPane.add(new JButton("LEFT"), "criteria");
        splitPane.add(new JButton("MIDDLE"), "results");
        splitPane.add(new JButton("RIGHT"), "actions");
        splitPane.getMultiSplitLayout().layoutByWeight(splitPane);
       
        JPanel controls = new JPanel();
        JButton leftControl = new JButton(new CollapsibleSplitAction("Left", "criteria", splitPane));
        JButton rightControl = new JButton(new CollapsibleSplitAction("Right", "actions", splitPane));
        controls.add(leftControl);
        controls.add(rightControl);
       
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        frame.getContentPane().add(controls, gbc);
       
        gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        frame.getContentPane().add(splitPane, gbc);
       
        frame.pack();
        frame.setVisible(true);
    }

}

@SuppressWarnings("serial")
class CollapsibleSplitAction extends AbstractAction
{
    private String title;
    private boolean visible = true;
    private String node;
    private JXMultiSplitPane splitPane;
   
    public CollapsibleSplitAction(String title, String node, JXMultiSplitPane splitPane)
    {
        this.title = title;
        this.node = node;
        this.splitPane = splitPane;
        putValue(AbstractAction.NAME, "Show "+title);
    }
   
    public void actionPerformed(ActionEvent arg0)
    {
        String oldName = (String)super.getValue(AbstractAction.NAME);
        Node splitNode = splitPane.getMultiSplitLayout().getNodeForName(node);
        Component comp = splitPane.getMultiSplitLayout().getComponentForNode( splitNode );
        String newName;
        if (visible)
        {
            // split was visible, we are now hiding it.
            newName = "Show "+title;
        }
        else
        {
            newName = "Hide "+title;
        }
        visible = !visible;
        comp.setVisible( visible ); 
        splitNode.setVisible(visible);
        
        
        System.out.println("Setting "+node+" visible: "+visible);
        super.putValue(AbstractAction.NAME, newName);
        firePropertyChange(AbstractAction.NAME, oldName, newName);

    }
} 
