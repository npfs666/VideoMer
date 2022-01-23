package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;

import com.jhlabs.image.BlurFilter;

/**
 * A demo to show the abilities of the {@link LockableUI}.
 * 
 * @see BusyPainterUI  
 */
@SuppressWarnings("serial")
public class LockableDemo extends JFrame {
    private JXLayer<JComponent> layer;
    private LockableUI blurUI = 
            new LockableUI(new BufferedImageOpEffect(new BlurFilter()));

    private JCheckBoxMenuItem disablingItem =
            new JCheckBoxMenuItem(new AbstractAction("Lock the layer") {

                public void actionPerformed(ActionEvent e) {
                    blurItem.setEnabled(!disablingItem.isSelected());
                    embossItem.setEnabled(!disablingItem.isSelected());
                    busyPainterItem.setEnabled(!disablingItem.isSelected());

                    blurUI.setLocked(disablingItem.isSelected());
                }
            });

    private JRadioButtonMenuItem blurItem = new JRadioButtonMenuItem("Blur effect");
    private JRadioButtonMenuItem embossItem = new JRadioButtonMenuItem("Unlock button");
    private JRadioButtonMenuItem busyPainterItem = new JRadioButtonMenuItem("BusyPainter");

    public LockableDemo() {
        super("Lockable layer demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent view = createLayerPanel();
        layer = new JXLayer<JComponent>(view);

        layer.setUI(blurUI);
        add(layer);
        add(createToolPanel(), BorderLayout.EAST);
        setSize(380, 300);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LockableDemo().setVisible(true);
            }
        });
    }



    private JComponent createLayerPanel() {
        JComponent panel = new JPanel();
        panel.add(new JCheckBox("JCheckBox"));
        panel.add(new JRadioButton("JRadioButton"));
        panel.add(new JTextField(15));
        JButton button = new JButton("Have a nice day");
        button.setMnemonic('H');
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(LockableDemo.this,
                        "actionPerformed");
            }
        });
        panel.add(button);
        panel.add(new JTextField(15));
        panel.add(new JCheckBox("JCheckBox"));
        panel.add(new JRadioButton("JRadioButton"));
        panel.add(new JTextField(15));
        panel.add(new JCheckBox("JCheckBox"));
        panel.add(new JRadioButton("JRadioButton"));
        panel.setBorder(BorderFactory.createEtchedBorder());
        return panel;
    }

    private JComponent createToolPanel() {
        JComponent box = Box.createVerticalBox();
        JCheckBox button = new JCheckBox(disablingItem.getText());
        button.setModel(disablingItem.getModel());
        box.add(Box.createGlue());
        box.add(button);
        box.add(Box.createGlue());
        JRadioButton blur = new JRadioButton(blurItem.getText());
        blur.setModel(blurItem.getModel());
        box.add(blur);
        JRadioButton emboss = new JRadioButton(embossItem.getText());
        emboss.setModel(embossItem.getModel());
        box.add(emboss);
        JRadioButton translucent = new JRadioButton(busyPainterItem.getText());
        translucent.setModel(busyPainterItem.getModel());
        box.add(translucent);
        box.add(Box.createGlue());
        return box;
    }

    
    
    
    
    
}

