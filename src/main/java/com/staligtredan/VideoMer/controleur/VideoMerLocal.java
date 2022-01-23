package com.staligtredan.VideoMer.controleur;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.staligtredan.VideoMer.vue.JFramePrincipal;


/**
 * <code>VideoManager</code> permet de créer le controlleur en superGlobal
 * et d'afficher l'écran principal. <br/>
 * Cette classe permet aussi de gérer les Look & Feel
 * 
 * @author Brendan Jaouen
 * @version 0.1 19/08/2008
 */
public final class VideoMerLocal {
	
	private static String javaVersionRequired = "1.8.0_00";
	
	/**
	 * Crée & affiche l'interface graphique
	 */
    private static void createAndShowGUI() {
        
    	
    	// Avant de faire quoique ce soit, on vérifie la version du JRE
    	if( System.getProperty("java.version").compareTo(javaVersionRequired) < 0 ) {
    		JOptionPane.showMessageDialog(null, 
    				"Votre version de java n'est pas assez récente. \n" +
    				"Version requise   : " + javaVersionRequired + "\n" +
    				"Version installée : " + System.getProperty("java.version"),
    				"Problème de version Java",
    				JOptionPane.ERROR_MESSAGE
    				);
    		System.exit(12);
    	}

    	// Création du controlleur
    	DefaultController controller = new DefaultController();
    	
    	// Gestion des Look & Feel
    	try {
    		//controller.installLookAndFeels();
    		UIManager.setLookAndFeel(controller.getLookAndFeel());
    		//UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
    		//UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	// Création de l'écran principal
		JFramePrincipal thisClass = new JFramePrincipal(controller);
		controller.setMainFrame(thisClass);
		
		Toolkit toolkit =  Toolkit.getDefaultToolkit ();
		Dimension dim = toolkit.getScreenSize();
		
		// Si presque au plus large => mode agrandi
		if( controller.getPreferences().getFrameDimension().width > (dim.width * 0.95) )
			thisClass.setExtendedState(JFrame.MAXIMIZED_BOTH);
		else
			thisClass.setSize(controller.getPreferences().getFrameDimension());

		thisClass.setLocation(controller.getPreferences().getFrameLocation());
		
    	// Affichage
		thisClass.setVisible(true);
    }
	
    /**
     * Fonction main() qui permet de lancer le programme
     * 
     * @param args
     */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}

}
