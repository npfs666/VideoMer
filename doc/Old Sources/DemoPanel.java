/*
 * $Id: DemoPanel.java,v 1.29 2005/10/31 04:41:45 zfq Exp zfq $
 *
 * Copyright (C) 2001-2003 Extreme Component, Inc. All rights reserved.
 * Use is subject to license terms. 
 */
package test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SingleSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import com.zfqjava.swing.JBean;
import com.zfqjava.swing.JCommonPane;
/**
 * DemoPanel
 *
 * @author $Author: zfq $
 * @version $Revision: 1.29 $ $Date: 2005/10/31 04:41:45 $
 */
@SuppressWarnings("serial")

public class DemoPanel extends JBean {
	
	private static final String BUNDLE = "resources.demopanel";
	private JToolBar toolBar;
	private JTabbedPane tabbedPane;
	private JPanel sourceCodePanel;
	private JPanel javaDocPanel;
	private String demoName;


	public DemoPanel() {
		setLayout(new BorderLayout());
		getResourceManager().addBundle(BUNDLE);
		initComponents();	
		add(createMenuBar(), BorderLayout.NORTH);
		add(createCenterPanel(), BorderLayout.CENTER);
	}

	public void addDemo(String name, JComponent c) {
	}

	public void addDemo(JComponent c) {
		tabbedPane.removeAll();
		demoName = getDemoName(c);
		loaded = false;
		tabbedPane.addTab(demoName, c);
		if(isSourceCodeAvaliable()) {
			tabbedPane.addTab("Source Code", getSourceCodePanel());
		}
		if(isJavaDocAvaliable()) {
			tabbedPane.addTab("JavaDoc", getJavaDocPanel());
		}
		tabbedPane.setSelectedIndex(0);	
	}

	private String getDemoName(JComponent c) {
		String className = c.getClass().getName();
		// debug
		// System.out.println("demo class name: " + className);
		return className;
	}

	@SuppressWarnings("rawtypes")
	private void initComponents() {
		// start init create source panel.
		sourceCodePanel = createSourceCodePanel();
		codeMap = new HashMap();
		// end init
		javaDocPanel = createJavaDocPanel();
	}

	// source code panel
	private JTextArea textArea;    
	private JPanel createSourceCodePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		textArea = new JTextArea(20, 50);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
		return panel;
	}

	// java doc panel
	private JEditorPane editorPane;
	private JPanel createJavaDocPanel() {
		JPanel panel = new JPanel();
		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.addHyperlinkListener(createHyperLinkListener());	
		panel.setLayout(new BorderLayout());
		panel.add(new JScrollPane(editorPane), BorderLayout.CENTER);
		return panel;
	}

	private void showWaitCursor(Component c) {
		c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	private void showDefaultCursor(Component c) {
		c.setCursor(Cursor.getDefaultCursor());	    
	}

	private HyperlinkListener createHyperLinkListener() {
		return new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				showWaitCursor(editorPane);
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if (e instanceof HTMLFrameHyperlinkEvent) {
						((HTMLDocument)editorPane.getDocument()).processHTMLFrameHyperlinkEvent(
								(HTMLFrameHyperlinkEvent)e);
					} else {
						try {
							editorPane.setPage(e.getURL());
						} catch (IOException ioe) {
							System.out.println("IOE: " + ioe);
						}
					}
				}
				showDefaultCursor(editorPane);
			}
		};
	}

	private JPanel getSourceCodePanel() {
		return sourceCodePanel;
	}

	private JPanel getJavaDocPanel() {
		return javaDocPanel;
	}

	// center panel
	private JPanel createCenterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		toolBar = new JToolBar();
		toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
		panel.add(toolBar, BorderLayout.NORTH);
		tabbedPane = new JTabbedPane();
		tabbedPane.getModel().addChangeListener(new TabListener());
		panel.add(tabbedPane, BorderLayout.CENTER);
		return panel;
	}


	private class TabListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			SingleSelectionModel model = (SingleSelectionModel) e.getSource();
			boolean srcSelected = model.getSelectedIndex() == 1;
			boolean docSelected = model.getSelectedIndex() == 2;
			if(srcSelected) {
				showSourceCode();
			} else if(docSelected) {
				showJavaDoc();
			} else {
				// do nothing
			}
		}
	}

	private boolean isJavaDocAvaliable() {
		URL url = getJavaDocURL(demoName);
		return url != null;
	}

	private boolean isSourceCodeAvaliable() {
		return getSourceCodeURL(demoName) != null;
	}

	private URL getJavaDocURL(String demoName) {
		String fileName = "api/index.html";
		// return getClass().getClassLoader().getSystemResource(fileName);
		return getClass().getResource(fileName);	    
	}

	private URL getSourceCodeURL(String demoName) {
		if(demoName == null)
			return null;
		String fileName = "src/" + demoName + ".java";
		// System.out.println("source code file: " + fileName);	
		return getClass().getResource(fileName);
	}

	private boolean loaded;
	private void showSourceCode() {
		final URL url = getSourceCodeURL(demoName);
		// System.out.println("source code: " + url);
		if(url != null) {
			if(!codeMap.containsKey(url)) {
				// first loading
				textArea.setText("Source Code loading...");
				textArea.repaint();

				Runnable runner = new Runnable() {
					public void run() {
						String source = loadSourceCode(url);			    
						textArea.setText(source);
						textArea.setCaretPosition(0);
						loaded = true;
					}
				};
				SwingUtilities.invokeLater(runner);		
			} else {
				if(!loaded) {
					String source = (String)codeMap.get(url);
					textArea.setText(source);
					textArea.setCaretPosition(0);
					loaded = true;		    
				}
			}
		}
	}

	private void showJavaDoc() {
		final URL url = getJavaDocURL(demoName);
		// System.out.println("java doc: " + url);
		Runnable runner = new Runnable() {
			public void run() {
				try {
					editorPane.setPage(url);
				} catch (Exception e) {
					System.out.println("Could not load file: " + url + "\nError: " + e.getMessage());
				}
			}
		};
		SwingUtilities.invokeLater(runner);			

	}

	
	@SuppressWarnings("rawtypes")
	private Map codeMap;    

	@SuppressWarnings("unchecked")
	private String loadSourceCode(URL url) {
		String sourceString = null;
		StringBuffer sourceCode = new StringBuffer();
		InputStream is;
		InputStreamReader isr;	
		try {
			is = url.openStream();
			isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			char[] buf = new char[8192];
			while(true) {
				int i = reader.read(buf);
				if(i != -1) {
					sourceCode.append(new String(buf, 0, i));
				} else {
					break;
				}
			}
			sourceString = sourceCode.toString();
			codeMap.put(url, sourceString);
			return sourceString;
		} catch (Exception e) {
			sourceCode.append("Could not load file: " + url + "\nError: " + e.getMessage());
		}
		return sourceCode.toString();
	}


	private JMenuBar createMenuBar() {
		//  	JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		return createMenuBar("main-menubar");
	}
	@SuppressWarnings("rawtypes")
	public void doChooselaf() {
		Map map = JCommonPane.showLookAndFeelDialog(getFrame(), null);
		// the following code should move to JCommonPane for a specified
		// property.
		String lafclass = (String)map.get(JCommonPane.LAF_CLASS);
		try {
			UIManager.setLookAndFeel(lafclass);
			SwingUtilities.updateComponentTreeUI(getFrame());
			getFrame().validate();
			getFrame().repaint();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void doLtr() {
		Window w = getWindow();
		setComponentTree(w, ComponentOrientation.LEFT_TO_RIGHT);
		resizeWindow(w);	    	
	}
	public void doRtl() {
		Window w = getWindow();
		setComponentTree(w, ComponentOrientation.RIGHT_TO_LEFT);
		resizeWindow(w);	
	}
	public void doUnknown() {
		Window w = getWindow();
		setComponentTree(w, ComponentOrientation.UNKNOWN);
		resizeWindow(w);		
	}
	public void doLocation() {
		JCommonPane.showLocationDialog(getFrame(), null);
	}
	public void doLogin() {
		JCommonPane.showLoginDialog(getFrame(), null);
	}
	@SuppressWarnings("rawtypes")
	private HashMap localMenuMap = new HashMap();

	@SuppressWarnings({ "unused", "unchecked" })
	private JMenu createLocaleMenu() {
		JMenu menu = new JMenu("Locale");
		menu.setMnemonic('c');
		Locale[] locales = Locale.getAvailableLocales();
		Locale defaultLocale = Locale.getDefault();
		ActionListener listener = new LocaleActionListener();
		ButtonGroup group = new ButtonGroup();
		for(int i=0,count=locales.length; i<count; i++) {
			JRadioButtonMenuItem rbItem = new JRadioButtonMenuItem(locales[i].getDisplayName());
			if(locales[i].equals(defaultLocale)) {
				rbItem.setSelected(true);
			}
			localMenuMap.put(rbItem, locales[i]);
			rbItem.addActionListener(listener);
			group.add(rbItem);
			menu.add(rbItem);	    
		}
		return menu;
	}

	private class LocaleActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButtonMenuItem menu = (JRadioButtonMenuItem)e.getSource();
			Locale locale = (Locale)localMenuMap.get(menu);
			if(locale != null) {
				Locale.setDefault(locale);
				getWindow().setLocale(locale);
				SwingUtilities.updateComponentTreeUI(getWindow());
				resizeWindow(getWindow());		
			}
		}
	}

	private Window getWindow() {
		for(Container c = this; c != null; c = c.getParent()) {
			if(c instanceof Window) {
				return (Window)c;
			}
		}
		return null;
	}

	private void setComponentTree(Component c, ComponentOrientation orientation) {
		if(c instanceof Container) {
			Component[] components = ((Container)c).getComponents();
			for(int i=0; i<components.length; i++) {
				components[i].setComponentOrientation(orientation);
				components[i].invalidate();
				setComponentTree(components[i], orientation);
			}
		}
	}

	private void resizeWindow(Window w) {
		Dimension d = w.getPreferredSize();
		Toolkit toolkit = w.getToolkit();
		Dimension size = toolkit.getScreenSize();	
		d.width = Math.min(d.width, size.width);
		d.height = Math.min(d.height, size.height);
		w.setSize(d);
		centerOnScreen(w);
		w.validate();	
	}

	private static void centerOnScreen(Window window) {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation((d.width - window.getWidth())/2,
				(d.height - window.getHeight())/2);
	}    

	// Todo:
		// add toolbar, add beaninfo images ... 32x32
	// add Source panel, JavaDoc pannel, ComponentDemo panel

}
