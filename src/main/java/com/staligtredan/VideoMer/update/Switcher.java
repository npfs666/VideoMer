package com.staligtredan.VideoMer.update;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class Switcher {

	private static File updaterTMP = new File("TMP.updater.jar");
	private static File updater = new File("updater.jar");
	private static File videoMer = new File("VideoMer.jar");
	
	public static void main( String[] args ) throws InterruptedException {
		
		Thread.sleep(800);
		
		try {
			System.setErr(new PrintStream("logs/errorSwitch.log"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Si updater à mettre à jour
		if( updaterTMP.exists() && updaterTMP.isFile() ) {
			
			updater.delete();
			
			updaterTMP.renameTo(updater);
		}
		
		// Ensuite on lance videoMer si il existe
		if( videoMer.exists() && videoMer.isFile() ) {
			
			try {
				Desktop.getDesktop().open(videoMer);
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		} else {
			
			try {
				Desktop.getDesktop().open(updater);
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}

		
	}

}
