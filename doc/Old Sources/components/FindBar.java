package components;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;

import org.jdesktop.swingx.JXFindBar;

/**
 * <code>FindBar</code> est une barre de recherche de type linux.
 * Elle apparait & disparait quand on a besoin d'elle, en overlay
 * 
 * @author Brendan
 * @version 0.1a 01/03/2012
 */
@SuppressWarnings("serial")
public class FindBar extends JXFindBar {
	
	public void addButtonListener(ActionListener l) {
		findNext.addActionListener(l);
		findPrevious.addActionListener(l);
	}
	
	public void addTextFieldListener(KeyListener l) {
		searchField.addKeyListener(l);
	}
	
	public void setIcon() {
		findNext.setIcon(new ImageIcon(getClass().getResource("/imgs/16x16/nav_down_blue.png")));
		findPrevious.setIcon(new ImageIcon(getClass().getResource("/imgs/16x16/nav_up_blue.png")));
	}
	
	public void setText(String s) {
		searchField.setText(s);
	}
	
	public String getText() {
		return searchField.getText();
	}
	
	public void requestFocus() {
		searchField.grabFocus();
		
	}

}
