package com.staligtredan.VideoMer.update.modeleXml;

/**
 * Mod�lisation d'une biblioth�que pour le stockage XML
 * 
 * @author Brendan
 * @version 0.1 23/04/2012
 */
public class Library {
	
	/**
	 * Library OS
	 */
	public static enum OS  {unknown, linux, solaris, windows, macos;}
	
	/**
	 * Library Name
	 */
	private String libName;
	
	/**
	 * Library Version
	 */
	private String libVersion;	
	
	/**
	 * Relative file path
	 */
	private String filePath;
	
	/**
	 * Operating System
	 */
	private OS operatingSystem;
	
	/**
	 * Taille du fichier en octets
	 */
	private long fileSize;
	
	
	
	public Library() {
		
		libName = "";
		filePath = "";
		libVersion = "";
		operatingSystem = OS.unknown;
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
	public OS getOperatingSystem() {
		return operatingSystem;
	}

	/**
	 * @param operatingSystem the operatingSystem to set
	 */
	public void setOperatingSystem(OS operatingSystem) {
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