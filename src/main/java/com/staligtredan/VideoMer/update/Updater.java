package com.staligtredan.VideoMer.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.staligtredan.VideoMer.update.vue.JFrameUpdate;

/**
 * Instance globale de l'appli de mise à jour
 * Contient le main()
 * 
 * @author Brendan
 * @version 0.1.0 23/04/2012
 * @version 2.0.0 22/12/2016 MAJ du dossier des logs
 */
public class Updater {
	
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
    	
    	// Look and Feel
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	// Création de l'écran principal
		JFrameUpdate thisClass = new JFrameUpdate(controller);
		
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
            	
            	File logsFolder = new File("logs");
            	
            	// 2.0.0 | Si le dossier logs n'existe pas on le crée
            	if( !logsFolder.exists() && !logsFolder.isDirectory() )
            		logsFolder.mkdir();
            	
            	try {
					System.setOut(new PrintStream("logs/outUpdate.log"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
        		try {
					System.setErr(new PrintStream("logs/errorUpdate.log"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
                createAndShowGUI();
            }
        });
	}
}
