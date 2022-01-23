package test.fullScreen2;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

import com.sun.jna.NativeLibrary;

public abstract class VlcjTest {
	  
	  /**
	   * Log level, used only if the -Dvlcj.log= system property has not already 
	   * been set.
	   */
	  private static final String VLCJ_LOG_LEVEL = "INFO";

	  /**
	   * Change this to point to your own vlc installation, or comment out the code
	   * if you want to use your system default installation.
	   * <p>
	   * This is a bit more explicit than using the -Djna.library.path= system 
	   * property.
	   */
	  private static final String NATIVE_LIBRARY_SEARCH_PATH = null;
	  
	  /**
	   * Set to true to dump out native JNA memory structures.
	   */
	  private static final String DUMP_NATIVE_MEMORY = "false";
	  
	  /**
	   * Static initialisation.
	   */
	  static {
	    if(null == System.getProperty("vlcj.log")) {
	      System.setProperty("vlcj.log", VLCJ_LOG_LEVEL);
	    }
	    
	    // Safely try to initialise LibX11 to reduce the opportunity for native
	    // crashes - this will silently throw an Error on Windows (and maybe MacOS)
	    // that can safely be ignored
	    LibXUtil.initialise();
	    
	    if(null != NATIVE_LIBRARY_SEARCH_PATH) {
	      //Logger.info("Explicitly adding JNA native library search path: '{}'", NATIVE_LIBRARY_SEARCH_PATH);
	      NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
	    }
	    
	    System.setProperty("jna.dump_memory", DUMP_NATIVE_MEMORY);
	  }

	  /**
	   * Set the standard look and feel.
	   */
	  protected static final void setLookAndFeel() {
	    String lookAndFeelClassName = null;
	    LookAndFeelInfo[] lookAndFeelInfos = UIManager.getInstalledLookAndFeels();
	    for(LookAndFeelInfo lookAndFeel : lookAndFeelInfos) {
	      if("Nimbus".equals(lookAndFeel.getName())) {
	        lookAndFeelClassName = lookAndFeel.getClassName();
	      }
	    }
	    if(lookAndFeelClassName == null) {
	      lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
	    }
	    try {
	      UIManager.setLookAndFeel(lookAndFeelClassName);
	    }
	    catch(Exception e) {
	      // Silently fail, it doesn't matter
	    }
	  }
	}

