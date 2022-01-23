package update.modeleXml;

/**
 * Modélisation d'une bibliothèque pour le stockage XML
 * 
 * @author Brendan
 * @version 0.1 23/04/2012
 */
public class Library {
	
	/**
	 * Library works on all OS (typically any JAVA lib)
	 */
	public final static int OS_ALL = 0;
	
	/**
	 * Library works on Windows
	 */
	public final static int OS_WINDOWS = 1;
	
	/**
	 * Library works on MAX
	 */
	public final static int OS_MAC = 2;
	
	/**
	 * Library works on UNIX
	 */
	public final static int OS_UNIX = 3;
	
	public static enum OS  {linux, solaris, windows, macos, unknown;}
	
	private OS test;
	
	/**
	 * File Name
	 */
	private String libName;
	
	/**
	 * File Version
	 */
	private String libVersion;	
	
	/**
	 * Relative file path
	 */
	private String filePath;
	
	/**
	 * Operating System
	 */
	private int operatingSystem;
	
	/**
	 * Taille du fichier en octets
	 */
	private long fileSize;
	
	
	
	public Library() {
		
		libName = "";
		filePath = "";
		libVersion = "";
		operatingSystem = 0;
		test = OS.linux;
	}
	
	@Override
	public boolean equals(Object o) {
		return libName.equalsIgnoreCase(((Library)o).libName);
	}
	
	/**
	 * @return the fileVersion
	 */
	public String getLibVersion() {
		return libVersion;
	}
	
	/**
	 * @param fileVersion the fileVersion to set
	 */
	public void setLibVersion(String fileVersion) {
		this.libVersion = fileVersion;
	}

	/**
	 * @return the operatingSystem
	 */
	public int getOperatingSystem() {
		return operatingSystem;
	}

	/**
	 * @param operatingSystem the operatingSystem to set
	 */
	public void setOperatingSystem(int operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	/**
	 * @return the fileName
	 */
	public String getLibName() {
		return libName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setLibName(String fileName) {
		this.libName = fileName;
	}

	/**
	 * @return the fileSize
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the fileName
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFilePath(String fileName) {
		this.filePath = fileName;
	}
}