package update.modeleXml;

import java.util.ArrayList;

public class Update {
	
	/**
	 * Liste des librairies à mettre à jour
	 */
	private ArrayList<Library> libraries;
	
	/**
	 * Version du patch actuel
	 */
	private String patchVersion;
	
	
	
	public Update() {
		
		libraries = new ArrayList<Library>();
		patchVersion = "";
	}
	
	public Update(String patchVersion) {
		libraries = new ArrayList<Library>();
		this.patchVersion = patchVersion;
	}
	
	public void addLib(Library lib) {
		libraries.add(lib);
	}
	
	/**
	 * @return the files
	 */
	public ArrayList<Library> getFiles() {
		return libraries;
	}
	
	/**
	 * @param files the files to set
	 */
	public void setFiles(ArrayList<Library> files) {
		this.libraries = files;
	}
	
	/**
	 * @return the patchVersion
	 */
	public String getPatchVersion() {
		return patchVersion;
	}
	
	/**
	 * @param patchVersion the patchVersion to set
	 */
	public void setPatchVersion(String patchVersion) {
		this.patchVersion = patchVersion;
	}
}